package org.feygo.ksim.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.feygo.ksim.tools.AAL;

public class SimBoardConf {
	/** ���ñ���  **/
	private String name;
	private Map<String,ArrayList<String>> Flow;
	private ArrayList<SimTitleConf> Layout;
	private Map<String,SimColConf> Cols;
	private ArrayList<Map<String,String>> Lanes;
	
	/** �����ñ��� **/
	private List<String[]> LayoutTier;
	private int[] LayoutMatrix;
	private String FirstColId;
	private String LastColId;
	private HashMap<String, ArrayList<String>> FlowPreColMap;
	private HashMap<String, ArrayList<String>> FlowNexColMap;
	
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
	public ArrayList<Map<String, String>> getLanes() {
		return Lanes;
	}
	public void setLanes(ArrayList<Map<String, String>> lanes) {
		Lanes = lanes;
	}
	public List<String[]> getLayoutTier() {
		return LayoutTier;
	}
	public int[] getLayoutMatrix() {
		return LayoutMatrix;
	}
	public String getFirstColId() {
		return FirstColId;
	}
	public String getLastColId() {
		return LastColId;
	}
	
	@Override
	public String toString() {
		StringBuffer sBuffer=new StringBuffer();
		sBuffer.append("��kanban:").append(name).append("��\n");
		sBuffer.append("=== Flow ===\n");
		Flow.forEach(new BiConsumer<String, ArrayList<String>>() {
			@Override
			public void accept(String t, ArrayList<String> u) {
				sBuffer.append(t).append(":").append(u).append("\n");				
			}
		});
		sBuffer.append("=== Layout ===\n");
		Layout.forEach(new Consumer<SimTitleConf>() {
			@Override
			public void accept(SimTitleConf t) {
				sBuffer.append(t).append("\n");
			}
		});
		sBuffer.append("=== Cols ===\n");
		Cols.forEach(new BiConsumer<String, SimColConf>() {
			@Override
			public void accept(String t, SimColConf u) {
				sBuffer.append(t).append(":").append(u).append("\n");
			}
		});
		sBuffer.append("=== Lanes ===\n");
		Lanes.forEach(new Consumer<Map<String, String>>() {
			@Override
			public void accept(Map<String, String> t) {
				sBuffer.append(t).append("\n");
			}
		});
		return sBuffer.toString();
	}
	
	public void fresh() {
		// Ϊcol���colId
		freshColId();
		// ���㿴�岼�־���
		freshLayoutMatrix();
		// ���㿴�岼�ֲ㼶
		freshLayoutTier();
		// �������Ҳ�ĵ�һ��
		freshFirstColId();
		freshLastColId();
		// ����col����ת����
		freshFlowPreColMap();
		freshFlowNexColMap();
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
		// ��ʼλ�� ���У��У��п�ȣ��п�ȣ�
		List<String[]> Tier=new ArrayList<String[]>();
		// ����Ƿ���㿴��
		int TierCnt=LayoutMatrix[0];
		int pIndex=0;
		for(int i=0;i<Layout.size();i++) {
			SimTitleConf pConf=Layout.get(i);
			List<SimTitleConf> sList=pConf.getSub();
			// ���õײ㿴��
			int[] layout;
			// ���õڶ��㿴��
			if(sList==null||sList.isEmpty()) {
				// һ�㿴��
				//layout=new int[] { pIndex,1,1,2,1};
				Tier.add(new String[]{pConf.getId(), String.valueOf(pIndex),"1","1","2","1"});
				pIndex=pIndex+1;
			}else {
				// һ�㿴�� һ��ͷ
				Tier.add(new String[]{pConf.getId(), String.valueOf(pIndex),"1",String.valueOf(sList.size()),"1","0"});
				// ���㿴��
				for(int sIndex=0;sIndex<sList.size();sIndex++) {
					SimTitleConf sConf=sList.get(sIndex);
					Tier.add(new String[]{sConf.getId(), String.valueOf(pIndex+sIndex),"2","1","1","1"});
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
	private void freshFirstColId() {
		String firstId;
		SimTitleConf fConf=Layout.get(0);
		ArrayList<SimTitleConf> sub=fConf.getSub();
		if(sub!=null&&!sub.isEmpty()) {
			firstId=sub.get(0).getId();
		}else {
			firstId=fConf.getId();
		}
		AAL.a("��������һSimCol��"+firstId);
		FirstColId=firstId;
	}
	private void freshLastColId(){
		String lastId;
		SimTitleConf conf=Layout.get(Layout.size()-1);
		ArrayList<SimTitleConf> sub=conf.getSub();
		if(sub!=null&&!sub.isEmpty()) {
			lastId=sub.get(sub.size()-1).getId();
		}else {
			lastId=conf.getId();
		}
		AAL.a("�����Ҳ��һSimCol��"+lastId);
		LastColId=lastId;
	}
	private void freshFlowPreColMap() {
		//Map<String,ArrayList<String>> Flow
		FlowPreColMap=new HashMap<String, ArrayList<String>>();
		Flow.forEach(new BiConsumer<String, ArrayList<String>>() {
			@Override
			public void accept(String flowId, ArrayList<String> flowList) {
				for(int i=1;i<flowList.size();i++) {
					String colId=flowList.get(i);
					String preColId=flowList.get(i-1);
					ArrayList<String> list=FlowPreColMap.get(colId);
					if(list==null) {
						list=new ArrayList<String>() ;
					}
					list.add(preColId+"@"+flowId);
					FlowPreColMap.put(colId, list);
				}				
			}			
		});		
		AAL.a("����FlowPreColMap��"+FlowPreColMap);
	}
	public ArrayList<String> getFlowPreCol(String colId) {
		return FlowPreColMap.get(colId);
	}
	private void freshFlowNexColMap() {
		//Map<String,ArrayList<String>> Flow
		FlowNexColMap=new HashMap<String, ArrayList<String>>();
		Flow.forEach(new BiConsumer<String, ArrayList<String>>() {
			@Override
			public void accept(String flowId, ArrayList<String> flowList) {
				for(int i=0;i<flowList.size()-1;i++) {
					String colId=flowList.get(i);
					String nexColId=flowList.get(i+1);
					ArrayList<String> list=FlowNexColMap.get(colId);
					if(list==null) {
						list=new ArrayList<String>() ;
					}
					list.add(nexColId+"@"+flowId);
					FlowNexColMap.put(colId, list);
				}				
			}			
		});		
		AAL.a("����FlowNexColMap��"+FlowNexColMap);
	}
	public ArrayList<String> getFlowNexCol(String colId) {
		return FlowNexColMap.get(colId);
	}
	
}
