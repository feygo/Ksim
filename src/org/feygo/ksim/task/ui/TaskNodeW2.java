package org.feygo.ksim.task.ui;

import org.feygo.ksim.sim.Simulator;
import org.feygo.ksim.task.TaskBean;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;

/**
 * @author Feygo
 * ֻ�н������͹�����
 */
public class TaskNodeW2 extends BorderPane {

	private TaskBean taskBean;
	
	protected ProgressBar pBar;
	
	public TaskBean getTaskBean() {
		return taskBean;
	}
	@Override
	public String toString() {		
		return taskBean.toString();
	}
	public double getProgress() {
		return pBar.getProgress();
	}

	public void setProgress(double progress) {
		pBar.setProgress(progress);
	}

	public TaskNodeW2(TaskBean taskBean) {
		this.taskBean=taskBean;
		this.setId(taskBean.getId());		
	}
	

	public void initTaskNode() {
		Label idLabel=new Label(taskBean.getId());
		this.setTop(idLabel);
		Label estLabel=new Label(taskBean.getEst()+"");
		this.setCenter(estLabel);
		pBar=new ProgressBar();
		pBar.prefWidthProperty().bind(this.widthProperty());
		this.setBottom(pBar);		
		this.getStyleClass().add("TaskNodeCSS");
	}
	public void intoCol(String colId) {
		taskBean.setCurColId(colId);
		taskBean.setTmpStartTime(Simulator.getSim().getCurrentTime());		
	}
	public void outofCol() {
		taskBean.setTmpEndTime(Simulator.getSim().getCurrentTime());
		//taskBean���¼�¼��Ϣ
		taskBean.record();
	}
	public void workDone() {
		taskBean.setTmpDoneTime(Simulator.getSim().getCurrentTime());
	}
	public void workStart() {
		taskBean.setTmpWorkTime(Simulator.getSim().getCurrentTime());
		pBar.setProgress(0.0);
	}
	public double increment(double inc) {
		// ��ǰ����
		double prog=pBar.getProgress();
		// �ܹ�����
		int est=taskBean.getEst();
		// ���������Ľ���
		double incprog=inc/est;
		double nextProg;
		if((prog+incprog)>1) {
			nextProg=1;
		}else {
			nextProg=prog+incprog;
		}
		pBar.setProgress(nextProg);
		return nextProg;
	}


	
}
