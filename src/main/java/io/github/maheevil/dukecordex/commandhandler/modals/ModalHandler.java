package io.github.maheevil.dukecordex.commandhandler.modals;

import io.github.maheevil.dukecordex.commandhandler.annotations.ModalArgField;
import org.javacord.api.event.interaction.ModalSubmitEvent;
import org.javacord.api.interaction.ModalInteraction;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.function.Consumer;

public class ModalHandler {
    private static final HashMap<String, ModalRunner<?>> modalActionMap = new HashMap<>();
    private static final HashMap<String, Consumer<ModalSubmitEvent>> nonClassActionMap = new HashMap<>();

    public static void modalListener(ModalSubmitEvent modalSubmitEvent) {
        ModalInteraction modalInteraction = modalSubmitEvent.getModalInteraction();
        String customId = modalInteraction.getCustomId();
        ModalRunner<?> modalRunner = modalActionMap.getOrDefault(customId, null);
        Consumer<ModalSubmitEvent> nonClassRunner = nonClassActionMap.getOrDefault(customId, null);

        if (modalRunner == null && nonClassRunner == null) {
            System.out.println("Unknown Modal Received : " + customId);
            return;
        }

        if(modalRunner != null){
            try{
                Object args = parseArgs(modalRunner, modalInteraction);

                if(args == null){
                    return;
                }

                modalRunner.runConsumer(
                        args,
                        modalSubmitEvent
                );
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            modalActionMap.remove(customId);

        }else {
            nonClassRunner.accept(modalSubmitEvent);
            nonClassActionMap.remove(customId);
        }
    }

    private static Object parseArgs(
            ModalRunner<?> mainRunner,
            ModalInteraction optionProvider
    )
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
    {
        var innerClass = mainRunner.argClass();
        // fuck it with non-static inner classes, check and yell at the start
        var constructor = innerClass.getConstructor();
        var argsInstance = constructor.newInstance();

        for(Field field : mainRunner.filteredFieldList()){
            var value = optionProvider.getTextInputValueByCustomId(field.getName());
            var annotation = field.getAnnotation(ModalArgField.class);

            if(value.isEmpty()){
                if(annotation.required()){
                    System.err.println("Modal arg signature does not match the local signature");
                    return null;
                }

                field.set(argsInstance,null);
                continue;
            }

            var valueGet = value.get();

            // this useless code is kept for the future when I add custom conversion types (like time/colour etc)
            if(field.getType().isAssignableFrom(valueGet.getClass())){
                field.set(argsInstance,valueGet);
            }else if(annotation.required()){
                System.err.println("One of the Arg field's type cannot assign the argument value");
                return null;
            }else {
                System.err.println("Skipped assigning an value to an option field - the field type is not assignable to value type.");
                field.set(argsInstance,null);
            }
        }
        return argsInstance;
    }

    public static void addModalActionRunner(String customID, Consumer<ModalSubmitEvent> genericModalLambda){
        nonClassActionMap.put(customID, genericModalLambda);
    }

    public static void addModalActionRunner(String customId, ModalRunner<?> modalRunner){
        modalActionMap.put(customId, modalRunner);
    }

    public static boolean isUniqueID(String id){
        return !modalActionMap.containsKey(id) && !nonClassActionMap.containsKey(id);
    }

}
