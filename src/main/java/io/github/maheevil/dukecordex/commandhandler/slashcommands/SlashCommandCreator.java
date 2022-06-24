package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class SlashCommandCreator{
    public static <T>SlashCommandRunner<T> create(String name, String description, Class<T> argClass, BiConsumer<T, SlashCommandCreateEvent> consumer){
        var eee = new SlashCommandRunner<T>();
        eee.argsClass = argClass;
        eee.biConsumer = consumer;
        eee.name = name;
        eee.description = description;
        return eee;
    }

    public static SlashCommandGroup createGroup(String name, String description, SlashCommandRunner<?>... commandRunners){
        var efe = new SlashCommandGroup();
        efe.name = name;
        efe.description = description;
        Arrays.stream(commandRunners).forEach(
                commandRunner -> {
                    efe.runners.put(commandRunner.name,commandRunner);
                }
        );
        return efe;
    }
}
