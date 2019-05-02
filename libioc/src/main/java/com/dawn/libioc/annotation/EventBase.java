package com.dawn.libioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)    //这个注解用在另外的注解上
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {
    String listenerSetter();

    Class<?> listennerType();

    String callBackMethod();
}
