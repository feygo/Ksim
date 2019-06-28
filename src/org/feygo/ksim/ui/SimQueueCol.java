package org.feygo.ksim.ui;

import org.feygo.ksim.conf.SimColConf;

/**
 * @author Feygo
 ** ������ ������
 ** �ص㣺���������ȫ������Ϊ��ɣ�û�й�����������ǰ��ȫ����ȡ��batchpullΪ0����������ּ�飬������Ʒ����
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
