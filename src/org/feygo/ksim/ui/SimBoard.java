package org.feygo.ksim.ui;

import org.feygo.ksim.conf.SimBoardConf;

import javafx.scene.layout.VBox;

public class SimBoard extends VBox {

	private SimBoardConf conf;
	
	public SimBoard(SimBoardConf conf){
		this.conf=conf;
		// ���ɱ�����
		initTitle();
		// ���ɿ�����
		InitKanBanCols();
	}

	private void InitKanBanCols() {
		//��ÿ���
	}

	private void initTitle() {
		
	}
}
