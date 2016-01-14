package org.xbug.search.woody.core.selector.select;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbug.search.woody.core.model.SettingConstant;
import org.xbug.search.woody.core.selector.select.parser.SelectParser;


public class RegexSelector extends BasicQuerySelector implements SelectParser {

	private static final Logger LOG = LoggerFactory.getLogger(RegexSelector.class);

	private Pattern pattern;

	private int selectedGroupNo;

	private int maxGroupNo;

	// list
	// key: groupId, value: value for the group
	private List<Map<Integer, String>> groupResults;

	public RegexSelector(String query) {
		super(query);
	}

	@Override
	protected List<String> $selectList(String text) {
		List<String> results = parse(this.query, text);
		return results;
	}

	private void process(String text) {
		Matcher m = this.pattern.matcher(text);
		while (m.find()) {
			Map<Integer, String> resultMap = new TreeMap<Integer, String>();
			for (int i = 0; i <= maxGroupNo; i++) {
				resultMap.put(i, m.group(i));
			}
			this.groupResults.add(resultMap);
		}
	}

	@Override
	protected boolean validate(StringBuffer buff) {
		boolean valid = super.validate(buff);
		groupResults = new ArrayList<Map<Integer, String>>();
		int groupNo = 0;
		Object _groupNo = this.get(SettingConstant.GROUP_NO);
		if (_groupNo != null)
			groupNo = (Integer) _groupNo;
		selectedGroupNo = groupNo;
		if (valid) {
			try {
				pattern = Pattern.compile(this.query, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
				maxGroupNo = pattern.matcher("").groupCount();
			} catch (PatternSyntaxException e) {
				LOG.warn("parse regex error", e);
				valid = false;
				buff.append(e.getDescription());
			}
			if (valid && (this.selectedGroupNo > maxGroupNo)) {
				valid = false;
				buff.append(String.format("No group:%d, max group:%d", this.selectedGroupNo, this.maxGroupNo));
			}
		}
		return valid;
	}

	@Override
	public Class<?> getImplClass() {
		return Pattern.class;
	}

	@Override
	public Object create(String html) {
		validate(new StringBuffer());
		return this.pattern;
	}

	@Override
	public List<String> parse(String query, String text) {
		List<String> results = new ArrayList<String>();
		this.process(text);// process
		for (Map<Integer, String> resultMap : groupResults) {
			String value = resultMap.get(selectedGroupNo);
			results.add(value);
		}
		return results;
	}

	@Override
	public String toString() {
		return "RegexSelector [pattern=" + pattern + ", selectedGroupNo=" + selectedGroupNo + ", maxGroupNo="
				+ maxGroupNo + ", groupResults=" + groupResults + ", query=" + query + ", dataMap=" + dataMap
				+ ", haveValidate=" + haveValidate + ", implClass=" + implClass + ", multi=" + multi + ", notNull="
				+ notNull + ", defaultValue=" + defaultValue + ", asString=" + asString + ", delimiter=" + delimiter
				+ "]";
	}

}
