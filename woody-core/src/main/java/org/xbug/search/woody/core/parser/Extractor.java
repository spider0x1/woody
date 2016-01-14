package org.xbug.search.woody.core.parser;

public interface Extractor {

	public void compile(Class<?> clazz, boolean recompile);

	public void compile(Class<?> clazz);

	public Object process(String html) throws Exception;

	public <T> T process(String html, Class<T> clazz, boolean recompile) throws Exception;

	public <T> T process(String html, Class<T> clazz) throws Exception;
	
	public void purge();
	
}
