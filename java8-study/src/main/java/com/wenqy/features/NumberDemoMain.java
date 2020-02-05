package com.wenqy.features;

/**
 * 演示 数值处理
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月18日
 */
public class NumberDemoMain {

	public static void main(String[] args) {
		
		System.out.println(Integer.MAX_VALUE);      // 2147483647
		System.out.println(Integer.MAX_VALUE + 1);  // -2147483648
		
		long maxUnsignedInt = (1l << 32) - 1; // 
		String string = String.valueOf(maxUnsignedInt);
		int unsignedInt = Integer.parseUnsignedInt(string, 10); // 无符号转换
		String string2 = Integer.toUnsignedString(unsignedInt, 10);
		System.out.println(string2);
		try {
		    Integer.parseInt(string, 10);
		} catch (NumberFormatException e) {
		    System.err.println("could not parse signed int of " + maxUnsignedInt);
		}
		
		try {
		    Math.addExact(Integer.MAX_VALUE, 1);
		}
		catch (ArithmeticException e) {
		    System.err.println(e.getMessage());
		    // => integer overflow
		}
		
		try {
		    Math.toIntExact(Long.MAX_VALUE);
		}
		catch (ArithmeticException e) {
		    System.err.println(e.getMessage());
		    // => integer overflow
		}
	}
}
