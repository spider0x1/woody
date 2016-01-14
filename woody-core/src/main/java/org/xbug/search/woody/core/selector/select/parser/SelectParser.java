package org.xbug.search.woody.core.selector.select.parser;

import java.util.List;

public interface SelectParser {
	
	
	Class<?> getImplClass();
	
	Object create(String html);

	List<String> parse(String query, String text);
}
