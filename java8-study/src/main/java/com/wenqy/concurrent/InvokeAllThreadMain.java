package com.wenqy.concurrent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * invokeAll
 * 演示 批量提交多个callable
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月17日
 */
public class InvokeAllThreadMain {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newWorkStealingPool(); // ForkJoinPool 一个并行因子数来创建，默认值为主机CPU的可用核心数

		List<Callable<String>> callables = Arrays.asList(
		        () -> "task1",
		        () -> "task2",
		        () -> "task3");
		executor.invokeAll(callables)
		    .stream()
		    .map(future -> { // 返回的所有future，并每一个future映射到它的返回值
		        try {
		            return future.get();
		        }
		        catch (Exception e) {
		            throw new IllegalStateException(e);
		        }
		    })
		    .forEach(System.out::println); 
	}
}
