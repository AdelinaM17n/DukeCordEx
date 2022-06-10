package io.github.maheevil.dukecordex.commandhandler;

import io.github.maheevil.dukecordex.DukeCordEx;
import io.github.maheevil.dukecordex.commandhandler.annotations.ArgField;
import io.github.maheevil.dukecordex.commandhandler.annotations.ChatCommand;
import io.github.maheevil.dukecordex.commandhandler.annotations.NoArgs;
import org.javacord.api.entity.message.Message;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class Extension {
    public Message message = customConverter();
    public void init(){
        var methods = Arrays.stream(this.getClass().getDeclaredMethods()).filter(method -> method.isAnnotationPresent(ChatCommand.class));
        methods.forEach(method -> {
            var annotation = method.getAnnotation(ChatCommand.class);

            if(DukeCordEx.CommandMap.containsKey(annotation.name())) {
                System.err.println("Two commands cannot have the same name");
                return;
            }

            var orderedFieldList = findOrderedFieldList(annotation.argsClass());

            ChatCommandContainer container = new ChatCommandContainer(
                    findNonOptionalArgCount(annotation.argsClass()),
                    orderedFieldList,
                    method,
                    annotation.argsClass(),
                    //typeList,
                    annotation.requiredPerms(),
                    this
            );
            DukeCordEx.CommandMap.put(annotation.name(), container);
        });
    }

    private Field[] findOrderedFieldList(Class<?> clazz){
        var fieldList = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
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

    public <T>T customConverter(){
        return null;
    }
}
