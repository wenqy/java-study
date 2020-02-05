package com.wenqy.annotation;

import com.wenqy.common.vo.Person;

/**
 * 演示 可重复注解的调用
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
public class AnnotationMain {

	public static void main(String[] args) {
		Hint hint = Person.class.getAnnotation(Hint.class);
		System.out.println(hint);                   // null

		Hints hints1 = Person.class.getAnnotation(Hints.class);
		System.out.println(hints1.value().length);  // 2

		Hint[] hints2 = Person.class.getAnnotationsByType(Hint.class);
		System.out.println(hints2.length);          // 2
	}
}
