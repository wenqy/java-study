package com.wenqy.defaultinterfacemethod;

/**
 * 演示 调用 接口默认方法
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
public class DefaultInterfaceMethodMain {

	public static void main(String[] args) {
		
		DefaultInterfaceMethod interfaceMethod = new DefaultInterfaceMethod() {
			
			@Override
			public double calc(int a) {
				return sqrt(a * 100);
			}
		};
		System.out.println("calc：" + interfaceMethod.calc(100)); // calc：100.0
		System.out.println("sqrt：" + interfaceMethod.sqrt(16)); // sqrt：4.0
	}
}
