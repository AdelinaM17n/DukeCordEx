package io.github.maheevil.dukecordex.commandhandler.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ArgField {
    ArgParseType type() default ArgParseType.NORMAL;
    int index();
    boolean optional() default false;
}
