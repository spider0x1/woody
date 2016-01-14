package org.xbug.search.woody.core.selector.select.parser;

import java.util.List;
import java.util.Map;

public abstract class BasicXpathSelectParser extends BasicSelectParser {

	public static final String PREFIX = "xpath";

	public BasicXpathSelectParser(Class<?> implClass, Map<String, Object> context) {
		super(implClass, context);
	}

	@Override
	public abstract Object create(String html);

	@Override
	public abstract List<String> parse(String query, String text);

}
