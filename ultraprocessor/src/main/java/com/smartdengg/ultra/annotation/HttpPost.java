package com.smartdengg.ultra.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by SmartDengg on 2016/2/14.
 */
@Documented @Retention(value = RetentionPolicy.RUNTIME) @Target(value = ElementType.TYPE)
@HttpType(type = Type.POST) @Inherited public @interface HttpPost {

  String url();

  boolean log() default true;
}
