package org.feygo.ksim.ui;

import java.util.Map;
import java.util.function.BiConsumer;

import org.feygo.ksim.conf.SimBoardConf;
import org.feygo.ksim.conf.SimColConf;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
		GridPane gp=new GridPane();
		// 获得看板的实际看板列，以确定看板列的宽度
		int colCnt=conf.getLayoutMatrix()[1];
		int tierCnt=conf.getLayoutMatrix()[0];
		// 获得看板的层级 以决定看板生成方式
		Map<String, int[]> layoutMap=conf.getLayoutTier();
		layoutMap.forEach(new BiConsumer<String, int[]>() {
			@Override
			public void accept(String colId, int[] layout) {
				//添加col头
				SimColConf colConf=conf.getCols().get(colId);
				SimTitle tLabel=new SimTitle(colConf);
				gp.add(tLabel, layout[0], layout[1],layout[2],layout[3]);
				if(layout[4]==1) {
					//虚拟列
					SimCol col=new SimCol(colConf);
					col.prefWidthProperty().bind(gp.widthProperty().divide(colCnt));
					gp.add(col, layout[0], tierCnt+1,1,1);
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
