package org.feygo.ksim.task;

import java.util.HashMap;
import java.util.Map;

public class TaskConstant {

	public static String TaskType_Feature="功能";
	public static String TaskTypeSimple_Feature="F";
	
	public static String TaskType_Bug="缺陷";
	public static String TaskTypeSimple_Bug="B";
	
	public static String TaskType_Technical="技术任务";
	public static String TaskTypeSimple_Technical="T";
	
	public static String TaskType_Maintenance="维护任务";
	public static String TaskTypeSimple_Maintenance="M";
	
	public static String getServiceTypeShort(String type) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("Urgency", "急");
		map.put("ordinary", "普");
		map.put("Focus", "缓");
		return map.get(type);		
	}

}
