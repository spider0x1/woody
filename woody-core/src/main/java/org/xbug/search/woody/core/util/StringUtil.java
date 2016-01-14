package org.xbug.search.woody.core.util;

public class StringUtil {

	/**
	 * Return true if value is null or when trimmed has zero length.
	 * 
	 * @param value
	 *            the value to test
	 * @return true if null or trimmed value has zero length
	 */
	public static boolean isNullOrEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}
}
