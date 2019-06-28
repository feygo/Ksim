package org.feygo.ksim.ui;

import org.feygo.ksim.task.TaskBean;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;

public class TaskNodeW2 extends BorderPane {

	private TaskBean taskBean;
	
	private double progress;
	
	public TaskBean getTaskBean() {
		return taskBean;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public TaskNodeW2(TaskBean taskBean) {
		this.taskBean=taskBean;
		initTaskNode();
		this.getStyleClass().add("TaskNodeCSS");
	}

	private void initTaskNode() {
		Label idLabel=new Label(taskBean.getId());
		this.setTop(idLabel);
		Label estLabel=new Label(taskBean.getEst());
		this.setCenter(estLabel);
		ProgressBar pBar=new ProgressBar();
		pBar.prefWidthProperty().bind(this.widthProperty());
		this.setBottom(pBar);		
	}
	public void intoCol(String colId,int curTime) {
		taskBean.setCurColId(colId);
		taskBean.setTmpStartTime(curTime);
	}
	public void outofCol(int curTime) {
		taskBean.setTmpDoneTime(curTime);
		//taskBean更新记录信息
	}

	@Override
	public String toString() {		
		return taskBean.toString();
	}
	
}
