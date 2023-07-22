package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import io.github.maheevil.dukecordex.commandhandler.annotations.SlashCommandArgField;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
public class SlashCommandCreator {
    public static <T> SlashCommandRunner<T> create(String name, String description, ReplyType replyType, Class<T> argClass, BiConsumer<T, SlashCommandContext> consumer) {
        var fieldList = Arrays.stream(argClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(SlashCommandArgField.class))
                .toList();

        return new SlashCommandRunner<>(
                name,
                description,
                replyType,
                argClass,
                fieldList,
                consumer
        );
    }

    public static <T> SlashCommandRunner<T> create(String name, String description, ReplyType replyType, Consumer<SlashCommandContext> consumer) {
        return new SlashCommandRunner<>(
                name,
                description,
                replyType,
                consumer
        );
    }

    public static SlashCommandGroup createGroup(String name, String description, SlashCommandRunner<?>... commandRunners) {
        var commandGroup = new SlashCommandGroup(
                name,
                description
        );
        Arrays.stream(commandRunners).forEach(
                commandRunner -> commandGroup.runners.put(commandRunner.name, commandRunner)
        );
        return commandGroup;
    }
}
