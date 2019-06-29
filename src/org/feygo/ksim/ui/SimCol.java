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
import org.feygo.ksim.task.TaskBean;
import org.feygo.ksim.task.TaskFactory;
import org.feygo.ksim.tools.AAL;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class SimCol extends ScrollPane {

	protected SimColConf conf;

	private ObservableList<Node> nodeList;
	
	private List<TaskNodeW2> workDoneList=new ArrayList<TaskNodeW2>();

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
		vBox.prefHeightProperty().bind(this.heightProperty());
		//vBox.prefWidthProperty().bind(this.prefViewportWidthProperty());
		//vBox.prefWidthProperty().bind(this.widthProperty().multiply(0.9));

	}

	public void clearTaskNode() {
		getNodeList().clear();
		getWorkDoneList("").clear();
	}
	public void addTaskNode(TaskNodeW2 node) {
		getNodeList().add(node);
		if(node.getProgress()>=1) {
			addToWorkDoneList(node);
		}
		node.intoCol(conf.getId());
		//执行拆分检查
		checkDisagg(node);
	}

	public void removeTaskNode(TaskNodeW2 node) {
		removeTaskNodeOnly(node);
		node.outofCol();
	}
	protected void removeTaskNodeOnly(TaskNodeW2 node) {
		getNodeList().remove(node);
		getWorkDoneList("").remove(node);
	}
	
	protected ObservableList<Node> getNodeList() {
		return nodeList;
	}

	/**
	 * * 拉取
	 */
	public void pull() {
		// 检查在制品,以及拉取的任务数量
		// 0为 不拉取，-1为无限拉取，n为个数拉取
		int pullCnt = 0;
		int wip = conf.getWip();
		int batchPull = conf.getBatchPull();
		if (wip == 0) {
			if (batchPull == 0) {
				pullCnt = -1;
			} else {
				pullCnt = batchPull;
			}
		} else {
			// 检查当前列的空位
			int curCnt = getNodeList().size();
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
			}else if (pullCnt==-1||ccList.size() <= pullCnt) {
				AAL.a("看板列" + conf.getId() + "准备拉取 "+pullCnt+"个但拉取"+ccList.size() +"个："+ ccList);
				Simulator.getSim().moveTaskNode(ccList, this);
				
			} else {
				List<TaskNodeW2> subList=ccList.subList(0, pullCnt);
				AAL.a("看板列" + conf.getId() + "准备拉取 "+pullCnt +"个从:"+ccList.size()+"中：" + subList);
				Simulator.getSim().moveTaskNode(subList, this);				
			}
		}
	}

	/**
	 * *获得完成工作列表
	 * @param flowId
	 * @return
	 */
	protected List<TaskNodeW2> getWorkDoneList(String flowId) {
		return workDoneList;
	}

	public void work() {
		//平均工作法
		// 获得列上的工作效率
		double tp=conf.getTp();
		// 获得列上所有的工作项
		int taskCnt=getNodeList().size();
		int doneCnt=getWorkDoneList("").size();
		// 获得工作增量的效率
		double inc=tp/(taskCnt-doneCnt);
		// 给tasklist列上任务增加增量
		getNodeList().forEach(new Consumer<Node>() {
			@Override
			public void accept(Node t) {
				TaskNodeW2 node=(TaskNodeW2)t;
				if(node.getProgress()<0.0) {
					node.workStart();
				}else if(node.getProgress()<1.0) {
					double progress=node.increment(inc);
					if(progress>=1.0) {
						addToWorkDoneList(node);
						node.workDone();
					}
				}
			}
		});
	}
	protected void addToWorkDoneList(TaskNodeW2 node) {
		workDoneList.add(node);
	}

	/**
	 * 根据列的最大est限制 来拆分工作
	 * @param node
	 */
	public void checkDisagg(TaskNodeW2 node) {
		// 获得最大限额
		int estMax=conf.getDisaggMax();
		int est=node.getTaskBean().getEst();
		//判断是否需要拆解
		if(est>estMax&&estMax!=0) {
			TaskFactory tFactory=Simulator.getSim().getTaskFactory();
			List<TaskBean> dBeanList=tFactory.disaggWorkBean(node.getTaskBean(),estMax);
			dBeanList.forEach(new Consumer<TaskBean>() {
				@Override
				public void accept(TaskBean bean) {
					bean.setDisaggCol(getId());
					TaskNodeW2 sNode=tFactory.getNodeByBean(bean);
					sNode.getStyleClass().add("SubTaskNodeCSS");
					addTaskNode(sNode);
				}
			});
			removeTaskNodeOnly(node);
		}
	}

	/**
	 * 通知自己的流程后列拉一下自己
	 */
	public void messagePull() {
		ArrayList<String> nexColIdList=Simulator.getSim().getSimBoard().getSimBoardConf().getFlowNexCol(getId());
		if(nexColIdList!=null&&!nexColIdList.isEmpty()) {
			nexColIdList.forEach(new Consumer<String>() {
				@Override
				public void accept(String t) {
					String nexColId = t.split("@")[0];
					SimCol col=(SimCol)Simulator.getSim().getSimBoard().lookup("#"+nexColId);
					col.pull();
				}
			});		
		}
	}
}
