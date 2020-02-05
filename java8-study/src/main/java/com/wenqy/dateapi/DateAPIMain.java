package com.wenqy.dateapi;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

/**
 * 演示 Date API 用法
 * 	这里记录下一个时间处理的类库：https://www.joda.org/joda-time/#
 * @version V5.0
 * @author wenqy
 * @date   2020年1月16日
 */
public class DateAPIMain {

	/**
	 * Clock提供了对当前时间和日期的访问功能。Clock是对当前时区敏感的
	 * 
	 * @author wenqy
	 * @date 2020年1月16日 下午4:36:49
	 */
	private static void testClock() {
		System.out.println("----->testClock----->");
		Clock clock = Clock.systemDefaultZone();
		long millis = clock.millis();
		System.out.println(millis); // 获取当前毫秒时间
		Instant instant = clock.instant();
		Date date = Date.from(instant);   // java.util.Date
		System.out.println(date); // 获取日期 Thu Jan 16 16:38:01 CST 2020
	}
	
	/**
	 * 时区类可以用一个ZoneId来表示。
	 * 时区类还定义了一个偏移量，用来在当前时刻或某时间与目标时区时间之间进行转换。
	 * 
	 * @author wenqy
	 * @date 2020年1月16日 下午4:40:38
	 */
	private static void testTimezones() {
		System.out.println("----->testTimezones----->");
		System.out.println(ZoneId.getAvailableZoneIds());
		// prints all available timezone ids

		ZoneId zone1 = ZoneId.of("Europe/Berlin");
		ZoneId zone2 = ZoneId.of("Brazil/East");
		ZoneId zone3 = ZoneId.of("Asia/Shanghai");
		System.out.println(zone1.getRules());
		System.out.println(zone2.getRules());
		System.out.println(zone3.getRules());
	}
	
	/**
	 * 本地时间类表示一个没有指定时区的时间
	 * 
	 * @author wenqy
	 * @date 2020年1月16日 下午4:49:11
	 */
	private static void testLocalTime() {
		System.out.println("----->testLocalTime----->");
		LocalTime now1 = LocalTime.now(ZoneId.of("Europe/Berlin"));
		LocalTime now2 = LocalTime.now(ZoneId.of("Asia/Shanghai"));
		// 比较两个时间
		System.out.println(now1.isBefore(now2));  // true

		long hoursBetween = ChronoUnit.HOURS.between(now1, now2);
		long minutesBetween = ChronoUnit.MINUTES.between(now1, now2);
		// 柏林时区在东一区，上海时区在东八区   柏林比北京时间慢7小时
		System.out.println(hoursBetween);       // 7
		System.out.println(minutesBetween);     // 420
		// 时间字符串解析操作
		LocalTime late = LocalTime.of(23, 59, 59);
		System.out.println(late);       // 23:59:59

		DateTimeFormatter germanFormatter =
		    DateTimeFormatter
		        .ofLocalizedTime(FormatStyle.SHORT)
		        .withLocale(Locale.GERMAN);

		LocalTime leetTime = LocalTime.parse("13:37", germanFormatter);
		System.out.println(leetTime);   // 13:37
	}
	
	/**
	 * 本地日期
	 * 	每一次操作都会返回一个新的时间对象
	 * 
	 * @author wenqy
	 * @date 2020年1月16日 下午5:05:52
	 */
	private static void testLocalDate() {
		System.out.println("----->testLocalDate----->");
		LocalDate today = LocalDate.now(); // 今天
		LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS); // 明天=今天+1
		LocalDate yesterday = tomorrow.minusDays(2); // 昨天=明天-2
		System.out.println(yesterday);
		LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
		DayOfWeek dayOfWeek = independenceDay.getDayOfWeek(); // 获取周
		System.out.println(dayOfWeek); // FRIDAY
	}
	
	/**
	 * 日期-时间
	 * 	final对象
	 * @author wenqy
	 * @date 2020年1月16日 下午5:12:37
	 */
	private static void testLocalDateTime() {
		System.out.println("----->testLocalDateTime----->");
		LocalDateTime sylvester = LocalDateTime.of(2019, Month.DECEMBER, 31, 23, 59, 59);

		DayOfWeek dayOfWeek = sylvester.getDayOfWeek();
		System.out.println(dayOfWeek);      // TUESDAY

		Month month = sylvester.getMonth();
		System.out.println(month);          // DECEMBER

		long minuteOfDay = sylvester.getLong(ChronoField.MINUTE_OF_DAY);
		System.out.println(minuteOfDay);    // 1439
		
		// 加上时区，转日期Date
		Instant instant = sylvester
		        .atZone(ZoneId.systemDefault())
		        .toInstant();

		Date legacyDate = Date.from(instant);
		System.out.println(legacyDate);     // Tue Dec 31 23:59:59 CST 2019
		
		// 自定义格式对象
		DateTimeFormatter formatter =
		    DateTimeFormatter
		        .ofPattern("yyyy-MM-dd HH:mm:ss");  // 线程安全，不可变
		LocalDateTime parsed = LocalDateTime.parse("2020-01-16 17:13:00", formatter);
		String string = formatter.format(parsed);
		System.out.println(string);     // 2020-01-16 17:13:00
	}
	
	public static void main(String[] args) {
		testClock();
		testTimezones();
		testLocalTime();
		testLocalDate();
		testLocalDateTime();
	}
}
