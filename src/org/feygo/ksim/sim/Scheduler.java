package org.feygo.ksim.sim;

import org.feygo.ksim.tools.AAL;
import org.feygo.ksim.tools.TimeFormater;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class Scheduler extends ScheduledService<Number>{

	int sum=-1;
	@Override
	protected Task<Number> createTask() {
		Task<Number> task=new Task<Number>() {

			@Override
			protected Number call() throws Exception {
				sum=sum+1;
				return sum;
			}

			@Override
			protected void updateValue(Number value) {
				super.updateValue(value);
				AAL.a("=====ʱ�ӣ�"+TimeFormater.getXDXH8(value.intValue()));
				Simulator sim=Simulator.getSim();
				//����ʱ��
				sim.setCurrentTime(value.intValue());
				//��ȡ����
				sim.periodPull();
				//��������
				sim.periodWork();
				//�������Ƿ�ֹͣ
				if(!sim.periodCheckContinue()) {
					AAL.a("ǰ�о����������������ֹͣ��");
					Scheduler.this.cancel();
					AAL.a("��ʱ��״̬��"+Scheduler.this.getState());
					// ����ʱ��ֹͣʱ���������ݳ־û�
					sim.recordData();
				};
			}
			
		};
		return task;
	}

}
