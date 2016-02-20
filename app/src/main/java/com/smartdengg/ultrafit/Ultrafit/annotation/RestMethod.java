package com.smartdengg.ultrafit.ultrafit.annotation;

import com.smartdengg.ultrafit.ultrafit.type.RestType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by SmartDengg on 2016/2/14.
 */
@Documented @Retention(value = RetentionPolicy.RUNTIME) @Target(value = ElementType.ANNOTATION_TYPE) @Inherited
public @interface RestMethod {
  RestType type();
}
