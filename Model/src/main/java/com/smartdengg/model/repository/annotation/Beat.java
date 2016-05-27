package com.smartdengg.model.repository.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by SmartDengg on 2016/5/28.
 */
@Documented
@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.ANNOTATION_TYPE)
public @interface Beat {}
