package org.xbug.search.woody.core.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xbug.search.woody.core.model.SupportedLanguage;


/**
 * extended config info
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface Setting {

	/**
	 * determine whether use which groupNo, default group 1.
	 * 
	 * @return
	 */
	int groupNo() default 1;

	/**
	 * determine whether get the attribute if the value is not empty when
	 * {@code type} is CSS
	 * 
	 * @return
	 */
	String attrName() default "";

	/**
	 * determine whether trim the value if the value is {@code true}
	 * 
	 * @return
	 */
	boolean trim() default true;

	/**
	 * determine whether remove tag the value if the value is {@code true},
	 * default not remove
	 * 
	 * @return
	 */
	boolean outerHtml() default false;

	/**
	 * set query of filters. use to filter some nodes from result
	 * 
	 * @return query of filters
	 */
	String[] fliters() default {};

	/**
	 * call function to process result, default call
	 * <code>function self($this){return $this;}</code> <br>
	 * 
	 * income value will be <code>$this</code>:indicate current result of query
	 * 
	 * @return Function
	 */
	Function function() default @Function;
	
	public static @interface Function {
		/** default language is javaScript */
		SupportedLanguage language() default SupportedLanguage.JavaScript;

		/** if {@link #classpath()} is {@code true} will lookup from classpath */
		String path() default "utils.js";

		boolean classpath() default true;

		/** method */
		String value() default "self";

		String[] args() default { "$this" };
	}

}
