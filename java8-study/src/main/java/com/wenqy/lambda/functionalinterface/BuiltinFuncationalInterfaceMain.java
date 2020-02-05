package com.wenqy.lambda.functionalinterface;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.wenqy.common.vo.Person;

/**
 * 演示 内置函数接口
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
public class BuiltinFuncationalInterfaceMain {

	/**
	 * 布尔类型函数
	 * 
	 * @author wenqy
	 * @date 2020年1月16日 上午11:39:26
	 */
	private static void testPredicates() {
		System.out.println("------>testPredicates------>");
		Predicate<String> predicate = (s) -> s.length() > 0;
		System.out.println(predicate.test("foo")); // true
		System.out.println(predicate.negate().test("foo")); // false
		
		Predicate<Boolean> nonNull = Objects::nonNull;
		Predicate<Boolean> isNull = Objects::isNull;
		System.out.println(nonNull.test(null)); // false
		System.out.println(isNull.test(null)); // true

		Predicate<String> isEmpty = String::isEmpty;
		Predicate<String> isNotEmpty = isEmpty.negate();
		System.out.println(isEmpty.test("")); // true
		System.out.println(isNotEmpty.test("")); // false
	}
	
	/**
	 * 内置 Function 函数，将多个函数串联
	 * 
	 * @author wenqy
	 * @date 2020年1月16日 上午11:41:35
	 */
	private static void testFunctions() {
		System.out.println("------>testFunctions------>");
		Function<String, Integer> toInteger = Integer::valueOf;
		Function<String, String> backToString = toInteger.andThen(String::valueOf);
		System.out.println(backToString.apply("123"));     // "123"
	}
	
	/**
	 * 内置 Supplier 函数
	 * 	产生一个给定类型的结果
	 * @author wenqy
	 * @date 2020年1月16日 上午11:52:51
	 */
	private static void testSuppliers() {
		System.out.println("------>testSuppliers------>");
		Supplier<Person> personSupplier = Person::new;
		Person person = personSupplier.get();   // new Person
		person.setFirstName("wen");
		person.setLastName("qy");
		System.out.println(person);
	}
	
	/**
	 * 内置 Consumer 函数
	 * 	输入参数上需要进行操作
	 * @author wenqy
	 * @date 2020年1月16日 下午1:48:33
	 */
	private static void testConsumers() {
		System.out.println("------>testConsumers------>");
		Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.getFirstName());
		greeter.accept(new Person("Luke", "Skywalker"));
	}
	
	/**
	 * 
	 * 内置 Comparator 函数
	 * 		用于比较
	 * @author wenqy
	 * @date 2020年1月16日 下午1:54:29
	 */
	private static void testComparators() {
		System.out.println("------>testComparators------>");
		Comparator<Person> comparator = (p1, p2) -> p1.getFirstName().compareTo(p2.getFirstName());

		Person p1 = new Person("John", "Doe");
		Person p2 = new Person("Alice", "Wonderland");

		System.out.println(comparator.compare(p1, p2)); // > 0
		System.out.println(comparator.reversed().compare(p1, p2)); // < 0
	}
	
	/**
	 * Optional
	 * 	值容器，用来防止NullPointerException产生
	 * @author wenqy
	 * @date 2020年1月16日 下午1:58:44
	 */
	private static void testOptionals() {
		System.out.println("------>testOptionals------>");
		Optional<String> optional = Optional.of("bam");

		System.out.println(optional.isPresent()); // true
		System.out.println(optional.get()); // "bam"
		System.out.println(optional.orElse("fallback")); // "bam"

		optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"
	}
	
	public static void main(String[] args) {
		testPredicates();
		testFunctions();
		testSuppliers();
		testConsumers();
		testComparators();
		testOptionals();
	}
}
