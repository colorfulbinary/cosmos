package com.cosmos.workflow.logic;

public interface ILogicSource {
	public static final String chooseSource = 
			"import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;"
			+"import com.cosmos.workflow.logic.Logic;%s"
			+"public class %s extends Logic{"
			+"public ISequenceLogicData execute(ISequenceLogicData payload) throws Exception{"
			+"payload.setSuccess((%s));"
			+"return payload;}}";
	
	public static final String codeSource = 
			"import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;"
			+"import com.cosmos.workflow.logic.Logic;%s"
			+"public class %s extends Logic{"
			+"public ISequenceLogicData execute(ISequenceLogicData payload)  throws Exception{%s return payload;}}";
}
