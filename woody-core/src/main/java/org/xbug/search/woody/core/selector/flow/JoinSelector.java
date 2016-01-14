package org.xbug.search.woody.core.selector.flow;

import java.util.List;

import org.xbug.search.woody.core.selector.Selector;


public class JoinSelector extends BasicFlowSelector {
	
	public JoinSelector(List<Selector> selectors) {
		super(selectors);
	}

	public JoinSelector(Selector... selectors) {
		super(selectors);
	}

	@Override
	public String select(String text) {
		StringBuffer buff = new StringBuffer();
		for (Selector selector : selectors) {
			String result = selector.select(text);
			if (result == null)
				result = EMPTY_RESULT;
			buff.append(result);
		}
		return buff.toString();
	}

	@Override
	public List<String> selectList(String text) {
		throw new UnsupportedOperationException("didn't implement yet");
	}

}
