package org.feygo.ksim;

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
		ui.setPrefWidth(800);
		ui.setPrefHeight(600);
		
		Scene scene=new Scene(ui);
		
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Ksim¿´°å·ÂÕæÈí¼þ v0.5");
		primaryStage.setHeight(600);
		primaryStage.setWidth(800);
		primaryStage.show();
	}


}
