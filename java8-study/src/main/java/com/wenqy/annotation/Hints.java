package com.wenqy.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 包装注解，它包括了一个实际注解的数组
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Hints {
    Hint[] value();
}