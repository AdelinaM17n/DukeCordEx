package io.github.maheevil.dukecordex.commandhandler.modals;

import org.javacord.api.entity.message.component.HighLevelComponent;
import org.javacord.api.event.interaction.ModalSubmitEvent;
import org.javacord.api.interaction.InteractionBase;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Modal {
    public static void create(
            String customId,
            String title,
            List<HighLevelComponent> stuff,
            InteractionBase interactionBase,
            Consumer<ModalSubmitEvent> actionRunner
    ){
        if(!ModalHandler.isUniqueID(customId)){
            System.out.println("There is another queried modal with the same ID : " + customId);
            return;
        }

        interactionBase.respondWithModal(customId,title, stuff);
        ModalHandler.addModalActionRunner(customId, actionRunner);
    }

    public static <T> void create(
            String customId,
            Class<T> tClass,
            InteractionBase interactionBase,
            BiConsumer<ModalSubmitEvent, T> actionRunner
    ){

    }
}
