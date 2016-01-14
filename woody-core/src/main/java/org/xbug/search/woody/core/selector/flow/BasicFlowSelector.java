package org.xbug.search.woody.core.selector.flow;

import java.util.ArrayList;
import java.util.List;

import org.xbug.search.woody.core.selector.Selector;


public abstract class BasicFlowSelector implements Selector {

	protected List<Selector> selectors;

	public BasicFlowSelector(Selector... selectors) {
		this.selectors = new ArrayList<Selector>();
		for (Selector selector : selectors) {
			this.selectors.add(selector);
		}
		validate0();
	}

	public BasicFlowSelector(List<Selector> selectors) {
		this.selectors = selectors;
		if (this.selectors == null)
			this.selectors = new ArrayList<Selector>();
		validate0();
	}

	@Override
	public abstract String select(String text);

	@Override
	public abstract List<String> selectList(String text);
	
	protected void validate0() {
		if (selectors == null || selectors.isEmpty())
			throw new IllegalStateException("selectors can't empty!");
	} 

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [selectors=" + selectors + "]";
	}

}
