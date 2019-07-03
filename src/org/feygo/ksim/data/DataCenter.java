package org.feygo.ksim.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.feygo.ksim.data.ui.TaskFlowTable;
import org.feygo.ksim.task.TaskRecord;
import org.feygo.ksim.tools.AAL;

import javafx.beans.Observable;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.util.Callback;

public class DataCenter {

	private static DataCenter center;
	
	private TaskFlowTable tfTable;
	
	public TaskFlowTable getTaskFlowTable() {
		return tfTable;
	}
	public void setTaskFlowTable(TaskFlowTable tfTable) {
		this.tfTable = tfTable;
	}
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
		//AAL.a(taskRecordOL.toString());
		if(tfTable!=null) {
			tfTable.fresh();
		}		
	}
}
