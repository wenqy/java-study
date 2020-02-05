package com.wenqy.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.LongBinaryOperator;
import java.util.stream.IntStream;

import com.wenqy.common.utils.ConcurrentUtils;

/**
 * 原子操作
 * 		比较与交换（CAS），它是由多数现代CPU直接支持的原子指令
 * @version V5.0
 * @author wenqy
 * @date   2020年1月18日
 */
public class AtomicDemoMain {

	/**
	 * AtomicInteger
	 * 		incrementAndGet
	 * @author wenqy
	 * @date 2020年1月18日 下午4:27:10
	 */
	private void atomicIntegerIncre() {
		AtomicInteger atomicInt = new AtomicInteger(0);

		ExecutorService executor = Executors.newFixedThreadPool(2);

		IntStream.range(0, 1000)
		    .forEach(i -> executor.submit(atomicInt::incrementAndGet)); // ++

		ConcurrentUtils.stop(executor);

		System.out.println(atomicInt.get());    // => 1000
	}
	
	/**
	 * AtomicInteger
	 * 	updateAndGet
	 * @author wenqy
	 * @date 2020年1月18日 下午4:29:26
	 */
	private void atomicIntegerUpdateAndGet() {
		AtomicInteger atomicInt = new AtomicInteger(0);

		ExecutorService executor = Executors.newFixedThreadPool(2);

		IntStream.range(0, 1000)
		    .forEach(i -> {
		        Runnable task = () ->
		            atomicInt.updateAndGet(n -> n + 2); // 结果累加2
		        executor.submit(task);
		    });

		ConcurrentUtils.stop(executor);

		System.out.println(atomicInt.get());    // => 2000
	}
	
	/**
	 * LongAdder
	 * 		AtomicLong的替代，用于向某个数值连续添加值
	 * 		内部维护一系列变量来减少线程之间的争用，而不是求和计算单一结果
	 * 		当多线程的更新比读取更频繁时，这个类通常比原子数值类性能更好。
	 * 		这种情况在抓取统计数据时经常出现，例如，你希望统计Web服务器上请求的数量。
	 * 		LongAdder缺点是较高的内存开销，因为它在内存中储存了一系列变量。
	 * @author wenqy
	 * @date 2020年1月18日 下午4:33:29
	 */
	private void longAdder() {
		LongAdder adder = new LongAdder();
		ExecutorService executor = Executors.newFixedThreadPool(2);

		IntStream.range(0, 1000)
		    .forEach(i -> executor.submit(adder::increment));

		ConcurrentUtils.stop(executor);

		System.out.println(adder.sumThenReset());   // => 1000
	}
	
	/**
	 * LongAccumulator
	 * 		LongAccumulator是LongAdder的更通用的版本
	 * 		内部维护一系列变量来减少线程之间的争用
	 * @author wenqy
	 * @date 2020年1月18日 下午4:35:11
	 */
	private void longAccumulator() {
		LongBinaryOperator op = (x, y) -> 2 * x + y;
		LongAccumulator accumulator = new LongAccumulator(op, 1L);

		ExecutorService executor = Executors.newFixedThreadPool(2);
		// i=0  2 * 1 + 0 = 2;
		// i=2	2 * 2 + 2 = 6;
		// i=3  2 * 6 + 3 = 15;
		// i=4  2 * 15 + 4 = 34;
		IntStream.range(0, 10)
		    .forEach(i -> executor.submit(() -> {
		    			accumulator.accumulate(i);
		    			System.out.println("i:" + i + " result:" + accumulator.get());
	    			})
		    	);
		// 初始值为1。每次调用accumulate(i)的时候，当前结果和值i都会作为参数传入lambda表达式。
		ConcurrentUtils.stop(executor);

		System.out.println(accumulator.getThenReset());     // => 2539
	}
	
	/**
	 * concurrentMap
	 * 
	 * @author wenqy
	 * @date 2020年1月18日 下午4:38:09
	 */
	private void concurrentMap() {
		System.out.println("----->concurrentMap----->");
		ConcurrentMap<String, String> map = new ConcurrentHashMap<>();
		map.put("foo", "bar");
		map.put("han", "solo");
		map.put("r2", "d2");
		map.put("c3", "p0");
		
		map.forEach((key, value) -> System.out.printf("%s = %s\n", key, value));
		
		String value = map.putIfAbsent("c3", "p1");
		System.out.println(value);    // p0  提供的键不存在时，将新的值添加到映射
		
		System.out.println(map.getOrDefault("hi", "there"));    // there 传入的键不存在时，会返回默认值
		
		map.replaceAll((key, val) -> "r2".equals(key) ? "d3" : val);
		System.out.println(map.get("r2"));    // d3
		
		map.compute("foo", (key, val) -> val + val);
		System.out.println(map.get("foo"));   // barbar 转换单个元素，而不是替换映射中的所有值
		
		map.merge("foo", "boo", (oldVal, newVal) -> newVal + " was " + oldVal);
		System.out.println(map.get("foo"));   // boo was foo
	}
	
	/**
	 * concurrentHashMap
	 * 
	 * @author wenqy
	 * @date 2020年1月18日 下午4:38:42
	 */
	private void concurrentHashMap() {
		System.out.println("----->concurrentHashMap----->");
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
		map.put("foo", "bar");
		map.put("han", "solo");
		map.put("r2", "d2");
		map.put("c3", "p0");
		
		map.forEach(1, (key, value) ->
	    System.out.printf("key: %s; value: %s; thread: %s\n",
	        key, value, Thread.currentThread().getName())); // 可以并行迭代映射中的键值对
		
		String result = map.search(1, (key, value) -> {
		    System.out.println(Thread.currentThread().getName());
		    if ("foo".equals(key)) { // 当前的键值对返回一个非空的搜索结果
		        return value; // 只要返回了非空的结果，就不会往下搜索了
		    }
		    return null; 
		}); // ConcurrentHashMap是无序的。搜索函数应该不依赖于映射实际的处理顺序
		System.out.println("Result: " + result);
		
		String searchResult = map.searchValues(1, value -> {
		    System.out.println(Thread.currentThread().getName());
		    if (value.length() > 3) {
		        return value;
		    }
		    return null;
		}); // 搜索映射中的值

		System.out.println("Result: " + searchResult);
		
		String reduceResult = map.reduce(1,
		    (key, value) -> {
		        System.out.println("Transform: " + Thread.currentThread().getName());
		        return key + "=" + value;
		    },
		    (s1, s2) -> {
		        System.out.println("Reduce: " + Thread.currentThread().getName());
		        return s1 + ", " + s2;
		    });
		// 第一个函数将每个键值对转换为任意类型的单一值。
		// 第二个函数将所有这些转换后的值组合为单一结果，并忽略所有可能的null值
		System.out.println("Result: " + reduceResult);
	}
	
	public static void main(String[] args) {
		AtomicDemoMain demoMain = new AtomicDemoMain();
		demoMain.atomicIntegerIncre();
		demoMain.atomicIntegerUpdateAndGet();
		demoMain.longAdder();
		demoMain.longAccumulator();
		demoMain.concurrentMap();
		demoMain.concurrentHashMap();
	}
}
