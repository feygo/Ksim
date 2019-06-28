package org.feygo.ksim.conf;

public class SimColConf {
	private String title;
	private String id;
	private double tp;
	private int wip;
	private int batchPull;
	
	// 此列的最大任务项，超过执行拆解
	private int disaggMax;
	// 此列中有分解任务，则等待合并
	private String mergeCol;
	
	
	public int getDisaggMax() {
		return disaggMax;
	}
	public void setDisaggMax(int disaggMax) {
		this.disaggMax = disaggMax;
	}
	public String getMergeCol() {
		return mergeCol;
	}
	public void setMergeCol(String mergeCol) {
		this.mergeCol = mergeCol;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getTp() {
		return tp;
	}
	public void setTp(double tp) {
		this.tp = tp;
	}
	public int getWip() {
		return wip;
	}
	public void setWip(int wip) {
		this.wip = wip;
	}
	public int getBatchPull() {
		return batchPull;
	}
	public void setBatchPull(int batchPull) {
		this.batchPull = batchPull;
	}
	@Override
	public String toString() {
		StringBuffer sBuffer=new StringBuffer();
		sBuffer.append("{");
		sBuffer.append("title:").append(title);
		sBuffer.append(" id:").append(id);
		sBuffer.append(" tp:").append(tp);
		sBuffer.append(" wip:").append(wip);
		sBuffer.append(" batchPull:").append(batchPull);
		sBuffer.append("}");
		return sBuffer.toString();
	}
	
	
}
