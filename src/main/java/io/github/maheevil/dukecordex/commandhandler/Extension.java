package io.github.maheevil.dukecordex.commandhandler;

import io.github.maheevil.dukecordex.DukeCordEx;
import io.github.maheevil.dukecordex.commandhandler.annotations.NoArgs;
import io.github.maheevil.dukecordex.commandhandler.annotations.SlashCommandArgField;
import io.github.maheevil.dukecordex.commandhandler.slashcommands.SlashCommandEx;
import io.github.maheevil.dukecordex.commandhandler.slashcommands.SlashCommandGroup;
import io.github.maheevil.dukecordex.commandhandler.slashcommands.SlashCommandRunner;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.event.message.MessageCreateEvent;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
            String baseName, String description, String guild, Class<T> argClass, BiConsumer<T, SlashCommandCreateEvent> consumer
    ){
        var commandInstance = new SlashCommandEx(baseName,description,guild,this);
        var fieldlist = Arrays.stream(argClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(SlashCommandArgField.class)).toList();
        commandInstance.baseBranchingCommands.put(
                "main",
                new SlashCommandRunner<>(
                        "main",
                        null,
                        argClass,
                        fieldlist,
                        consumer
                )
        );
        DukeCordEx.SlashCommandMap.put(baseName,commandInstance);
        return null;
    }

    protected final<T> Object registerBasicSlashCommand(
            String baseName, String description, String guild, Consumer<SlashCommandCreateEvent> consumer
    ){
        var commandInstance = new SlashCommandEx(baseName,description,guild,this);
        commandInstance.baseBranchingCommands.put(
                "main",
                new SlashCommandRunner<>(
                        "main",
                        null,
                        consumer
                )
        );
        DukeCordEx.SlashCommandMap.put(baseName,commandInstance);
        return null;
    }
    @SafeVarargs
    protected final<T> Object registerBasicSlashCommand(String baseName, String description, String guild, SlashCommandRunner<T>... slashCommandRunners){
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
