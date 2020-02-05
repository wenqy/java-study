package com.wenqy.features;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 演示 File 操作
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月18日
 */
public class FileDemoMain {

	public static void main(String[] args) throws IOException {
		listFiles();
		findFiles();
		handleFiles();
	}

	/**
	 * 文件查找
	 * @throws IOException
	 * @author wenqy
	 * @date 2020年1月18日 下午3:02:05
	 */
	private static void findFiles() throws IOException {
		Path start = Paths.get("");
		int maxDepth = 5;
		try (Stream<Path> stream = Files.find(start, maxDepth, (path, attr) ->
		        String.valueOf(path).endsWith(".js"))) {
		    String joined = stream
		        .sorted()
		        .map(String::valueOf)
		        .collect(Collectors.joining("; "));
		    System.out.println("Found: " + joined);
		}
	}

	/**
	 * 获取文件列表
	 * @throws IOException
	 * @author wenqy
	 * @date 2020年1月18日 下午3:02:26
	 */
	private static void listFiles() throws IOException {
		try (Stream<Path> stream = Files.list(Paths.get(""))) {
		    String joined = stream
		        .map(String::valueOf)
		        .filter(path -> !path.startsWith("."))
		        .sorted()
		        .collect(Collectors.joining("; "));
		    System.out.println("List: " + joined);
		    // 列出了当前工作目录的所有文件，之后将每个路径都映射为它的字符串表示。之后结果被过滤、排序，最后连接为一个字符串
		}
	}
	
	/**
	 * 文件读写处理
	 * @throws IOException
	 * @author wenqy
	 * @date 2020年1月18日 下午3:02:52
	 */
	private static void handleFiles() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("res/nashorn1.js")); // 整个文件都会读进内存,不高效。文件越大，所用的堆区也就越大
		lines.add("print('foobar');");
		Files.write(Paths.get("res/nashorn1-modified.js"), lines);
		
		try (Stream<String> stream = Files.lines(Paths.get("res/nashorn1.js"))) { // 行读取，高效点
		    stream
		        .filter(line -> line.contains("print"))
		        .map(String::trim)
		        .forEach(System.out::println);
		}
		
		try (BufferedReader reader = Files.newBufferedReader(Paths.get("res/nashorn1.js"))) { // BufferedReader 更精细
		    System.out.println(reader.readLine());
		}
		
		Path pathOut = Paths.get("res/output.js");
		try (BufferedWriter writer = Files.newBufferedWriter(pathOut)) { // BufferedWriter 写入文件
		    writer.write("print('Hello World');");
		}
		
		try (BufferedReader reader = Files.newBufferedReader(Paths.get("res/nashorn1.js"))) {
		    long countPrints = reader
		        .lines() // 流式处理
		        .filter(line -> line.contains("print"))
		        .count();
		    System.out.println(countPrints);
		}
	}
}
