package org.feygo.ksim.task;

public class TaskBean {
	
	private String id;
	private int est;
	private String serviceType;
	private String workType;
	
	/****/
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
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	@Override
	public String toString() {
		StringBuffer sBuffer=new StringBuffer();
		sBuffer.append("[");
		sBuffer.append(id).append(":").append(est);
		if(workType!=null) {
			sBuffer.append("-").append(workType);
		}
		if(serviceType!=null) {
			sBuffer.append("-").append(serviceType);
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

	
}
