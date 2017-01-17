package com.cosmos.workflow.logic;

import java.net.URL;
import java.net.URLClassLoader;

class LogicClassLoader extends URLClassLoader{

	public LogicClassLoader(ClassLoader parent) {
		super(new URL[0], parent);
	}

	public Class<?> loadClass(String fullName, JavaClassCodeFileObject jco) {
		byte[] classData = jco.getClassCodeByteArray();
		return this.defineClass(fullName, classData, 0, classData.length);
	}
}
