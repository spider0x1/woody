package org.xbug.search.woody.core.selector;

import java.util.ArrayList;
import java.util.List;

public interface Selector {

	String EMPTY_RESULT = "";
	List<String> EMPTY_RESULT_LIST = new ArrayList<String>();

	String select(String text);

	List<String> selectList(String text);
}
