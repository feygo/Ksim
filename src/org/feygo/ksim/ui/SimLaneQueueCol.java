package org.feygo.ksim.ui;

import org.feygo.ksim.conf.SimColConf;

public class SimLaneQueueCol extends SimLaneCol {

	public SimLaneQueueCol(SimColConf conf) {
		super(conf);
		conf.setWip(0);
		conf.setBatchPull(0);
		conf.setDisaggMax(0);
	}
	@Override
	public void addTaskNode(TaskNodeW2 node) {
		node.setProgress(1);
		super.addTaskNode(node);
	}

	@Override
	public void work() {
		if(!getNodeList().isEmpty()) {
			super.messagePull();
		}	
	}
}