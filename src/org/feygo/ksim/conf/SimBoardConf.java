package org.feygo.ksim.conf;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SimBoardConf {
	private String name;
	private Map<String,ArrayList<String>> Flow;
	private ArrayList<SimTitleConf> Layout;
	private Map<String,SimColConf> Cols;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, ArrayList<String>> getFlow() {
		return Flow;
	}
	public void setFlow(Map<String, ArrayList<String>> flow) {
		Flow = flow;
	}
	public ArrayList<SimTitleConf> getLayout() {
		return Layout;
	}
	public void setLayout(ArrayList<SimTitleConf> layout) {
		Layout = layout;
	}
	public Map<String, SimColConf> getCols() {
		return Cols;
	}
	public void setCols(Map<String, SimColConf> cols) {
		Cols = cols;
	}
	@Override
	public String toString() {
		StringBuffer sBuffer=new StringBuffer();
		sBuffer.append("¡¾kanban:").append(name).append("¡¿\n");
		sBuffer.append("=== flow ===\n");
		Flow.forEach(new BiConsumer<String, ArrayList<String>>() {
			@Override
			public void accept(String t, ArrayList<String> u) {
				sBuffer.append(t).append(":").append(u).append("\n");				
			}
		});
		sBuffer.append("=== layout ===\n");
		Layout.forEach(new Consumer<SimTitleConf>() {
			@Override
			public void accept(SimTitleConf t) {
				sBuffer.append(t).append("\n");
			}
		});
		sBuffer.append("=== cols ===\n");
		Cols.forEach(new BiConsumer<String, SimColConf>() {
			@Override
			public void accept(String t, SimColConf u) {
				sBuffer.append(t).append(":").append(u).append("\n");
			}
		});
		return sBuffer.toString();
	}
	
	
	
}
