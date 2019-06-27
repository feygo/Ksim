package org.feygo.ksim.ui;

import org.feygo.ksim.conf.SimColConf;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class SimCol extends ScrollPane {

	private SimColConf conf;
	
	public SimCol(SimColConf conf) {
		this.conf=conf;
		initCol();
	}

	private void initCol() {
		VBox vBox=new VBox();
		vBox.setId(conf.getId());
		this.setContent(vBox);
	}
}
