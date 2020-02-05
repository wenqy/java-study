package com.wenqy.streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.wenqy.common.vo.Bar;
import com.wenqy.common.vo.Foo;
import com.wenqy.common.vo.Outer;
import com.wenqy.common.vo.Person;

/**
 * 演示 Stream 更为详细的操作
 * 数据流操作要么是衔接操作，要么是终止操作。
 * 	无干扰:当一个函数不修改数据流的底层数据源
 * 	无状态:当一个函数的操作的执行是确定性
 * @version V5.0
 * @author wenqy
 * @date   2020年1月17日
 */
public class StreamDetailDemoMain {

	private List<Person> getPersionList() {
		List<Person> persons =
			    Arrays.asList(
			        new Person("Max", 18),
			        new Person("Peter", 23),
			        new Person("Pamela", 23),
			        new Person("David", 12));
		return persons;
	}
	/**
	 * 从多种数据源创建数据流
	 * 
	 * @author wenqy
	 * @date 2020年1月17日 上午11:00:15
	 */
	private void diffStreamType() {
		System.out.println("----->diffStreamType----->");
		Arrays.asList("a1", "a2", "a3")
		    .stream()
		    .findFirst()
		    .ifPresent(System.out::println);  // a1
		
		Stream.of("a1", "a2", "a3")
		    .findFirst()
		    .ifPresent(System.out::println);  // a1
		
		IntStream.range(1, 4)	// 基本数据类型 // 1 2 3
	    	.forEach(System.out::println);
	}
	
	/**
	 * 基本数据流操作
	 * 
	 * @author wenqy
	 * @date 2020年1月17日 上午11:01:26
	 */
	private void baseStream() {
		System.out.println("----->baseStream----->");
		Arrays.stream(new int[] {1, 2, 3})
		    .map(n -> 2 * n + 1)
		    .average() // 终止操作，求平均值
		    .ifPresent(System.out::println);  // 5.0
		
		Stream.of("a1", "a2", "a3")
		    .map(s -> s.substring(1))
		    .mapToInt(Integer::parseInt) // 对象数据流转换为基本数据流
		    .max()
		    .ifPresent(System.out::println);  // 3
		
		IntStream.range(1, 4)
		    .mapToObj(i -> "a" + i) // 基本数据流转换为对象数据流
		    .forEach(System.out::println); // a1 a2 a3
	}
	
	/**
	 * 处理顺序
	 * 
	 * @author wenqy
	 * @date 2020年1月17日 上午11:08:33
	 */
	private void streamHandleSort() {
		System.out.println("----->streamHandleSort----->");
		Stream.of("d2", "a2", "b1", "b3", "c")
		    .filter(s -> {
		        System.out.println("filter: " + s);
		        return true;
		    })
		    .forEach(s -> System.out.println("forEach: " + s)); 
		// filter:  d2 forEach: d2 ... 每个元素在调用链上垂直移动
		
		Stream.of("d2", "a2", "b1", "b3", "c")
		    .map(s -> {
		        System.out.println("map: " + s);
		        return s.toUpperCase();
		    })
		    .anyMatch(s -> {
		        System.out.println("anyMatch: " + s);
		        return s.startsWith("A");
		    });
		// map:d2 anyMatch:D2 map:a2 anyMatch:A2 anyMatch返回true时终止
		
		Stream.of("d2", "a2", "b1", "b3", "c")
		    .map(s -> {
		        System.out.println("map: " + s);
		        return s.toUpperCase();
		    })
		    .filter(s -> {
		        System.out.println("filter: " + s);
		        return s.startsWith("A");
		    })
		    .forEach(s -> System.out.println("forEach: " + s));
		// map和filter会对底层集合的每个字符串调用五次，而forEach只会调用一次
		
		Stream.of("d2", "a2", "b1", "b3", "c")
		    .filter(s -> {
		        System.out.println("filter: " + s);
		        return s.startsWith("a");
		    })
		    .map(s -> {
		        System.out.println("map: " + s);
		        return s.toUpperCase();
		    })
		    .forEach(s -> System.out.println("forEach: " + s));
		// filter移动到调用链的顶端  map只会调用一次 执行更快
		
		Stream.of("d2", "a2", "b1", "b3", "c")
		    .sorted((s1, s2) -> {
		        System.out.printf("sort: %s; %s\n", s1, s2);
		        return s1.compareTo(s2);
		    })
		    .filter(s -> {
		        System.out.println("filter: " + s);
		        return s.startsWith("a");
		    })
		    .map(s -> {
		        System.out.println("map: " + s);
		        return s.toUpperCase();
		    })
		    .forEach(s -> System.out.println("forEach: " + s));
		// 排序是一类特殊的衔接操作。它是有状态的操作，因为你需要在处理中保存状态来对集合中的元素排序
		
		Stream.of("d2", "a2", "b1", "b3", "c")
		    .filter(s -> {
		        System.out.println("filter: " + s);
		        return s.startsWith("a");
		    })
		    .sorted((s1, s2) -> {
		        System.out.printf("sort: %s; %s\n", s1, s2);
		        return s1.compareTo(s2);
		    })
		    .map(s -> {
		        System.out.println("map: " + s);
		        return s.toUpperCase();
		    })
		    .forEach(s -> System.out.println("forEach: " + s));
		// 重排调用链来优化性能,这个例子中sorted永远不会调用,极大提升性能
	}
	
	/**
	 * 复用数据流
	 * 
	 * @author wenqy
	 * @date 2020年1月17日 上午11:40:02
	 */
	private void streamReuse() {
		System.out.println("----->streamReuse----->");
		Stream<String> stream =
			    Stream.of("d2", "a2", "b1", "b3", "c")
			        .filter(s -> s.startsWith("a"));

		stream.anyMatch(s -> true);    // ok
//		stream.noneMatch(s -> true);   // exception  java.lang.IllegalStateException: stream has already been operated upon or closed
		
		Supplier<Stream<String>> streamSupplier =
			    () -> Stream.of("d2", "a2", "b1", "b3", "c")
			            .filter(s -> s.startsWith("a"));
	    // 每次对get()的调用都构造了一个新的数据流，我们将其保存来调用终止操作
		streamSupplier.get().anyMatch(s -> true);   // ok
		streamSupplier.get().noneMatch(s -> true);  // ok
	}
	
	/**
	 * collect是非常有用的终止操作，将流中的元素存放在不同类型的结果中，例如List、Set或者Map。
	 * collect接受收集器（Collector），它由四个不同的操作组成：
	 * 	供应器（supplier）、累加器（accumulator）、组合器（combiner）和终止器（finisher）
	 * 
	 * @author wenqy
	 * @date 2020年1月17日 上午11:45:18
	 */
	private void streamCollect() {
		System.out.println("----->streamCollect----->");
		
		List<Person> persons = getPersionList();
		
		List<Person> filtered =
		    persons
		        .stream()
		        .filter(p -> p.getFirstName().startsWith("P"))
		        .collect(Collectors.toList()); // 构造list
		System.out.println(filtered);    // [Person [firstName=Peter, lastName=null, age=23], Person [firstName=Pamela, lastName=null, age=23]]
		
		Map<Integer, List<Person>> personsByAge = persons
		    .stream()
		    .collect(Collectors.groupingBy(p -> p.getAge())); // 构造map key: age
		personsByAge
		    .forEach((age, p) -> System.out.format("age %s: %s\n", age, p));
		
		IntSummaryStatistics ageSummary =
		    persons
		        .stream()
		        .collect(Collectors.summarizingInt(p -> p.getAge()));
		System.out.println(ageSummary); // 统计：简单计算最小年龄、最大年龄、算术平均年龄、总和和数量
		
		String phrase = persons
		    .stream()
		    .filter(p -> p.getAge() >= 18)
		    .map(p -> p.getFirstName()) // 键必须是唯一的，否则会抛出IllegalStateException异常
		    .collect(Collectors.joining(" and ", "In China ", " are of legal age."));
		System.out.println(phrase); // 所有人连接为一个字符串
		
		Collector<Person, StringJoiner, String> personNameCollector =
		    Collector.of(
		        () -> new StringJoiner(" | "),          // supplier
		        (j, p) -> j.add(p.getFirstName().toUpperCase()),  // accumulator
		        (j1, j2) -> j1.merge(j2),               // combiner
		        StringJoiner::toString);                // finisher
		String names = persons
		    .stream()
		    .collect(personNameCollector);
		System.out.println(names);  // MAX | PETER | PAMELA | DAVID
		// 构建自己特殊收集器。将流中的所有人转换为一个字符串，包含所有大写的名称，并以|分割。
	}
	
	/**
	 * flatMap将流中的每个元素，转换为其它对象的流。
	 * 所以每个对象会被转换为零个、一个或多个其它对象，以流的形式返回。
	 * 这些流的内容之后会放进flatMap所返回的流中
	 * 
	 * @author wenqy
	 * @date 2020年1月17日 下午1:38:09
	 */
	private void streamFlatMap() {
		System.out.println("----->streamFlatMap----->");
		List<Foo> foos = new ArrayList<>();

		// create foos
		IntStream
		    .range(1, 4)
		    .forEach(i -> foos.add(new Foo("Foo" + i)));
		// create bars
		foos.forEach(f ->
		    IntStream
		        .range(1, 4)
		        .forEach(i -> f.bars.add(new Bar("Bar" + i + " <- " + f.name))));
		foos.stream()
		    .flatMap(f -> f.bars.stream())
		    .forEach(b -> System.out.println(b.name)); // 将含有三个foo对象中的流转换为含有九个bar对象的流
		
		IntStream.range(1, 4)
		    .mapToObj(i -> new Foo("Foo" + i))
		    .peek(f -> IntStream.range(1, 4)
		        .mapToObj(i -> new Bar("Bar" + i + " <- " + f.name))
		        .forEach(f.bars::add)) // 简化为流式操作的单一流水线
		    .flatMap(f -> f.bars.stream())
		    .forEach(b -> System.out.println(b.name));
		
		Optional.of(new Outer())
		    .flatMap(o -> Optional.ofNullable(o.nested))
		    .flatMap(n -> Optional.ofNullable(n.inner))
		    .flatMap(i -> Optional.ofNullable(i.foo))
		    .ifPresent(System.out::println);
		// 如果存在的话，每个flatMap的调用都会返回预期对象的Optional包装，
		// 否则为null的Optional包装,避免潜在NullPointerException
	}
	
	/**
	 * 归约操作将所有流中的元素组合为单一结果
	 * 
	 * @author wenqy
	 * @date 2020年1月17日 下午2:01:23
	 */
	private void streamReduce() {
		System.out.println("----->streamReduce----->");
		List<Person> persons = getPersionList();
		persons
		    .stream()
		    .reduce((p1, p2) -> p1.getAge() > p2.getAge() ? p1 : p2)
		    .ifPresent(System.out::println);    // 计算年龄最大的人 Pamela
		
		Person result =
		    persons
		        .stream()
		        .reduce(new Person("", 0), (p1, p2) -> {
		            p1.setAge(p1.getAge() + p2.getAge());
		            p1.setFirstName(p1.getFirstName() + p2.getFirstName());
		            return p1;  // 构造带有聚合后名称和年龄的新Person对象
		        });
		// name=MaxPeterPamelaDavid; age=76
		System.out.format("name=%s; age=%s.\n", result.getFirstName(), result.getAge());
		
		Integer ageSum = persons
		    .stream()
		    .reduce(0, (sum, p) -> sum += p.getAge(), (sum1, sum2) -> sum1 + sum2);
		System.out.println(ageSum);  // 计算所有人的年龄总和 76
		
		Integer ageSum2 = persons
		    .stream()
		    .reduce(0,
		        (sum, p) -> {
		            System.out.format("accumulator: sum=%s; person=%s\n", sum, p);
		            return sum += p.getAge();
		        },
		        (sum1, sum2) -> {
		            System.out.format("combiner: sum1=%s; sum2=%s\n", sum1, sum2);
		            return sum1 + sum2;
		        });
		System.out.println(ageSum2); // 输出调试信息，combiner并没有输出
		
		Integer ageSum3 = persons
		    .parallelStream()
		    .reduce(0,
		        (sum, p) -> {
		            System.out.format("accumulator: sum=%s; person=%s [%s]\n", sum, p, Thread.currentThread().getName());
		            return sum += p.getAge();
		        },
		        (sum1, sum2) -> {
		            System.out.format("combiner: sum1=%s; sum2=%s [%s]\n", sum1, sum2, Thread.currentThread().getName());
		            return sum1 + sum2;
		        });
		System.out.println(ageSum3); // 并行方式
	}
	
	/**
	 * 并行流
	 * 
	 * @author wenqy
	 * @date 2020年1月17日 下午2:31:57
	 */
	private void streamParallel() {
		System.out.println("----->streamParallel----->");
		ForkJoinPool commonPool = ForkJoinPool.commonPool();
		System.out.println(commonPool.getParallelism());    // 底层线程池的大小 -- 取决于CPU的物理核数 本机 默认 7 
		// 可用JVM参数增减 -Djava.util.concurrent.ForkJoinPool.common.parallelism=5
		
		Arrays.asList("a1", "a2", "b1", "c2", "c1")
		    .parallelStream()
		    .filter(s -> {
		        System.out.format("filter: %s [%s]\n",
		            s, Thread.currentThread().getName());
		        return true;
		    })
		    .map(s -> {
		        System.out.format("map: %s [%s]\n",
		            s, Thread.currentThread().getName());
		        return s.toUpperCase();
		    })
		    .forEach(s -> System.out.format("forEach: %s [%s]\n",
		        s, Thread.currentThread().getName()));
		// 并行流使用了所有公共的ForkJoinPool中的可用线程来执行流式操作
		
		Arrays.asList("a1", "a2", "b1", "c2", "c1")
		    .parallelStream()
		    .filter(s -> {
		        System.out.format("filter: %s [%s]\n",
		            s, Thread.currentThread().getName());
		        return true;
		    })
		    .map(s -> {
		        System.out.format("map: %s [%s]\n",
		            s, Thread.currentThread().getName());
		        return s.toUpperCase();
		    })
		    .sorted((s1, s2) -> {
		        System.out.format("sort: %s <> %s [%s]\n",
		            s1, s2, Thread.currentThread().getName());
		        return s1.compareTo(s2);
		    })
		    .forEach(s -> System.out.format("forEach: %s [%s]\n",
		        s, Thread.currentThread().getName()));
		// sort看起来只在主线程上串行执行。实际上，并行流上的sort在背后使用了Java8中新的方法Arrays.parallelSort()。
		// 如javadoc所说，这个方法会参照数据长度来决定以串行或并行来执行,如果指定数据的长度小于最小粒度，它使用相应的Arrays.sort方法来排序
		// 所有并行流操作都共享相同的JVM相关的公共ForkJoinPool。所以你可能需要避免实现又慢又卡的流式操作，因为它可能会拖慢你应用中严重依赖并行流的其它部分。
	}
	
	public static void main(String[] args) {
		StreamDetailDemoMain demoMain = new StreamDetailDemoMain();
		demoMain.diffStreamType();
		demoMain.baseStream();
		demoMain.streamHandleSort();
		demoMain.streamReuse();
		demoMain.streamCollect();
		demoMain.streamFlatMap();
		demoMain.streamReduce();
		demoMain.streamParallel();
	}
}
