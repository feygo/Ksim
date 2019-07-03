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
	 * �������node
	 * @param node
	 */
	public void addTaskNode(TaskNodeW2 node) {
		addTaskNodeOnly(node);
		node.intoCol(conf.getId());
		//ִ�в�ּ��
		checkDisagg(node);
	}
	protected void addTaskNodeOnly(TaskNodeW2 node) {
		getNodeList().add(node);
	}

	/**
	 * ����ɾ��Node
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
	 * *�����ɹ����б�
	 * @param flowId
	 * @return
	 */
	protected List<TaskNodeW2> getWorkDoneList(String flowId) {
		return workDoneList;
	}
	/**
	 * * ��ȡ���������ิд
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
			//ȥ��
			HashSet<TaskNodeW2> tmpSet=new HashSet<TaskNodeW2>(cList);
			List<TaskNodeW2> ccList=new ArrayList<TaskNodeW2>(tmpSet);
			// ��ѡ��������
			if(ccList.isEmpty()) {
				AAL.a("������" + conf.getId() + "�޿���ȡ��");				
			}else if (pullCnt==-1||ccList.size() <= pullCnt) {
				AAL.a("������" + conf.getId() + "׼����ȡ��"+preColString.toString()+"����ȡ"+pullCnt+"������ȡ"+ccList.size() +"����"+ ccList);
				Simulator.getSim().moveTaskNode(ccList, this);
				
			} else {
				List<TaskNodeW2> subList=ccList.subList(0, pullCnt);
				AAL.a("������" + conf.getId() + "׼����ȡ��"+preColString.toString()+"����ȡ"+pullCnt +"������:"+ccList.size()+"�У�" + subList);
				Simulator.getSim().moveTaskNode(subList, this);				
			}
		}
	}

	

	/**
	 * �������������ิд
	 */
	public void work() {		
		List<String> pIdList=new ArrayList<String>();
		List<TaskNodeW2> changeNodeList=new ArrayList<TaskNodeW2>();
		// ������������
		int[] incCnt=new int[] {0};
		// ��tasklist����������������
		getNodeList().forEach(new Consumer<Node>() {
			@Override
			public void accept(Node t) {
				TaskNodeW2 node=(TaskNodeW2)t;
				if(node.getProgress()<0.0) {
					if(isWorkerCol()) {
						node.workStart();
					}						
				} 
				//�����Ľڵ㡣
				TaskNodeW2 workDoneNode=null;
				// ���й���, �ǹ����к� �����н���Ϊ 1����Ϊ���������
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
				//����Ƿ�Ҫ���кϲ����
				boolean isMerge=checkMerge(node);
				if(isMerge) {
					// ����ǣ����Ժϲ��Ƿ�ɹ���
					String pId=node.getTaskBean().getpId();
					TaskNodeW2 mNode=mergeTaskNode(pId);
					if(mNode!=null) {
						// ���Խ��кϲ�
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
			AAL.a("������" + conf.getId()+"Ϊ"+incCnt[0]+"�����������������");
		}
		/** ɾ���ϲ�������ڵ� **/
		removeSubNode(pIdList);
		changeNodeList.forEach(new Consumer<TaskNodeW2>() {
			@Override
			public void accept(TaskNodeW2 node) {
				addTaskNodeOnly(node);
			}
		});
		//��󷽷�����ȡ֪ͨ
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
				// ����ϲ��ɹ���������������ɾ��ԭ������������ϲ�ʧ�ܣ�������ȴ�
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
		//ƽ��������
		// ������ϵĹ���Ч��
		double tp=conf.getTp();
		// ����������еĹ�����
		int taskCnt=getNodeList().size();
		int doneCnt=getWorkDoneList("").size();
		// ��ù���������Ч��
		double inc=tp/(taskCnt-doneCnt);
		return inc;
	}
	
	protected void addToWorkDoneList(TaskNodeW2 node) {
		workDoneList.add(node);
	}

	/**
	 * �����е����est���� ����ֹ���
	 * @param node
	 */
	protected void checkDisagg(TaskNodeW2 node) {
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

	protected boolean sendMessagePull() {
		return false;
	}
	/**
	 * ֪ͨ�Լ������̺�����һ���Լ�
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
					AAL.a(getId()+"��"+flowId+"����"+nexColId+"������ȡ֪ͨ��");
					nexCol.pull();
				}
			});		
		}
	}
	private Map<String, ArrayList<TaskNodeW2>> mergeNodeMap=new HashMap<String, ArrayList<TaskNodeW2>>();
	
	/**
	 * *����Ƿ�ϲ������������кϲ�����������ӵ�map��
	 * @param node
	 * @return
	 */
	private boolean checkMerge(TaskNodeW2 node) {
		boolean isMerge=false;
		//���node�Ƿ�ָ���˺ϲ���
		String mCol=node.getTaskBean().getMergeCol();
		if(mCol!=null) {
			if(getId().equalsIgnoreCase(mCol)) {
				//���node�Ƿ�Ϊ������
				String pId=node.getTaskBean().getpId();
				if(pId!=null) {
					isMerge=true;
					// �����������������ֹ�ڴ��ϲ�����map�С�
					ArrayList<TaskNodeW2> sList=mergeNodeMap.get(pId);
					if(sList==null) {
						sList=new ArrayList<TaskNodeW2>();
						mergeNodeMap.put(pId, sList);
					}
					if(!sList.contains(node)) {
						sList.add(node);
					}					
					AAL.a(pId+"-��ǰmergeNodeMap:"+sList.toString());					
				}
			}
		}		
		return isMerge;
	}

	/**
	 * �ϲ������������Ϊ1
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
			AAL.a(pId+"������ϲ�Ϊ��������"+mBean);
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
