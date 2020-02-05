package com.wenqy.lambda.functionalinterface;

/**
 * 
 * 每一个lambda都能够通过一个特定的接口，与一个给定的类型进行匹配。
 * 一个所谓的函数式接口必须要有且仅有一个抽象方法声明。
 * 每个与之对应的lambda表达式必须要与抽象方法的声明相匹配。
 * 
 * @FunctionalInterface 标注。
 * 	编译器会注意到这个标注，如果接口中定义了第二个抽象方法的话，编译器会抛出异常
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
@FunctionalInterface
public interface Converter<F, T> {

	/**
	 * 类型转换  F->T
	 * @param from
	 * @return
	 * @author wenqy
	 * @date 2020年1月16日 上午10:22:32
	 */
	T convert(F from);
	
//	T mapping(F from);
}
