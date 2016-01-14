package org.xbug.search.woody.core.selector.flow;

import java.util.List;

import org.xbug.search.woody.core.selector.Selector;
import org.xbug.search.woody.core.selector.select.BasicQuerySelector;
import org.xbug.search.woody.core.util.StringUtil;


public class AndSelector extends BasicFlowSelector {

	public AndSelector(List<Selector> selectors) {
		super(selectors);
	}

	public AndSelector(Selector... selectors) {
		super(selectors);
	}
	
	@Override
	protected void validate0() {
		super.validate0();
		if (selectors.size() != 2) {
			throw new IllegalStateException("selectors size must be equals '2' in AndSelector!");
		}
		for (Selector selector : selectors) {
			if (!BasicQuerySelector.class.isAssignableFrom(selector.getClass())) {
				throw new IllegalStateException("selector must be assignable from 'BasicQuerySelector' in in AndSelector!");
			}
		}
	}

	@Override
	public String select(String text) {
		BasicQuerySelector selector0 = (BasicQuerySelector)selectors.get(0);
		BasicQuerySelector selector1 = (BasicQuerySelector)selectors.get(1);
		
		if (selector0.isMulti() && !selector1.isMulti()) {
			selector0 = (BasicQuerySelector)selectors.get(1);
			selector1 = (BasicQuerySelector)selectors.get(0);
		}
		
		text = selector0.select(text);
		if (StringUtil.isNullOrEmpty(text))
			return EMPTY_RESULT;
		text = selector1.select(text);
		
		return text;
	}

	@Override
	public List<String> selectList(String text) {
		List<String> results = EMPTY_RESULT_LIST;
		for (int i = 0; i < selectors.size(); i++) {
			Selector selector = selectors.get(i);
			results = selector.selectList(text);
			if (results == null || results.size() == 0) {
				return EMPTY_RESULT_LIST;
			}
			if (i < selectors.size() -1) {
				text = results.get(0);
			}
		}
		return results;
	}

}
