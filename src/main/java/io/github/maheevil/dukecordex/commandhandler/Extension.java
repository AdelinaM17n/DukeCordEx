package io.github.maheevil.dukecordex.commandhandler;

import io.github.maheevil.dukecordex.DukeCordEx;
import io.github.maheevil.dukecordex.commandhandler.annotations.NoArgs;
import io.github.maheevil.dukecordex.commandhandler.annotations.SlashCommandArgField;
import io.github.maheevil.dukecordex.commandhandler.slashcommands.*;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
@SuppressWarnings("SameParameterValue")
public class Extension {
    protected static String configSetting(){
        return "GLOBAL";
        //TODO - AN ACTUAL FUCKING CONFIG SYSTEM
    }
    protected static String global(){
        return "GLOBAL";
    }
    protected final <T>ChatCommandEntry registerChatCommand(String name, Class<T> tClass, PermissionType[] permissionTypes, BiConsumer<T, MessageCreateEvent> consumer){
        if(!DukeCordEx.CommandMap.containsKey(name)) {
            DukeCordEx.CommandMap.put(name, new ChatCommandContainer<>(
                    consumer, tClass, this, permissionTypes
            ));
            return new ChatCommandEntry(name,true);
        }else {
            return new ChatCommandEntry(name,false);
        }
    }

    protected final ChatCommandEntry registerChatCommand(String name, PermissionType[] permissionTypes, Consumer<MessageCreateEvent> consumer){
        if(!DukeCordEx.CommandMap.containsKey(name)) {
            DukeCordEx.CommandMap.put(name, new ChatCommandContainer<>(
                    consumer, NoArgs.class,this,permissionTypes
            ));
            return new ChatCommandEntry(name,true);
        }else {
            return new ChatCommandEntry(name,false);
        }
    }

    protected final <T>ChatCommandEntry registerChatCommand(String name, Class<T> tClass, BiConsumer<T, MessageCreateEvent> consumer){
        if(!DukeCordEx.CommandMap.containsKey(name)) {
            DukeCordEx.CommandMap.put(name, new ChatCommandContainer<>(
                    consumer, tClass, this, null
            ));
            return new ChatCommandEntry(name,true);
        }else {
            return new ChatCommandEntry(name,false);
        }
    }

    protected final ChatCommandEntry registerChatCommand(String name, Consumer<MessageCreateEvent> consumer){
        if(!DukeCordEx.CommandMap.containsKey(name)) {
            DukeCordEx.CommandMap.put(name, new ChatCommandContainer<>(
                    consumer, NoArgs.class,this,null
            ));
            return new ChatCommandEntry(name,true);
        }else {
            return new ChatCommandEntry(name,false);
        }
    }

    protected final<T> Object registerBasicSlashCommand(
            String baseName, String description, String guild, ReplyType replyType, Class<T> argClass, BiConsumer<T, SlashCommandContext> consumer
    ){
        var commandInstance = new SlashCommandEx(baseName,description,guild,this);
        var fieldlist = Arrays.stream(argClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(SlashCommandArgField.class)).toList();
        commandInstance.baseBranchingCommands.put(
                "main",
                new SlashCommandRunner<>(
                        "main",
                        null,
                        replyType,
                        argClass,
                        fieldlist,
                        consumer
                )
        );
        DukeCordEx.SlashCommandMap.put(baseName,commandInstance);
        return null;
    }

    protected final<T> Object registerBasicSlashCommand(
            String baseName, String description, String guild, ReplyType replyType, Consumer<SlashCommandContext> consumer
    ){
        var commandInstance = new SlashCommandEx(baseName,description,guild,this);
        commandInstance.baseBranchingCommands.put(
                "main",
                new SlashCommandRunner<>(
                        "main",
                        null,
                        replyType,
                        consumer
                )
        );
        DukeCordEx.SlashCommandMap.put(baseName,commandInstance);
        return null;
    }

    protected final Object registerBasicSlashCommand(String baseName, String description, String guild, SlashCommandRunner<?>... slashCommandRunners){
        var baseCommandInstance = new SlashCommandEx(baseName,description,guild,this);
        Arrays.stream(slashCommandRunners).forEach(
                tSlashCommandRunner -> baseCommandInstance.baseBranchingCommands.put(tSlashCommandRunner.name,tSlashCommandRunner)
        );
        DukeCordEx.SlashCommandMap.put(baseName,baseCommandInstance);
        return null;
    }

    protected final Object registerGroupedSlashCommand(String baseName, String description, String guild, SlashCommandGroup... slashCommandGroups){
        var baseCommandInstance = new SlashCommandEx(baseName,description,guild,this);
        Arrays.stream(slashCommandGroups).forEach(
                slashCommandGroup -> baseCommandInstance.slashCommandGroups.put(slashCommandGroup.name, slashCommandGroup)
        );
        DukeCordEx.SlashCommandMap.put(baseName,baseCommandInstance);
        return null;
    }
}
