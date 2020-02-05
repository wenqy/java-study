package com.wenqy.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 
 * 演示 Callable
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月17日
 */
public class CallableThreadMain {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Callable<Integer> task = () -> {
		    try {
		        TimeUnit.SECONDS.sleep(5); // 休眠5s后返回整数
		        return 123;
		    }
		    catch (InterruptedException e) {
		        throw new IllegalStateException("task interrupted", e);
		    }
		};
		
		ExecutorService executor = Executors.newFixedThreadPool(1); // 固定线程池
		Future<Integer> future = executor.submit(task);

		System.out.println("future done? " + future.isDone());

//		executor.shutdownNow(); // 如果关闭executor，所有的未中止的future都会抛出异常。
		
		Integer result = future.get(); // 在调用get()方法时，当前线程会阻塞等待，直到callable在返回实际的结果123之前执行完成

		System.out.println("future done? " + future.isDone());
		System.out.println("result: " + result);
		
		executor.shutdownNow(); // 需要显式关闭
		System.out.println("result: " + future.get());
	}
}
