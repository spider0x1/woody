package org.xbug.search.woody.core.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ExtractBy {

	/**
	 * Extractor expression, support XPath, CSS Selector and regex.
	 * 
	 * @return extractor expression
	 */
	String value();

	/**
	 * Extractor type, support XPath, CSS Selector and regex.
	 * 
	 * @return extractor type
	 */
	ExprType type() default ExprType.XPATH;
	
	/**
	 * assign the implement of {@link #type()}, default using default implement.
	 * @return
	 */
	Class<?> impl() default Void.class;

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
	 * @return the default value
	 */
	String defaultValue() default "";
	
	/**
	 * If you set {@link org.xbug.search.woody.core.model.annotation.ExtractBy#multi()} and the entity type is not List, you need set {@code asString} is 'true'
	 * to tell compiler, I am not List but I am String*.
	 * Usually you need set {@link #delimiter}
	 * @return
	 */
	boolean asString() default false;
	
	String delimiter() default "|";

	/**
	 * set extra setting for string process, group etc
	 * 
	 * @return Setting object
	 */
	Setting setting() default @Setting;
	

	KV[] dataMap() default {@KV};
}
