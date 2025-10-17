package org.web.annotation;

import java.lang.annotation.*;

import org.web.common.HttpMethod;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String path();
    HttpMethod method() default HttpMethod.GET;
}
