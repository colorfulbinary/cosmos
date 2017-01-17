package com.cosmos.workflow.logic;

import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class CompileException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private List<Diagnostic<? extends JavaFileObject>> diagnostics;
	
	public List<Diagnostic<? extends JavaFileObject>> getDiagnostics() {
		return diagnostics;
	}

	public CompileException(List<Diagnostic<? extends JavaFileObject>> diagnostics){
		this.diagnostics = diagnostics;
	}
	
	public CompileException(List<Diagnostic<? extends JavaFileObject>> diagnostics,String msg){
		super(msg);
		this.diagnostics = diagnostics;
	}

}
