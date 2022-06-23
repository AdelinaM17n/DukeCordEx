package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import java.util.function.BiConsumer;

public class SlashCommandCreator{
    public static <T>SlashCommandRunner<T> create(String name, String description, Class<T> argClass, BiConsumer<T,SlashCommandContext> consumer){
        var eee = new SlashCommandRunner<T>();
        eee.argsClass = argClass;
        eee.biConsumer = consumer;
        eee.name = name;
        eee.description = description;
        return eee;
    }
}
