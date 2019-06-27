package org.feygo.ksim.ui;

import java.net.URL;

import org.feygo.ksim.conf.ConfLoader;
import org.feygo.ksim.conf.SimBoardConf;
import org.feygo.ksim.tools.AAL;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * @author Feygo
 * ������
 */
public class MainUI extends BorderPane {

	public MainUI(){
		setId("MainUI");
		//Menu�б�
		showMenu();
		
	}
	public void showSimBoard(SimBoardConf conf) {
		//simboard
		SimBoard simboard=new SimBoard(conf);
		simboard.prefWidthProperty().bind(this.widthProperty());
		this.setCenter(simboard);
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
				showSimBoard(conf);
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
				
			}
		});
		hBox.getChildren().add(loadTaskButton);
		
		/**  ��������   */
		Button startSimButton=new Button();
		startSimButton.setText("��������");
		startSimButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
			}
		});
		hBox.getChildren().add(startSimButton);
		
		/**  ֹͣ����   */
		Button stopSimButton=new Button();
		stopSimButton.setText("ֹͣ���� ");
		stopSimButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
			}
		});
		hBox.getChildren().add(stopSimButton);
		
		
		hBox.prefWidthProperty().bind(this.prefWidthProperty());
		this.setTop(hBox);
	}
}
