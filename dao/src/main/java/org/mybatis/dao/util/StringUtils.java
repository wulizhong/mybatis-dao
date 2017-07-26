package org.mybatis.dao.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 作者 :吴立中
 * @version 1.0
 * @date 创建时间：2016年7月13日 上午11:48:58
 * 
 */
public class StringUtils {

	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		return str.isEmpty();
	}

	public static String humpToUnderline(String hump) {
		return humpToUnderline(hump, false);
	}

	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	public static String humpToUnderline(String hump, boolean upperCase) {

		if (hump == null || "".equals(hump)) {
			return "";
		}
		hump = String.valueOf(hump.charAt(0)).toUpperCase().concat(hump.substring(1));
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
		Matcher matcher = pattern.matcher(hump);
		while (matcher.find()) {
			String word = matcher.group();
			if (upperCase) {
				sb.append(word.toUpperCase());
			} else {
				sb.append(word.toLowerCase());
			}

			sb.append(matcher.end() == hump.length() ? "" : "_");
		}
		return sb.toString();
	}

	public static String underlineToHump(String line, boolean lowerHump) {
		if (line == null || "".equals(line)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
		Matcher matcher = pattern.matcher(line);
		while (matcher.find()) {
			String word = matcher.group();
			sb.append(lowerHump && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
			int index = word.lastIndexOf('_');
			if (index > 0) {
				sb.append(word.substring(1, index).toLowerCase());
			} else {
				sb.append(word.substring(1).toLowerCase());
			}
		}
		return sb.toString();
	}

	public static String convertToSqlParam(String str) {
		return str.replaceAll(".*([';]+|(--)+).*", " ");
		// 我认为 应该是return str.replaceAll("([';])+|(--)+","");
	}

	public static String test() {
		return "";
		// 我认为 应该是return str.replaceAll("([';])+|(--)+","");
	}
}
