package org.feygo.ksim.ui;

import org.feygo.ksim.conf.SimColConf;
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
