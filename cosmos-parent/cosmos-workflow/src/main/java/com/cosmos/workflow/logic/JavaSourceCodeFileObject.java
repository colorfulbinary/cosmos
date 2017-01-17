package com.cosmos.workflow.logic;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

class JavaSourceCodeFileObject extends SimpleJavaFileObject{

	private String sourceCode;
	
	public JavaSourceCodeFileObject(String className, String sourceCode) {
		super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
		this.sourceCode = sourceCode;
	}
	
	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors)
			throws IOException {
		return this.sourceCode;
	}

}
