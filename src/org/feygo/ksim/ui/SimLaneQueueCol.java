package org.feygo.ksim.ui;

import org.feygo.ksim.conf.SimColConf;
import org.feygo.ksim.task.ui.TaskNodeW2;

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
	protected boolean isWorkerCol() {
		return false;
	}
	
	@Override
	protected boolean sendMessagePull() {
		if(!getNodeList().isEmpty()) {
			return true;
		}
		return false;
	}
}
