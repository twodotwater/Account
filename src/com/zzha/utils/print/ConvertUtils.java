package com.zzha.utils.print;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;

public class ConvertUtils {

	/** 格式 yyyy/MM/dd\nHH:mm:ss */
	public static final String PATTERN_0 = "yyyy/MM/dd\nHH:mm:ss";
	/** 格式 YYYY-mm-dd */
	public static final String PATTERN_1 = "yyyy-MM-dd";
	/** 格式 HH:MM:SS */
	public static final String PATTERN_2 = "HH:mm:ss";
	/** 格式 HH:MM */
	public static final String PATTERN_3 = "HH:mm";
	/** 格式 mmddHHMMSS */
	public static final String PATTERN_4 = "MMddHHmmss";
	/** 格式 yyyy/MM/dd\tHH:mm:ss */
	public static final String PATTERN_5 = "yyyy/MM/dd\tHH:mm:ss";

	/**
	 * 将时间毫秒值转换为格式化的日期或时间
	 * 
	 * @param pattern
	 *            格式化字符串
	 * @param millis
	 *            时间毫秒值
	 * @return
	 */
	public static String formatDate(String pattern, long millis) {

		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date(millis));
	}

	public static <T> ArrayList<T> transSetToList(Set<T> set) {
		ArrayList<T> list = new ArrayList<T>();
		list.addAll(set);
		return list;
	}

	public static <T> HashSet<T> transListToSet(List<T> list) {
		HashSet<T> set = new HashSet<T>();
		set.addAll(list);
		return set;
	}

	public static int readInt(byte[] content, int start, int len) {
		int res = 0;
		for (int i = 0; i < len; i++) {
			res |= (content[i + start] & 0xFF) << (i * 8);
		}
		return res;
	}

	/**
	 * 读取字节数组中的一部分，转换为float类型
	 * 
	 * @param content
	 *            字节数组
	 * @param start
	 *            开始索引
	 * @param len
	 *            读取长度
	 * @return
	 */
	public static float readFloat(byte[] content, int start, int len) {
		int res = 0;
		for (int i = 0; i < len; i++) {
			res |= (content[i + start] & 0xFF) << (i * 8);
		}
		return Float.intBitsToFloat(res);
	}

	/**
	 * 读取字节List中的一部分，转换为float类型
	 * 
	 * @param content
	 *            List<Byte>
	 * @param start
	 *            开始索引
	 * @param len
	 *            读取长度
	 * @return
	 */
	public static float readFloat(List<Byte> content, int start, int len) {
		int res = 0;
		for (int i = 0; i < len; i++) {
			res |= (content.get(i + start) & 0xFF) << (i * 8);
		}
		return Float.intBitsToFloat(res);
	}

	/**
	 * 保留2位小数
	 * 
	 * @param value
	 * @return
	 */
	public static float formatDoubleOrFloat(double value) {
		return (int) (value * 100) / 100f;
	}

	/**
	 * 将字符串解析为double值
	 * 
	 * @param str
	 * @return
	 */
	public static double parseString(String str) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return Double.MIN_VALUE;
		}
	}

	/**
	 * 打印Log
	 * 
	 * @param TAG
	 *            tag
	 * @param msg
	 * @param isExp
	 *            是否是异常信息
	 */
	public static void printLog(String TAG, Object msg, boolean isExp) {
		if (isExp) {
			Log.e(TAG, "--" + msg);
		} else {
			Log.i(TAG, "--" + msg);
		}
	}
}
