package com.wenqy.features;

import java.util.Optional;
import java.util.function.Supplier;

import com.wenqy.common.vo.Inner;
import com.wenqy.common.vo.Nested;
import com.wenqy.common.vo.Outer;

/**
 * 演示 避免 Null检查
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月18日
 */
public class OptionalNullDemoMain {

	public static void main(String[] args) {
		Outer outer = new Outer();
		if (outer != null && outer.nested != null && outer.nested.inner != null) {
		    System.out.println(outer.nested.inner.foo);
		}
		
		Optional.of(new Outer())
		    .map(Outer::getNested)
		    .map(Nested::getInner)
		    .map(Inner::getFoo)
		    .ifPresent(System.out::println);
		
		Outer obj = new Outer();
		resolve(() -> obj.getNested().getInner().getFoo())
		    .ifPresent(System.out::println);
		// 这两个解决方案可能没有传统 null 检查那么高的性能
	}
	
	private static <T> Optional<T> resolve(Supplier<T> resolver) {
	    try {
	        T result = resolver.get();
	        return Optional.ofNullable(result);
	    }
	    catch (NullPointerException e) {
	        return Optional.empty();
	    }
	}
}
