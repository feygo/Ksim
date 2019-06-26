package org.feygo.ksim.ui;

import org.feygo.ksim.conf.SimBoardConf;

import javafx.scene.layout.VBox;

public class SimBoard extends VBox {

	private SimBoardConf conf;
	
	public SimBoard(SimBoardConf conf){
		this.conf=conf;
		// 生成标题栏
		initTitle();
		// 生成看板栏
		InitKanBanCols();
	}

	private void InitKanBanCols() {
		//获得看板
	}

	private void initTitle() {
		
	}
}
