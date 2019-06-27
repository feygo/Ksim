package org.feygo.ksim;

import java.net.URL;

import org.feygo.ksim.tools.AAL;
import org.feygo.ksim.ui.MainUI;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		MainUI ui=new MainUI();
		ui.setId("MainUI");
		ui.setPrefWidth(800);
		ui.setPrefHeight(600);
		
		Scene scene=new Scene(ui);
		/** 加载css */
		URL cssUrl=this.getClass().getClassLoader().getResource("css/ui.css");
		scene.getStylesheets().add(cssUrl.toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Ksim看板仿真软件 v0.5");
		primaryStage.setHeight(600);
		primaryStage.setWidth(800);
		primaryStage.show();
	}


}
