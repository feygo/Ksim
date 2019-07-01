package org.feygo.ksim.ui;

import org.feygo.ksim.task.TaskBean;
import org.feygo.ksim.task.TaskConstant;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class TaskNodeW4 extends TaskNodeW2 {

	public TaskNodeW4(TaskBean taskBean) {
		super(taskBean);		
	}

	@Override
	public void initTaskNode() {
		Label idLabel=new Label();
		String labalString=getTaskBean().getId()+"#"+getTaskBean().getEst();
		idLabel.setText(labalString);
		this.setTop(idLabel);
		
		pBar=new ProgressBar();
		pBar.prefWidthProperty().bind(this.widthProperty());
		this.setBottom(pBar);		
		
		Label taskTypeLabel=new Label(getTaskBean().getTaskType());
		taskTypeLabel.setText(getTaskBean().getTaskType());
		this.setLeft(taskTypeLabel);
		
		Label serviceTypeLabel = new Label();
		serviceTypeLabel.setText(TaskConstant.getServiceTypeShort(getTaskBean().getServiceType()));
		this.setCenter(serviceTypeLabel);
		
		this.getStyleClass().add("TaskNodeCSS");
	}
	
}
