package org.feygo.ksim.conf;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @author Feygo
 * 限定看板两层
 */
public class SimTitleConf {

	private String id;
	private ArrayList<SimTitleConf> sub;
	@Override
	public String toString() {
		StringBuffer sBuffer=new StringBuffer();
		sBuffer.append(id).append(":").append(sub);
		return sBuffer.toString();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<SimTitleConf> getSub() {
		return sub;
	}
	public void setSub(ArrayList<SimTitleConf> sub) {
		this.sub = sub;
	}
	
}
