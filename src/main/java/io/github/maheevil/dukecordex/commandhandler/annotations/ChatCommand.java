package io.github.maheevil.dukecordex.commandhandler.annotations;

import org.javacord.api.entity.permission.PermissionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ChatCommand {
    String name();

    Class<?> argsClass() default NoArgs.class;

    PermissionType[] requiredPerms() default {};
}
