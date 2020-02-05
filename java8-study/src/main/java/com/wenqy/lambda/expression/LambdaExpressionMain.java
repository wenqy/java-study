package com.wenqy.lambda.expression;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 演示 Lambda 写法
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
public class LambdaExpressionMain {

	/**
	 * Java8之前方法（匿名对象）实现排序
	 * @param names
	 * @author wenqy
	 * @date 2020年1月16日 上午10:07:45
	 */
	private static void sortByBefore8Method(List<String> names) {
		Collections.sort(names, new Comparator<String>() {
		    @Override
		    public int compare(String a, String b) {
		        return b.compareTo(a);
		    }
		});
	}
	
	/**
	 * 用Lambda表达式排序，Java编译器能够自动识别参数类型
	 * @param names
	 * @author wenqy
	 * @date 2020年1月16日 上午10:09:40
	 */
	private static void sortByLambda(List<String> names) {
//		可以这样写
//		Collections.sort(names, (String a, String b) -> {
//		    return b.compareTo(a);
//		});
		
//		也可以这样写
//		Collections.sort(names, (String a, String b) -> b.compareTo(a));
		
//		还可以这样写
		Collections.sort(names, (a, b) -> b.compareTo(a));
	}
	
	public static void main(String[] args) {
		List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
		sortByBefore8Method(names); // 匿名内部类实现
		System.out.println(names);
		List<String> nameArr = Arrays.asList("peter", "anna", "mike", "xenia");
		sortByLambda(nameArr);	// Lambda实现
		System.out.println(nameArr);
		
	}
}
