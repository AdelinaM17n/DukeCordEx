package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import io.github.maheevil.dukecordex.DukeCordEx;
import io.github.maheevil.dukecordex.commandhandler.annotations.SlashCommandArgField;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

public class SlashCommandHandler {
    public static void handleSlashCommandEvent(SlashCommandCreateEvent event) {
        var slashCommandInteraction = event.getSlashCommandInteraction();
        if(!DukeCordEx.SlashCommandMap.containsKey(slashCommandInteraction.getCommandName())){
            System.err.println("Unknown command");
            return;
        }

        var commandInstance = DukeCordEx.SlashCommandMap.get(slashCommandInteraction.getCommandName());
        var firstOption = slashCommandInteraction.getOptionByIndex(0).orElse(null);

        if(firstOption == null || !firstOption.isSubcommandOrGroup()){
            if(!commandInstance.baseBranchingCommands.containsKey("main")){
                System.err.println("Invalid command registration");
                return;
            }

            var mainRunner = commandInstance.baseBranchingCommands.get("main");

            if(firstOption == null){
                mainRunner.runConsumer(null,event);
                //return;
            }else {
                try{
                    var innerClass = mainRunner.argsClass;
                    var constructor = innerClass.getConstructor(commandInstance.extensionInstance.getClass());
                    var argsInstance = constructor.newInstance(commandInstance.extensionInstance);
                    //boolean encounteredError = false;

                    for(Field field : mainRunner.filteredFieldList){
                        var value = slashCommandInteraction.getOptionByName(field.getName());
                        var annotation = field.getAnnotation(SlashCommandArgField.class);

                        if(value.isEmpty() && annotation.required()){
                            System.err.println("Command arg signature does not match the local signature");
                            break;
                        }else if(value.isEmpty()){
                            field.set(argsInstance,null);
                            continue;
                        }

                        var valueGet = getOptionValueAsObject(value.get(), annotation.type());
                        if(field.getType().isAssignableFrom(valueGet.getClass())){
                            field.set(argsInstance,valueGet);
                        }else if(annotation.required()){
                            System.err.println("One of the Arg field's type cannot assign the argument value");
                            break;
                        }else {
                            System.err.println("Skipped assigning an value to an option field - the field type is not assignable to value type.");
                            field.set(argsInstance,null);
                        }
                    }

                    mainRunner.runConsumer(argsInstance,event);

                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static Object getOptionValueAsObject(SlashCommandInteractionOption value, SlashCommandOptionType type){
        return switch(type){
            case STRING -> value.getStringValue().orElseThrow();
            case USER -> value.getUserValue().orElse(value.requestUserValue().orElseThrow().join());
            case CHANNEL -> value.getChannelValue().orElseThrow();
            case BOOLEAN -> value.getBooleanValue().orElseThrow();
            case DECIMAL -> value.getDecimalValue().orElseThrow();
            case MENTIONABLE -> value.getMentionableValue().orElse(value.requestUserValue().orElseThrow().join());
            case LONG -> value.getLongValue().orElseThrow();
            case ROLE -> value.getRoleValue().orElseThrow();
            case UNKNOWN, SUB_COMMAND, SUB_COMMAND_GROUP -> null;
        };
    }
}
