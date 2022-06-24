package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import io.github.maheevil.dukecordex.commandhandler.annotations.SlashCommandArgField;
import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SlashCommandRegisterer {
    public static void pushAllSlashCommands(List<SlashCommandEx> list, DiscordApi api){
        list.forEach(
                entry -> {
                    var command = SlashCommand.with(entry.baseName,entry.description);

                    if(entry.slashCommandGroups.isEmpty() && !entry.baseBranchingCommands.isEmpty()){
                        if(entry.baseBranchingCommands.containsKey("main") && entry.baseBranchingCommands.size() < 2){
                            AtomicBoolean error = new AtomicBoolean(false);

                            var runner = entry.baseBranchingCommands.get("main");
                            if(runner.argsClass != null){
                                Arrays.stream(runner.argsClass.getDeclaredFields())
                                        .filter(field -> field.isAnnotationPresent(SlashCommandArgField.class))
                                        .forEach(field -> error.set(!parseArgs(field, command)));
                            }

                            if(!error.get()) pushToDiscord(command, api, entry.guildOrNotId);
                            else System.err.println("Parsing arguments failed");

                        }else if(entry.baseBranchingCommands.size() > 1){
                            AtomicBoolean error = new AtomicBoolean(false);

                            entry.baseBranchingCommands.values().forEach(
                                    baseSubCommand -> error.set(!parseBaseSubCommands(baseSubCommand,command))
                            );

                            if(!error.get()) pushToDiscord(command, api, entry.guildOrNotId);
                            else System.err.println("Parsing arguments failed");

                        }
                    } else if(!entry.slashCommandGroups.isEmpty()) {
                        entry.slashCommandGroups.values().forEach(
                                slashCommandGroup -> {
                                    List<SlashCommandOption> groupedSubCommands = getGroupSubComList(slashCommandGroup);
                                    command.addOption(SlashCommandOption.createWithOptions(
                                            SlashCommandOptionType.SUB_COMMAND_GROUP,
                                            slashCommandGroup.name,
                                            slashCommandGroup.description,
                                            groupedSubCommands
                                    ));
                                }
                        );
                        // TODO - VERY FUCKING URGENT - BETTER ERROR HANDLING
                        pushToDiscord(
                                command, api, entry.guildOrNotId
                        );

                    }
                }
        );
    }

    private static void pushToDiscord(SlashCommandBuilder command, DiscordApi api, String guildOrNotId){
        if(guildOrNotId.equals("GLOBAL")) {
            command.createGlobal(api);
        }else {
            command.createForServer(api.getServerById(guildOrNotId).orElseThrow());
        }
    }
    public static List<SlashCommandOption> getGroupSubComList(SlashCommandGroup group){
        List<SlashCommandOption> list = new ArrayList<>();
        group.runners.values().forEach(
                subCommandRunner -> {
                    List<SlashCommandOption> argsOptionsList = parseArgList(subCommandRunner);

                    if(argsOptionsList == null){
                        System.err.println("Error");
                        return;
                    }

                    if(!argsOptionsList.isEmpty()){
                        list.add(
                                SlashCommandOption.createWithOptions(
                                        SlashCommandOptionType.SUB_COMMAND,
                                        subCommandRunner.name,
                                        subCommandRunner.description,
                                        argsOptionsList
                                )
                        );
                    }else {
                        list.add(
                                SlashCommandOption.create(
                                        SlashCommandOptionType.SUB_COMMAND,
                                        subCommandRunner.name,
                                        subCommandRunner.description
                                )
                        );
                    }
                }
        );
        return list;
    }
    public static List<SlashCommandOption> parseArgList(SlashCommandRunner<?> subcom){
        AtomicBoolean encounteredError = new AtomicBoolean(false);
        List<SlashCommandOption> argsOptionsList = new ArrayList<>();

        Arrays.stream(subcom.argsClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(SlashCommandArgField.class))
                .forEach(
                        field -> {
                            var annotation = field.getAnnotation(SlashCommandArgField.class);
                            if(annotation.type() == SlashCommandOptionType.SUB_COMMAND){
                                System.err.println("Invalid Slash Command arg configuration");
                                encounteredError.set(false);
                                return;
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
                );
        return encounteredError.get() ? null : argsOptionsList;
    }
    public static boolean parseBaseSubCommands(SlashCommandRunner<?> subcom, SlashCommandBuilder command){
        List<SlashCommandOption> argsOptionsList = parseArgList(subcom);

        if(argsOptionsList == null) return false;

        if(!argsOptionsList.isEmpty()){
            command.addOption(SlashCommandOption.createWithOptions(
                    SlashCommandOptionType.SUB_COMMAND,
                    subcom.name,
                    subcom.description,
                    argsOptionsList
            ));
        }else {
            command.addOption(SlashCommandOption.createWithOptions(
                    SlashCommandOptionType.SUB_COMMAND,
                    subcom.name,
                    subcom.description
            ));
        }

       return true;
    }

    public static boolean parseArgs(Field field, SlashCommandBuilder command){
        var annotation = field.getAnnotation(SlashCommandArgField.class);
        if(annotation.type() == SlashCommandOptionType.SUB_COMMAND){
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
