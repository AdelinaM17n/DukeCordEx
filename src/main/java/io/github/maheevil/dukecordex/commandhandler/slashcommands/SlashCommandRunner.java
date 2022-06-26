package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
public class SlashCommandRunner<T> {
    public final String name;
    public final String description;
    public final Class<T> argsClass;
    public final List<Field> filteredFieldList;
    private final Consumer<SlashCommandCreateEvent> consumer;
    private final BiConsumer<T,SlashCommandCreateEvent> biConsumer;

    public SlashCommandRunner(String name, String description, Class<T> argsClass,List<Field> hashMap, BiConsumer<T,SlashCommandCreateEvent> consumer){
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
