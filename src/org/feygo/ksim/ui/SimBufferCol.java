package org.feygo.ksim.ui;

import org.feygo.ksim.conf.SimColConf;
import org.feygo.ksim.tools.AAL;

/**
 * @author Feygo
 * *缓冲区 看板列
 * *特点：进入的任务全部设置为未完成，没有工作动作，对前列全部拉取（batchpull为0），不做拆分检查
 */
public class SimBufferCol extends SimCol {

	public SimBufferCol(SimColConf conf) {
		super(conf);
		conf.setBatchPull(0);
	}

	@Override
	public void addTaskNode(TaskNodeW2 node) {
		node.setProgress(-1);
		nodeList.add(node);
		workDoneList.add(node);
		node.intoCol(getId());
	}

	@Override
	public void work() {
		
	}
	
	

}
