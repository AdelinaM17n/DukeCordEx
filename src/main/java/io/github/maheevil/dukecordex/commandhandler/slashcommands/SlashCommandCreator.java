package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import io.github.maheevil.dukecordex.commandhandler.annotations.SlashCommandArgField;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SlashCommandCreator{
    public static <T>SlashCommandRunner<T> create(String name, String description, Class<T> argClass, BiConsumer<T, SlashCommandCreateEvent> consumer){
        var fieldList = Arrays.stream(argClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(SlashCommandArgField.class))
                .toList();

        return new SlashCommandRunner<>(
                name,
                description,
                argClass,
                fieldList,
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
