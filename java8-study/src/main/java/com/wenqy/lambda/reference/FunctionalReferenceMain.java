package com.wenqy.lambda.reference;

import com.wenqy.common.vo.Person;
import com.wenqy.common.vo.PersonFactory;
import com.wenqy.lambda.functionalinterface.Converter;

/**
 * 演示 Lambda 方法和构造函数引用
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
public class FunctionalReferenceMain {

	static class Something {
	    String startsWith(String s) {
	        return String.valueOf(s.charAt(0));
	    }
	}
	
	private static <F, T> void convert(Converter<F,T> converter, F from) {
		T converted = converter.convert(from);
		System.out.println(converted);
	}
	
	public static void main(String[] args) {
		Converter<String, Integer> converter1 = Integer::valueOf; // 静态方法引用
		convert(converter1,"123"); // "1"
		
		Something something = new Something();
		Converter<String, String> converter2 = something::startsWith; // 对象方法引用
		convert(converter2,"Java"); // "J"
		
		PersonFactory<Person> personFactory = Person::new;  // 构造函数引用(构造参数需一致)
		Person person = personFactory.create("Peter", "Parker");
		System.out.println(person); // Person [firstName=Peter, lastName=Parker]
	}
}
