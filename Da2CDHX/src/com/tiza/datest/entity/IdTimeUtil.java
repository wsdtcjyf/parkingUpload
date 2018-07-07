package com.tiza.datest.entity;

import java.util.LinkedList;
import java.util.Queue;

public class IdTimeUtil {
	
	private static Queue<IdTimeObj> queue = new LinkedList<IdTimeObj>();

	public static void add(IdTimeObj obj){
		queue.add(obj);
	}
	
	public static boolean hasOne(IdTimeObj obj){
		boolean has = queue.contains(obj);
		deleteOverTime();
		if(!has){
		add(obj);
		}
		return has;
	}
	
	private static void deleteOverTime(){
		while(true){
			IdTimeObj obj = queue.peek();
			if(obj!=null){
				if((System.currentTimeMillis()-obj.getTime())>1800*1000){
					queue.remove();
				}else{
					break;
				}
			}else{
				break;
			}
		}
	}
	
}
