package org.xbug.search.woody.core.selector.select.parser;

import java.util.List;
import java.util.Map;

public abstract class BasicSelectParser implements SelectParser {

	protected Class<?> implClass;

	protected Map<String, Object> context;

	public BasicSelectParser(Class<?> implClass, Map<String, Object> context) {
		super();
		this.implClass = implClass;
		this.context = context;
	}

	public Class<?> getImplClass() {
		return implClass;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	@Override
	public abstract Object create(String html);

	@Override
	public abstract List<String> parse(String query, String text);

}
