package io.github.maheevil.dukecordex.commandhandler.modals;

import org.javacord.api.event.interaction.ModalSubmitEvent;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiConsumer;

public record ModalRunner<T>(
        Class<T> argClass,
        List<Field> filteredFieldList,
        BiConsumer<ModalSubmitEvent, T> actionRunner
){
    public void runConsumer(Object argObject, ModalSubmitEvent context){
        this.actionRunner.accept(context, this.argClass.cast(argObject));
    }
}
