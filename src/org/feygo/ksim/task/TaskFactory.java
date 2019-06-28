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

import org.feygo.ksim.conf.ConfLoader;
import org.feygo.ksim.conf.SimBoardConf;
import org.feygo.ksim.tools.AAL;
import org.feygo.ksim.ui.TaskNodeW2;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TaskFactory {
	
	public static void main(String[] args) {
		TaskFactory taskFactory=new TaskFactory();
		List<TaskBean> conf=taskFactory.loadFromFile("C:\\1.dev\\git-repository\\Ksim\\src\\conf\\TaskBean_Demo.json");
		AAL.a(conf.toString());
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
		return new TaskNodeW2(bean);
	}
}
