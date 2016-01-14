package org.xbug.search.woody.core.parser;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.htmlcleaner.TagNode;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbug.search.woody.core.model.ExtractedField;
import org.xbug.search.woody.core.model.ObjectCache;
import org.xbug.search.woody.core.model.SupportedClassType;
import org.xbug.search.woody.core.model.annotation.ExprType;
import org.xbug.search.woody.core.model.annotation.OP;
import org.xbug.search.woody.core.selector.Selector;
import org.xbug.search.woody.core.selector.flow.AndSelector;
import org.xbug.search.woody.core.selector.flow.JoinSelector;
import org.xbug.search.woody.core.selector.flow.OrSelector;
import org.xbug.search.woody.core.selector.select.CssSelector;
import org.xbug.search.woody.core.selector.select.RegexSelector;
import org.xbug.search.woody.core.selector.select.TestSelector;
import org.xbug.search.woody.core.selector.select.XpathSelector;
import org.xbug.search.woody.core.selector.select.parser.BasicCssSelectParser;
import org.xbug.search.woody.core.selector.select.parser.BasicXpathSelectParser;
import org.xbug.search.woody.core.selector.select.parser.HtmlCleanerXpathSelectParser;
import org.xbug.search.woody.core.selector.select.parser.JsoupCssSelectParser;
import org.xbug.search.woody.core.util.StringUtil;


public abstract class BasicExtractor implements Extractor {

	private static final Logger LOG = LoggerFactory.getLogger(BasicExtractor.class);

	protected List<ExtractedField> extractedFields;

	protected Class<?> clazz;

	private Field[] fields;

	private static final String PREFIX = "class";

	protected String baseUri;

	public BasicExtractor(String baseUri) {
		this.baseUri = baseUri;
		this.extractedFields = new ArrayList<ExtractedField>();
	}

	public BasicExtractor() {
		this.extractedFields = new ArrayList<ExtractedField>();
	}
	
	protected abstract Selector getSelector(Field field);

	protected abstract boolean multi(Field field);

	protected abstract boolean notNull(Field field);

	protected abstract String defaultValue(Field field);

	protected abstract Class<?> javaBean(Field field);

	protected abstract Map<String, Object> dataMap(Field field);
	
	protected abstract boolean asString(Field field);
	
	protected abstract String delimiter(Field field);
	
	public BasicExtractor baseUri(String baseUri) {
		this.baseUri = baseUri;
		return this;
	}

	@Override
	public void compile(Class<?> clazz, boolean recompile) {
		this.clazz = clazz;
		validate0();
		this.fields = clazz.getDeclaredFields();
		String className = this.clazz.getCanonicalName();
		String key = ObjectCache.createKeyString(PREFIX, className);
		List<ExtractedField> _extractedFields = null;
		if (recompile) {
			_extractedFields = $compile();
			ObjectCache.put(ObjectCache.CLASS, key, _extractedFields);
		} else {
			@SuppressWarnings("unchecked")
			ArrayList<ExtractedField> obj = ObjectCache.safeGet(ObjectCache.CLASS, key, ArrayList.class);
			if (obj != null) {
				_extractedFields = obj;
			} else {
				_extractedFields = $compile();
				ObjectCache.put(ObjectCache.CLASS, key, _extractedFields);
			}
		}
		this.extractedFields = _extractedFields;
	}

	@Override
	public void compile(Class<?> clazz) {
		this.compile(clazz, false);
	}

	private List<ExtractedField> $compile() {
		List<ExtractedField> _extractedFields = new ArrayList<ExtractedField>();
		for (Field field : fields) {
			if (!field.isAccessible())
				field.setAccessible(true);
			Selector selector = getSelector(field);
			if (selector == null)
				continue;
			Method setterMethod = getSetterMethod(clazz, field);
			ExtractedField extractedField = new ExtractedField(selector, field, setterMethod);
			// set isMulti value
			boolean isMulti = multi(field);
			extractedField.setMulti(isMulti);
			// set isNotNull value
			boolean isNotNull = notNull(field);
			extractedField.setNotNull(isNotNull);
			// set defaultValue value
			String defaultValue = defaultValue(field);
			extractedField.setDefaultValue(defaultValue);
			// set javeBean value
			// Class<?> isJavaBean = javaBean(field);
			// extractedField.setJaveBean(isJavaBean);
			// set dataMap value
			Map<String, Object> dataMap = dataMap(field);
			extractedField.setDataMap(dataMap);
			//set asString value
			boolean asString = asString(field);
			extractedField.setAsString(asString);
			//set delimiter value
			String delimiter = delimiter(field);
			extractedField.setDelimiter(delimiter);
			
			validate1(field, extractedField);
			_extractedFields.add(extractedField);
		}
		return _extractedFields;

	}

	@Override
	public Object process(String html) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		Object o = this.clazz.newInstance();
		if (StringUtil.isNullOrEmpty(html))
			return o;
		try {
			// put parse object to cache
			pushParserInstancesToCache(html);
		} catch (Exception e) {
			// nothing
		}
		for (ExtractedField extractedField : extractedFields) {
			Selector selector = extractedField.getSelector();
			Object value = null;
			boolean isNull = false;
			// judge whether or not is 'isMulti'
			if (extractedField.isMulti()) {
				List<String> selectedResultList = selector.selectList(html);
				if (selectedResultList == null || selectedResultList.isEmpty()) {
					isNull = true;
				}
				if (extractedField.isAsString()) {
					StringBuffer buff = new StringBuffer();
					if (!isNull) {
						int size = selectedResultList.size();
						String del = extractedField.getDelimiter();
						for (int i = 0 ; i < size; i++) {
							buff.append(selectedResultList.get(i));
							if (i != size - 1) {
								buff.append(del);
							}
						}
						value = buff.toString();
					}
				} else {
					value = selectedResultList;
				}
			} else {
				String selectedResult = selector.select(html);
				value = selectedResult;
				if (StringUtil.isNullOrEmpty(selectedResult)) {
					isNull = true;
				}
			}
			// check isNotNull
			if (extractedField.isNotNull() && isNull) {
				throw new RuntimeException(String.format("The '%s' filed is not null/empty", extractedField.getField()
						.getName()));
			}
			setField(o, extractedField, value);
		}
		return o;
	}

	@SuppressWarnings({ "unchecked" })
	public <T> T process(String html, Class<T> clazz, boolean recompile) throws InstantiationException,
			IllegalAccessException, InvocationTargetException {
		this.compile(clazz, recompile);
		return (T) this.process(html);
	}

	public <T> T process(String html, Class<T> clazz) throws InstantiationException, IllegalAccessException,
			InvocationTargetException {
		return process(html, clazz, false);
	}

	protected Selector createSingleSelector(String query, ExprType type, Class<?> implClass, boolean multi, boolean notNull, String defaultValue, boolean asString, String delimiter, Map<String, Object> dataMap) {
		Selector selector = null;
		switch (type) {
		case XPATH:
			selector = new XpathSelector(query).putAllWithValidate(dataMap).impl(implClass).multi(multi).notNull(notNull).defaultValue(defaultValue).asString(asString).delimiter(delimiter);
			break;
		case CSS:
			selector = new CssSelector(query).putAllWithValidate(dataMap).impl(implClass).multi(multi).notNull(notNull).defaultValue(defaultValue).asString(asString).delimiter(delimiter);
			break;
		case REGEX:
			selector = new RegexSelector(query).putAllWithValidate(dataMap).impl(implClass).multi(multi).notNull(notNull).defaultValue(defaultValue).asString(asString).delimiter(delimiter);
			break;
		case TEST:
			selector = new TestSelector(query).putAllWithValidate(dataMap).impl(implClass).multi(multi).notNull(notNull).defaultValue(defaultValue).asString(asString).delimiter(delimiter);
			break;
		default:
			selector = new XpathSelector(query).putAllWithValidate(dataMap).impl(implClass).multi(multi).notNull(notNull).defaultValue(defaultValue).asString(asString).delimiter(delimiter);
			break;
		}
		return selector;
	}

	protected Selector appendSelector(OP op, List<Selector> selectorList) {
		Selector selector = null;
		switch (op) {
		case OR:
			selector = new OrSelector(selectorList);
			break;
		case AND:
			selector = new AndSelector(selectorList);
			break;
		case JOIN:
			selector = new JoinSelector(selectorList);
			break;
		default:
			selector = new OrSelector(selectorList);
			break;
		}
		return selector;
	}

	private void validate0() {
		if (this.clazz == null) {
			throw new IllegalStateException(String.format("the extracted class: %s is null", this.clazz.toString()));
		}
	}

	private void validate1(Field field, ExtractedField extractedField) {
		if (extractedField != null) {
			Class<?> fclazz = field.getType();
			if (!extractedField.isMulti()) {
				SupportedClassType supportedClassType = SupportedClassType.fromValue(fclazz);
				if (!SupportedClassType.in(supportedClassType, null)) {
					Set<String> classes = SupportedClassType.getAllTypes(null);
					throw new IllegalStateException("Field " + field.getName() + " must in " + classes);
				}

			} else if (extractedField.isMulti() && (!List.class.isAssignableFrom(fclazz) && !Set.class.isAssignableFrom(fclazz))) {
				if (!extractedField.isAsString()){
					throw new IllegalStateException("Field " + field.getName() + " must be list");
				}
			}
		}
	}

	private Method getSetterMethod(Class<?> clazz, Field field) {
		String name = "set" + StringUtils.capitalize(field.getName());
		try {
			Method setterMethod = clazz.getDeclaredMethod(name, field.getType());
			setterMethod.setAccessible(true);
			return setterMethod;
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	private void setField(Object o, ExtractedField fieldExtractor, Object value) throws IllegalAccessException,
			InvocationTargetException {

		Field field = fieldExtractor.getField();
		Method setterMethod = fieldExtractor.getSetterMethod();

		SupportedClassType supportedClassType = SupportedClassType.fromValue(field.getType());
		SupportedClassType.addDataMap(supportedClassType, fieldExtractor.getDataMap());
		String defaultValue = fieldExtractor.getDefaultValue();
		// default value
		switch (supportedClassType) {
		case List:
			if (value == null)
				value = Collections.EMPTY_LIST;
			break;
		case Set:
			if (value == null)
				value = Collections.EMPTY_SET;
			break;
		case Date:
			if (value == null)
				value = new Date();
			break;
		case Array:
			if (value == null) {
				value = Array.newInstance(String.class, 0);
			}
			break;
		case Integer:
		case Long:
		case Short:
		case Byte:
			if (StringUtil.isNullOrEmpty((String) value)) {
				if (StringUtil.isNullOrEmpty(defaultValue)) {
					value = "0";
				} else {
					value = defaultValue;
				}
			}
			break;
		case Double:
		case Float:
			if (StringUtil.isNullOrEmpty((String) value)) {
				if (StringUtil.isNullOrEmpty(defaultValue)) {
					value = "0.0";
				} else {
					value = defaultValue;
				}
			}
			break;
		default:
			if (value == null || ((String) value).trim().isEmpty()) {
				if (SupportedClassType.Character == supportedClassType) {
					if (StringUtil.isNullOrEmpty(defaultValue)) {
						value = "";
					} else {
						value = defaultValue.charAt(0);
					}
				} else {
					if (StringUtil.isNullOrEmpty(defaultValue)) {
						value = "";
					} else {
						value = defaultValue;
					}
				}
			}
			break;
		}

		Object _value = null;
		switch (supportedClassType) {
		case String: {
			_value = SupportedClassType.String.parseValue(value);
		}
			break;
		case Character: {
			_value = SupportedClassType.Character.parseValue(value);
		}
			break;
		case Byte: {
			_value = SupportedClassType.Byte.parseValue(value);
		}
			break;
		case Short: {
			_value = SupportedClassType.Short.parseValue(value);
		}
			break;
		case Integer: {
			_value = SupportedClassType.Integer.parseValue(value);
		}
			break;
		case Long: {
			_value = SupportedClassType.Long.parseValue(value);
		}
			break;
		case Double: {
			_value = SupportedClassType.Double.parseValue(value);
		}
			break;
		case Float: {
			_value = SupportedClassType.Float.parseValue(value);
		}
			break;
		case Boolean: {
			if (StringUtils.isEmpty((String) value) || Boolean.FALSE.toString().equalsIgnoreCase((String) value)) {
				value = Boolean.FALSE.toString();
			} else {
				value = Boolean.TRUE.toString();
			}
			_value = SupportedClassType.Boolean.parseValue(value);
		}
			break;
		case List: {
			_value = SupportedClassType.List.parseValue(value);
		}
			break;
		case Set: {
			_value = SupportedClassType.Set.parseValue(value);
		}
			break;
		case Date: {
			_value = SupportedClassType.Date.parseValue(value);
		}
			break;
		default:
			throw new IllegalArgumentException("Unsupported class type: " + field.getType().getCanonicalName());
		}
		boolean success = false;
		if (_value != null) {
			success = invokeSetterMethod(o, setterMethod, _value);
			if (!success) {
				field.set(o, _value);
			}
		} else {
			LOG.warn(String.format("the parse result is null at field: %s", field.getName()));
		}

	}

	private boolean invokeSetterMethod(Object o, Method setterMethod, Object value) {
		boolean success = false;
		if (setterMethod != null) {
			try {
				setterMethod.invoke(o, value);
				success = true;
			} catch (Exception e) {
				LOG.warn(
						String.format("Invoke stter method error, at:%s, will try to use 'field.set'",
								setterMethod.getName()), e);
			}
		}
		return success;
	}

	private void pushParserInstancesToCache(String html) throws Exception {
		//ObjectCache.purge(ObjectCache.HTML);
		Map<String, Object> map = new HashMap<String, Object>();
		// CSS
		String cssKey = ObjectCache.createKeyString(BasicCssSelectParser.PREFIX, html);
		if (ObjectCache.get(ObjectCache.PARSER, cssKey) == null) {
			BasicCssSelectParser cssSelectParser = new JsoupCssSelectParser(Document.class, map);
			Document doc = (Document) cssSelectParser.create(html);
			ObjectCache.put(ObjectCache.PARSER, cssKey, doc);
		}
		// XPATH
		String xpathKey = ObjectCache.createKeyString(BasicXpathSelectParser.PREFIX, html);
		if (ObjectCache.get(ObjectCache.PARSER, xpathKey) == null) {
			BasicXpathSelectParser xpathSelectParser = new HtmlCleanerXpathSelectParser(TagNode.class, map);
			TagNode tagNode = (TagNode) xpathSelectParser.create(html);
			ObjectCache.put(ObjectCache.PARSER, xpathKey, tagNode);
		}
	}

	@Override
	public void purge() {
		final boolean force = true;
		ObjectCache.purge(ObjectCache.PARSER, force);
		ObjectCache.purge(ObjectCache.CLASS, force);
	}

	public List<ExtractedField> getExtractedFields() {
		return extractedFields;
	}

	public Class<?> getClazz() {
		return clazz;
	}

}
