package org.xbug.search.woody.core.selector.select;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbug.search.woody.core.model.FuntionObj;
import org.xbug.search.woody.core.model.SettingConstant;
import org.xbug.search.woody.core.model.SupportedLanguage;
import org.xbug.search.woody.core.parser.JavaScriptEngine;
import org.xbug.search.woody.core.selector.Selector;
import org.xbug.search.woody.core.util.StringUtil;


public abstract class BasicQuerySelector implements Selector {

	private static final Logger LOG = LoggerFactory.getLogger(BasicQuerySelector.class);

	protected String query;

	protected Map<String, Object> dataMap;

	protected boolean haveValidate;

	protected Class<?> implClass;
	
	protected boolean multi;
	
	protected boolean notNull;
	
	protected String defaultValue;
	
	protected boolean asString;
	
	protected String delimiter;

	public BasicQuerySelector(String query) {
		super();
		this.query = query;
		this.dataMap = new HashMap<String, Object>();
		this.haveValidate = false;
		this.implClass = null;
	}

	public BasicQuerySelector impl(Class<?> implClass) {
		this.implClass = implClass;
		return this;
	}
	
	public BasicQuerySelector multi(boolean multi) {
		this.multi = multi;
		return this;
	}
	
	public BasicQuerySelector notNull(boolean notNull) {
		this.notNull = notNull;
		return this;
	}
	
	public BasicQuerySelector defaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}
	
	public BasicQuerySelector asString(boolean asString) {
		this.asString = asString;
		return this;
	}
	
	public BasicQuerySelector delimiter(String delimiter) {
		this.delimiter = delimiter;
		return this;
	}

	protected abstract List<String> $selectList(String text);

	protected String $select(String text) {
		List<String> results = selectList(text);
		String result = null;
		if (isMulti()) {
			int size = results.size();
			String del = getDelimiter();
			StringBuffer buff = new StringBuffer();
			for (int i = 0 ; i < size; i++) {
				buff.append(results.get(i));
				if (i != size - 1) {
					buff.append(del);
				}
			}
			result = buff.toString();
		}else {
			result = first(results);
		}
		return result;
	}

	protected String deepProcess(String result) {
		// create a doc instance from the result text
		Document doc = Jsoup.parse(result,"",Parser.xmlParser());
		// filter
		Object _filters = this.get(SettingConstant.FLITERS);
		String[] filters = null;
		if (_filters != null) {
			filters = (String[]) _filters;
		}
		if (filters.length > 0 && !(filters.length == 1 && StringUtil.isNullOrEmpty(filters[0]))) {
			for (String filter : filters) {
				doc.select(filter).remove();
			}
		}
		// outer html
		Object outerHtml = this.get(SettingConstant.OUTER_HTML);
		boolean isouterHtml = false;
		if (outerHtml != null) {
			isouterHtml = (Boolean) outerHtml;
		}
		if (isouterHtml) {
			result = doc.html();
		} else {
			result = doc.text();
		}
		// invoke function
		Object _function = this.get(SettingConstant.FUNCTION);
		if (_function != null) {
			FuntionObj functionObj = (FuntionObj) _function;
			result = callFunction(result, functionObj);
		}
		// trim
		Object trim = this.get(SettingConstant.TRIM);
		boolean isTrim = true;
		if (trim != null) {
			isTrim = (Boolean) trim;
		}
		if (result != null && isTrim) {
			result = result.trim();
		}
		return result;
	}

	private String callFunction(String originalValue, FuntionObj functionObj) {
		if (functionObj == null)
			return originalValue;
		final String $this = "$this";
		SupportedLanguage language = functionObj.language();
		String path = functionObj.path();
		String method = functionObj.method();
		boolean classpath = functionObj.classpath();
		// replace '$this' to current originalValue
		String[] _args = functionObj.args();
		boolean have$this = false;
		List<Object> argList = new ArrayList<Object>();
		for (Object arg : _args) {
			if (arg.toString().equalsIgnoreCase($this) && !(have$this)) {
				have$this = true;
			}
			argList.add(arg);
		}
		if (!have$this) {
			argList.add(0, $this);
		}
		Object[] args = new Object[argList.size()];
		for (int i = 0; i < argList.size(); i++) {
			Object arg = argList.get(i);
			if ($this.equals(arg.toString())) {
				arg = originalValue;
			}
			args[i] = arg;
		}
		// XXX now just support 'javaScript',
		// in future will support more language
		switch (language) {
		case JavaScript: {
			JavaScriptEngine javaScriptEngine = classpath ? JavaScriptEngine.me(path) : JavaScriptEngine.me(new File(
					path));
			try {
				originalValue = (String) javaScriptEngine.call(method, args);
			} catch (Exception e) {
				LOG.warn(e.toString());
			}
		}
			break;
		default:
			throw new IllegalArgumentException("Unsupport language: " + language);
		}
		return originalValue;
	}

	@Override
	public String select(String text) {
		this.validate0(this.getClass().getSimpleName() + "#Select(String)");
		String result = $select(text);
		if (!StringUtil.isNullOrEmpty(result) && !(this instanceof TestSelector)) {
			result = deepProcess(result);
		}
		return result;
	}
	
	@Override
	public List<String> selectList(String text) {
		this.validate0(this.getClass().getSimpleName() + "#selectList(String)");
		List<String> results = new ArrayList<String>();
		List<String> _results = $selectList(text);
		if (_results != null && _results.size() > 0) {
			for (String result : _results) {
				if (!StringUtil.isNullOrEmpty(result)) {
					result = deepProcess(result);
				}
				results.add(result);
			}
		}
		return results;
	}

	protected String first(List<String> results) {
		return results.size() > 0 ? results.get(0) : EMPTY_RESULT;
	}

	protected boolean validate(StringBuffer buff) {
		if (StringUtil.isNullOrEmpty(this.query))
			return false;
		return true;
	}

	protected BasicQuerySelector runValidate() {
		StringBuffer buff = new StringBuffer();
		if (!validate(buff)) {
			throw new IllegalArgumentException(String.format("The select query is invalid. Query:-->%s<--,msg:%s",
					this.query, buff.toString()));
		}
		return this;
	}

	protected void validate0(String methodName) {
		if (!this.haveValidate) {
			throw new IllegalArgumentException("don't invoke 'runValidate()' before execute method: " + methodName);
		}
	}

	/**
	 * please use {@link #putWithValidate(String, Object)} instead of the method
	 */
	@Deprecated
	public BasicQuerySelector put(String key, Object value) {
		this.dataMap.put(key, value);
		return this;
	}

	public BasicQuerySelector putWithValidate(String key, Object value) {
		this.haveValidate = true;
		return this.put(key, value).runValidate();
	}

	/** please use {@link #putAllWithValidate(Map)} instead of the method */
	@Deprecated
	public BasicQuerySelector putAll(Map<String, Object> dataMap) {
		if (dataMap != null) {
			this.dataMap.putAll(dataMap);
		}
		return this;
	}

	public BasicQuerySelector putAllWithValidate(Map<String, Object> dataMap) {
		this.haveValidate = true;
		return this.putAll(dataMap).runValidate();
	}

	public Object get(String key) {
		return this.dataMap.get(key);
	}
	

	public String getQuery() {
		return query;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public boolean isHaveValidate() {
		return haveValidate;
	}

	public Class<?> getImplClass() {
		return implClass;
	}

	public boolean isMulti() {
		return multi;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public boolean isAsString() {
		return asString;
	}

	public String getDelimiter() {
		return delimiter;
	}

	@Override
	public String toString() {
		return "BasicQuerySelector [query=" + query + ", dataMap=" + dataMap + ", haveValidate=" + haveValidate
				+ ", implClass=" + implClass + ", multi=" + multi + ", notNull=" + notNull + ", defaultValue="
				+ defaultValue + ", asString=" + asString + ", delimiter=" + delimiter + "]";
	}

}
