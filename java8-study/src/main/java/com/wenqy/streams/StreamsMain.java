package com.wenqy.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 演示 Stream 操作
 * java.util.Stream表示了某一种元素的序列，在这些元素上可以进行各种操作。
 * Stream操作可以是中间操作，也可以是完结操作。
 * 完结操作会返回一个某种类型的值，而中间操作会返回流对象本身，
 * 	并且你可以通过多次调用同一个流操作方法来将操作结果串起来
 * （就像StringBuffer的append方法一样）
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
public class StreamsMain {

	List<String> stringCollection = new ArrayList<>();

	public StreamsMain() {
		init();
	}
	
	private void init() {
		if (stringCollection.isEmpty()) {
			stringCollection.add("ddd2");
			stringCollection.add("aaa2");
			stringCollection.add("bbb1");
			stringCollection.add("aaa1");
			stringCollection.add("bbb3");
			stringCollection.add("ccc");
			stringCollection.add("bbb2");
			stringCollection.add("ddd1");
		}
	}

	/**
	 * 
	 * Filter接受一个predicate接口类型的变量，并将所有流对象中的元素进行过滤。
	 * 该操作是一个中间操作，因此它允许我们在返回结果的基础上再进行其他的流操作（forEach）。
	 * ForEach接受一个function接口类型的变量，用来执行对每一个元素的操作。
	 * ForEach是一个中止操作。它不返回流，所以我们不能再调用其他的流操作。
	 * @author wenqy
	 * @date 2020年1月16日 下午2:34:58
	 */
	private void testFilter() {
		System.out.println("----->testFilter----->");
		stringCollection
			.stream()
			.filter((s) -> s.startsWith("a")) // 过滤以“a”开头的集合
			.forEach(System.out::println);

	}
	
	/**
	 * Sorted是一个中间操作，能够返回一个排过序的流对象的视图。
	 * 流对象中的元素会默认按照自然顺序进行排序，除非你自己指定一个Comparator接口来改变排序规则
	 * 
	 * @author wenqy
	 * @date 2020年1月16日 下午2:38:28
	 */
	private void testSorted() {
		System.out.println("----->testSorted----->");
		stringCollection
			.stream()
			.sorted() // 自然排序
			.filter((s) -> s.startsWith("a")) // 过滤以“a”开头的集合
			.forEach(System.out::println);
		// sorted不会改变原来集合中元素的顺序。原来string集合中的元素顺序是没有改变的。
		System.out.println(stringCollection);
	}
	
	/**
	 * map是一个对于流对象的中间操作，通过给定的方法，它能够把流对象中的每一个元素对应到另外一个对象上。
	 * 下面的例子就演示了如何把每个string都转换成大写的string. 
	 * 不但如此，你还可以把每一种对象映射成为其他类型。
	 * 对于带泛型结果的流对象，具体的类型还要由传递给map的泛型方法来决定。
	 * 
	 * @author wenqy
	 * @date 2020年1月16日 下午2:43:38
	 */
	private void testMap() {
		System.out.println("----->testMap----->");
		stringCollection
		    .stream()
		    .map(String::toUpperCase) // 每个元素转大写
		    .sorted((a, b) -> b.compareTo(a))
		    .forEach(System.out::println);
		// sorted不会改变原来集合中元素的顺序。原来string集合中的元素顺序是没有改变的。
		System.out.println(stringCollection);
	}
	
	/**
	 * 匹配操作有多种不同的类型，都是用来判断某一种规则是否与流对象相互吻合的。
	 * 所有的匹配操作都是终结操作，只返回一个boolean类型的结果。
	 * 
	 * @author wenqy
	 * @date 2020年1月16日 下午2:47:48
	 */
	private void testMatch() {
		System.out.println("----->testMatch----->");
		boolean anyStartsWithA =
		    stringCollection
		        .stream()
		        .anyMatch((s) -> s.startsWith("a"));

		System.out.println(anyStartsWithA);      // true

		boolean allStartsWithA =
		    stringCollection
		        .stream()
		        .allMatch((s) -> s.startsWith("a"));

		System.out.println(allStartsWithA);      // false

		boolean noneStartsWithZ =
		    stringCollection
		        .stream()
		        .noneMatch((s) -> s.startsWith("z"));

		System.out.println(noneStartsWithZ);      // true
	}
	
	/**
	 * Count是一个终结操作，它的作用是返回一个数值，用来标识当前流对象中包含的元素数量。
	 * 
	 * @author wenqy
	 * @date 2020年1月16日 下午2:51:40
	 */
	private void testCount() {
		System.out.println("----->testCount----->");
		long startsWithB =
		    stringCollection
		        .stream()
		        .filter((s) -> s.startsWith("b"))
		        .count();

		System.out.println(startsWithB);    // 3
	}
	
	/**
	 * 该操作是一个终结操作，它能够通过某一个方法，对元素进行削减操作。
	 * 该操作的结果会放在一个Optional变量里返回。
	 * 
	 * @author wenqy
	 * @date 2020年1月16日 下午2:54:00
	 */
	private void testReduce() {
		System.out.println("----->testReduce----->");
		Optional<String> reduced =
		    stringCollection
		        .stream()
		        .sorted()
		        .reduce((s1, s2) -> s1 + "#" + s2);

		reduced.ifPresent(System.out::println); // "aaa1#aaa2#bbb1#bbb2#bbb3#ccc#ddd1#ddd2"
		
		
	}

	public static void main(String[] args) {
		StreamsMain streams = new StreamsMain();
		streams.testFilter();
		streams.testSorted();
		streams.testMap();
		streams.testMatch();
		streams.testCount();
		streams.testReduce();
	}
}
