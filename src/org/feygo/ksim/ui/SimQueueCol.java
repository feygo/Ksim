package org.feygo.ksim.ui;

import org.feygo.ksim.conf.SimColConf;

/**
 * @author Feygo
 ** 队列行 看板列
 ** 特点：进入的任务全部设置为完成，没有工作动作，对前列全部拉取（batchpull为0），不做拆分检查，无在制品限制
 */
public class SimQueueCol extends SimCol {

	public SimQueueCol(SimColConf conf) {
		super(conf);
		conf.setWip(0);
		conf.setBatchPull(0);		
	}

	@Override
	public void addTaskNode(TaskNodeW2 node) {
		node.setProgress(1);
		nodeList.add(node);
		workDoneList.add(node);
		node.intoCol(getId());
	}

	@Override
	public void work() {

	}

	
}
