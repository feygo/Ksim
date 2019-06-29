package org.feygo.ksim.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.feygo.ksim.conf.SimBoardConf;
import org.feygo.ksim.conf.SimColConf;
import org.feygo.ksim.sim.Simulator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class SimLaneCol extends SimCol {
	
	private Map<String, List<TaskNodeW2>> laneWorkDoneMap=new HashMap<String, List<TaskNodeW2>>();
	
	private Map<String, ObservableList<Node>> laneNodeMap=new HashMap<String, ObservableList<Node>>();

	private String defaultLane;
	
	public SimLaneCol(SimColConf conf) {
		super(conf);
		if(conf.getColType()==null||!conf.getColType().startsWith("Lane")) {
			throw new RuntimeException("看板列"+conf.getId()+"的ColType属性未配置为Lane类型，无法初始化对象~");
		}
		intiLaneCol();
	}
	private void intiLaneCol(){
		SimBoardConf simBoardConf=Simulator.getSim().getSimBoard().getSimBoardConf();
		String colId=conf.getId();
		VBox vBox = new VBox();		
		vBox.setId(colId+"_vBox");
		simBoardConf.getLanes().forEach(new Consumer<Map<String, String>>() {
			@Override
			public void accept(Map<String, String> laneMap) {
				VBox tmpVBox=new VBox();
				String laneId=laneMap.get("id");
				String index=laneMap.get("index");
				tmpVBox.setId(colId+"_"+laneId);
				vBox.getChildren().add(Integer.valueOf(index).intValue(),tmpVBox);
				tmpVBox.prefWidthProperty().bind(vBox.widthProperty());	
				tmpVBox.prefHeightProperty().bind(vBox.heightProperty().divide(simBoardConf.getLanes().size()));
				tmpVBox.getStyleClass().add("SimLaneColCSS");
				
				String defaultString=laneMap.get("default");
				if(defaultString!=null&&"1".equalsIgnoreCase(defaultString)) {
					defaultLane=laneId;
				}
				laneNodeMap.put(laneId, tmpVBox.getChildren());
				laneWorkDoneMap.put(laneId, new ArrayList<TaskNodeW2>());
			}
		});
		vBox.prefWidthProperty().bind(this.widthProperty().multiply(0.9));
		vBox.prefHeightProperty().bind(this.heightProperty());
		//vBox.prefWidthProperty().bind(this.widthProperty().multiply(0.9));
		this.setContent(vBox);
	}
	
	private String getLaneId(String serviceType) {
		String laneId;
		if(laneNodeMap.containsKey(serviceType)) {
			laneId=serviceType;
		}else if(defaultLane!=null) {
			laneId=defaultLane;
		}else {
			throw new RuntimeException("SimBoardConf文件中Lanes属性未配置default默认泳道！");
		}
		return laneId;
	}
	@Override
	public void addTaskNode(TaskNodeW2 node) {
		//判断 node的servietype
		String serviceType=node.getTaskBean().getServiceType();
		String laneId=getLaneId(serviceType);
		laneNodeMap.get(laneId).add(node);
		if(node.getProgress()>=1) {
			laneWorkDoneMap.get(laneId).add(node);
		}
		node.intoCol(conf.getId());
		//执行拆分检查
		checkDisagg(node);
	}
	@Override
	protected void removeTaskNodeOnly(TaskNodeW2 node) {
		//判断 node的servietype
		String serviceType=node.getTaskBean().getServiceType();
		String laneId=getLaneId(serviceType);
		laneNodeMap.get(laneId).remove(node);
		laneWorkDoneMap.get(laneId).remove(node);
	}
	@Override
	protected ObservableList<Node> getNodeList() {
		ObservableList<Node> rList=FXCollections.observableArrayList();
		laneNodeMap.forEach(new BiConsumer<String, ObservableList<Node>>() {
			@Override
			public void accept(String t, ObservableList<Node> u) {
				rList.addAll(u);				
			}
		});
		return rList;
	}
	@Override
	protected List<TaskNodeW2> getWorkDoneList(String flowId) {
		List<TaskNodeW2> rList=new ArrayList<TaskNodeW2>();
		laneWorkDoneMap.forEach(new BiConsumer<String, List<TaskNodeW2>>() {
			@Override
			public void accept(String t, List<TaskNodeW2> u) {
				rList.addAll(u);				
			}
		});
		return rList;
	}
	@Override
	protected void addToWorkDoneList(TaskNodeW2 node) {
		String serviceType=node.getTaskBean().getServiceType();
		String laneId=getLaneId(serviceType);
		laneWorkDoneMap.get(laneId).add(node);
	}
	

	
}
