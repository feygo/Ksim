package org.feygo.ksim.ui;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.feygo.ksim.conf.SimColConf;
import org.feygo.ksim.sim.Simulator;
import org.feygo.ksim.task.ui.TaskNodeW2;
import org.feygo.ksim.tools.AAL;

/**
 * @author Feygo
 * *������ ������
 * *�ص㣺���������ȫ������Ϊδ��ɣ�û�й�����������ǰ��ȫ����ȡ��batchpullΪ0����������ּ��
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
