package io.github.maheevil.dukecordex.commandhandler;

import org.javacord.api.entity.permission.PermissionType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public record ChatCommandContainer(
        int nonOptionalArgCount,
        Field[] orderedFieldList,
        Method executionMethod,
        Class<?> argsClass,
        PermissionType[] requiredPerms,
        Extension extensionInstance
){
    public boolean hasNonOptionalArgs() {return nonOptionalArgCount > 0;}
    @Override public Field[] orderedFieldList() {return Arrays.copyOf(orderedFieldList, orderedFieldList.length);}
    @Override public PermissionType[] requiredPerms(){return Arrays.copyOf(requiredPerms,requiredPerms.length);}
}