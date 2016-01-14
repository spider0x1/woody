package org.xbug.search.woody.core.selector.select.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbug.search.woody.core.model.ObjectCache;


public class HtmlCleanerXpathSelectParser extends BasicXpathSelectParser {
	
	private static final Logger LOG = LoggerFactory.getLogger(HtmlCleanerXpathSelectParser.class);

	public HtmlCleanerXpathSelectParser(Class<?> implClass, Map<String, Object> context) {
		super(implClass, context);
	}

	@Override
	public Object create(String html) {
		return new HtmlCleaner().clean(html);
	}

	@Override
	public List<String> parse(String query, String text) {
		List<String> results = new ArrayList<String>();
		// get TagNode object from cache, just for all html
		HtmlCleaner htmlCleaner = new HtmlCleaner();
		String key = ObjectCache.createKeyString(PREFIX, text);
		TagNode tagNode = (TagNode)ObjectCache.safeGet(ObjectCache.PARSER, key, this.getImplClass());
		try {
			if (tagNode == null) {
				tagNode = (TagNode)create(text);
			}
			Object[] objects = tagNode.evaluateXPath(query);
			if (objects != null && objects.length > 0) {
				for (Object object : objects) {
					String val = null;
					if (object instanceof TagNode) {
						TagNode tagNode1 = (TagNode) object;
						val = htmlCleaner.getInnerHtml(tagNode1);
					} else {
						val = object.toString();
					}
					results.add(val);
				}
			}
		} catch (XPatherException e) {
			LOG.warn("Parse text using xpath error", e);
		}
		return results;
	}
	
	
}
