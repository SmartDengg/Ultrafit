package com.smartdengg.model.repository.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import rx.annotations.Beta;

/**
 * Created by SmartDengg on 2016/5/27.
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
@Beta
public @interface MaxConnect {

    int count() default 1;
}
