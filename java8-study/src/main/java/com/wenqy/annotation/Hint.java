package com.wenqy.annotation;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Repeatable，Java 8 允许我们对同一类型使用多重注解
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
@Repeatable(Hints.class)
@Retention(RetentionPolicy.RUNTIME) // 需要指定runtime,否则测试会失败
public @interface Hint {
    String value();
}