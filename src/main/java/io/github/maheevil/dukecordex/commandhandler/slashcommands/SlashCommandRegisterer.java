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

                            Arrays.stream(entry.baseBranchingCommands.get("main").argsClass.getDeclaredFields())
                                    .filter(field -> field.isAnnotationPresent(SlashCommandArgField.class))
                                    .forEach(field -> error.set(!parseArgs(field, command)));

                            // TODO - URGENT. ADD GUILD SPECIFIC AND GLOBAL OPTIONS
                            if(!error.get()) command.createForServer(api.getServerById("870341202652827648").orElseThrow());
                            else System.err.println("Parsing arguments failed");

                        }else if(entry.baseBranchingCommands.size() > 1){
                            AtomicBoolean error = new AtomicBoolean(false);

                            entry.baseBranchingCommands.values().forEach(
                                    baseSubCommand -> error.set(!parseBaseSubCommands(baseSubCommand,command))
                            );

                            if(!error.get()) command.createForServer(api.getServerById("870341202652827648").orElseThrow());
                            else System.err.println("Parsing arguments failed");
                        }
                    }
                }
        );
    }

    public static boolean parseBaseSubCommands(SlashCommandRunner<?> subcom, SlashCommandBuilder command){
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

        if(encounteredError.get()) return false;

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

    //public static boolean

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
