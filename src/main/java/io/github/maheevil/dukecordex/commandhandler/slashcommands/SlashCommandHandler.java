package io.github.maheevil.dukecordex.commandhandler.slashcommands;

import io.github.maheevil.dukecordex.DukeCordEx;
import io.github.maheevil.dukecordex.commandhandler.Extension;
import io.github.maheevil.dukecordex.commandhandler.annotations.SlashCommandArgField;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class SlashCommandHandler {
    public static void handleSlashCommandEvent(SlashCommandCreateEvent event) {
        var slashCommandInteraction = event.getSlashCommandInteraction();
        if(!DukeCordEx.SlashCommandMap.containsKey(slashCommandInteraction.getCommandName())){
            System.err.println("Unknown command");
            return;
        }

        var commandInstance = DukeCordEx.SlashCommandMap.get(slashCommandInteraction.getCommandName());
        var baseSubfirstOption = slashCommandInteraction.getOptionByIndex(0).orElse(null);

        if(baseSubfirstOption == null || !baseSubfirstOption.isSubcommandOrGroup()){
            runCommandRunner(
                    "main",
                    commandInstance.baseBranchingCommands,
                    baseSubfirstOption,
                    slashCommandInteraction,
                    event,
                    commandInstance.extensionInstance
            );
        }else{
            var subFirstOption = baseSubfirstOption.getOptionByIndex(0).orElse(null);
            if(subFirstOption == null || !subFirstOption.isSubcommandOrGroup()){
                runCommandRunner(
                        baseSubfirstOption.getName(),
                        commandInstance.baseBranchingCommands,
                        subFirstOption,
                        baseSubfirstOption,
                        event,
                        commandInstance.extensionInstance
                );
            }else {
                var subSubFirstOption = subFirstOption.getOptionByIndex(0).orElse(null);

                if(!commandInstance.slashCommandGroups.containsKey(baseSubfirstOption.getName())){
                    System.err.println("Invalide Command registration");
                    return;
                }

                if(subSubFirstOption == null || !subSubFirstOption.isSubcommandOrGroup()){
                    runCommandRunner(
                            subFirstOption.getName(),
                            commandInstance.slashCommandGroups.get(baseSubfirstOption.getName()).runners,
                            subSubFirstOption,
                            subFirstOption,
                            event,
                            commandInstance.extensionInstance
                    );
                }else {
                    System.err.println("How did we get here? - received an invalid slash command run object");
                }
            }

        }
    }

    private static void runCommandRunner(
            String commandRunnerName,
            HashMap<String,SlashCommandRunner<?>> commandRunnerHashMap,
            SlashCommandInteractionOption subFirstOption,
            Object parentOptionProvider,
            SlashCommandCreateEvent event,
            Extension extension
    ){
        if(!commandRunnerHashMap.containsKey(commandRunnerName)){
            System.err.println("Invalid command registration");
            return;
        }

        var mainRunner = commandRunnerHashMap.get(commandRunnerName);

        if(subFirstOption == null){
            mainRunner.runConsumer(null,event);
        }else {
            try{
                mainRunner.runConsumer(
                        parseArgs(
                                mainRunner,
                                extension,
                                parentOptionProvider,
                                commandRunnerName.equals("main")
                        ),
                        event
                );
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Object parseArgs(
            SlashCommandRunner<?> mainRunner,
            Extension extensionInstance,
            Object optionProvider,
            boolean baseCommand
    )
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
    {
        var innerClass = mainRunner.argsClass;
        var constructor = innerClass.getConstructor(extensionInstance.getClass());
        var argsInstance = constructor.newInstance(extensionInstance);
        //boolean encounteredError = false;

        for(Field field : mainRunner.filteredFieldList){
            var value = baseCommand
                    ? ((SlashCommandInteraction) optionProvider).getOptionByName(field.getName())
                    : ((SlashCommandInteractionOption) optionProvider).getOptionByName(field.getName());
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
        return argsInstance;
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
