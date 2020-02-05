package com.wenqy.concurrent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 批量提交callable
 * 	invokeAny:
 * 		在等待future对象的过程中，这个方法将会阻塞直到第一个callable中止然后返回这一个callable的结果	
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月17日
 */
public class InvokeAnyThreadMain {

	private static Callable<String> callable(String result, long sleepSeconds) {
	    return () -> {
	        TimeUnit.SECONDS.sleep(sleepSeconds);
	        return result;
	    };
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService executor = Executors.newWorkStealingPool();

		List<Callable<String>> callables = Arrays.asList(
		callable("task1", 2),
		callable("task2", 1),
		callable("task3", 3));

		String result = executor.invokeAny(callables);
		System.out.println(result); // task2
	}
}
