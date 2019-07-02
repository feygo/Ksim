package org.feygo.ksim.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.feygo.ksim.data.DataCenter;

public class TaskBean {
	
	private String id;
	private int est;
	private String serviceType;
	private String taskType;
	// 此列中有分解任务，则等待合并
	private String mergeCol;
	
	/** 任务拆解变量 **/	
	private String pId;
	private String disaggCol;
	private int disaggLevel;
	private int disaggCnt;
	
	/** 时间计算变量 **/
	private String curColId;
	private int tmpStartTime;
	private int tmpEndTime;
	private int tmpWorkTime;
	private int tmpDoneTime;
	
	private Map<String,TaskRecord> recordMap=new HashMap<String,TaskRecord>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getEst() {
		return est;
	}
	public void setEst(int est) {
		this.est = est;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	@Override
	public String toString() {
		StringBuffer sBuffer=new StringBuffer();
		sBuffer.append("[");
		sBuffer.append(id).append("#").append(est);
		if(taskType!=null) {
			sBuffer.append("-").append(taskType);
		}
		if(serviceType!=null) {
			sBuffer.append("-").append(serviceType);
		}
		if(mergeCol!=null) {
			sBuffer.append("-mg:").append(mergeCol);
		}
		if(curColId!=null) {
			sBuffer.append(",col:").append(curColId);
		}
		if(pId!=null) {
			sBuffer.append(",pId:").append(pId);
		}
		if(disaggCol!=null) {
			sBuffer.append(",disaggCol:").append(disaggCol);
		}
		if(disaggLevel!=0) {
			sBuffer.append(",disaggLevel:").append(disaggLevel);
		}
		if(disaggCnt!=0) {
			sBuffer.append(",disaggCnt:").append(disaggCnt);
		}
		sBuffer.append("]");
		return sBuffer.toString();
	}
	
	
	public String getCurColId() {
		return curColId;
	}
	public void setCurColId(String curColId) {
		this.curColId = curColId;
	}
	public int getTmpStartTime() {
		return tmpStartTime;
	}
	public void setTmpStartTime(int tmpStartTime) {
		this.tmpStartTime = tmpStartTime;
	}
	public int getTmpEndTime() {
		return tmpEndTime;
	}
	public void setTmpEndTime(int tmpEndTime) {
		this.tmpEndTime = tmpEndTime;
	}
	public int getTmpWorkTime() {
		return tmpWorkTime;
	}
	public void setTmpWorkTime(int tmpWorkTime) {
		this.tmpWorkTime = tmpWorkTime;
	}
	public int getTmpDoneTime() {
		return tmpDoneTime;
	}
	public void setTmpDoneTime(int tmpDoneTime) {
		this.tmpDoneTime = tmpDoneTime;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getDisaggCol() {
		return disaggCol;
	}
	public void setDisaggCol(String disaggCol) {
		this.disaggCol = disaggCol;
	}
	public int getDisaggLevel() {
		return disaggLevel;
	}
	public void setDisaggLevel(int disaggLevel) {
		this.disaggLevel = disaggLevel;
	}
	public int getDisaggCnt() {
		return disaggCnt;
	}
	public void setDisaggCnt(int disaggCnt) {
		this.disaggCnt = disaggCnt;
	}
	public String getMergeCol() {
		return mergeCol;
	}
	public void setMergeCol(String mergeCol) {
		this.mergeCol = mergeCol;
	}
	
	public Map<String, TaskRecord> getRecordMap() {
		return recordMap;
	}
	public void record() {
		TaskRecord tRecord=new TaskRecord();
		tRecord.setColId(curColId);
		tRecord.setTaskId(id);
		tRecord.setIntoColTime(tmpStartTime);
		tRecord.setOutofColTime(tmpEndTime);
		tRecord.setWorkStartTime(tmpWorkTime);
		tRecord.setWorkDoneTime(tmpDoneTime);
		tRecord.fresh();
		
		recordMap.put(curColId, tRecord);
		DataCenter.getDataCenter().addTaskRecord(tRecord);
		
		curColId=null;
		tmpStartTime=0;
		tmpEndTime=0;
		tmpWorkTime=0;
		tmpDoneTime=0;
	}

	
}
