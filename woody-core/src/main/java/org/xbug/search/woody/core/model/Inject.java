package org.xbug.search.woody.core.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface Inject {

	public static enum Type {ALL, JSON, MODEL}
	
	Type value() default Type.MODEL;
}
