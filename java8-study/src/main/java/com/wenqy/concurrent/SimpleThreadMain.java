package com.wenqy.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * 演示 Thread 和 Runnable
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月17日
 */
public class SimpleThreadMain {

	/**
	 * Runnable
	 * 
	 * @author wenqy
	 * @date 2020年1月17日 下午3:19:58
	 */
	private void testRunnable() {
		Runnable task = () -> {
		    String threadName = Thread.currentThread().getName();
		    System.out.println("Hello " + threadName);
		};

		task.run(); // 非线程方式调用，还在主线程里

		Thread thread = new Thread(task);
		thread.start();

		System.out.println("Done!");  // runnable是在打印'done'前执行还是在之后执行,顺序是不确定的
	}
	
	/**
	 * 设置线程休眠时间，模拟长任务
	 * 
	 * @author wenqy
	 * @date 2020年1月17日 下午3:25:01
	 */
	private void testRunnableWithSleep() {
		Runnable runnable = () -> {
		    try {
		        String name = Thread.currentThread().getName();
		        System.out.println("Foo " + name);
		        TimeUnit.SECONDS.sleep(1); // 休眠1s
		        System.out.println("Bar " + name);
		    }
		    catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		};

		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	public static void main(String[] args) {
		SimpleThreadMain threadMain = new SimpleThreadMain();
		threadMain.testRunnable();
		threadMain.testRunnableWithSleep();
	}
}
