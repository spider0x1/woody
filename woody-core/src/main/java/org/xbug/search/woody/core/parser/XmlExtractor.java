package org.xbug.search.woody.core.parser;

import java.lang.reflect.Field;
import java.util.Map;

import org.xbug.search.woody.core.selector.Selector;


public class XmlExtractor extends BasicExtractor {

	@Override
	protected Selector getSelector(Field field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean multi(Field field) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean notNull(Field field) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String defaultValue(Field field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<?> javaBean(Field field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, Object> dataMap(Field field) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean asString(Field field) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected String delimiter(Field field) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
