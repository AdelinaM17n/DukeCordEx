package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import io.github.maheevil.dukecordex.commandhandler.annotations.SlashCommandArgField;
import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
public class SlashCommandRegisterer {
    public static void pushAllSlashCommands(List<SlashCommandEx> list, DiscordApi api) {
        for (SlashCommandEx entry : list) {
            var command = SlashCommand.with(entry.baseName, entry.description);

            if (entry.slashCommandGroups.isEmpty() && !entry.baseBranchingCommands.isEmpty()) {
                if (entry.baseBranchingCommands.containsKey("main") && entry.baseBranchingCommands.size() < 2) {
                    AtomicBoolean error = new AtomicBoolean(false);
                    var runner = entry.baseBranchingCommands.get("main");

                    if (runner.argsClass != null) {
                        runner.filteredFieldList.forEach(
                                field -> error.set(!parseArgs(field, command))
                        );
                    }

                    if (!error.get()) pushToDiscord(command, api, entry.guildOrNotId);
                    else System.err.println("Parsing arguments failed");

                } else if (entry.baseBranchingCommands.size() > 1) {
                    AtomicBoolean error = new AtomicBoolean(false);

                    entry.baseBranchingCommands.values().forEach(
                            baseSubCommand -> error.set(!parseBaseSubCommands(baseSubCommand, command))
                    );

                    if (!error.get()) pushToDiscord(command, api, entry.guildOrNotId);
                    else System.err.println("Parsing arguments failed");

                }
            } else if (!entry.slashCommandGroups.isEmpty()) {
                entry.slashCommandGroups.values().forEach(
                        slashCommandGroup -> command.addOption(
                                SlashCommandOption.createWithOptions(
                                        SlashCommandOptionType.SUB_COMMAND_GROUP,
                                        slashCommandGroup.name,
                                        slashCommandGroup.description,
                                        getGroupSubComList(slashCommandGroup)
                                )
                        )
                );
                // TODO - VERY FUCKING URGENT - BETTER ERROR HANDLING
                pushToDiscord(
                        command, api, entry.guildOrNotId
                );
            }
        }
    }

    private static void pushToDiscord(SlashCommandBuilder command, DiscordApi api, String guildOrNotId) {
        if (guildOrNotId.equals("GLOBAL")) {
            command.createGlobal(api);
        } else {
            command.createForServer(api.getServerById(guildOrNotId).orElseThrow());
        }
    }

    public static List<SlashCommandOption> getGroupSubComList(SlashCommandGroup group) {
        List<SlashCommandOption> list = new ArrayList<>();

        for (SlashCommandRunner<?> subCommandRunner : group.runners.values()) {
            List<SlashCommandOption> argsOptionsList = parseArgList(subCommandRunner);

            if (argsOptionsList == null) {
                System.err.println("Error");
                continue;
            }

            if (!argsOptionsList.isEmpty()) {
                list.add(
                        SlashCommandOption.createWithOptions(
                                SlashCommandOptionType.SUB_COMMAND,
                                subCommandRunner.name,
                                subCommandRunner.description,
                                argsOptionsList
                        )
                );
            } else {
                list.add(
                        SlashCommandOption.create(
                                SlashCommandOptionType.SUB_COMMAND,
                                subCommandRunner.name,
                                subCommandRunner.description
                        )
                );
            }
        }

        return list;
    }

    public static List<SlashCommandOption> parseArgList(SlashCommandRunner<?> subcom) {
        List<SlashCommandOption> argsOptionsList = new ArrayList<>();

        for (Field field : subcom.filteredFieldList) {
            var annotation = field.getAnnotation(SlashCommandArgField.class);

            if (annotation.type() == SlashCommandOptionType.SUB_COMMAND) {
                System.err.println("Invalid Slash Command arg configuration");
                return null;
            }

            argsOptionsList.add(
                    SlashCommandOption.create(
                            annotation.type(),
                            field.getName(),
                            annotation.description(),
                            annotation.required()
                    )
            );
        }

        return argsOptionsList;
    }

    public static boolean parseBaseSubCommands(SlashCommandRunner<?> subcom, SlashCommandBuilder command) {
        List<SlashCommandOption> argsOptionsList = parseArgList(subcom);

        if (argsOptionsList == null) return false;

        if (!argsOptionsList.isEmpty()) {
            command.addOption(SlashCommandOption.createWithOptions(
                    SlashCommandOptionType.SUB_COMMAND,
                    subcom.name,
                    subcom.description,
                    argsOptionsList
            ));
        } else {
            command.addOption(SlashCommandOption.createWithOptions(
                    SlashCommandOptionType.SUB_COMMAND,
                    subcom.name,
                    subcom.description
            ));
        }

        return true;
    }

    public static boolean parseArgs(Field field, SlashCommandBuilder command) {
        var annotation = field.getAnnotation(SlashCommandArgField.class);

        if (annotation.type() == SlashCommandOptionType.SUB_COMMAND) {
            System.err.println("Invalid Slash Command arg configuration");
            return false;
        }

        command.addOption(
                SlashCommandOption.create(
                        annotation.type(),
                        field.getName(),
                        annotation.description(),
                        annotation.required()
                )
        );

        return true;
    }
}
