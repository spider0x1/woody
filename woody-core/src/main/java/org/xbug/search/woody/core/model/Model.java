package org.xbug.search.woody.core.model;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xbug.search.woody.core.util.StringUtil;


public abstract class Model {
	private static boolean FORMAT = true;
	private static SimpleDateFormat SDF = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");

	public Model() {
		super();
	}

	public boolean isValid() {
		return true;
	}

	@SuppressWarnings("unchecked")
	public static <T> T create(String json, Class<T> clazz) {
		Object obj = null;
		Field[] fields = clazz.getDeclaredFields();
		try {
			JSONObject jsonObj = new JSONObject(json);
			obj = clazz.newInstance();
			for (Field field : fields) {
				Inject inject = field.getAnnotation(Inject.class);
				if ((inject == null))
					continue;
				field.setAccessible(true);
				Class<?> fclazz = field.getType();
				String fname = field.getName();
				if (!jsonObj.has(fname)) {
					field.set(obj, null);
					continue;
				}

				SupportedClassType supportedClassType = SupportedClassType.fromValue(fclazz, null);
				switch (supportedClassType) {
				case String:
				case Character:
					field.set(obj, jsonObj.getString(fname));
					break;
				case Byte:
				case Short:
				case Integer:
					field.setInt(obj, jsonObj.getInt(fname));
					break;
				case Long:
					field.setLong(obj, jsonObj.getLong(fname));
					break;
				case Double:
				case Float:
					field.setDouble(obj, jsonObj.getDouble(fname));
					break;
				case Boolean:
					field.setBoolean(obj, jsonObj.getBoolean(fname));
					break;
				case Date:
					field.set(obj, SDF.parse(jsonObj.getString(fname)));
					break;
				case Array:
					break;
				default: {
					SupportedClassType otherType = SupportedClassType.addExtraType(SupportedClassType.Others,
							JSONObject.class, JSONArray.class);
					try {
						field.set(obj, otherType.cast(jsonObj.get(fname)));
					} catch (Exception e) {
						throw new IllegalArgumentException(SupportedClassType.getAllTypes(otherType).toString(), e);
					}
				}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (T) obj;
	}

	public String toJson() {
		Class<?> clazz = this.getClass();
		boolean all = false;
		if (clazz.getAnnotation(Inject.class) != null) {
			all = true;
		}
		Field[] fields = clazz.getDeclaredFields();
		JSONObject jsonObj = new JSONObject();
		try {
			for (Field field : fields) {
				if (!all) {
					Inject inject = field.getAnnotation(Inject.class);
					if (inject == null)
						continue;
				}
				field.setAccessible(true);
				Class<?> fclazz = field.getType();
				String fname = field.getName();
				SupportedClassType supportedClassType = SupportedClassType.fromValue(fclazz, null);
				if (field.get(this) == null) {
					jsonObj.put(fname, "");
					continue;
				}
				switch (supportedClassType) {
				case String:
					jsonObj.put(fname, (String) field.get(this));
					break;
				case Character:
					jsonObj.put(fname, field.getChar(this));
					break;
				case Byte:
					jsonObj.put(fname, field.getByte(this));
					break;
				case Short:
					jsonObj.put(fname, field.getShort(this));
					break;
				case Integer:
					jsonObj.put(fname, field.getInt(this));
					break;
				case Long:
					jsonObj.put(fname, field.getLong(this));
					break;
				case Double:
					jsonObj.put(fname, field.getDouble(this));
					break;
				case Float:
					jsonObj.put(fname, field.getFloat(this));
					break;
				case Boolean:
					jsonObj.put(fname, field.getBoolean(this));
					break;
				case List:
				case Set:
					@SuppressWarnings("unchecked")
					Collection<String> coll = (Collection<String>) field.get(this);
					jsonObj.put(fname, coll);
					break;
				case Date:
					jsonObj.put(fname, SDF.format((java.util.Date) field.get(this)));
					break;
				case Array:
					Object val = field.get(this);
					int len = Array.getLength(val);
					JSONArray jsonArray = new JSONArray();
					for (int i = 0; i < len; i++) {
						jsonArray.put(Array.get(val, i));
					}
					jsonObj.put(fname, jsonArray);
					break;
				default: {
					if (this != null) {
						try {
							jsonObj.put(fname, field.get(this));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObj.toString();
	}

	public String toTsv() {
		final char tab = '\t';
		Field[] fields = this.getClass().getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		try {
			for (int i = 0, len = fields.length; i < len; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				Inject inject = field.getAnnotation(Inject.class);
				if (refuse(inject, Inject.Type.JSON))
					continue;
				Class<?> typeClass = field.getType();
				String value = getToString(typeClass, field);
				sb.append(trim(value));
				if (i < len - 1) {
					sb.append(tab);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();

	}

	private String trim(String str) {
		if (StringUtil.isNullOrEmpty(str))
			return "";
		return str.replaceAll("\\s+", " ").trim();
	}

	public String toString() {
		final char N = '\n';
		Field[] fields = this.getClass().getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append("[");
		if (FORMAT) {
			sb.append(N);
		}
		try {
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				if (i > 0) {
					sb.append(",");
					if (FORMAT) {
						sb.append(N);
					}
				}
				sb.append(" ");
				sb.append(field.getName()).append("=");
				Class<?> typeClass = field.getType();
				String value = getToString(typeClass, field);
				sb.append(value);
			}
			if (FORMAT) {
				sb.append(N);
			}
			sb.append("]");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String getToString(Class<?> typeClass, Field field) {
		String value = null;
		try {
			Object val = field.get(this);
			if (val != null) {
				if (typeClass.isArray()) {
					int len = Array.getLength(val);
					Object array = Array.newInstance(Object.class, len);
					for (int i = 0; i < len; i++) {
						Array.set(array, i, Array.get(val, i));
					}
					value = Arrays.toString((Object[]) array);
				} else if (typeClass.isAssignableFrom(Collection.class)) {
					value = ((Collection<?>) val).toString();
				} else if (typeClass.isAssignableFrom(Map.class)) {
					value = ((Map<?, ?>) val).toString();
				} else {
					value = val.toString();
				}
			} else {
				val = "null";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static void setFormat(boolean format) {
		FORMAT = format;
	}

	public String toHeader() {
		final String tab = "\t";
		Field[] fields = this.getClass().getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		try {
			for (int i = 0, len = fields.length; i < len; i++) {
				Field field = fields[i];
				Inject inject = field.getAnnotation(Inject.class);
				if (refuse(inject, null))
					continue;
				String fname = field.getName();
				sb.append(fname);
				if (i < len - 1) {
					sb.append(tab);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();

	}

	private boolean refuse(Inject inject, Inject.Type refusedType) {
		if (inject == null)
			return true;
		if (refusedType == null)
			return false;
		if (inject.value().equals(refusedType))
			return true;
		if (inject.value().equals(Inject.Type.ALL))
			return false;
		return false;
	}
}