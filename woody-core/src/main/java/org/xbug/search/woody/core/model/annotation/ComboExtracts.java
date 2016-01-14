package org.xbug.search.woody.core.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Combo 'ComboExtract' extractor with and/or operator.
 * 
 * @author code4crafter@gmail.com <br>
 * @since 0.2.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ComboExtracts {

	/**
	 * The extractors to be combined.
	 * 
	 * @return the extractors to be combined
	 */
	ComboExtract[] value();

	/**
	 * Combining operation of extractors.<br>
	 * 
	 * @return combining operation of extractors
	 */
	OP op() default OP.OR;

	/**
	 * Define whether the extractor return more than one result. When set to
	 * 'true', the extractor return a list of string (so you should define the
	 * field as List). <br>
	 * 
	 * @return whether the extractor return more than one result
	 */
	boolean multi() default false;

	/**
	 * Define whether the field can be null.<br>
	 * If set to 'true' and the extractor get no result, the entire class will
	 * be discarded. <br>
	 * 
	 * @return whether the field can be null
	 */
	boolean notNull() default false;

	/**
	 * Define whether use the value as default value if the value is not
	 * empty<br>
	 * Notice: If {@link #notNull()} is {@code true}, the default value is disable
	 * 
	 * @return
	 */
	String defaultValue() default "";

	KV[] dataMap() default {@KV};
}
