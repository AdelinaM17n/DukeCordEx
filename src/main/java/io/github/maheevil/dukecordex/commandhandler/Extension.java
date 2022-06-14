package io.github.maheevil.dukecordex.commandhandler;

import io.github.maheevil.dukecordex.DukeCordEx;
import io.github.maheevil.dukecordex.commandhandler.annotations.NoArgs;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Extension {
    protected final  <T>ChatCommandEntry registerChatCommand(String name, Class<T> tClass, PermissionType[] permissionTypes, BiConsumer<T, MessageCreateEvent> consumer){
        if(!DukeCordEx.CommandMap.containsKey(name)) {
            DukeCordEx.CommandMap.put(name, new ChatCommandContainer<T>(
                    consumer, tClass,this,permissionTypes
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

    protected final  <T>ChatCommandEntry registerChatCommand(String name, Class<T> tClass, BiConsumer<T, MessageCreateEvent> consumer){
        if(!DukeCordEx.CommandMap.containsKey(name)) {
            DukeCordEx.CommandMap.put(name, new ChatCommandContainer<T>(
                    consumer, tClass,this,null
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
}
