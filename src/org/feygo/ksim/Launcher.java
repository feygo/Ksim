package org.feygo.ksim;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import org.feygo.ksim.tools.AAL;
import org.feygo.ksim.ui.MainUI;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Launcher extends Application {
	
	private static Stage primaryStage;
	
	public static Set<Stage> stages=new HashSet<Stage>();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		
		primaryStage=stage;
		
		MainUI ui=new MainUI();
		ui.setId("MainUI");
		ui.setPrefWidth(800);
		ui.setPrefHeight(600);
		
		Scene scene=new Scene(ui);
		/** 加载css */
		URL cssUrl=this.getClass().getClassLoader().getResource("css/ui.css");
		scene.getStylesheets().add(cssUrl.toExternalForm());
		
		stage.setScene(scene);
		stage.setTitle("Ksim看板仿真软件 v0.5");
		stage.setHeight(600);
		stage.setWidth(800);
		stage.show();
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				stages.forEach(new Consumer<Stage>() {
					@Override
					public void accept(Stage t) {
						if(t!=null) {
							AAL.a(t.getTitle()+" closed");
							t.close();
						}
					}
				});
				AAL.a("PrimaryStage closed");
			}
		});
	}
	public static Stage getPrimaryStage() {
		return primaryStage;
	}


}
