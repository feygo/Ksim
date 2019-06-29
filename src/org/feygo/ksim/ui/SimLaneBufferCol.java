package org.feygo.ksim.ui;

import org.feygo.ksim.conf.SimColConf;

public class SimLaneBufferCol extends SimLaneCol {

	public SimLaneBufferCol(SimColConf conf) {
		super(conf);
		conf.setBatchPull(0);
		conf.setDisaggMax(0);
	}
	@Override
	public void addTaskNode(TaskNodeW2 node) {
		node.setProgress(-1);
		super.addTaskNode(node);
		super.addToWorkDoneList(node);
	}

	@Override
	public void work() {
		if(!getNodeList().isEmpty()) {
			super.messagePull();
		}	
	}
}
