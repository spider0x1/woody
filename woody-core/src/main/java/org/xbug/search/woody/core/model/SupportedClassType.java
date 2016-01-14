package org.xbug.search.woody.core.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbug.search.woody.core.util.StringUtil;


public enum SupportedClassType {

	Byte(byte.class, Byte.class) {
		@Override
		public Object parseValue(java.lang.Object value) {
			if (value == null)
				return null;
			boolean validate = validate(value);
			if (!validate) {
				throw new IllegalArgumentException("Invalid type!");
			}
			if (value instanceof java.lang.Byte)
				return value;
			return java.lang.Byte.parseByte((String) value);
		}

		@Override
		public java.lang.String getInstancenName() {
			return "Byte";
		}

		@Override
		public boolean validate(Object value) {
			if (value instanceof java.lang.Byte || value instanceof java.lang.String)
				return true;
			return false;
		}

		@Override
		public java.lang.String getPrefix() {
			return HEADER + ".byte";
		}

	}, // byte or Byte
	Character(char.class, Character.class) {
		@Override
		public Object parseValue(java.lang.Object value) {
			if (value == null)
				return null;
			if (value instanceof java.lang.Character)
				return value;
			String strValue = (String) value;
			if (StringUtil.isNullOrEmpty(strValue)) {
				return null;
			}
			return strValue.trim().charAt(0);
		}

		@Override
		public java.lang.String getInstancenName() {
			return "Character";
		}

		@Override
		public boolean validate(Object value) {
			// TODO Auto-generated method stub
			return false;
		}

	}, // char, Character
	Short(short.class, Short.class) {
		@Override
		public Object parseValue(java.lang.Object value) {
			if (value == null)
				return null;
			if (value instanceof java.lang.Short)
				return value;
			String strValue = (String) value;
			if (StringUtil.isNullOrEmpty(strValue) || !StringUtils.isNumeric(strValue)) {
				return null;
			}
			return java.lang.Short.parseShort(strValue);
		}

		@Override
		public java.lang.String getInstancenName() {
			return "Short";
		}

		@Override
		public boolean validate(Object value) {
			// TODO Auto-generated method stub
			return false;
		}

	}, // short or Short
	Integer(int.class, Integer.class) {
		@Override
		public Object parseValue(java.lang.Object value) {
			if (value == null)
				return null;
			if (value instanceof java.lang.Integer)
				return value;
			String strValue = (String) value;
			if (StringUtil.isNullOrEmpty(strValue) || !StringUtils.isNumeric(strValue)) {
				return null;
			}
			return java.lang.Integer.parseInt(strValue);
		}

		@Override
		public java.lang.String getInstancenName() {
			return "Integer";
		}

		@Override
		public boolean validate(Object value) {
			// TODO Auto-generated method stub
			return false;
		}

	}, // int or Integer
	Long(long.class, Long.class) {
		@Override
		public Object parseValue(java.lang.Object value) {
			if (value == null)
				return null;
			if (value instanceof java.lang.Long)
				return value;
			String strValue = (String) value;
			if (StringUtil.isNullOrEmpty(strValue) || !StringUtils.isNumeric(strValue)) {
				return null;
			}
			return java.lang.Long.parseLong(strValue);
		}

		@Override
		public java.lang.String getInstancenName() {
			return "Long";
		}

		@Override
		public boolean validate(Object value) {
			// TODO Auto-generated method stub
			return false;
		}

	}, // long or Long
	Double(double.class, Double.class) {
		@Override
		public Object parseValue(java.lang.Object value) {
			if (value == null)
				return null;
			if (value instanceof java.lang.Double)
				return value;
			return java.lang.Double.parseDouble((String) value);
		}

		@Override
		public java.lang.String getInstancenName() {
			return "Double";
		}

		@Override
		public boolean validate(Object value) {
			// TODO Auto-generated method stub
			return false;
		}

	}, // double or Double
	Float(float.class, Float.class) {
		@Override
		public Object parseValue(java.lang.Object value) {
			if (value == null)
				return null;
			if (value instanceof java.lang.Float)
				return value;
			return java.lang.Float.parseFloat((String) value);
		}

		@Override
		public java.lang.String getInstancenName() {
			return "Float";
		}

		@Override
		public boolean validate(Object value) {
			// TODO Auto-generated method stub
			return false;
		}

	}, // float or Float
	Boolean(boolean.class, Boolean.class) {
		@Override
		public Object parseValue(java.lang.Object value) {
			if (value == null)
				return null;
			if (value instanceof java.lang.Boolean)
				return value;
			return java.lang.Boolean.parseBoolean((String) value);
		}

		@Override
		public java.lang.String getInstancenName() {
			return "Boolean";
		}

		@Override
		public boolean validate(Object value) {
			// TODO Auto-generated method stub
			return false;
		}

	}, // boolean or Boolean
	String(String.class) {
		@Override
		public Object parseValue(java.lang.Object value) {
			if (value == null)
				return null;
			if (value instanceof java.lang.String)
				return value;
			return (String) value;
		}

		@Override
		public java.lang.String getInstancenName() {
			return "String";
		}

		@Override
		public boolean validate(Object value) {
			// TODO Auto-generated method stub
			return false;
		}

	}, // String
	Array(String[].class, int[].class, Object[].class) {
		@Override
		public Object parseValue(Object value) {
			int len = java.lang.reflect.Array.getLength(value);
			Object array = java.lang.reflect.Array.newInstance(this.valueOf()[this.getMatchedIndex()], len);
			for (int i = 0; i < len; i++) {
				java.lang.reflect.Array.set(array, i, java.lang.reflect.Array.get(value, i));
			}
			return array;
		}

		@Override
		public java.lang.String getInstancenName() {
			return "Array";
		}

		@Override
		public boolean validate(Object value) {
			// TODO Auto-generated method stub
			return false;
		}

	}, // String[]
	List(java.util.List.class) {
		@SuppressWarnings("unchecked")
		@Override
		public Object parseValue(java.lang.Object value) {
			if (value == null)
				return null;
			if (value instanceof java.util.List)
				return value;
			return (ArrayList<java.lang.String>) value;
		}

		@Override
		public java.lang.String getInstancenName() {
			return "List";
		}

		@Override
		public boolean validate(Object value) {
			// TODO Auto-generated method stub
			return false;
		}

	}, // List
	Set(java.util.Set.class) {
		@SuppressWarnings("unchecked")
		@Override
		public Object parseValue(java.lang.Object value) {
			if (value == null)
				return null;
			if (value instanceof java.util.Set)
				return value;
			Set<String> set = new HashSet<String>();
			set.addAll((Collection<String>) List.parseValue(value));
			return set;
		}

		@Override
		public java.lang.String getInstancenName() {
			return "Set";
		}

		@Override
		public boolean validate(Object value) {
			// TODO Auto-generated method stub
			return false;
		}

	}, // Set
	Date(java.util.Date.class) {
		@Override
		public Object parseValue(Object value) {
			if (value == null)
				return null;
			final String regexKey = getPrefix() + ".regex";
			final String formatKey = getPrefix() + ".format";
			boolean validate = validate(value);
			if (!validate) {
				throw new IllegalArgumentException(java.lang.String.format(
						"need have '%s' and '%s' if the value is string", regexKey, formatKey));
			}
			if (value instanceof java.util.Date)
				return value;
			Date date = null;
			String regex = (String) this.dataMap().get(regexKey);

			String format = (String) this.dataMap().get(formatKey);
			if (StringUtil.isNullOrEmpty(format)) {
				throw new IllegalArgumentException(java.lang.String.format("the '%s' can't empty/null", formatKey));
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			String strDate = null;
			if (StringUtil.isNullOrEmpty(regex)) {
				strDate = (String)value;
			} else {
				final Pattern pattern = Pattern.compile(regex);
				Matcher m = pattern.matcher((String) value);
				if (m.find()) {
					strDate = m.group();
				}
			}
			try {
				date = dateFormat.parse(strDate);
			} catch (ParseException e) {
				LOG.warn("parse string to date error", e);
			}
			
			return date;
		}

		@Override
		public java.lang.String getInstancenName() {
			return "Date";
		}

		@Override
		public boolean validate(Object value) {
			final String formatKey = getPrefix() + ".format";// sct.date.format
			if (value instanceof java.util.Date)
				return true;
			if (value instanceof java.lang.String) {
				if (this.dataMap().containsKey(formatKey))
					return true;
			}
			return false;
		}

	},
	Others(Void.class) {
		@Override
		public Object parseValue(java.lang.Object value) {
			return String.parseValue(value);
		}

		@Override
		public java.lang.String getInstancenName() {
			return "Others";
		}

		@Override
		public boolean validate(Object value) {
			// TODO Auto-generated method stub
			return false;
		}

	};// Others class type.Void.class just as a mark

	private Class<?>[] classes;

	private int matchedIndex;

	private Map<String, Object> dataMap;

	private static final Logger LOG = LoggerFactory.getLogger(SupportedClassType.class);

	private static final String HEADER = "sct";

	private SupportedClassType(Class<?>... classes) {
		this.classes = classes;
		this.matchedIndex = 0;
		this.dataMap = new HashMap<String, Object>();
	}

	public abstract Object parseValue(Object value);

	public abstract String getInstancenName();

	public abstract boolean validate(Object value);

	public String getPrefix() {
		return (HEADER + "." + getInstancenName()).toLowerCase();
	}

	public int getMatchedIndex() {
		return this.matchedIndex;
	}

	@SuppressWarnings("unchecked")
	public <T> T cast(T obj) {
		if (obj == null) {
			return null;
		}
		Object o = null;

		for (Class<?> clazz : classes) {
			if (obj.getClass().isAssignableFrom(clazz)) {
				o = clazz.cast(obj);
			}
		}
		if (o == null) {
			throw new IllegalArgumentException(java.lang.String.format("Can't cast the object from %s from class %s",
					obj.getClass(), Arrays.toString(this.classes)));
		}
		return (T) o;
	}

	public Class<?>[] valueOf() {
		return this.classes;
	}

	public Map<String, Object> dataMap() {
		return this.dataMap;
	}

	public String[] classNames() {
		String[] classNames = new String[this.classes.length];
		for (int i = 0; i < this.classes.length; i++) {
			classNames[i] = this.classes[i].getCanonicalName();
		}
		return classNames;
	}

	public static SupportedClassType fromValue(Class<?> clazz) {
		return fromValue(clazz, null);
	}

	public static SupportedClassType fromValue(Class<?> clazz, SupportedClassType otherType) {
		if (clazz == null) {
			return Others;
		} else {
			// Array type
			if (clazz.isArray()) {
				return Array;
			}
			// deal with otherType
			String clazzName = StringUtils.capitalize(clazz.getSimpleName().toLowerCase());
			if (otherType != null) {
				Class<?>[] _classes = otherType.valueOf();
				for (int i = 0; i < _classes.length; i++) {
					Class<?> c = _classes[i];
					String ccName = StringUtils.capitalize(c.getSimpleName().toLowerCase());
					if (clazzName.equals(ccName)) {
						Others.matchedIndex = i;
						return Others;
					}
				}
			}
			SupportedClassType[] classTypes = SupportedClassType.values();
			for (SupportedClassType classType : classTypes) {
				if (!Others.equals(classType)) {
					Class<?>[] _classes = classType.valueOf();
					for (int i = 0; i < _classes.length; i++) {
						Class<?> c = _classes[i];
						String ccName = StringUtils.capitalize(c.getSimpleName().toLowerCase());
						if (clazzName.equals(ccName)) {
							classType.matchedIndex = i;
							return classType;
						}
					}
				}
			}
		}
		return Others;
	}

	public static SupportedClassType fromValue(String className, SupportedClassType otherType)
			throws ClassNotFoundException {
		return fromValue(Class.forName(className), otherType);
	}

	/**
	 * add extra type
	 * 
	 * @param extraType
	 * @return
	 */
	public static SupportedClassType addExtraType(SupportedClassType supportedClassType, Class<?>... extraType) {
		Set<Class<?>> clazzSet = new HashSet<Class<?>>();
		for (Class<?> clazz : supportedClassType.valueOf()) {
			clazzSet.add(clazz);
		}
		if (Others.equals(supportedClassType)) {
			clazzSet.remove(Void.class);
		}
		for (Class<?> clazz : extraType) {
			clazzSet.add(clazz);
		}
		supportedClassType.classes = clazzSet.toArray(supportedClassType.classes);
		return supportedClassType;
	}

	public static SupportedClassType addDataMap(SupportedClassType supportedClassType, String key, Object value) {
		supportedClassType.dataMap.put(key, value);
		return supportedClassType;
	}

	public static SupportedClassType addDataMap(SupportedClassType supportedClassType, Map<String, Object> dataMap) {
		if (dataMap != null) {
			supportedClassType.dataMap.putAll(dataMap);
		}
		return supportedClassType;
	}

	public static Set<String> getAllTypes(SupportedClassType otherType) {
		Set<String> classes = new HashSet<String>();
		if (otherType != null) {
			classes.addAll(Arrays.asList(otherType.classNames()));
		}
		for (SupportedClassType sct : SupportedClassType.values()) {
			if (!SupportedClassType.Others.equals(sct))
				classes.addAll(Arrays.asList(sct.classNames()));
		}
		return classes;
	}

	public static boolean in(SupportedClassType classType, SupportedClassType otherType) {
		if (classType == null)
			return false;
		for (SupportedClassType sct : SupportedClassType.values()) {
			if (!SupportedClassType.Others.equals(sct) && classType.equals(sct))
				return true;
		}
		if (otherType != null && classType.equals(otherType)) {
			return true;
		}
		return false;
	}
}
