package org.feygo.ksim.ui;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.feygo.ksim.conf.SimColConf;
import org.feygo.ksim.sim.Simulator;
import org.feygo.ksim.task.ui.TaskNodeW2;
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
		conf.setDisaggMax(0);
	}

	@Override
	public void addTaskNode(TaskNodeW2 node) {
		node.setProgress(-1);
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
