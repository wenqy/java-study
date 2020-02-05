package com.wenqy.lambda.accessdomain;

import com.wenqy.lambda.functionalinterface.Converter;

/**
 * 演示 Lambda内部 访问范围
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
public class LambdaAccessDomainMain {

	static class Lambda {
	    static int outerStaticNum;
	    int outerNum;
	    
	    /**
	     * 访问成员变量
	     * 
	     * @author wenqy
	     * @date 2020年1月16日 上午11:15:31
	     */
	    void accessOuterNum() {
	        Converter<Integer, String> converter = (from) -> {
	            outerNum = 23;
	            return String.valueOf(outerNum + from);
	        };
	        System.out.println("accessOuterNum：" + converter.convert(2)); // 25
//	        outerNum = 3;
	    }
	    
	    /**
	     * 访问静态变量
	     * 
	     * @author wenqy
	     * @date 2020年1月16日 上午11:18:41
	     */
	    void accessOuterStaticNum() {
	    	Converter<Integer, String> converter = (from) -> {
	    		outerStaticNum = 72;
	    		return String.valueOf(outerStaticNum + from);
	    	};
	    	System.out.println("accessOuterStaticNum：" + converter.convert(2)); // 74
//	    	outerStaticNum = 3;
	    }

		/**
		 * 访问局部变量
		 * 		可以访问lambda表达式外部的final局部变量
		 * 		但是与匿名对象不同的是，变量num并不需要一定是final
		 * @author wenqy
		 * @date 2020年1月16日 上午11:06:09
		 */
		void accessLocalVariable() {
//			final int num = 1;
			int num = 1;
			Converter<Integer, String> stringConverter =
			        (from) -> String.valueOf(from + num);
			System.out.println("accessLocalVariable：" + stringConverter.convert(2));     // 3
//			num = 3; // 编译的时候被隐式地当做final变量来处理，无法改变值
		}
	}
	
	public static void main(String[] args) {
		Lambda lambda = new Lambda();
		lambda.accessLocalVariable();
		lambda.accessOuterNum();	
		lambda.accessOuterStaticNum();
		
//		默认方法无法在lambda表达式内部被访问,无法通过编译
//		DefaultInterfaceMethod formula = (a) -> sqrt( a * 100);
	}

}
