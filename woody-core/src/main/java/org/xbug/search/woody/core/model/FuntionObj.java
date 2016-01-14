package org.xbug.search.woody.core.model;

import java.util.Arrays;

public class FuntionObj {

	private SupportedLanguage language;

	private String path;

	private boolean classpath;

	private String method;

	private String[] args;

	public FuntionObj() {
		this.language = SupportedLanguage.JavaScript;
		this.classpath = true;
	}

	public SupportedLanguage language() {
		return language;
	}

	public FuntionObj setLanguage(SupportedLanguage language) {
		this.language = language;
		return this;
	}

	public String path() {
		return path;
	}

	public FuntionObj setPath(String path) {
		this.path = path;
		return this;
	}

	public boolean classpath() {
		return classpath;
	}

	public FuntionObj setClasspath(boolean classpath) {
		this.classpath = classpath;
		return this;
	}

	public String method() {
		return method;
	}

	public FuntionObj setMethod(String method) {
		this.method = method;
		return this;
	}

	public String[] args() {
		return args;
	}

	public FuntionObj setArgs(String[] args) {
		this.args = args;
		return this;
	}

	@Override
	public String toString() {
		return "FuntionObj [language=" + language + ", path=" + path + ", classpath=" + classpath + ", method="
				+ method + ", args=" + Arrays.toString(args) + "]";
	}

}
