package io.github.maheevil.dukecordex.commandhandler.annotations;

import org.javacord.api.interaction.SlashCommandOptionType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SlashCommandArgField {
    SlashCommandOptionType type() default SlashCommandOptionType.STRING;
    String description() default "No description provided";
    boolean required() default true;
}
