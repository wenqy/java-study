package com.wenqy.defaultinterfacemethod;

/**
 * 接口默认方法DEMO
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
public interface DefaultInterfaceMethod {

	double calc(int a);
	
	/**
	 * 接口使用 <b>default</b>关键字，添加非抽象方法实现（扩展方法）
	 * @param a
	 * @return
	 * @author wenqy
	 * @date 2020年1月16日 上午9:55:20
	 */
	default double sqrt(int a) {
		return Math.sqrt(a);
	}
}
