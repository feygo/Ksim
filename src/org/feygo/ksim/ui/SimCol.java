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
		//ִ�в�ּ��
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
	 * * ��ȡ
	 */
	public void pull() {
		// �������Ʒ,�Լ���ȡ����������
		// 0Ϊ ����ȡ��-1Ϊ������ȡ��nΪ������ȡ
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
			// ��鵱ǰ�еĿ�λ
			int curCnt = getNodeList().size();
			if (curCnt >= wip) {
				AAL.a("������" + conf.getId() + "WIPΪ"+wip+"������" + curCnt+"��λ���㣬��������ȡ��");
			} else {
				int blank = wip - curCnt;
				if (batchPull != 0) {
					if (batchPull > blank) {
						AAL.a("������" + conf.getId() + "������ȡ" + batchPull + "����λ" + blank + "��������ȡ��");
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
		// ������ǰ���У���ÿ���ȡ�ı�ѡ������
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
			//ȥ��
			HashSet<TaskNodeW2> tmpSet=new HashSet<TaskNodeW2>(cList);
			List<TaskNodeW2> ccList=new ArrayList<TaskNodeW2>(tmpSet);
			// ��ѡ��������
			if(ccList.isEmpty()) {
				AAL.a("������" + conf.getId() + "�޿���ȡ��");				
			}else if (pullCnt==-1||ccList.size() <= pullCnt) {
				AAL.a("������" + conf.getId() + "׼����ȡ "+pullCnt+"������ȡ"+ccList.size() +"����"+ ccList);
				Simulator.getSim().moveTaskNode(ccList, this);
				
			} else {
				List<TaskNodeW2> subList=ccList.subList(0, pullCnt);
				AAL.a("������" + conf.getId() + "׼����ȡ "+pullCnt +"����:"+ccList.size()+"�У�" + subList);
				Simulator.getSim().moveTaskNode(subList, this);				
			}
		}
	}

	/**
	 * *�����ɹ����б�
	 * @param flowId
	 * @return
	 */
	protected List<TaskNodeW2> getWorkDoneList(String flowId) {
		return workDoneList;
	}

	public void work() {
		//ƽ��������
		// ������ϵĹ���Ч��
		double tp=conf.getTp();
		// ����������еĹ�����
		int taskCnt=getNodeList().size();
		int doneCnt=getWorkDoneList("").size();
		// ��ù���������Ч��
		double inc=tp/(taskCnt-doneCnt);
		// ��tasklist����������������
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
	 * �����е����est���� ����ֹ���
	 * @param node
	 */
	public void checkDisagg(TaskNodeW2 node) {
		// �������޶�
		int estMax=conf.getDisaggMax();
		int est=node.getTaskBean().getEst();
		//�ж��Ƿ���Ҫ���
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
	 * ֪ͨ�Լ������̺�����һ���Լ�
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
