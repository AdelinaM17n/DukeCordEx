package io.github.maheevil.dukecordex.commandhandler.annotations;

import org.javacord.api.entity.permission.PermissionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ChatCommand {
    String name();
    Class<?> argsClass() default NoArgs.class;
    PermissionType[] requiredPerms() default {};
}
