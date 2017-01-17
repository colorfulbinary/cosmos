package com.cosmos.workflow.logic;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

class JavaCompileEngine {

	private static JavaCompileEngine instance = new JavaCompileEngine();
	
	public static JavaCompileEngine getInstance(){
		return JavaCompileEngine.instance;
	}
	
	private URLClassLoader parent;

	private String classPath;

	private JavaCompileEngine() {
		this.parent = (URLClassLoader) this.getClass().getClassLoader();
		StringBuilder sb = new StringBuilder();
		// 获取编译路径
		for (URL url : this.parent.getURLs()) {
			String p = url.getFile();
			sb.append(p).append(File.pathSeparator);
		}
		this.classPath = sb.toString();
	}
	
	public Object getObject(String className,String sourceCode,List<String> options) throws CompileException{
		Object result = null;
		if (options == null) {
			options  = new ArrayList<String>(4);
		}
		if (!options.contains("-classpath")) {
			options.add("-classpath");
			options.add(this.classPath);
		}
		if (!options.contains("-encoding")) {
			options.add("-encoding");
			options.add("UTF-8");
		}
		//获取当前系统编译器
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		//用于编译诊断
		DiagnosticCollector<JavaFileObject> dia = new DiagnosticCollector<JavaFileObject>();
		JavaClassFileManager manager = new JavaClassFileManager(compiler.getStandardFileManager(dia, null, null));
		JavaSourceCodeFileObject sourceObj = new JavaSourceCodeFileObject(className, sourceCode);
		CompilationTask task = compiler.getTask(null, manager, dia, options, null, Arrays.asList(sourceObj));
		boolean res = task.call();
		if (res) {
			//编译成功
			LogicClassLoader loader = new LogicClassLoader(this.parent);
			Class<?> clazz = loader.loadClass(className, manager.getClassCodeFileObject());
			try {
				loader.close();
				result = clazz.newInstance();
			} catch (Exception e) {
				throw new CompileException(null,"编译成功，获取实例时发生异常:" + e.getMessage());
			} 
		} else {
			throw new CompileException(dia.getDiagnostics(),"编译失败");
		}
		try {
			manager.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String compilePrint(Diagnostic<?> diagnostic) {
//        System.out.println("Code:" + diagnostic.getCode());
//        System.out.println("Kind:" + diagnostic.getKind());
//        System.out.println("Position:" + diagnostic.getPosition());
//        System.out.println("Start Position:" + diagnostic.getStartPosition());
//        System.out.println("End Position:" + diagnostic.getEndPosition());
//        System.out.println("Source:" + diagnostic.getSource());
//        System.out.println("Message:" + diagnostic.getMessage(null));
//        System.out.println("LineNumber:" + diagnostic.getLineNumber());
//        System.out.println("ColumnNumber:" + diagnostic.getColumnNumber());
        StringBuffer res = new StringBuffer();
        res.append("Code:[" + diagnostic.getCode() + "]\n");
        res.append("Kind:[" + diagnostic.getKind() + "]\n");
        res.append("Position:[" + diagnostic.getPosition() + "]\n");
        res.append("Start Position:[" + diagnostic.getStartPosition() + "]\n");
        res.append("End Position:[" + diagnostic.getEndPosition() + "]\n");
        res.append("Source:[" + diagnostic.getSource() + "]\n");
        res.append("Message:[" + diagnostic.getMessage(null) + "]\n");
        res.append("LineNumber:[" + diagnostic.getLineNumber() + "]\n");
        res.append("ColumnNumber:[" + diagnostic.getColumnNumber() + "]\n");
        return res.toString();
    }
}
