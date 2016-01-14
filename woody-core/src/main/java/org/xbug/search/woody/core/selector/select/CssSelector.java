package org.xbug.search.woody.core.selector.select;

import java.util.List;

import org.jsoup.nodes.Document;
import org.xbug.search.woody.core.model.annotation.ExprType;
import org.xbug.search.woody.core.selector.select.parser.SelectParseFactory;
import org.xbug.search.woody.core.selector.select.parser.SelectParser;


public class CssSelector extends BasicQuerySelector {

	public static final String PREFIX = "css";

	public CssSelector(String query) {
		super(query);
	}

	@Override
	protected List<String> $selectList(String text) {
		if (Void.class.equals(this.getImplClass())) {
			this.impl(Document.class);
		}
		SelectParser cssSelectParser = SelectParseFactory.create(ExprType.CSS, this.getImplClass(), dataMap);
		List<String> results = cssSelectParser.parse(this.query, text);
		return results;
	}

	@Override
	public String toString() {
		return "CssSelector [query=" + query + ", dataMap=" + dataMap + ", haveValidate=" + haveValidate
				+ ", implClass=" + implClass + ", multi=" + multi + ", notNull=" + notNull + ", defaultValue="
				+ defaultValue + ", asString=" + asString + ", delimiter=" + delimiter + "]";
	}

}
