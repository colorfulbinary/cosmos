package com.cosmos.workflow.activities.sequence.action.call;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cosmos.utils.text.StringUtils;
import com.cosmos.workflow.activities.WorkflowException;
import com.cosmos.workflow.activities.sequence.action.ActionActivity;
import com.cosmos.workflow.activities.sequence.data.ISequenceLogicData;

public class EchoActivity extends ActionActivity{
	
	private static final long serialVersionUID = -7618354784513891796L;

	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	
	private static final SimpleDateFormat format_day = new SimpleDateFormat("yyyy-MM-dd");

	public static final int CONSOLE = 1;
	
	public static final int FILE = 2;
	
	public static final byte[] LINE_CHAR = "\r\n".getBytes();
	
	private String path;
	
	private int type;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public void release() {
	}

	@Override
	public void action(ISequenceLogicData data) throws WorkflowException {
		String key = this.in;
		if(!StringUtils.isEmptyOrNull(key)){
			key = data.getBoundValue(key);
//			if(key.startsWith("{")){
//				key = data.get(key.substring(1,key.length() - 1)).toString();
//			} 
			String info = String.format("[%s]:%s", format.format(new Date()),key);
			if((this.getType() & CONSOLE) == CONSOLE){
				System.out.println(info);
			}
			File file = null;
			if(!StringUtils.isEmptyOrNull(this.path)){
				String path = data.getBoundValue(this.path);
				if((this.getType() & FILE) == FILE && !StringUtils.isEmptyOrNull(path)){
					path = path.replaceAll("\\$\\{date\\}", format_day.format(new Date()));
					file = new File(path);
					FileOutputStream output = null;
					try {
						output = new FileOutputStream(file, file.exists());
						output.write(info.getBytes("utf-8"));
						output.write(LINE_CHAR);
						output.flush();
					} catch (IOException e) {
						e.printStackTrace();
					} finally{
						if(output!=null){
							try {
								output.close();
							} catch (IOException e) {
							}
						}
					}
				}
			}
		}
	}
}
