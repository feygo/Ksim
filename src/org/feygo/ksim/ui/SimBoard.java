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
	}
	public void initSimBoard() {
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
		List<String[]> tireList=conf.getLayoutTier();
		tireList.forEach(new Consumer<String[]>() {
			@Override
			public void accept(String[] layout) {
				//���colͷ
				String colId=layout[0];
				SimColConf colConf=conf.getCols().get(colId);
				SimTitle tLabel=new SimTitle(colConf);
				gp.add(tLabel, Integer.valueOf(layout[1]).intValue(), Integer.valueOf(layout[2]).intValue(),Integer.valueOf(layout[3]).intValue(),Integer.valueOf(layout[4]).intValue());
				
				if(layout[5].equals("1")) {
					//ռλ������
					SimCol col=getSimColByConf(colConf);
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
	public SimCol getSimColByConf(SimColConf conf) {
		String colType=conf.getColType();
		SimCol col=null;
		if("Buffer".equalsIgnoreCase(colType)) {
			col=new SimBufferCol(conf);
		}else if("Queue".equalsIgnoreCase(colType)){
			col=new SimQueueCol(conf);
		}else if("Lane".equalsIgnoreCase(colType)) {
			col=new SimLaneCol(conf);
		}else if("LaneBuffer".equalsIgnoreCase(colType)){
			col=new SimLaneBufferCol(conf);
		}else if("LaneQueue".equalsIgnoreCase(colType)) {
			col=new SimLaneQueueCol(conf);
		}else {
			col=new SimCol(conf);
		}
		return col;
	}
}
