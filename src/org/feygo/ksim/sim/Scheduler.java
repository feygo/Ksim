package org.feygo.ksim.sim;

import org.feygo.ksim.tools.AAL;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class Scheduler extends ScheduledService<Number>{

	int sum=0;
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
				AAL.a("=====ʱ�ӣ�"+value.intValue());
				Simulator sim=Simulator.getSim();
				//����ʱ��
				sim.setCurrentTime(value.intValue());
				//��ȡ����
				sim.periodPull(value.intValue());
				//��������
				//Simulator.getSim().periodWork(value.intValue());
			}
			
		};
		return task;
	}

}
