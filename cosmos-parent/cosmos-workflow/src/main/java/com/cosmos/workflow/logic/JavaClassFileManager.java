package com.cosmos.workflow.logic;

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.JavaFileObject.Kind;

class JavaClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager>{

	private JavaClassCodeFileObject javaClassCode;
	
	protected JavaClassFileManager(StandardJavaFileManager fileManager) {
		super(fileManager);
	}
	
	@Override
	public JavaFileObject getJavaFileForOutput(Location location,
			String className, Kind kind, FileObject sibling) throws IOException {
	    this.javaClassCode = new JavaClassCodeFileObject(className, kind);
		return javaClassCode;
	}
	
	public JavaClassCodeFileObject getClassCodeFileObject(){
		return this.javaClassCode;
	}

}
