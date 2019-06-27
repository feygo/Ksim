package org.feygo.ksim.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.feygo.ksim.tools.AAL;

public class SimBoardConf {
	/** 配置变量  **/
	private String name;
	private Map<String,ArrayList<String>> Flow;
	private ArrayList<SimTitleConf> Layout;
	private Map<String,SimColConf> Cols;
	
	/** 非配置变量 **/
	private Map<String, int[]> LayoutTier;
	private int[] LayoutMatrix;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, ArrayList<String>> getFlow() {
		return Flow;
	}
	public void setFlow(Map<String, ArrayList<String>> flow) {
		Flow = flow;
	}
	public ArrayList<SimTitleConf> getLayout() {
		return Layout;
	}
	public void setLayout(ArrayList<SimTitleConf> layout) {
		Layout = layout;
	}
	public Map<String, SimColConf> getCols() {
		return Cols;
	}
	public void setCols(Map<String, SimColConf> cols) {
		Cols = cols;
	}
	
	public Map<String, int[]> getLayoutTier() {
		return LayoutTier;
	}
	public int[] getLayoutMatrix() {
		return LayoutMatrix;
	}
	@Override
	public String toString() {
		StringBuffer sBuffer=new StringBuffer();
		sBuffer.append("【kanban:").append(name).append("】\n");
		sBuffer.append("=== flow ===\n");
		Flow.forEach(new BiConsumer<String, ArrayList<String>>() {
			@Override
			public void accept(String t, ArrayList<String> u) {
				sBuffer.append(t).append(":").append(u).append("\n");				
			}
		});
		sBuffer.append("=== layout ===\n");
		Layout.forEach(new Consumer<SimTitleConf>() {
			@Override
			public void accept(SimTitleConf t) {
				sBuffer.append(t).append("\n");
			}
		});
		sBuffer.append("=== cols ===\n");
		Cols.forEach(new BiConsumer<String, SimColConf>() {
			@Override
			public void accept(String t, SimColConf u) {
				sBuffer.append(t).append(":").append(u).append("\n");
			}
		});
		return sBuffer.toString();
	}
	
	public void fresh() {
		// 为col添加colId
		freshColId();
		// 计算看板布局矩阵
		freshLayoutMatrix();
		// 计算看板布局层级
		freshLayoutTier();
		
	}
	/**
	 * [tier,cols]
	 * @return
	 */
	private void freshLayoutMatrix() {
		int tier=1;
		int cols=0;
		for(int i=0;i<Layout.size();i++) {
			SimTitleConf pConf=Layout.get(i);			
			List<SimTitleConf> sList=pConf.getSub();
			if(sList!=null&&!sList.isEmpty()) {
				tier=2;
				cols=cols+sList.size();
			}else {
				cols=cols+1;
			}
		}
		LayoutMatrix=new int[] {tier,cols};
	}
	
	private void freshLayoutTier() {
		// 起始位置 ，行，列，行跨度，列跨度，
		Map<String,int[]> Tier=new HashMap<String,int[]>();
		// 检查是否二层看板
		int TierCnt=LayoutMatrix[0];
		int pIndex=0;
		for(int i=0;i<Layout.size();i++) {
			SimTitleConf pConf=Layout.get(i);
			List<SimTitleConf> sList=pConf.getSub();
			// 设置底层看板
			int[] layout;
			// 设置第二层看板
			if(sList==null||sList.isEmpty()) {
				// 一层看板
				layout=new int[] { pIndex,1,1,2,1};
				Tier.put(pConf.getId(), layout);
				pIndex=pIndex+1;
			}else {
				// 一层看板 一层头
				layout=new int[] { pIndex,1,sList.size(),1,0};
				Tier.put(pConf.getId(), layout);
				// 二层看板
				for(int sIndex=0;sIndex<sList.size();sIndex++) {
					SimTitleConf sConf=sList.get(sIndex);
					int[] layout2=new int[] { pIndex+sIndex,2,1,1,1};
					Tier.put(sConf.getId(), layout2);
				}
				pIndex=pIndex+sList.size();
			}
			//int columnIndex, int rowIndex, int colspan, int rowspan
		}
		//AAL.a(Tier.toString());
		LayoutTier=Tier;
	}
	private void freshColId() {
		Cols.forEach(new BiConsumer<String, SimColConf>() {
			@Override
			public void accept(String id, SimColConf colConf) {
				colConf.setId(id);				
			}
		});
	}
	
	
	
}
