package org.xbug.search.woody.core.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JavaScriptEngine {

	private ScriptEngineManager mgr;

	private ScriptEngine engine;

	private Invocable inv;

	public JavaScriptEngine(String code) {
		init();
		try {
			engine.eval(code);
			inv = (Invocable) engine;
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	public JavaScriptEngine(File file) {
		init();
		try {
			engine.eval(new FileReader(file));
			inv = (Invocable) engine;
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public JavaScriptEngine(InputStream in) {
		init();
		if (in == null) {
			throw new RuntimeException("can't find the js file(read IO is null)");
		}
		try {
			engine.eval(new InputStreamReader(in));
			inv = (Invocable) engine;
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	public static JavaScriptEngine me(File file) {
		return new JavaScriptEngine(file);
	}

	public static JavaScriptEngine me(String file) {
		return new JavaScriptEngine(JavaScriptEngine.class.getClassLoader().getResourceAsStream(file));
	}

	public void init() {
		mgr = new ScriptEngineManager(JavaScriptEngine.class.getClassLoader());
		engine = mgr.getEngineByMimeType("application/javascript");
	}

	public Object call(String method, Object... args) throws ScriptException, NoSuchMethodException {
		return inv.invokeFunction(method, args);
	}

}
