package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import io.github.maheevil.dukecordex.commandhandler.annotations.NoArgs;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SlashCommandRunner<T> {
    public String name;
    public String description;
    public Class<T> argsClass;
    public Consumer<SlashCommandCreateEvent> consumer;
    public BiConsumer<T,SlashCommandCreateEvent> biConsumer;

    public SlashCommandRunner(String name, String description, Class<T> argsClass, BiConsumer<T,SlashCommandCreateEvent> consumer){
        this.name = name;
        this.description = description;
        this.argsClass = argsClass;
        this.biConsumer = consumer;
        this.consumer = null;
    }
    public SlashCommandRunner(String name, String description, Consumer<SlashCommandCreateEvent> consumer){
        this.name = name;
        this.description = description;
        this.consumer = consumer;
        this.biConsumer = null;
        this.argsClass = null;
    }

    public void runConsumer(Object argObject, SlashCommandCreateEvent context){
        if(argObject != null)
            this.biConsumer.accept(this.argsClass.cast(argObject),context);
        else
            this.consumer.accept(context);
    }
}
