package org.feygo.ksim.conf;

public class SimColConf {
	private String title;
	private String id;
	private double tp;
	private int wip;
	private int batchPull;
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
