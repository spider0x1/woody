package org.xbug.search.woody.core.model;

import java.util.HashMap;
import java.util.Map;

import org.xbug.search.woody.core.util.MD5Signature;

public final class ObjectCache {

	public static final int PARSER = 0x01;
	public static final int CLASS = 0x02;

	public static final boolean DEBUG = false;

	private static Map<Integer, Map<String, Object>> CACHE = new HashMap<Integer, Map<String, Object>>();

	private static int MAX_CACHE_SIZE = 0xff;

	private static int CLASS_MAX_CACHE_ZIE = 0xffff;

	public synchronized static void put(int type, String key, Object value, boolean force) {
		if (!force && get(type, key) != null) {
			return;
		}
		Map<String, Object> map = CACHE.get(type);
		if (map == null) {
			map = new HashMap<String, Object>();
		}
		map.put(key, value);
		CACHE.put(type, map);
		if (DEBUG) {
			Map<String, Object> parserMap = CACHE.get(PARSER);
			Map<String, Object> classMap = CACHE.get(CLASS);
			System.out.println("CACHE['parser'] " + (parserMap == null ? "0" : parserMap.keySet()));
			System.out.println("CACHE['class'] " + (classMap == null ? "0" : classMap.keySet()));
		}
	}
	
	public synchronized static void put(int type, String key, Object value) {
		put(type, key, value, false);
	}

	public synchronized static Object get(int type, String key) {
		Map<String, Object> map = CACHE.get(type);
		return map == null ? null : map.get(key);
	}

	public synchronized static boolean purge(int type, boolean force) {
		Map<String, Object> map = CACHE.get(type);
		if (map == null || map.isEmpty())
			return false;
		if (force) {
			CACHE.remove(type);
			return true;
		}
		int max = (CLASS == type) ? CLASS_MAX_CACHE_ZIE : MAX_CACHE_SIZE;
		if (map.size() > max) {
			CACHE.remove(type);
			return true;
		}
		return false;
	}

	public synchronized static boolean purge(int type) {
		return purge(type, false);
	}

	public synchronized void adjustCacheSize(int type, int size) {
		switch (type) {
		case CLASS:
			CLASS_MAX_CACHE_ZIE = size;
			break;
		default:
			MAX_CACHE_SIZE = size;
			break;
		}
	}

	public static String createKeyString(String prefix, String text) {
		return MD5Signature.md5Str(prefix + "#" + text);
	}

	private static boolean validate(Object obj, Class<?> clazz) {
		if ((obj != null) && (obj.getClass().isAssignableFrom(clazz))) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static <T> T safeGet(int type, String key, Class<T> clazz) {
		Object obj = get(type, key);
		boolean valid = validate(obj, clazz);
		return valid ? (T)obj : null;
	}
}