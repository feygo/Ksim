package org.feygo.ksim.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import javax.management.RuntimeErrorException;

import org.feygo.ksim.conf.ConfLoader;
import org.feygo.ksim.conf.SimBoardConf;
import org.feygo.ksim.task.ui.TaskNodeW2;
import org.feygo.ksim.tools.AAL;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TaskFactory {
	
	public static void main(String[] args) {
		TaskFactory taskFactory=new TaskFactory();
//		List<TaskBean> conf=taskFactory.loadFromFile("C:\\1.dev\\git-repository\\Ksim\\src\\conf\\TaskBean_Demo.json");
//		AAL.a(conf.toString());
		
//		AAL.a(taskFactory.fibonacci(3)+"");
//		AAL.a(taskFactory.unFibonacci(8)+"");
//		//AAL.a(taskFactory.unFibonacci(9)+"");
//		for(int i=0;i<12;i++) {
//			AAL.a(i+"-"+taskFactory.fibonacci(i));
//		}
		
		AAL.a(taskFactory.disaggWork(13, 8)+"");
		
		TaskBean tBean=new TaskBean();
		tBean.setId("tt");
		tBean.setEst(21);
		AAL.a(taskFactory.disaggWorkBean(tBean, 8)+"");
		
	}

	public List<TaskBean> loadFromFile(String filePath) {
		Gson gson=new Gson();
		SimBoardConf conf=null;
		List<TaskBean> rList=null;
		try {
			String encoding = "UTF-8";
			File file=new File(filePath);			
			FileReader fileReader =new FileReader(file, Charset.forName(encoding));
			Type type = new TypeToken<ArrayList<TaskBean>>() {}.getType();
			rList=gson.fromJson(fileReader, type);		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rList;
	}
	public List<TaskBean> loadFromFile(URL filePath) {
		Gson gson=new Gson();
		SimBoardConf conf=null;
		List<TaskBean> rList=null;
		try {
			String encoding = "UTF-8";
			URI fileUri=filePath.toURI();
			File file=new File(fileUri);			
			FileReader fileReader =new FileReader(file, Charset.forName(encoding));
			Type type = new TypeToken<ArrayList<TaskBean>>() {}.getType();
			rList=gson.fromJson(fileReader, type);		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rList;
	}

	public TaskNodeW2 getNodeByBean(TaskBean bean) {
		TaskNodeW2 node=new TaskNodeW2(bean);
		node.initTaskNode();
		return node;
	}

	public List<TaskBean> disaggWorkBean(TaskBean taskBean, int estMax) {
		// 先拆解工作量 
		int est=taskBean.getEst();
		List<Integer> estList=disaggWork(est,estMax);
		// 然后clone原有taskbean，设置新的bean属性
		List<TaskBean> rList=new ArrayList<TaskBean>();

		for(int i=0;i<estList.size();i++) {
			TaskBean cBean=cloneBean(taskBean);
			int index=i+1;
			cBean.setpId(taskBean.getId());
			cBean.setId(taskBean.getId()+"-"+index);
			cBean.setEst(estList.get(i));
			cBean.setDisaggLevel(taskBean.getDisaggLevel()+1);
			cBean.setDisaggCnt(estList.size());
			rList.add(cBean);
		}
		return rList;
	}

	protected TaskBean cloneBean(TaskBean taskBean) {
		TaskBean newBean=new TaskBean();
		newBean.setId(taskBean.getId());
		newBean.setEst(taskBean.getEst());
		newBean.setServiceType(taskBean.getServiceType());
		newBean.setTaskType(taskBean.getTaskType());
		newBean.setMergeCol(taskBean.getMergeCol());
		
		newBean.setCurColId(taskBean.getCurColId());
		newBean.setTmpDoneTime(taskBean.getTmpDoneTime());
		newBean.setTmpEndTime(taskBean.getTmpEndTime());
		newBean.setTmpStartTime(taskBean.getTmpStartTime());
		newBean.setTmpWorkTime(taskBean.getTmpWorkTime());
		
		newBean.setDisaggCnt(taskBean.getDisaggCnt());
		newBean.setDisaggCol(taskBean.getDisaggCol());
		newBean.setDisaggLevel(taskBean.getDisaggLevel());
		newBean.setpId(taskBean.getpId());
		
		return newBean;
	}

	private List<Integer> disaggWork(int est, int estMax) {
		// TODO Auto-generated method stub
		int max=est*2;
		int min=Double.valueOf(est*0.8).intValue();
		int maxLv=unFibonacci(estMax);
		
		List<Integer> rList=new ArrayList<Integer>();
		int sum=0;
		while(true) {
			int rLv=Double.valueOf(Math.random()*maxLv).intValue();
			if(rLv!=0) {
				int disNum=fibonacci(rLv);
				rList.add(disNum);
				sum=sum+disNum;
				AAL.a("dis:"+rLv+"-"+disNum);
				if(sum<max&&sum>min) {
					break;
				}
			}
		}
		return rList;
	}
	
	private int fibonacci(int lv) {
		if ((lv == 0) || (lv == 1)) {
            return lv;
		}else {
            return fibonacci(lv - 1) + fibonacci(lv - 2);
        }
	}
	private int unFibonacci(int number) {
		int lv=0;
		int tmpLv=0;
		int num=0;
		while(num<number) {
			num=fibonacci(tmpLv);
			if(num==number) {
				lv=tmpLv;
				break;
			}else {
				tmpLv++;
			}			
		}
		if(number!=0&&lv==0) {
			throw new RuntimeException(number+"并不是斐波那契数列中的数值，请检查配置");
		}
		return lv;
	}

	public TaskBean mergeWorkBean(List<TaskBean> sList) {
		TaskBean mBean=new TaskBean();
		TaskBean sBean=sList.get(0);
		mBean.setId(sBean.getpId());
		mBean.setTaskType(sBean.getTaskType());
		mBean.setServiceType(sBean.getServiceType());
		mBean.setMergeCol(sBean.getMergeCol());
		
		mBean.setCurColId(sBean.getCurColId());
		int est=0;
		int tmpStartTime=sBean.getTmpStartTime();
		int tmpEndTime=sBean.getTmpEndTime();
		for(int i=0;i<sList.size();i++) {
			TaskBean bean=sList.get(i);
			est=est+bean.getEst();
			if(tmpStartTime>bean.getTmpStartTime()) {
				tmpStartTime=bean.getTmpStartTime();
			}
			if(tmpEndTime<bean.getTmpEndTime()) {
				tmpEndTime=bean.getTmpEndTime();
			}
		}
		mBean.setEst(est);
		mBean.setTmpEndTime(tmpEndTime);
		mBean.setTmpStartTime(tmpStartTime);

		return mBean;
	}

	public List<TaskBean> getBeanFromNode(List<TaskNodeW2> sList) {
		List<TaskBean> beanList=new ArrayList<TaskBean>();
		sList.forEach(new Consumer<TaskNodeW2>() {
			@Override
			public void accept(TaskNodeW2 node) {
				beanList.add(node.getTaskBean());
			}
		});
		return beanList;
	}
	
	public void writeRecordToFile(List list) {
		Gson gson=new Gson();
		String s=gson.toJson(list);
		System.out.println(s);
	}
}
