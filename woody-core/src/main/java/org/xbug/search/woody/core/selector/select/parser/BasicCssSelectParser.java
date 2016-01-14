package org.xbug.search.woody.core.selector.select.parser;

import java.util.List;
import java.util.Map;

public abstract class BasicCssSelectParser extends BasicSelectParser {

	public static final String PREFIX = "css";

	public BasicCssSelectParser(Class<?> implClass, Map<String, Object> context) {
		super(implClass, context);
	}

	@Override
	public abstract Object create(String html);

	@Override
	public abstract List<String> parse(String query, String text);

}
