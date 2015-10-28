/**
 * 
 */
package com.bluemobi.utils;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static String a = "";
	
	public static final String format = "yyyy-MM-dd HH:mm:ss";
	public static void main(String[] aa){
		String t = "http://www.uhoem.com/product/detail/130.jhtml";
		t = t.substring(t.lastIndexOf("/")+1);
		t = t.substring(0,t.lastIndexOf("."));
		System.err.println(t);
		//System.err.println(test1(10));
		//System.err.println(1111);

	}

	public static void test(Integer i){
		if(i>1){
			a=","+i+a;
			i--;
			test(i);
		}
	}

	public static String test1(Integer i){
		test(i);
		return a;
	}

	// 传入时间毫秒，返回友好时间格式
	public static String time(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		try {
			Long now = new Date().getTime();
			Long mss = now - time;

			long days = mss / (1000 * 60 * 60 * 24);
			long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
			long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
			long seconds = (mss % (1000 * 60)) / 1000;
			if(days>0){
				if(days == 1){
					return "昨天";
				}else {
					Date date = new Date();
					date.setTime(time);
					return sdf.format(date);
				}
			}
			if(hours>0){
				return hours+"小时前";
			}
			if(minutes>0){
				return minutes+"分钟前";
			}
			return "1分钟内";
		}catch (Exception e){
			e.printStackTrace();
			return sdf.format(new Date());
		}
	}

	// 传入时间字符串和format格式，返回友好时间格式
	public static String stringToFriendlyDate(String tempTime,String ... patterns){
		if(StringUtils.isEmpty(tempTime)){
			return "";
		}
		String pattern = "yyyy-MM-dd HH:mm:ss";
		if(patterns.length > 0){
			pattern = patterns[0];
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		// 时间格式化
		Long templong = 0L;
		try {
			templong = sdf.parse(tempTime).getTime();
			return time(templong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		return days + " days " + hours + " hours " + minutes + " minutes "
				+ seconds + " seconds ";
	}

	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	
}