package org.feygo.ksim.tools;

public class TimeFormater {

	public static void main(String[] args) {

		AAL.a(getXDXH8(0));
		AAL.a(getXDXH8(2));
		AAL.a(getXDXH8(8));
		AAL.a(getXDXH8(10));

	}
	public static String getXDXH8(int time) {		
		int period=8;
		int day=Double.valueOf(time/period).intValue();
		int hour=Double.valueOf(time%period).intValue();
		String timeString=day+"Ìì"+hour+"Ð¡Ê±";
		return timeString;
	}

}
