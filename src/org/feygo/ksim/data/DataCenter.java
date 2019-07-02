package org.feygo.ksim.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.feygo.ksim.task.TaskRecord;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class DataCenter {

	private static DataCenter center;
	
	public static DataCenter getDataCenter() {
		if(center==null) {
			center=new DataCenter();
		}
		return center;
	}
	
	private ObservableList<Map<String, TaskRecord>> taskRecordOL=null;
	private Map<String,Map<String, TaskRecord>> taskRecordMap=new HashMap<String, Map<String,TaskRecord>>();
	
	public ObservableList<Map<String, TaskRecord>> getTaskRecordObservableList(){
		if(taskRecordOL==null) {
			taskRecordOL=FXCollections.observableArrayList();
		}
		return taskRecordOL;
	}
	public void addTaskRecord(TaskRecord record) {
		String taskId=record.getTaskId();
		if(!taskRecordMap.containsKey(taskId)) {
			Map<String, TaskRecord> colRecord=new HashMap<String, TaskRecord>();
			taskRecordMap.put(taskId,colRecord );
			getTaskRecordObservableList().add(colRecord);
		}
		Map<String, TaskRecord> cRecord=taskRecordMap.get(taskId);
		String colId=record.getColId();
		cRecord.put(colId, record);
	}
}
