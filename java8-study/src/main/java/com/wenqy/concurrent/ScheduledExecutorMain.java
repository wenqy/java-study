package com.wenqy.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 演示 调度线程池
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月17日
 */
public class ScheduledExecutorMain {

	/**
	 * 获取剩余延迟
	 * @throws InterruptedException
	 * @author wenqy
	 * @date 2020年1月17日 下午4:56:58
	 */
	private static void scheduleDelay() throws InterruptedException {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

		Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime());
		ScheduledFuture<?> future = executor.schedule(task, 3, TimeUnit.SECONDS); // 3s后执行

		TimeUnit.MILLISECONDS.sleep(1337);

		long remainingDelay = future.getDelay(TimeUnit.MILLISECONDS);
		System.out.printf("Remaining Delay: %sms\n", remainingDelay); // 剩余的延迟
		
		executor.shutdown();
	}
	
	/**
	 * 以固定频率来执行一个任务
	 * 
	 * @throws InterruptedException
	 * @author wenqy
	 * @date 2020年1月17日 下午4:57:45
	 */
	private static void scheduleAtFixedRate() throws InterruptedException {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

		Runnable task = () -> {
			System.out.println("at fixed rate Scheduling: " + System.nanoTime());
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};

		int initialDelay = 0; // 初始化延迟，用来指定这个任务首次被执行等待的时长
		int period = 1;
		executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS); // 不考虑任务的实际用时
	}
	
	/**
	 * 以固定延迟来执行一个任务
	 * 	等待时间 period 是在一次任务的结束和下一个任务的开始之间
	 * @throws InterruptedException
	 * @author wenqy
	 * @date 2020年1月17日 下午5:03:28
	 */
	private static void scheduleWithFixedDelay() throws InterruptedException {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

		Runnable task = () -> {
		    try {
		        TimeUnit.SECONDS.sleep(2);
		        System.out.println("WithFixedDelay Scheduling: " + System.nanoTime());
		    }
		    catch (InterruptedException e) {
		        System.err.println("task interrupted");
		    }
		};

		executor.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);
	}
	
	public static void main(String[] args) throws InterruptedException {
		scheduleDelay();
		scheduleAtFixedRate();
		scheduleWithFixedDelay();
	}
}
