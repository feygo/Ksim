package org.feygo.ksim.data.ui;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

import org.feygo.ksim.conf.SimColConf;
import org.feygo.ksim.data.DataCenter;
import org.feygo.ksim.sim.Simulator;
import org.feygo.ksim.task.TaskRecord;
import org.feygo.ksim.tools.AAL;
import org.feygo.ksim.ui.SimCol;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

public class TaskFlowTable extends BorderPane {
	
	private TableView<Map<String,TaskRecord>> tv;

	public TaskFlowTable() {
		initTaskFlowTable();
	}
	private void initTaskFlowTable() {
		Label titleLabel=new Label();
		titleLabel.setText("����Ч��");
		this.setTop(titleLabel);
		
		ObservableList<Map<String, TaskRecord>> dataSource=DataCenter.getDataCenter().getTaskRecordObservableList();
				
		tv=new TableView<Map<String,TaskRecord>>();
		tv.setItems(dataSource);
		
		TableColumn<Map<String,TaskRecord>, String> tc_taskId=new TableColumn<Map<String,TaskRecord>, String>("������");
		tv.getColumns().add(tc_taskId);
		tc_taskId.setPrefWidth(150);
		tc_taskId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String,TaskRecord>,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Map<String, TaskRecord>, String> param) {
				if(param.getValue()!=null&&!param.getValue().isEmpty()) {
					String[] ks=new String[] {};
					ks=param.getValue().keySet().toArray(ks);
					TaskRecord record=param.getValue().get(ks[0]);
					return new SimpleStringProperty(record.getTaskId());
				}else {
					return null;
				}
			}
			
		});
		
		ArrayList<SimCol> colList=Simulator.getSim().getSimCols();
		colList.forEach(new Consumer<SimCol>() {
			@Override
			public void accept(SimCol col) {
				SimColConf conf=col.getSimColConf();
				TableColumn<Map<String,TaskRecord>, String> tc=new TableColumn<Map<String,TaskRecord>, String>(conf.getTitle());
				tv.getColumns().add(tc);
				tc.setPrefWidth(150);
				String colId=conf.getId();
				tc.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map<String,TaskRecord>,String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(CellDataFeatures<Map<String, TaskRecord>, String> param) {
						if(param.getValue()!=null&&param.getValue().get(colId)!=null) {
							TaskRecord record=param.getValue().get(colId);
							return new SimpleStringProperty(record.toString());
						}else {
							return null;
						}
					}
				});		
			}
		});
		this.setCenter(tv);
		tv.prefWidthProperty().bind(this.widthProperty());		
	}
	public void fresh() {
		tv.refresh();
	}
}