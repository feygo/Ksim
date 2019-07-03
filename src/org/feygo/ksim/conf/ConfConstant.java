package org.feygo.ksim.conf;

public class ConfConstant {

	
	public static String ColType_Lane="Lane";
	public static String ColType_LaneBuffer="LaneBuffer";
	public static String ColType_LaneQueue="LaneQueue";
	public static String ColType_Buffer="Buffer";
	public static String ColType_Queue="Queue";
	
	public static String ColTypeString_Buffer="»º³åÇø";
	public static String ColTypeString_Queue="¶ÓÁÐ";
	
	public static String getColTypeStringByType(String type) {
		if(ColType_Lane.equalsIgnoreCase(type)) {
			return "";
		}else if(ColType_LaneBuffer.equalsIgnoreCase(type)) {
			return ColTypeString_Buffer;		
		}else if(ColType_LaneQueue.equalsIgnoreCase(type)) {
			return ColTypeString_Queue;
		}else if(ColType_Buffer.equalsIgnoreCase(type)) {
			return ColTypeString_Buffer;
		}else if(ColType_Queue.equalsIgnoreCase(type)) {
			return ColTypeString_Queue;
		}else {
			return "";
		}
	}
	
}
