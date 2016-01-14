package org.xbug.search.woody.core.selector.select.parser;

import java.util.Map;
import java.util.regex.Pattern;

import org.htmlcleaner.TagNode;
import org.jsoup.nodes.Document;
import org.xbug.search.woody.core.model.annotation.ExprType;
import org.xbug.search.woody.core.selector.select.RegexSelector;
import org.xbug.search.woody.core.selector.select.TestSelector;


public class SelectParseFactory {

	public static SelectParser create(ExprType type, Class<?> impl, Map<String, Object> context) {
		return create(type, impl, null, context);
	}

	public static SelectParser create(ExprType type, Class<?> impl, String query, Map<String, Object> context) {
		SelectParser selectParaser = null;
		switch (type) {
		case XPATH:
			if (impl == null)
				impl = TagNode.class;// default
				// if (impl.isAssignableFrom(TagNode.class)) {
			selectParaser = new HtmlCleanerXpathSelectParser(impl, context);
			// }
			break;
		case CSS:
			if (impl == null)
				impl = Document.class;
			// if (impl.isAssignableFrom(Document.class)) {
			selectParaser = new JsoupCssSelectParser(impl, context);
			// }
			break;
		case REGEX:
			if (impl == null)
				impl = Pattern.class;
			selectParaser = (SelectParser) new RegexSelector(query).putAllWithValidate(context);
			break;
		case TEST:
			if (impl == null)
				impl = Pattern.class;
			selectParaser = (SelectParser) new TestSelector(query).putAllWithValidate(context);
			break;
		default:
			selectParaser = new HtmlCleanerXpathSelectParser(impl, context);
			break;
		}
		return selectParaser;
	}
}
