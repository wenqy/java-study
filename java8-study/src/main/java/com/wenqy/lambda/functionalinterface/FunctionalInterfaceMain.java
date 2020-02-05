package com.wenqy.lambda.functionalinterface;

/**
 * 演示 调用 含 @FunctionalInterface 注解接口
 * 	测试 该注解下 多个抽象方法的情形
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
public class FunctionalInterfaceMain {

	public static void main(String[] args) {
		Converter<String, Integer> converter = (from) -> Integer.valueOf(from);
		Integer converted = converter.convert("123");
		System.out.println(converted);    // 123
	}
}
