package org.feygo.ksim.conf;

public class SimColConf {
	private String title;
	private String id;
	private double tp;
	private int wip;
	private int batchPull;
	private String colType;
	
	// 此列的最大任务项，超过执行拆解
	private int disaggMax;
	
	
	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
	public int getDisaggMax() {
		return disaggMax;
	}
	public void setDisaggMax(int disaggMax) {
		this.disaggMax = disaggMax;
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
		if(colType!=null) {
			sBuffer.append(colType);
		}
		sBuffer.append("col:").append(title);
		sBuffer.append(" id:").append(id);
		sBuffer.append(" tp:").append(tp);
		sBuffer.append(" wip:").append(wip);
		sBuffer.append(" batchPull:").append(batchPull);
		sBuffer.append("}");
		return sBuffer.toString();
	}
	
	
}
