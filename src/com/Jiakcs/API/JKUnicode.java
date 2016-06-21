package com.Jiakcs.API;

import java.io.IOException;

/**
 * Unicode字符转换类
 * 
 * @author Jiakcs
 *
 */
public class JKUnicode {
	/**
	 * unicode 转字符串
	 */
	public static Boolean isUnicode(String unicode) {
		if (unicode.contains("\\\\u"))
			return true;
		if (unicode.trim().startsWith("u")) 
			return true;
		return false;
	}

	/**
	 * 字符串转换unicode
	 */
	public static String string2Unicode(String string) {
		StringBuffer unicode = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			// 取出每一个字符
			char c = string.charAt(i);
			// 转换为unicode
			unicode.append("u" + Integer.toHexString(c));
		}
		return unicode.toString();
	}

	/**
	 * unicode 转字符串
	 */
	public static String unicode2String(String unicode) {
		String[] hex = null;
		if (unicode.contains("\\\\u")) {
			hex = unicode.split("\\\\u");
		} else if (unicode.trim().startsWith("u")) {
			hex = unicode.split("u");
		}
		if (hex != null) {
			StringBuffer string = new StringBuffer();
			for (int i = 1; i < hex.length; i++) {
				// 转换出每一个代码点
				int data = Integer.parseInt(hex[i], 16);
				// 追加成string
				string.append((char) data);
			}
			return string.toString();
		}
		return unicode;
	}
}
