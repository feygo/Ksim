package org.feygo.ksim.task;

public class TaskRecord {
	
	/** 基本属性 **/
	private int intoColTime;
	private int outofColTime;
	private int workStartTime;
	private int workDoneTime;
	private String colId;
	private String taskId;
	/** 计算属性 **/
	private int cycleTime;
	private int valueTime;
	private int nonValueTime;

	public int getIntoColTime() {
		return intoColTime;
	}

	public void setIntoColTime(int intoColTime) {
		this.intoColTime = intoColTime;
	}

	public int getOutofColTime() {
		return outofColTime;
	}

	public void setOutofColTime(int outofColTime) {
		this.outofColTime = outofColTime;
	}

	public int getWorkStartTime() {
		return workStartTime;
	}

	public void setWorkStartTime(int workStartTime) {
		this.workStartTime = workStartTime;
	}

	public int getWorkDoneTime() {
		return workDoneTime;
	}

	public void setWorkDoneTime(int workDoneTime) {
		this.workDoneTime = workDoneTime;
	}

	public String getColId() {
		return colId;
	}

	public void setColId(String colId) {
		this.colId = colId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getCycleTime() {
		return cycleTime;
	}

	public void setCycleTime(int cycleTime) {
		this.cycleTime = cycleTime;
	}
	
	public int getValueTime() {
		return valueTime;
	}

	public void setValueTime(int valueTime) {
		this.valueTime = valueTime;
	}

	public int getNonValueTime() {
		return nonValueTime;
	}

	public void setNonValueTime(int nonValueTime) {
		this.nonValueTime = nonValueTime;
	}

	public void fresh() {
		cycleTime=outofColTime-intoColTime;
		valueTime=workDoneTime-workStartTime;
		nonValueTime=cycleTime-valueTime;
	}

	@Override
	public String toString() {
		StringBuffer sBuffer=new StringBuffer();
		sBuffer.append(intoColTime).append("-");
		sBuffer.append(workStartTime).append("-");
		sBuffer.append(workDoneTime).append("-");
		sBuffer.append(outofColTime);
		sBuffer.append(" CT:").append(cycleTime);		
		return sBuffer.toString();
	}
	
}
