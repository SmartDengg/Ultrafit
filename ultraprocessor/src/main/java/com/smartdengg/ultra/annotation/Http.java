package com.smartdengg.ultra.annotation;

import com.smartdengg.ultra.internal.AnnotationType;
import com.smartdengg.ultra.internal.Type;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建时间:  2017/07/29 15:43 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
@Documented @Retention(value = RetentionPolicy.RUNTIME) @Target(value = ElementType.TYPE)
@AnnotationType(value = Type.HTTP) public @interface Http {

  enum Type {
    /**
     * GET请求
     */
    GET,

    /**
     * POST请求
     */
    POST
  }

  Type value();

  String url() default "";

  boolean log() default true;
}
