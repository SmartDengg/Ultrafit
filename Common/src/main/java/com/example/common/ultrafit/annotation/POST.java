package com.example.common.ultrafit.annotation;

import com.example.common.ultrafit.type.RestType;
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
@RestMethod(type = RestType.POST) @Inherited public @interface POST {
  String stringUrl();
}
