package com.wenqy.map;

import java.util.HashMap;
import java.util.Map;

/**
 * map是不支持流操作的。而更新后的map现在则支持多种实用的新方法
 * 
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
public class MapMain {

	public static void main(String[] args) {
		Map<Integer, String> map = new HashMap<>();

		for (int i = 0; i < 10; i++) {
		    map.putIfAbsent(i, "val" + i); // 旧值存在时返回旧值，不进行替换
		}

		map.forEach((id, val) -> System.out.println(val));
		
		map.computeIfPresent(3, (num, val) -> val + num);
		map.get(3);             // val33

		map.computeIfPresent(9, (num, val) -> null); // 返回null时，remove(key)
		map.containsKey(9);     // false

		map.computeIfAbsent(23, num -> "val" + num);
		map.containsKey(23);    // true

		map.computeIfAbsent(3, num -> "bam"); // 旧值存在时返回旧值，不进行替换
		map.get(3);             // val33
		
		map.remove(3, "val3");  // 校验value删除
		map.get(3);             // val33

		map.remove(3, "val33");
		map.get(3);             // null
		
		map.getOrDefault(42, "not found");  // not found 不存在则返回默认值
		
		map.merge(9, "val9", (value, newValue) -> value.concat(newValue)); // 先前key 9 已经删除了，不存在key，则返回 value
		System.out.println(map.get(9));             // val9

		map.merge(9, "concat", (value, newValue) -> value.concat(newValue)); // 存在key,才进行合并
		System.out.println(map.get(9));             // val9concat
	}
}
