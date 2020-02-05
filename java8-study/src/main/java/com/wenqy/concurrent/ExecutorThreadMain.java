package com.wenqy.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 演示 Executor 管理线程池
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月17日
 */
public class ExecutorThreadMain {

	public static void main(String[] args) {
		ExecutorService executor = Executors.newSingleThreadExecutor(); // 单线程线程池
		executor.submit(() -> {
			String threadName = Thread.currentThread().getName();
			System.out.println("Hello " + threadName);
			try {
				TimeUnit.SECONDS.sleep(6);
			} catch (InterruptedException e) {
				System.err.println("my is interrupted");
			} // 休眠1s
		});
		// => Hello pool-1-thread-1
		
		// Executors必须显式的停止-否则它们将持续监听新的任务
		try {
		    System.out.println("attempt to shutdown executor");
		    executor.shutdown(); // 等待正在执行的任务执行完
		    executor.awaitTermination(5, TimeUnit.SECONDS); // 等待指定时间优雅关闭executor。在等待最长5s的时间后，executor最终会通过中断所有的正在执行的任务关闭
		    System.out.println("wait for 5s to shutdown");
	    } catch (InterruptedException e) {
		    System.err.println("tasks interrupted");
		} finally {
		    if (!executor.isTerminated()) {
		        System.err.println("cancel non-finished tasks");
		    }
		    executor.shutdownNow(); // 终止所有正在执行的任务并立即关闭executor
		    System.out.println("shutdown finished");
		}
	}
}
