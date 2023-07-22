package io.github.maheevil.dukecordex.commandhandler.annotations;

import org.javacord.api.entity.message.component.TextInputStyle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModalArgField {
    int maxLength() default 0;

    int minLength() default 0;

    String label();

    String defaultValue() default "Default Value";

    TextInputStyle textInputStyle() default TextInputStyle.SHORT;

    boolean required() default false;

}
