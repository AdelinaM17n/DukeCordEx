package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SlashCommandRunner<T> {
    public final String name;
    public final String description;
    public final Class<T> argsClass;
    public final HashMap<String,Field> filteredFieldList;
    private final Consumer<SlashCommandCreateEvent> consumer;
    private final BiConsumer<T,SlashCommandCreateEvent> biConsumer;

    public SlashCommandRunner(String name, String description, Class<T> argsClass,HashMap<String,Field> hashMap, BiConsumer<T,SlashCommandCreateEvent> consumer){
        this.name = name;
        this.description = description;
        this.argsClass = argsClass;
        this.biConsumer = consumer;
        this.consumer = null;
        this.filteredFieldList = hashMap;
    }
    public SlashCommandRunner(String name, String description, Consumer<SlashCommandCreateEvent> consumer){
        this.name = name;
        this.description = description;
        this.consumer = consumer;
        this.biConsumer = null;
        this.argsClass = null;
        this.filteredFieldList = null;
    }

    public void runConsumer(Object argObject, SlashCommandCreateEvent context){
        if(argObject != null && biConsumer != null && this.argsClass != null)
            this.biConsumer.accept(this.argsClass.cast(argObject),context);
        else if(consumer != null)
            this.consumer.accept(context);
        else
            System.err.println("Error - the specified consumer is null");
    }
}
