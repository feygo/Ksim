package org.feygo.ksim.ui;

import java.net.URL;
import java.util.List;

import org.feygo.ksim.conf.ConfLoader;
import org.feygo.ksim.conf.SimBoardConf;
import org.feygo.ksim.sim.Scheduler;
import org.feygo.ksim.sim.Simulator;
import org.feygo.ksim.task.TaskBean;
import org.feygo.ksim.task.TaskFactory;
import org.feygo.ksim.tools.AAL;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * @author Feygo
 * ������
 */
public class MainUI extends BorderPane {
	
	private Scheduler scheduler;

	public MainUI(){
		setId("MainUI");
		//Menu�б�
		showMenu();
		scheduler=new Scheduler();
		scheduler.setPeriod(Duration.seconds(1));
		
	}
	public SimBoard showSimBoard(SimBoardConf conf) {
		//simboard
		SimBoard simboard=new SimBoard(conf);
		simboard.prefWidthProperty().bind(this.widthProperty());
		this.setCenter(simboard);
		return simboard;
	}
	
	public void showMenu() {
		HBox hBox=new HBox();
		/**  ��������   */
		Button loadConfButton=new Button();
		loadConfButton.setText("���뿴������");
		loadConfButton.setPrefWidth(100);
		loadConfButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//String filePath="C:\\1.dev\\git-repository\\Ksim\\conf\\SimboardConf.json";
				URL confUrl=this.getClass().getClassLoader().getResource("conf/SimboardConf.json");
				AAL.a("����Simboard�����ļ���"+confUrl.toExternalForm());
				SimBoardConf conf=ConfLoader.getSimConfByFile(confUrl);
				AAL.a(conf.toString());
				SimBoard simBoard=showSimBoard(conf);
				Simulator.getSim().setSimBoard(simBoard);
			}
		});
		hBox.getChildren().add(loadConfButton);
		
		/**  ���������   */
		Button loadTaskButton=new Button();
		loadTaskButton.setText("���������");
		loadTaskButton.setPrefWidth(100);
		loadTaskButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				URL confUrl=this.getClass().getClassLoader().getResource("conf/TaskBean_Demo.json");
				AAL.a("���������б��ļ���"+confUrl.toExternalForm());
				TaskFactory taskFactory=new TaskFactory();
				List<TaskBean> list=taskFactory.loadFromFile(confUrl);
				Simulator.getSim().setTaskFactory(taskFactory);
				Simulator.getSim().initTaskPool(list);
			}
		});
		hBox.getChildren().add(loadTaskButton);
		
		/**  ��������   */
		Button startSimButton=new Button();
		startSimButton.setText("��������");
		startSimButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				scheduler.start();
				AAL.a("��ʱ��״̬��"+scheduler.getState());
			}
		});
		hBox.getChildren().add(startSimButton);
		
		/**  ֹͣ����   */
		Button stopSimButton=new Button();
		stopSimButton.setText("ֹͣ���� ");
		stopSimButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				scheduler.cancel();
				AAL.a("��ʱ��״̬��"+scheduler.getState());
			}
		});
		hBox.getChildren().add(stopSimButton);
		
		
		hBox.prefWidthProperty().bind(this.prefWidthProperty());
		this.setTop(hBox);
	}
}
