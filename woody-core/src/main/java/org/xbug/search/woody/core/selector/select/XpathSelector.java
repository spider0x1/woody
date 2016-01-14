package org.xbug.search.woody.core.selector.select;

import java.util.List;

import org.htmlcleaner.TagNode;
import org.xbug.search.woody.core.model.annotation.ExprType;
import org.xbug.search.woody.core.selector.select.parser.SelectParseFactory;
import org.xbug.search.woody.core.selector.select.parser.SelectParser;


public class XpathSelector extends BasicQuerySelector {

	public XpathSelector(String query) {
		super(query);

	}

	@Override
	protected List<String> $selectList(String text) {
		if (Void.class.equals(this.getImplClass())) {
			this.impl(TagNode.class);
		}
		SelectParser xpathSelectParser = SelectParseFactory.create(ExprType.XPATH, this.getImplClass(), dataMap);
		List<String> results = xpathSelectParser.parse(query, text);
		return results;
	}

	@Override
	public String toString() {
		return "XpathSelector [query=" + query + ", dataMap=" + dataMap + ", haveValidate=" + haveValidate
				+ ", implClass=" + implClass + ", multi=" + multi + ", notNull=" + notNull + ", defaultValue="
				+ defaultValue + ", asString=" + asString + ", delimiter=" + delimiter + "]";
	}

}
