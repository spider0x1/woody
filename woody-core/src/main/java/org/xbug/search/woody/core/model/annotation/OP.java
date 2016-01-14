package org.xbug.search.woody.core.model.annotation;

/**
 * operator of match logic.
 * <p>
 * <b>AND</b>:need match all {@code expr}<br>
 * <b>OR</b>: just match one {@code expr}(In accordance with the order)
 * <b>JOIN</b>: join all {@code expr} as a string
 * </p>
 */
public enum OP {
	/**
	 * All extractors will be arranged as a pipeline. <br>
	 * The next extractor uses the result of the previous as source.
	 */
	AND,
	/**
	 * All extractors will do extracting separately, <br>
	 * and the results of extractors will combined as the final result.
	 */
	OR,
	/**
	 * All extractors will do extracting separately, <br>
	 * finally join those result as a string(if {@link ConfigInfo#joinPattern()}
	 * is not empty, will use the value as joined pattern)
	 * 
	 */
	JOIN
};
