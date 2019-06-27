package org.feygo.ksim.ui;

import org.feygo.ksim.conf.SimColConf;
import org.feygo.ksim.conf.SimTitleConf;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextAlignment;

public class SimTitle extends BorderPane {

	private SimColConf conf;
	
	public SimTitle(SimColConf conf){
		this.conf=conf;
		initTitle();
		//this.getStylesheets().add("SimTitle");
		this.getStyleClass().add("SimTitleCSS");
	}

	private void initTitle() {
		Label tLabel=new Label();
		tLabel.setText(conf.getTitle());
		tLabel.setTextAlignment(TextAlignment.CENTER);
		this.setTop(tLabel);
				
		if(conf.getWip()!=0) {
			Label wipLabel=new Label();
			wipLabel.setText("WIP:"+conf.getWip());
			tLabel.setTextAlignment(TextAlignment.CENTER);
			this.setCenter(wipLabel);
		}
	}
	
}
