package com.cosmos.workflow.activities.state;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.cosmos.workflow.WorkflowInitializer;
import com.cosmos.workflow.activities.Activity;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;
import com.cosmos.workflow.activities.sequence.logic.SequenceActivity;
import com.cosmos.workflow.logic.Logic;
import com.cosmos.workflow.runtime.WorkflowRuntimeException;
import com.cosmos.workflow.runtime.factory.WorkflowFactory;
import com.cosmos.workflow.runtime.factory.WorkflowInstance;

public class StateActivity extends STActivity{
	
	private static final long serialVersionUID = 1091207614718812971L;

	public static final int BEGIN = 0;
	
	public static final int NORMAL = 1;
	
	public static final int END = 2;
	
	
	private int type;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private Set<TargetStateEntity> targetList = new HashSet<TargetStateEntity>();
	
	public void putTargetStateInfo(TargetStateEntity entity){
		this.targetList.add(entity);
	}
	
	public Iterator<TargetStateEntity> targetIterator(){
		return targetList.iterator();
	}
	
	@Override
	public StateActivity execute(IStateLogicData data) throws WorkflowException {
		StateActivity target = null;
		if(this.type == StateActivity.END){
			return target;
		}
		Iterator<TargetStateEntity> iterator = this.targetList.iterator();
		while(iterator.hasNext()){
			TargetStateEntity entity = iterator.next();
			Logic logic = entity.getTargetLogic();
			ISequenceLogicData res = data.toSequenceLogicData();
			try {
				logic.execute(res);
			} catch (Exception e) {
				throw new WorkflowException(this,"状态机跳转判断逻辑执行错误:" + e.getMessage());
			}
			if(res.isSuccess()){
				String targetExecutorId = entity.getTargetExecutorId();
				if(targetExecutorId != null && !"".equals(targetExecutorId.trim())){
					WorkflowFactory<?> factory;
					try {
						factory = WorkflowFactory.getWorkflowFactory(WorkflowInitializer.getSequenceFactoryPath());
					} catch (WorkflowRuntimeException e1) {
						throw new WorkflowException(this,"没有找到顺序流工厂:" + targetExecutorId);
					}
					WorkflowInstance<?> targetExecutor = null;
					try {
						targetExecutor = factory.getWorkflowInstance(targetExecutorId);
					} catch (WorkflowRuntimeException e) {
						throw new WorkflowException(this,"没有找到流程:" + targetExecutorId);
					}
					Activity act = targetExecutor.getExecutableActivity(null);
					if(act == null || !(act instanceof SequenceActivity)){
						throw new WorkflowException(this,"没有找到要执行的实例");
					}
					((SequenceActivity)act).execute(data.toSequenceLogicData());
				}
				StateActivity targetActivity = entity.getTargetActivity();
				targetActivity.initialize(res);
				target = targetActivity;
				break;
			}
		}
		return target;
	}

	@Override
	public void release() {
		for (TargetStateEntity target : targetList) {
			target.setTargetExecutorId(null);
			target.setTargetLogic(null);
			target.getTargetActivity().releaseResource();
			target.setTargetActivity(null);
		}
		this.targetList.clear();
		this.targetList = null;
	}

	

}
