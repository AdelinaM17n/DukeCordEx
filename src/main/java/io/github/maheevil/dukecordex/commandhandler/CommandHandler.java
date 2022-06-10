package io.github.maheevil.dukecordex.commandhandler;

import io.github.maheevil.dukecordex.DukeCordEx;
import io.github.maheevil.dukecordex.commandhandler.annotations.ArgField;
import io.github.maheevil.dukecordex.commandhandler.annotations.ArgParseType;
import io.github.maheevil.dukecordex.commandhandler.annotations.NoArgs;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.message.MessageCreateEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CommandHandler {
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
            message.reply("Please enter arguement values needed for the command");
            return;
        }

        var guild = event.getServer().orElseThrow();
        var member = guild.getMemberById(event.getMessageAuthor().getId()).orElse(null);

        if(member == null){
            message.reply("Something went wrong");
        }

        if(!guild.hasPermissions(member,commandObject.requiredPerms())){
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
    /*@Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){
        if(event.getAuthor().isBot() || !event.getMessage().getContentRaw().startsWith(UntitledBot.prefix))
            return;

        String[] messageContents = event.getMessage().getContentRaw().substring(1).split("\\s");
        String command = messageContents[0];
        var commandArgContents = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(messageContents, 1, messageContents.length)));

        if(!UntitledBot.CommandMap.containsKey(command)) return;

        ChatCommandContainer commandObject = UntitledBot.CommandMap.get(command);
        var args = parseArgument(commandArgContents,commandObject, event.getJDA());

        if (args == null && commandObject.hasNonOptionalArgs()) {
            // TODO Perhaps it should indicate the user that the args weren't correct by replying to the command here
            // Since I am trying to make this platform independent as possible I won't do it currently.
            return;
        }

        if(!Objects.requireNonNull(event.getMember()).hasPermission(commandObject.requiredPerms())){
            event.getMessage().reply("Perms not met").complete();
            return;
        }

        try{
            if(commandObject.argsClass() == NoArgs.class || (!commandObject.hasNonOptionalArgs() && args == null)){
                commandObject.executionMethod().invoke(commandObject.extensionInstance(),event.getMessage(),event.getGuild());
                return;
            }
            var innerClass = commandObject.argsClass();
            var constructor = innerClass.getConstructor(commandObject.extensionInstance().getClass()); //innerClass.getDeclaredConstructor(commandObject.getArgsFieldTypeList());
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

            commandObject.executionMethod().invoke(commandObject.extensionInstance(),argsInstance,event.getMessage(),event.getGuild());
        }catch(NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }

    }*/
    public static Object[] parseArgument(ArrayList<String> contents, ChatCommandContainer containerObject, DiscordApi apiWrapper){
        if(contents.size() < containerObject.nonOptionalArgCount()) return null;

        Field[] orderedList = containerObject.orderedFieldList();
        Object[] listObjects = new Object[orderedList.length];
        //listObjects[0] = containerObject.extensionInstance();//commandObject.getClass().cast(commandObject);
        //HashMap<String,Object> hashMap = new HashMap<>();
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
                var ee = "ee";
                /*var orderedListType = orderedList[i].getType();
                if(orderedListType == String.class){
                    listObjects[i] = contents.get(0);
                    orderedList[i] = null;
                }else if (orderedListType == User.class){
                    if(contents.get(0).startsWith("<@")){
                        listObjects[i] = apiWrapper.retrieveUserById(contents.get(0).substring(2, 20)).complete();
                    }else{
                        listObjects[i] = apiWrapper.retrieveUserById(contents.get(0)).complete();
                    }
                    orderedList[i] = null;
                }else {
                    listObjects[i] = null;
                }
                contents.remove(0);
                orderedList[i] = null;*/
            }
        }

        return Arrays.stream(orderedList).anyMatch(field -> field != null && !field.getAnnotation(ArgField.class).optional()) ? null : listObjects;
    }
}
