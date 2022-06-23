package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import io.github.maheevil.dukecordex.commandhandler.annotations.ArgField;
import io.github.maheevil.dukecordex.commandhandler.annotations.SlashCommandArgField;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.util.Arrays;
import java.util.List;

public class SlashCommandCreatorUtils {
    public static void pushAllSlashCommands(List<SlashCommandEx> list, DiscordApi api){
        list.forEach(
                entry -> {
                    var command = SlashCommand.with(entry.baseName,entry.description);
                    if(entry.slashCommandGroups.isEmpty() && !entry.baseBranchingCommands.isEmpty()){
                        if(entry.baseBranchingCommands.containsKey("main") && entry.baseBranchingCommands.size() < 2){
                            Arrays.stream(entry.baseBranchingCommands.get("main").argsClass.getDeclaredFields())
                                    .filter(field -> field.isAnnotationPresent(SlashCommandArgField.class)).toList()
                                    .forEach(
                                            field -> {
                                                var annotation = field.getAnnotation(SlashCommandArgField.class);
                                                if(annotation.type() == SlashCommandOptionType.SUB_COMMAND){
                                                    System.err.println("Invalid Slash Command arg configuration");
                                                    return;
                                                }
                                                command.addOption(
                                                        SlashCommandOption.create(
                                                                annotation.type(),
                                                                field.getName(),
                                                                annotation.description(),
                                                                annotation.required()
                                                        )
                                                );
                                            }
                                    );
                            command.createForServer(api.getServerById("870341202652827648").orElseThrow()); // TODO - URGENT. ADD GUILD SPECIFIC AND GLOBAL OPTIONS
                        }else {
                            System.err.println("Invalid Slash Command Registration");
                        }
                    }
                }
        );
    }
}
