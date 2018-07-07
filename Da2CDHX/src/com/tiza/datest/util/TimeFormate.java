package com.tiza.datest.util;

public class TimeFormate {
	
	public static String formatHHmm(int time){
		int h = time / 3600;
		int m = time % 3600 / 60;
		String hh = Integer.toString(h);
		String mm = Integer.toString(m);
		if(hh.length() == 1){
			hh = "0" + hh;
		}
		if(mm.length() == 1){
			mm = "0" + mm;
		}
		return hh+":"+mm;
	}
}
