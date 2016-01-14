package org.xbug.search.woody.core.selector.select.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xbug.search.woody.core.model.ObjectCache;
import org.xbug.search.woody.core.model.SettingConstant;
import org.xbug.search.woody.core.util.StringUtil;


public class JsoupCssSelectParser extends BasicCssSelectParser {

	public JsoupCssSelectParser(Class<?> implClass, Map<String, Object> context) {
		super(implClass, context);
	}

	@Override
	public Object create(String html) {
		return Jsoup.parse(html);
	}

	@Override
	public List<String> parse(String query, String text) {

		List<String> results = new ArrayList<String>();
		// obtain Document object from cache,just for all html
		String key = ObjectCache.createKeyString(PREFIX, text);
		Document doc = (Document)ObjectCache.safeGet(ObjectCache.PARSER, key, this.getImplClass());
		if (doc == null) {
			doc = (Document)create(text);
		}
		Elements elements = doc.select(query);
		if (CollectionUtils.isNotEmpty(elements)) {
			for (Element ele : elements) {
				String result = process(ele);
				results.add(result);
			}
		}
		return results;
	}

	private String process(Element ele) {
		String result = null;
		Object _attrName = this.context.get(SettingConstant.ATTR_NAME);
		if (_attrName != null && !StringUtil.isNullOrEmpty((String) _attrName)) {
			String attrName = (String) _attrName;
			if (!StringUtil.isNullOrEmpty(attrName)) {
				String baseUri = (String)this.context.get(SettingConstant.BASE_URI);
				ele.setBaseUri(baseUri);
				result = ele.attr(attrName);
			}
		} else {
			result = ele.outerHtml();

		}
		return result;
	}

}
