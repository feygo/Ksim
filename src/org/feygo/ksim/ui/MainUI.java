package org.feygo.ksim.ui;

import java.net.URL;
import java.util.List;

import org.feygo.ksim.Launcher;
import org.feygo.ksim.conf.ConfLoader;
import org.feygo.ksim.conf.SimBoardConf;
import org.feygo.ksim.data.ui.TaskFlowTable;
import org.feygo.ksim.sim.Scheduler;
import org.feygo.ksim.sim.Simulator;
import org.feygo.ksim.task.TaskBean;
import org.feygo.ksim.task.TaskFactory;
import org.feygo.ksim.tools.AAL;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Feygo
 * ������
 */
public class MainUI extends BorderPane {
	
	private Scheduler scheduler;
	private int basePeriod=1000;

	public MainUI(){
		setId("MainUI");
		//Menu�б�
		showMenu();
		scheduler=new Scheduler();
		scheduler.setPeriod(Duration.millis(basePeriod));
		
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
				simBoard.initSimBoard();
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
		
		/** �ٶȵ��� */
		Slider schedulerSlider=new Slider(0,8,2);
		schedulerSlider.setValue(1);
		schedulerSlider.setShowTickMarks(true);
		schedulerSlider.setShowTickLabels(true);
		schedulerSlider.setMajorTickUnit(2);
		schedulerSlider.setBlockIncrement(2);
		hBox.getChildren().add(schedulerSlider);
		schedulerSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				//�ж�newValue�Ƿ�����
				int speed=0;
				if(newValue.doubleValue()%1==0.0&&newValue.doubleValue()!=0.0) {
					return;
				}else if(newValue.doubleValue()<1) {
					speed=1;					
				}else {					
					speed=Double.valueOf(Math.rint(newValue.doubleValue()/2)*2).intValue();
				}
				schedulerSlider.setValue(speed);				
				scheduler.setPeriod(Duration.millis(basePeriod/speed));
				AAL.a("Scheduler�������ڱ䶯������Ϊ"+speed+"����"+scheduler.getPeriod());
			}
			
		});
		
		/** ���ݼ�ش��� **/
		Button monitorDataButton=new Button();
		monitorDataButton.setText("���ݼ���");
		monitorDataButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TaskFlowTable tfTable=new TaskFlowTable();
				
				Scene scene=new Scene(tfTable);
				Stage monitorStage=new Stage();
				monitorStage.setScene(scene);
				//monitorStage.initOwner(Launcher.getPrimaryStage());
				monitorStage.setTitle("���ݼ�ش���");
				monitorStage.initModality(Modality.WINDOW_MODAL);
				monitorStage.show();
			}
		});
		hBox.getChildren().add(monitorDataButton);		
		
		
		hBox.prefWidthProperty().bind(this.prefWidthProperty());
		this.setTop(hBox);
		
		
	}
}
