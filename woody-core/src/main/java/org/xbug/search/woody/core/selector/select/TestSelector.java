package org.xbug.search.woody.core.selector.select;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.xbug.search.woody.core.selector.select.parser.SelectParser;


public class TestSelector extends BasicQuerySelector implements SelectParser {

	public TestSelector(String query) {
		super(query);
	}

	@Override
	protected List<String> $selectList(String text) {
		return parse(this.query, text);
	}

	@Override
	public Class<?> getImplClass() {
		return Pattern.class;
	}

	@Override
	public Object create(String html) {
		Pattern pattern = Pattern.compile(this.query, Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
		return pattern;
	}

	@Override
	public List<String> parse(String query, String text) {
		Pattern pattern = (Pattern) create("");
		boolean match = pattern.matcher(text).find();
		List<String> list = new ArrayList<String>();
		list.add(match ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
		return list;
	}

	@Override
	public String toString() {
		return "TestSelector [query=" + query + ", dataMap=" + dataMap + ", haveValidate=" + haveValidate
				+ ", implClass=" + implClass + ", multi=" + multi + ", notNull=" + notNull + ", defaultValue="
				+ defaultValue + ", asString=" + asString + ", delimiter=" + delimiter + "]";
	}

}
