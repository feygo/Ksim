package org.feygo.ksim.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.feygo.ksim.conf.SimBoardConf;
import org.feygo.ksim.conf.SimColConf;
import org.feygo.ksim.sim.Simulator;
import org.feygo.ksim.tools.AAL;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class SimCol extends ScrollPane {

	private SimColConf conf;

	private ObservableList<Node> nodeList;

	public SimCol(SimColConf conf) {
		this.conf = conf;
		this.setId(conf.getId());
		initCol();
		this.getStyleClass().add("SimColCSS");
	}

	private void initCol() {
		VBox vBox = new VBox();
		this.setContent(vBox);
		nodeList = vBox.getChildren();
		vBox.prefWidthProperty().bind(this.widthProperty().multiply(0.9));

	}

	public void addTaskNode(TaskNodeW2 node) {
		nodeList.add(node);
		node.intoCol(conf.getId(), Simulator.getSim().getCurrentTime());
	}

	public void removeTaskNode(TaskNodeW2 node) {
		nodeList.remove(node);
		node.outofCol(Simulator.getSim().getCurrentTime());
	}

	public void pull(int curTime) {
		// 检查在制品,以及拉取的任务数量
		int pullCnt = 0;
		int wip = conf.getWip();
		int batchPull = conf.getBatchPull();
		if (wip == 0) {
			if (batchPull != 0) {
				pullCnt = batchPull;
			} else {
				pullCnt = 1;
			}
		} else {
			// 检查当前列的空位
			int curCnt = nodeList.size();
			if (curCnt >= wip) {
				AAL.a("看板列" + conf.getId() + "WIP为"+wip+"，现有" + curCnt+"空位不足，不足以拉取！");
			} else {
				int blank = wip - curCnt;
				if (batchPull != 0) {
					if (batchPull > blank) {
						AAL.a("看板列" + conf.getId() + "批量拉取" + batchPull + "但空位" + blank + "不足以拉取！");
					} else {
						pullCnt = batchPull;
					}
				} else {
					pullCnt = blank;
				}
			}
		}
		if (pullCnt == 0) {
			return;
		}
		// 从流程前列中，获得可拉取的备选任务项
		SimBoardConf bConf = Simulator.getSim().getSimBoard().getSimBoardConf();
		ArrayList<String> preColIdList = bConf.getFlowPreCol(conf.getId());

		if (preColIdList != null) {
			List<TaskNodeW2> cList = new ArrayList<TaskNodeW2>();
			preColIdList.forEach(new Consumer<String>() {
				@Override
				public void accept(String f) {
					String preColId = f.split("@")[0];
					String flowId = f.split("@")[1];
					SimCol preCol = (SimCol) Simulator.getSim().getSimBoard().lookup("#" + preColId);
					cList.addAll(preCol.getWorkDoneList(flowId));
				}
			});
			//去重
			HashSet<TaskNodeW2> tmpSet=new HashSet<TaskNodeW2>(cList);
			List<TaskNodeW2> ccList=new ArrayList<TaskNodeW2>(tmpSet);
			// 候选队列排序
			if(ccList.isEmpty()) {
				AAL.a("看板列" + conf.getId() + "无可拉取项");				
			}else if (ccList.size() <= pullCnt) {
				AAL.a("看板列" + conf.getId() + "准备拉取"+ccList.size() +"|"+ ccList);
				Simulator.getSim().moveTaskNode(ccList, this);
				
			} else {
				List<TaskNodeW2> subList=ccList.subList(0, pullCnt);
				AAL.a("看板列" + conf.getId() + "准备拉取"+pullCnt +"|" + subList);
				Simulator.getSim().moveTaskNode(subList, this);				
			}
		}
	}

	private List<TaskNodeW2> getWorkDoneList(String flowId) {
		List<TaskNodeW2> list = new ArrayList<TaskNodeW2>();
		if (!nodeList.isEmpty()) {
			list.add((TaskNodeW2) nodeList.get(0));
		}
		return list;
	}

}
