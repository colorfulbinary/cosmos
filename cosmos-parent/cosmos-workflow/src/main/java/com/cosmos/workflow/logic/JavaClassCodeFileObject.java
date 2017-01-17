package com.cosmos.workflow.logic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

class JavaClassCodeFileObject extends SimpleJavaFileObject{

	private ByteArrayOutputStream out = new ByteArrayOutputStream(500);
	
	public JavaClassCodeFileObject(String className,Kind kind) {
		super(URI.create("string:///" + className.replace('.', '/') + kind.extension), kind);
	}
	
	@Override
	public OutputStream openOutputStream() throws IOException {
		return this.out;
	}
	
	public byte[] getClassCodeByteArray(){
		return this.out.toByteArray();
	}

}
