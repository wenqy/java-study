package com.wenqy.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 演示 并发 Stream 
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
public class ParallelStreamsMain {

//	int max = 1000000; // 大数据量并发才有优势
	int max = 400000;
	List<String> values = new ArrayList<>(max);
	
	public ParallelStreamsMain() {
		init();
	}
	
	private void init() {
		for (int i = 0; i < max; i++) {
		    UUID uuid = UUID.randomUUID();
		    values.add(uuid.toString());
		}
	}
	
	/**
	 * 顺序排序
	 * 
	 * @author wenqy
	 * @date 2020年1月16日 下午3:20:22
	 */
	private void sequenceSorted() {
		System.out.println("------>sequenceSorted----->");
		long t0 = System.nanoTime();

		long count = values.stream().sorted().count();
		System.out.println(count);

		long t1 = System.nanoTime();

		long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
		System.out.println(String.format("sequential sort took: %d ms", millis));
	}
	
	/**
	 * 并行排序
	 * 
	 * @author wenqy
	 * @date 2020年1月16日 下午3:21:56
	 */
	private void parallelSorted() {
		System.out.println("------>parallelSorted----->");
		long t0 = System.nanoTime();

		long count = values.parallelStream().sorted().count();
		System.out.println(count);

		long t1 = System.nanoTime();

		long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
		System.out.println(String.format("parallel sort took: %d ms", millis));

	}
	
	public static void main(String[] args) {
		ParallelStreamsMain streamsMain = new ParallelStreamsMain();
		streamsMain.parallelSorted();
		streamsMain.sequenceSorted();
	}
}
