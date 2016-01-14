package org.xbug.search.woody.core.selector.flow;

import java.util.List;

import org.xbug.search.woody.core.selector.Selector;
import org.xbug.search.woody.core.selector.select.TestSelector;
import org.xbug.search.woody.core.util.StringUtil;


public class OrSelector extends BasicFlowSelector {

	public OrSelector(List<Selector> selectors) {
		super(selectors);
	}

	public OrSelector(Selector... selectors) {
		super(selectors);
	}

	@Override
	public String select(String text) {
		for (Selector selector : selectors) {
			String result = selector.select(text);
			if (selector instanceof TestSelector && Boolean.FALSE.toString().equalsIgnoreCase(result)) {
				continue;
			}
			if (!StringUtil.isNullOrEmpty(result)) {
				return result;
			}
		}
		return EMPTY_RESULT;
	}

	@Override
	public List<String> selectList(String text) {
		for (Selector selector : selectors) {
			List<String> results = selector.selectList(text);
			if (results != null && results.size() > 0) {
				return results;
			}
		}
		return EMPTY_RESULT_LIST;
	}

}
