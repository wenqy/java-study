package com.wenqy.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 演示 Callable timeout
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月17日
 */
public class CallableThreadTimeoutMain {

	public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
		ExecutorService executor = Executors.newFixedThreadPool(1);

	    Future<Integer> future = executor.submit(() -> {
		    try {
		        TimeUnit.SECONDS.sleep(2);
		        return 123;
		    }
		    catch (InterruptedException e) {
		        throw new IllegalStateException("task interrupted", e);
		    }
		});
	    // 任何future.get()调用都会阻塞，然后等待直到callable中止,传入超时时长终止
	    future.get(1, TimeUnit.SECONDS);  // 抛出 java.util.concurrent.TimeoutException
	}
}
