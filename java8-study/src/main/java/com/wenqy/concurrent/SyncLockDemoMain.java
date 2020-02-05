package com.wenqy.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

import com.wenqy.common.utils.ConcurrentUtils;

/**
 * 
 * 演示 同步和锁
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月18日
 */
public class SyncLockDemoMain {

	ReentrantLock lock = new ReentrantLock();
	int count = 0;

	void increment() {
	    count = count + 1;
	}
	
	synchronized void incrementSync() {
	    count = count + 1;
	}
	
	void incrementSync2() {
	    synchronized (this) {
	        count = count + 1;
	    }
	}
	
	void incrementByLock() {
	    lock.lock();
	    try {
	        count++;
	    } finally {
	        lock.unlock();
	    }
	}
	
	/**
	 * 不安全 访问
	 * 
	 * @author wenqy
	 * @date 2020年1月18日 下午3:42:58
	 */
	private void unsafeIncre() {
		count = 0;
		ExecutorService executor = Executors.newFixedThreadPool(2);

		IntStream.range(0, 10000)
		    .forEach(i -> executor.submit(this::increment));

		ConcurrentUtils.stop(executor);

		System.out.println(count);  // 9975 实际结果在每次执行时都不同
	}
	
	/**
	 * 同步方法块 安全
	 * 
	 * @author wenqy
	 * @date 2020年1月18日 下午3:42:32
	 */
	private void safeIncre() {
		count = 0;
		ExecutorService executor = Executors.newFixedThreadPool(2);
		
		IntStream.range(0, 10000)
			.forEach(i -> executor.submit(this::incrementSync));
		
		ConcurrentUtils.stop(executor);
		
		System.out.println(count);  // 10000
	}
	
	/**
	 * 可重入锁
	 * 
	 * @author wenqy
	 * @date 2020年1月18日 下午3:41:50
	 */
	private void safeIncreByLock() {
		count = 0;
		ExecutorService executor = Executors.newFixedThreadPool(2);
		
		executor.submit(() -> {
		    lock.lock();
		    try {
		    	ConcurrentUtils.sleep(1);
		    } finally {
		        lock.unlock();
		    }
		});

		executor.submit(() -> {
		    System.out.println("Locked: " + lock.isLocked());
		    System.out.println("Held by me: " + lock.isHeldByCurrentThread());
		    boolean locked = lock.tryLock(); // 尝试拿锁而不阻塞当前线程
		    // 在访问任何共享可变变量之前，必须使用布尔值结果来检查锁是否已经被获取
		    System.out.println("Lock acquired: " + locked);
		});

		ConcurrentUtils.stop(executor);
	}
	
	/**
	 * 读写锁
	 * 
	 * @author wenqy
	 * @date 2020年1月18日 下午3:41:21
	 */
	private void readWriteLock() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Map<String, String> map = new HashMap<>();
		ReadWriteLock lock = new ReentrantReadWriteLock();

		executor.submit(() -> {
		    lock.writeLock().lock();
		    try {
		    	ConcurrentUtils.sleep(1);
		        map.put("foo", "bar");
		    } finally {
		        lock.writeLock().unlock();
		    }
		});
		
		Runnable readTask = () -> {
		    lock.readLock().lock();
		    try {
		        System.out.println(map.get("foo"));
		        ConcurrentUtils.sleep(1);
		    } finally {
		        lock.readLock().unlock();
		    }
		};

		executor.submit(readTask);
		executor.submit(readTask);

		ConcurrentUtils.stop(executor);
		// 两个读任务需要等待写任务完成。在释放了写锁之后，两个读任务会同时执行，并同时打印结果。
		// 它们不需要相互等待完成，因为读锁可以安全同步获取
	}
	
	/**
	 * java8 StampedLock
	 * 		tampedLock并没有实现重入特性，相同线程也要注意死锁
	 * @author wenqy
	 * @date 2020年1月18日 下午3:40:46
	 */
	private void stampedLock() {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Map<String, String> map = new HashMap<>();
		StampedLock lock = new StampedLock();

		executor.submit(() -> {
		    long stamp = lock.writeLock(); // 读锁或写锁会返回一个标记
		    try {
		    	ConcurrentUtils.sleep(1);
		        map.put("foo", "bar");
		    } finally {
		        lock.unlockWrite(stamp);
		    }
		});

		Runnable readTask = () -> {
		    long stamp = lock.readLock();
		    try {
		        System.out.println(map.get("foo"));
		        ConcurrentUtils.sleep(1);
		    } finally {
		        lock.unlockRead(stamp);
		    }
		};

		executor.submit(readTask);
		executor.submit(readTask);

		ConcurrentUtils.stop(executor);
	}
	
	/**
	 * 乐观锁
	 * 	乐观锁在刚刚拿到锁之后是有效的。和普通的读锁不同的是，乐观锁不阻止其他线程同时获取写锁。
	 * 	在第一个线程暂停一秒之后，第二个线程拿到写锁而无需等待乐观的读锁被释放。
	 * 	此时，乐观的读锁就不再有效了。甚至当写锁释放时，乐观的读锁还处于无效状态。
	 *	所以在使用乐观锁时，你需要每次在访问任何共享可变变量之后都要检查锁，来确保读锁仍然有效。
	 * 
	 * @author wenqy
	 * @date 2020年1月18日 下午3:49:31
	 */
	private void optimisticLock() {
		System.out.println("----->optimisticLock---->");
		ExecutorService executor = Executors.newFixedThreadPool(2);
		StampedLock lock = new StampedLock();

		executor.submit(() -> {
		    long stamp = lock.tryOptimisticRead();
		    try {
		        System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
		        ConcurrentUtils.sleep(1);
		        System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
		        ConcurrentUtils.sleep(2);
		        System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
		    } finally {
		        lock.unlock(stamp);
		    }
		});

		executor.submit(() -> {
		    long stamp = lock.writeLock();
		    try {
		        System.out.println("Write Lock acquired");
		        ConcurrentUtils.sleep(2);
		    } finally {
		        lock.unlock(stamp);
		        System.out.println("Write done");
		    }
		});

		ConcurrentUtils.stop(executor);
	}
	
	/**
	 * 读锁转换为写锁
	 * 
	 * @author wenqy
	 * @date 2020年1月18日 下午4:00:10
	 */
	private void convertToWriteLock() {
		count = 0;
		System.out.println("----->convertToWriteLock---->");
		ExecutorService executor = Executors.newFixedThreadPool(2);
		StampedLock lock = new StampedLock();

		executor.submit(() -> {
		    long stamp = lock.readLock();
		    try {
		        if (count == 0) {
		            stamp = lock.tryConvertToWriteLock(stamp); // 读锁转换为写锁而不用再次解锁和加锁
		            if (stamp == 0L) { // 调用不会阻塞，但是可能会返回为零的标记，表示当前没有可用的写锁
		                System.out.println("Could not convert to write lock");
		                stamp = lock.writeLock(); // 阻塞当前线程，直到有可用的写锁
		            }
		            count = 23;
		        }
		        System.out.println(count);
		    } finally {
		        lock.unlock(stamp);
		    }
		});

		ConcurrentUtils.stop(executor);
	}
	
	/**
	 * 信号量
	 * 
	 * @author wenqy
	 * @date 2020年1月18日 下午4:13:11
	 */
	private void doSemaphore() {
		ExecutorService executor = Executors.newFixedThreadPool(10);

		Semaphore semaphore = new Semaphore(5); // 并发访问总数

		Runnable longRunningTask = () -> {
		    boolean permit = false;
		    try {
		        permit = semaphore.tryAcquire(1, TimeUnit.SECONDS); 
		        if (permit) {
		            System.out.println("Semaphore acquired");
		            ConcurrentUtils.sleep(5);
		        } else { // 等待超时之后，会向控制台打印不能获取信号量的结果
		            System.out.println("Could not acquire semaphore");
		        }
		    } catch (InterruptedException e) {
		        throw new IllegalStateException(e);
		    } finally {
		        if (permit) {
		            semaphore.release();
		        }
		    }
		};

		IntStream.range(0, 10)
		    .forEach(i -> executor.submit(longRunningTask));

		ConcurrentUtils.stop(executor);
	}
	public static void main(String[] args) {
		SyncLockDemoMain lockDemoMain = new SyncLockDemoMain();
		lockDemoMain.unsafeIncre();
		lockDemoMain.safeIncre();
		lockDemoMain.safeIncreByLock();
		lockDemoMain.readWriteLock();
		lockDemoMain.stampedLock();
		lockDemoMain.optimisticLock();
		lockDemoMain.convertToWriteLock();
		lockDemoMain.doSemaphore();
	}
}
