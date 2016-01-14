package org.xbug.search.woody.core.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE,ElementType.FIELD })
public @interface ExtractedBean {

  ExtractBy border();
  
  Class<?> clazz();
}
