package com.wenqy.features;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 演示 String
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月18日
 */
public class StringDemoMain {

	public static void main(String[] args) {
		System.out.println(String.join(":", "foobar", "foo", "bar")); // foobar:foo:bar
		// 指定分割符，将字符串拼接
		
		System.out.println(
				"foobar:foo:bar"
			    .chars() // 字符数据流
			    .distinct()
			    .mapToObj(c -> String.valueOf((char)c))
			    .sorted()
			    .collect(Collectors.joining())
				); // :abfor
		
		System.out.println(
				Pattern.compile(":")
			    .splitAsStream("foobar:foo:bar")
			    .filter(s -> s.contains("bar"))
			    .sorted()
			    .collect(Collectors.joining(":"))
			); // bar:foobar
		
		System.out.println(
				Stream.of("bob@gmail.com", "alice@hotmail.com")
				    .filter(Pattern.compile(".*@gmail\\.com").asPredicate())
				    .count()
			); // 1
		
	}
}
