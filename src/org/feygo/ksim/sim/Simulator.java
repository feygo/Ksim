package org.feygo.ksim.sim;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.feygo.ksim.task.TaskBean;
import org.feygo.ksim.task.TaskFactory;
import org.feygo.ksim.tools.AAL;
import org.feygo.ksim.ui.SimBoard;
import org.feygo.ksim.ui.SimCol;
import org.feygo.ksim.ui.TaskNodeW2;

public class Simulator {

	private SimBoard simBoard;
	private TaskFactory taskFactory;
	private int CurrentTime;
	
	private ArrayList<SimCol> simCols=new ArrayList<SimCol>();
	
	private static Simulator sim;
	public static Simulator getSim() {
		if(sim==null) {
			sim=new Simulator();
		}
		return sim;
	}
	
	public int getCurrentTime() {
		return CurrentTime;
	}
	public void setCurrentTime(int currentTime) {
		CurrentTime = currentTime;
	}
	public SimBoard getSimBoard() {
		return simBoard;
	}
	public void setSimBoard(SimBoard simBoard) {
		this.simBoard = simBoard;
	}
	public TaskFactory getTaskFactory() {
		return taskFactory;
	}
	public void setTaskFactory(TaskFactory taskFactory) {
		this.taskFactory = taskFactory;
	}
	/**
	 ** 初始化任务池
	 * @param list
	 */
	public void initTaskPool(List<TaskBean> list) {
		//根据taskbean生成tasknode
		list.forEach(new Consumer<TaskBean>() {
			@Override
			public void accept(TaskBean bean) {
				TaskNodeW2 node=getTaskFactory().getNodeByBean(bean);
				node.setProgress(1);
				//将tasknode加入任务池中
				String colId=simBoard.getSimBoardConf().getFirstColId();
				SimCol col=(SimCol)simBoard.lookup("#"+colId);
				col.addTaskNode(node);
				AAL.a("任务池中加入任务："+node.getTaskBean());
			}
		});		
	}
	/**
	 ** 周期性 拉取
	 * @param curTime
	 */
	public void periodPull(int curTime) {
		//获得实际队列,倒叙拉取
		for(int i=simCols.size()-1;i>=0;i--) {
			SimCol simCol=simCols.get(i);
			simCol.pull(curTime);
		}
	}
	public void addSimCol(SimCol col) {
		simCols.add(col);
	}
	/**
	 ** 移动 任务项
	 * @param cList
	 * @param simCol
	 */
	public void moveTaskNode(List<TaskNodeW2> cList, SimCol simCol) {
		cList.forEach(new Consumer<TaskNodeW2>() {
			@Override
			public void accept(TaskNodeW2 taskNode) {
				String colId=taskNode.getTaskBean().getCurColId();
				SimCol preCol=(SimCol)simBoard.lookup("#"+colId);
				preCol.removeTaskNode(taskNode);
				simCol.addTaskNode(taskNode);
			}
		});
		
	}
	
	
}
