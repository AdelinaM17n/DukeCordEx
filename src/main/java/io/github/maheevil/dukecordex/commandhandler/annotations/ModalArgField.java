package io.github.maheevil.dukecordex.commandhandler.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModalArgField {
    int maxLength() default 0;
    int minLength() default 0;
    String placeholderString();
    String defaultValue() default "Default Value";
    boolean required() default false;

}
