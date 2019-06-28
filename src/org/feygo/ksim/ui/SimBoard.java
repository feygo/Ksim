package org.feygo.ksim.ui;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.feygo.ksim.conf.SimBoardConf;
import org.feygo.ksim.conf.SimColConf;
import org.feygo.ksim.sim.Simulator;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class SimBoard extends VBox {

	private SimBoardConf conf;
	
	public SimBoardConf getSimBoardConf() {
		return conf;
	}

	public SimBoard(SimBoardConf conf){
		this.conf=conf;
		// 生成标题栏
		initTitle();
		// 生成看板栏
		InitKanBanCols();
	}

	private void InitKanBanCols() {
		GridPane gp=new GridPane();
		// 获得看板的实际看板列，以确定看板列的宽度
		int colCnt=conf.getLayoutMatrix()[1];
		int tierCnt=conf.getLayoutMatrix()[0];
		// 获得看板的层级 以决定看板生成方式
		List<String[]> tireList=conf.getLayoutTier();
		tireList.forEach(new Consumer<String[]>() {
			@Override
			public void accept(String[] layout) {
				//添加col头
				String colId=layout[0];
				SimColConf colConf=conf.getCols().get(colId);
				SimTitle tLabel=new SimTitle(colConf);
				gp.add(tLabel, Integer.valueOf(layout[1]).intValue(), Integer.valueOf(layout[2]).intValue(),Integer.valueOf(layout[3]).intValue(),Integer.valueOf(layout[4]).intValue());
				
				if(layout[5].equals("1")) {
					//虚拟列
					SimCol col=new SimCol(colConf);
					col.prefWidthProperty().bind(gp.widthProperty().divide(colCnt));
					gp.add(col, Integer.valueOf(layout[1]).intValue(), tierCnt+1,1,1);
					Simulator.getSim().addSimCol(col);
				}				
			}
		});
		
		gp.prefWidthProperty().bind(this.widthProperty());
		gp.prefHeightProperty().bind(this.heightProperty().multiply(0.95));
		this.getChildren().add(gp);
	}

	private void initTitle() {
		
	}
}
