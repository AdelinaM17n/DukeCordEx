package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import org.javacord.api.event.interaction.SlashCommandCreateEvent;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SlashCommandCreator{
    public static <T>SlashCommandRunner<T> create(String name, String description, Class<T> argClass, BiConsumer<T, SlashCommandCreateEvent> consumer){
        return new SlashCommandRunner<>(
                name,
                description,
                argClass,
                consumer
        );
    }

    public static <T>SlashCommandRunner<T> create(String name, String description, Consumer<SlashCommandCreateEvent> consumer){
        return new SlashCommandRunner<>(
                name,
                description,
                consumer
        );
    }

    public static SlashCommandGroup createGroup(String name, String description, SlashCommandRunner<?>... commandRunners){
        var commandGroup = new SlashCommandGroup(
                name,
                description
        );
        Arrays.stream(commandRunners).forEach(
                commandRunner -> commandGroup.runners.put(commandRunner.name,commandRunner)
        );
        return commandGroup;
    }
}
