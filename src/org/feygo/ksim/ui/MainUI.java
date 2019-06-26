package org.feygo.ksim.ui;

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
 * 主界面
 */
public class MainUI extends BorderPane {

	public MainUI(){
		setId("MainUI");
		//Menu列表
		showMenu();
		
	}
	public void showSimBoard(SimBoardConf conf) {
		//simboard
		SimBoard simboard=new SimBoard(conf);
		this.setCenter(simboard);
	}
	
	public void showMenu() {
		HBox hBox=new HBox();
		/**  载入配置   */
		Button loadConfButton=new Button();
		loadConfButton.setText("载入看板配置");
		loadConfButton.setPrefWidth(100);
		loadConfButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String filePath="C:\\1.dev\\workspace\\Simboard\\conf\\SimboardConf.json";
				SimBoardConf conf=ConfLoader.getSimConfByFile(filePath);
				AAL.a(conf.toString());
				showSimBoard(conf);
			}
		});
		hBox.getChildren().add(loadConfButton);
		
		/**  生成任务池   */
		Button loadTaskButton=new Button();
		loadTaskButton.setText("生成任务池");
		loadTaskButton.setPrefWidth(100);
		loadTaskButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
			}
		});
		hBox.getChildren().add(loadTaskButton);
		
		/**  启动仿真   */
		Button startSimButton=new Button();
		startSimButton.setText("启动仿真");
		startSimButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
			}
		});
		hBox.getChildren().add(startSimButton);
		
		/**  停止仿真   */
		Button stopSimButton=new Button();
		stopSimButton.setText("停止仿真 ");
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
