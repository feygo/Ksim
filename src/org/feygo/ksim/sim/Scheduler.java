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
				AAL.a("=====时钟："+TimeFormater.getXDXH8(value.intValue()));
				Simulator sim=Simulator.getSim();
				//设置时钟
				sim.setCurrentTime(value.intValue());
				//拉取调度
				sim.periodPull();
				//工作调度
				sim.periodWork();
				//检查仿真是否停止
				if(!sim.periodCheckContinue()) {
					AAL.a("前列均无任务项，仿真运行停止！");
					Scheduler.this.cancel();
					AAL.a("定时器状态："+Scheduler.this.getState());
					// 当定时器停止时，进行数据持久化
					sim.recordData();
				};
			}
			
		};
		return task;
	}

}
