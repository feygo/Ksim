package org.feygo.ksim.task;

public class TaskBean {
	
	private String id;
	private int est;
	private String serviceType;
	private String taskType;
	
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

	
}
