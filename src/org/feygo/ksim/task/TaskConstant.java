package org.feygo.ksim.task;

import java.util.HashMap;
import java.util.Map;

public class TaskConstant {

	public static String TaskType_Feature="����";
	public static String TaskTypeSimple_Feature="F";
	
	public static String TaskType_Bug="ȱ��";
	public static String TaskTypeSimple_Bug="B";
	
	public static String TaskType_Technical="��������";
	public static String TaskTypeSimple_Technical="T";
	
	public static String TaskType_Maintenance="ά������";
	public static String TaskTypeSimple_Maintenance="M";
	
	public static String getServiceTypeShort(String type) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("Urgency", "��");
		map.put("ordinary", "��");
		map.put("Focus", "��");
		return map.get(type);		
	}

}
