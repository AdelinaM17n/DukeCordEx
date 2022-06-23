package io.github.maheevil.dukecordex.commandhandler;

import io.github.maheevil.dukecordex.commandhandler.annotations.ArgField;
import io.github.maheevil.dukecordex.commandhandler.annotations.NoArgs;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.event.message.MessageCreateEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ChatCommandContainer<T> {
    private final BiConsumer<T, MessageCreateEvent> BiConsumer;
    private final Consumer<MessageCreateEvent> consumer;
    public Class<T> argClass;
    private final PermissionType[] requiredPerms;
    private final Field[] orderedFieldList;
    public final int nonOptionalArgCount;
    public final boolean hasNonOptionalArgs;
    public final boolean hasRequiredPerms;
    public final Extension extensionInstance;

    public ChatCommandContainer(BiConsumer<T,MessageCreateEvent> BiConsumer, Class<T> argClass, Extension extension, PermissionType[] requiredPerms){
        this.BiConsumer = BiConsumer;
        this.consumer = null;
        this.argClass = argClass;
        this.requiredPerms = requiredPerms;
        this.extensionInstance = extension;
        this.orderedFieldList = initOrderedFieldList(argClass);
        this.nonOptionalArgCount = findNonOptionalArgCount(argClass);
        this.hasNonOptionalArgs = nonOptionalArgCount > 0;
        this.hasRequiredPerms= requiredPerms != null && requiredPerms.length > 0;
    }

    public ChatCommandContainer(Consumer<MessageCreateEvent> consumer,Class<T> argClass,Extension extension, PermissionType[] requiredPerms){
        this.BiConsumer = null;
        this.consumer = consumer;
        this.requiredPerms = requiredPerms;
        this.extensionInstance = extension;
        this.argClass = argClass;
        this.orderedFieldList = initOrderedFieldList(argClass);
        this.nonOptionalArgCount = findNonOptionalArgCount(argClass);
        this.hasNonOptionalArgs = nonOptionalArgCount > 0;
        this.hasRequiredPerms= requiredPerms != null && requiredPerms.length >= 1;
    }

    public Field[] initOrderedFieldList(Class<?> argClass){
        var fieldList = new ArrayList<>(Arrays.asList(argClass.getDeclaredFields()));
        fieldList.removeIf(field -> !field.isAnnotationPresent(ArgField.class));
        Field[] orderedList = new Field[fieldList.size()];
        for(Field field : fieldList){ orderedList[field.getAnnotation(ArgField.class).index()] = field; }
        return orderedList;
    }

    private int findNonOptionalArgCount(Class<?> clazz){
        if(clazz == NoArgs.class) return 0;
        var fieldList =  new ArrayList<>(Arrays.stream(clazz.getDeclaredFields()).toList());
        fieldList.removeIf(field -> !field.isAnnotationPresent(ArgField.class) || (field.isAnnotationPresent(ArgField.class) && field.getAnnotation(ArgField.class).optional()));
        return fieldList.size();
    }

    public Field[] getOrderedFieldList() {
        return Arrays.copyOf(this.orderedFieldList,this.orderedFieldList.length);
    }

    public PermissionType[] getRequiredPerms(){
        return Arrays.copyOf(this.requiredPerms,this.requiredPerms.length);
    }

    public void runConsumer(Object argObject, MessageCreateEvent context){
        if(argObject != null)
            this.BiConsumer.accept(this.argClass.cast(argObject),context);
        else
            this.consumer.accept(context);
    }
}