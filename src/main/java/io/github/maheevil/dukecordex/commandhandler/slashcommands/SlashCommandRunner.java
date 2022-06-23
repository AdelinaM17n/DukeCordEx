package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SlashCommandRunner<T> {
    public String name;
    public String description;
    public Class<T> argsClass;
    public Consumer<SlashCommandContext> consumer;
    public BiConsumer<T,SlashCommandContext> biConsumer;

    public void runConsumer(Object argObject, SlashCommandContext context){
        if(argObject != null)
            this.biConsumer.accept(this.argsClass.cast(argObject),context);
        else
            this.consumer.accept(context);
    }
}
