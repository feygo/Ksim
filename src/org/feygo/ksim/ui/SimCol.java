package org.feygo.ksim.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import org.feygo.ksim.task.TaskRecord;
import org.feygo.ksim.task.ui.TaskNodeW2;
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
	/**
	 * 列中添加node
	 * @param node
	 */
	public void addTaskNode(TaskNodeW2 node) {
		addTaskNodeOnly(node);
		node.intoCol(conf.getId());
		//执行拆分检查
		checkDisagg(node);
	}
	protected void addTaskNodeOnly(TaskNodeW2 node) {
		getNodeList().add(node);
	}

	/**
	 * 列中删除Node
	 * @param node
	 */
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
	 * *获得完成工作列表
	 * @param flowId
	 * @return
	 */
	protected List<TaskNodeW2> getWorkDoneList(String flowId) {
		return workDoneList;
	}
	/**
	 * * 拉取，不被子类复写
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
		StringBuffer preColString=new StringBuffer();
		if (preColIdList != null) {
			List<TaskNodeW2> cList = new ArrayList<TaskNodeW2>();
			preColIdList.forEach(new Consumer<String>() {
				@Override
				public void accept(String f) {
					preColString.append(f).append("|");
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
				AAL.a("看板列" + conf.getId() + "准备拉取从"+preColString.toString()+"中拉取"+pullCnt+"个但拉取"+ccList.size() +"个："+ ccList);
				Simulator.getSim().moveTaskNode(ccList, this);
				
			} else {
				List<TaskNodeW2> subList=ccList.subList(0, pullCnt);
				AAL.a("看板列" + conf.getId() + "准备拉取从"+preColString.toString()+"中拉取"+pullCnt +"个，从:"+ccList.size()+"中：" + subList);
				Simulator.getSim().moveTaskNode(subList, this);				
			}
		}
	}

	

	/**
	 * 工作，不被子类复写
	 */
	public void work() {		
		List<String> pIdList=new ArrayList<String>();
		List<TaskNodeW2> changeNodeList=new ArrayList<TaskNodeW2>();
		// 增量工作数量
		int[] incCnt=new int[] {0};
		// 给tasklist列上任务增加增量
		getNodeList().forEach(new Consumer<Node>() {
			@Override
			public void accept(Node t) {
				TaskNodeW2 node=(TaskNodeW2)t;
				if(node.getProgress()<0.0) {
					if(isWorkerCol()) {
						node.workStart();
					}						
				} 
				//工作的节点。
				TaskNodeW2 workDoneNode=null;
				// 进行工作, 非工作列和 工作列进度为 1的作为完成任务项
				if(isWorkerCol()) {
					double inc=workInc(node);
					double progress=node.increment(inc);
					incCnt[0]=incCnt[0]+1;
					if(node.getProgress()>=1.0) {
						workDoneNode=node;
					}
				}else {
					workDoneNode=node;
				}				
				//检查是否要进行合并检查
				boolean isMerge=checkMerge(node);
				if(isMerge) {
					// 如果是，尝试合并是否成功。
					String pId=node.getTaskBean().getpId();
					TaskNodeW2 mNode=mergeTaskNode(pId);
					if(mNode!=null) {
						// 尝试进行合并
						pIdList.add(pId);
						changeNodeList.add(mNode);
						workDoneNode=mNode;
					}else {
						workDoneNode=null;
					}
				}
				if(workDoneNode!=null) {
					addToWorkDoneList(workDoneNode);
					if(isWorkerCol()) {
						workDoneNode.workDone();
					}		
					String lastColId=Simulator.getSim().getSimBoard().getSimBoardConf().getLastColId();
					if(getId().equalsIgnoreCase(lastColId)) {
						workDoneNode.outofCol();
					}
				}
				
			}
		});
		if(incCnt[0]!=0) {
			AAL.a("看板列" + conf.getId()+"为"+incCnt[0]+"个任务项添加增量！");
		}
		/** 删除合并子任务节点 **/
		removeSubNode(pIdList);
		changeNodeList.forEach(new Consumer<TaskNodeW2>() {
			@Override
			public void accept(TaskNodeW2 node) {
				addTaskNodeOnly(node);
			}
		});
		//向后方发动拉取通知
		if(sendMessagePull()) {
			messagePull();
		}
		
	}
	
	protected boolean isWorkerCol() {
		return true;
	}

	private void removeSubNode(List<String> pIdList) {
		pIdList.forEach(new Consumer<String>() {
			@Override
			public void accept(String pId) {
				// 如果合并成功，则正常工作，删除原有子任务，如果合并失败，则继续等待
				ArrayList<TaskNodeW2> sNodeList=mergeNodeMap.get(pId);
				sNodeList.forEach(new Consumer<TaskNodeW2>() {
					@Override
					public void accept(TaskNodeW2 t) {
						removeTaskNodeOnly(t);
					}
				});
				mergeNodeMap.remove(pId);
			}
		});
		pIdList.clear();
	}
	
	protected double workInc(TaskNodeW2 node) {
		//平均工作法
		// 获得列上的工作效率
		double tp=conf.getTp();
		// 获得列上所有的工作项
		int taskCnt=getNodeList().size();
		int doneCnt=getWorkDoneList("").size();
		// 获得工作增量的效率
		double inc=tp/(taskCnt-doneCnt);
		return inc;
	}
	
	protected void addToWorkDoneList(TaskNodeW2 node) {
		workDoneList.add(node);
	}

	/**
	 * 根据列的最大est限制 来拆分工作
	 * @param node
	 */
	protected void checkDisagg(TaskNodeW2 node) {
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

	protected boolean sendMessagePull() {
		return false;
	}
	/**
	 * 通知自己的流程后列拉一下自己
	 */
	private void messagePull() {
		ArrayList<String> nexColIdList=Simulator.getSim().getSimBoard().getSimBoardConf().getFlowNexCol(getId());
		if(nexColIdList!=null&&!nexColIdList.isEmpty()) {
			nexColIdList.forEach(new Consumer<String>() {
				@Override
				public void accept(String t) {
					String nexColId = t.split("@")[0];
					String flowId =t.split("@")[1];
					SimCol nexCol=(SimCol)Simulator.getSim().getSimBoard().lookup("#"+nexColId);
					AAL.a(getId()+"的"+flowId+"流向"+nexColId+"发出拉取通知！");
					nexCol.pull();
				}
			});		
		}
	}
	private Map<String, ArrayList<TaskNodeW2>> mergeNodeMap=new HashMap<String, ArrayList<TaskNodeW2>>();
	
	/**
	 * *检查是否合并，并将不进行合并的子任务，添加到map中
	 * @param node
	 * @return
	 */
	private boolean checkMerge(TaskNodeW2 node) {
		boolean isMerge=false;
		//检查node是否指定了合并列
		String mCol=node.getTaskBean().getMergeCol();
		if(mCol!=null) {
			if(getId().equalsIgnoreCase(mCol)) {
				//检查node是否为子任务
				String pId=node.getTaskBean().getpId();
				if(pId!=null) {
					isMerge=true;
					// 是子任务，则将子任务防止在待合并任务map中。
					ArrayList<TaskNodeW2> sList=mergeNodeMap.get(pId);
					if(sList==null) {
						sList=new ArrayList<TaskNodeW2>();
						mergeNodeMap.put(pId, sList);
					}
					if(!sList.contains(node)) {
						sList.add(node);
					}					
					AAL.a(pId+"-当前mergeNodeMap:"+sList.toString());					
				}
			}
		}		
		return isMerge;
	}

	/**
	 * 合并子任务项，进度为1
	 * @param pId
	 * @return
	 */
	private TaskNodeW2 mergeTaskNode(String pId) {
		ArrayList<TaskNodeW2> sList=mergeNodeMap.get(pId);
		TaskNodeW2 tmpNode=sList.get(0);
		int sDCnt=tmpNode.getTaskBean().getDisaggCnt();
		if(sList.size()!=sDCnt) {
			return null;			
		}else {
			TaskFactory tFactory=Simulator.getSim().getTaskFactory();
			List<TaskBean> sBeanList=tFactory.getBeanFromNode(sList);
			TaskBean mBean=tFactory.mergeWorkBean(sBeanList);
			AAL.a(pId+"子任务合并为父任务项"+mBean);
			TaskNodeW2 mNode=tFactory.getNodeByBean(mBean);
			mNode.setProgress(1);
			return mNode;
		}
	}

	public boolean isEmpty() {
		return getNodeList().isEmpty();
	}

	public void recordDoneNode() {
		List<TaskBean> beanList=Simulator.getSim().getTaskFactory().getBeanFromNode(getWorkDoneList(""));
		List  recordList=new ArrayList<Map<String,TaskRecord>>(); 
		beanList.forEach(new Consumer<TaskBean>() {
			@Override
			public void accept(TaskBean bean) {
				recordList.add(bean.getRecordMap());
			}
		});		
		Simulator.getSim().getTaskFactory().writeRecordToFile(recordList);
	}

	public SimColConf getSimColConf() {
		return conf;
	}
	
}
