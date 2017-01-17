package com.cosmos.workflow.activities.sequence.action.call;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.cosmos.utils.identity.IDCreator;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;
import com.cosmos.workflow.logic.CompileException;
import com.cosmos.workflow.logic.ILogicSource;
import com.cosmos.workflow.logic.Logic;

public class CodeActivity extends ActionActivity{
	
	private static final long serialVersionUID = 881792561687635716L;

	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private String code;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	private String importString;
	
	
	public String getImportString() {
		return importString;
	}
	
	public void setImportString(String importString) {
		this.importString = importString;
	}
	
	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		lock.readLock().lock();
		if(this.getLogic() == null){
			lock.readLock().unlock();
			lock.writeLock().lock();
			if(this.getLogic() == null){
				try {
					Logic logic = Logic.getLogic("CODE"+IDCreator.getDateString(), ILogicSource.codeSource,this.getImportString(),this.getCode(),this.getActivityId());
					this.setLogic(logic);
					lock.readLock().lock();
				} catch (CompileException e) {
					throw new WorkflowException(this,"code编译失败:" + e.getMessage(),e);
				} finally {
					lock.writeLock().unlock();
				}
			}
		}
		lock.readLock().unlock();
		try {
			this.getLogic().execute(data);
		} catch (Exception e) {
			throw new WorkflowException(this,"动态执行错误:" + e.getMessage(),e);
		}
	}

	@Override
	public void release() {
		this.code = null;
		this.importString = null;
		this.lock = null;
	}

}
