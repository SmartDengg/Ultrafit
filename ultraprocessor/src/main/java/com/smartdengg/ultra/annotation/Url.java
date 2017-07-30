package com.smartdengg.ultra.annotation;

import com.smartdengg.ultra.internal.AnnotationType;
import com.smartdengg.ultra.internal.Type;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by SmartDengg on 2016/2/14.
 */
@Documented @Retention(value = RetentionPolicy.RUNTIME) @Target(value = ElementType.FIELD)
@AnnotationType(value = Type.URL) @Inherited public @interface Url {
}
