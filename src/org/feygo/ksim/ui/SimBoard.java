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
		// ���ɱ�����
		initTitle();
		// ���ɿ�����
		InitKanBanCols();
	}

	private void InitKanBanCols() {
		GridPane gp=new GridPane();
		// ��ÿ����ʵ�ʿ����У���ȷ�������еĿ��
		int colCnt=conf.getLayoutMatrix()[1];
		int tierCnt=conf.getLayoutMatrix()[0];
		// ��ÿ���Ĳ㼶 �Ծ����������ɷ�ʽ
		Map<String, int[]> layoutMap=conf.getLayoutTier();
		layoutMap.forEach(new BiConsumer<String, int[]>() {
			@Override
			public void accept(String colId, int[] layout) {
				//���colͷ
				SimColConf colConf=conf.getCols().get(colId);
				SimTitle tLabel=new SimTitle(colConf);
				gp.add(tLabel, layout[0], layout[1],layout[2],layout[3]);
				if(layout[4]==1) {
					//������
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
