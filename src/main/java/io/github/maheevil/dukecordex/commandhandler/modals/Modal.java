package io.github.maheevil.dukecordex.commandhandler.modals;

import io.github.maheevil.dukecordex.commandhandler.annotations.ModalArgField;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.entity.message.component.TextInput;
import org.javacord.api.event.interaction.ModalSubmitEvent;
import org.javacord.api.interaction.InteractionBase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Modal {
    public static void create(
            String customId,
            String title,
            List<HighLevelComponent> highLevelComponentList,
            InteractionBase interactionBase,
            Consumer<ModalSubmitEvent> actionRunner
    ){
        if(!ModalHandler.isUniqueID(customId)){
            System.out.println("There is another queried modal with the same ID : " + customId);
            return;
        }

        ModalHandler.addModalActionRunner(customId, actionRunner);
        interactionBase.respondWithModal(customId,title, highLevelComponentList);
    }

    public static <T> void create(
            String customId,
            String title,
            Class<T> tClass,
            InteractionBase interactionBase,
            BiConsumer<ModalSubmitEvent, T> actionRunner
    ){
        if(!ModalHandler.isUniqueID(customId)){
            System.out.println("There is another queried modal with the same ID : " + customId);
            return;
        }

        List<Field> fieldList = Arrays.stream(tClass.getDeclaredFields()).filter(x -> x.isAnnotationPresent(ModalArgField.class)).toList();
        List<HighLevelComponent> highLevelComponentList = parseComponents(fieldList);

        ModalRunner<T> tModalRunner = new ModalRunner<>(tClass, fieldList, actionRunner);
        ModalHandler.addModalActionRunner(customId, tModalRunner);

        interactionBase.respondWithModal(customId,title,highLevelComponentList);
    }

    public static List<HighLevelComponent> parseComponents(List<Field> fields){
        List<HighLevelComponent> highLevelComponentList = new ArrayList<>();

        for(Field field : fields){
            ModalArgField annotation = field.getAnnotation(ModalArgField.class);

            highLevelComponentList.add(
                    ActionRow.of(
                            TextInput.create(
                                    annotation.textInputStyle(),
                                    field.getName(),
                                    annotation.label(),
                                    annotation.required()
                            )
                    )
            );
        }

        return highLevelComponentList;
    }
}
