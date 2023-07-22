package io.github.maheevil.dukecordex.commandhandler.annotations;

import org.javacord.api.interaction.SlashCommandOptionType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SlashCommandArgField {
    SlashCommandOptionType type() default SlashCommandOptionType.STRING;

    String description() default "No description provided";

    boolean required() default true;
}
