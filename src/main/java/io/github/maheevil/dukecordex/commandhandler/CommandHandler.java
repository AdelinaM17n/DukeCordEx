package io.github.maheevil.dukecordex.commandhandler;

import io.github.maheevil.dukecordex.DukeCordEx;
import io.github.maheevil.dukecordex.commandhandler.annotations.ArgField;
import io.github.maheevil.dukecordex.commandhandler.annotations.ArgParseType;
import io.github.maheevil.dukecordex.commandhandler.annotations.NoArgs;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CommandHandler {
    public static HashMap<Class<?>, ConvertorInterface> converterMap = new HashMap<>(Map.ofEntries(
            Map.entry(String.class, (string , discordApi) -> string),
            Map.entry(User.class, (string , discordApi) -> {
                if(string.startsWith("<@")){
                    return discordApi.getUserById(string.substring(2, 20)).exceptionally(x -> null).join();
                }else{
                    return discordApi.getUserById(string).exceptionally(x -> null).join();
                }
            }),
            Map.entry(ServerTextChannel.class,(string, discordApi) -> {
                if(string.startsWith("<#")){
                    return discordApi.getServerTextChannelById(string.substring(2, 20)).orElse(null);
                }else{
                    return discordApi.getServerTextChannelById(string).orElse(null);
                }
            }),
            Map.entry(Channel.class,((string, discordApi) -> {
                if(string.startsWith("<#")){
                    return discordApi.getChannelById(string.substring(2, 20)).orElse(null);
                }else{
                    return discordApi.getChannelById(string).orElse(null);
                }
            }))
    ));

    public interface ConvertorInterface {
        Object get(String string,DiscordApi discordApi);
    }

    public static void onMessageCreate(MessageCreateEvent event){
        if(!event.isServerMessage() || !event.getMessageAuthor().isUser() || !event.getMessageContent().startsWith("!"))
            return;

        var message = event.getMessage();
        String[] messageContents = message.getContent().substring(1).split("\\s");
        String command = messageContents[0];
        var commandArgContents = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(messageContents, 1, messageContents.length)));

        if(!DukeCordEx.CommandMap.containsKey(command)) return;

        ChatCommandContainer commandObject = DukeCordEx.CommandMap.get(command);
        var args = parseArgument(commandArgContents,commandObject, event.getApi());

        if (args == null && commandObject.hasNonOptionalArgs()) {
            message.reply("Please enter argument values needed for the command");
            return;
        }

        var guild = event.getServer().orElseThrow();
        var commandUser = message.getUserAuthor().orElse(null);

        if(commandUser == null){
            message.reply("Something went wrong");
            return;
        }

        if(!guild.hasPermissions(commandUser,commandObject.requiredPerms())){
            message.reply("You are missing permissions needed for the command");
            return;
        }

        try{
            if(commandObject.argsClass() == NoArgs.class || (!commandObject.hasNonOptionalArgs() && args == null)){
                commandObject.executionMethod().invoke(commandObject.extensionInstance(),message,guild);
                return;
            }
            var innerClass = commandObject.argsClass();
            var constructor = innerClass.getConstructor(commandObject.extensionInstance().getClass());
            var argsInstance = constructor.newInstance(commandObject.extensionInstance());
            var orderedList = commandObject.orderedFieldList();

            assert args != null;
            for(int i=0; i < orderedList.length; i++){
                if(args[i] != null && !orderedList[i].getType().isAssignableFrom(args[i].getClass())){
                    System.err.println("Arg type does not match");
                    return;
                }
                orderedList[i].set(argsInstance,args[i]);
            }

            commandObject.executionMethod().invoke(commandObject.extensionInstance(),argsInstance,message,guild);
        }catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }
    }

    public static Object[] parseArgument(ArrayList<String> contents, ChatCommandContainer containerObject, DiscordApi apiWrapper){
        if(contents.size() < containerObject.nonOptionalArgCount()) return null;

        Field[] orderedList = containerObject.orderedFieldList();
        Object[] listObjects = new Object[orderedList.length];
        boolean hasMetCoalesc = false;

        for(int i =0; i <= orderedList.length-1; i++){
            if(orderedList[i] == null) return null;

            if(hasMetCoalesc){
                listObjects[i] = null;
            }else if(orderedList[i].getAnnotation(ArgField.class).type() == ArgParseType.STRING_COALESCING){
                listObjects[i] =  String.join(" ", contents);
                orderedList[i] = null;
                hasMetCoalesc = true;
            }else {
                var currentFieldType = orderedList[i].getType();
                listObjects[i] = converterMap.containsKey(currentFieldType)
                        ? converterMap.get(currentFieldType).get(contents.get(0),apiWrapper) : null;
                if(listObjects[i] != null) orderedList[i] = null;
                contents.remove(0);
            }
        }

        return Arrays.stream(orderedList)
                .anyMatch(field -> field != null && !field.getAnnotation(ArgField.class).optional())
                ? null : listObjects;
    }
}
