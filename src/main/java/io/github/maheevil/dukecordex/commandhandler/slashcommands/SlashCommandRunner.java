package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SlashCommandRunner<T> {
    public String name;
    public String description;
    public Class<T> argsClass;
    public Consumer<SlashCommandCreateEvent> consumer;
    public BiConsumer<T,SlashCommandCreateEvent> biConsumer;

    public void runConsumer(Object argObject, SlashCommandCreateEvent context){
        if(argObject != null)
            this.biConsumer.accept(this.argsClass.cast(argObject),context);
        else
            this.consumer.accept(context);
    }
}
