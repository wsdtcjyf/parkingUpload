package com.tiza.datest.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.lingtu.crypt.Base64;

public class TransferUtils {
	
	/**
	 * @param alarms
	 * @return byte[]
	 * @报警中文转化成bit
	 */
	public static byte[] trslarm2Bit(String[] alarms){
		byte[] ait = new byte[8];
		List<String> strList = new ArrayList<String>();
		strList.add("紧急报警");
		strList.add("预警");
		strList.add("卫星定位模块发生故障");
		strList.add("卫星定位天线未接或被剪断");
		strList.add("卫星定位天线短路");
		strList.add("ISU主电源欠压");
		strList.add("ISU主电源掉电");
		strList.add("液晶(LCD)显示ISU故障");
		strList.add("语音合成(TTS)模块故障");
		strList.add("摄像头故障");
		strList.add("计价器故障");
		strList.add("服务评价器故障(前后排)");
		strList.add("LED广告屏故障");
		strList.add("液晶(LCD)显示屏故障");
		strList.add("安全访问模块故障");
		strList.add("LED顶灯故障");
		strList.add("连续驾驶超时");
		strList.add("当天累计驾驶超时");
		strList.add("超时停车");
		strList.add("进出区域/路线");
		strList.add("路段行驶时间不足/过长");
		strList.add("禁行路段行驶");
		strList.add("车速传感器故障");
		strList.add("车辆非法点火");
		strList.add("车辆非法位移");
		strList.add("ISU存储异常");
		strList.add("录音设备故障");
		strList.add("计价器实时时钟超过规定的误差范围");
		
		for (String as : alarms) {
			for (int i = 0; i < 28; i++) {
				if(null != strList.get(i) && null != as){
					if((strList.get(i)).contains(as)){
						ait[7-i/4] = (byte) (ait[7-i/4] | (1 << (i%4 == 0 ? i/4 : i%4)) );
					}
				}
			}
		}
		return ait;
	}
	
	/**
	 * @param strs
	 * @return
	 * @封装bit数据
	 */
	public static byte[] transerState2Bit(String[] state){
		byte[] bits = new byte[8];
		int res = 0;
		for (String str : state) {
			if(str.equals("未定位")){
				res = 1 | 0;
				bits[7] = (byte) (bits[7] | res);
			}else if(str.equals("非营运")){
				res = res | (1 << 3);
				bits[7] = (byte) res;
			}else if(str.equals("ACC开")){
				res = 1 << 0;
				bits[5] = (byte) (bits[5] | res) ;
			}else if(str.equals("重车")){
				res = res | ( 1 << 1 );
				bits[5] = (byte) (bits[5] |res) ;
			}
		}
	/*	List<String> strList = new ArrayList<String>();
		strList.add("未卫星定位");
		strList.add("南纬");
		strList.add("西经");
		strList.add("停运状态");
		strList.add("预约(任务车)");
		strList.add("空转重");
		strList.add("重转空");
		strList.add("0");
		strList.add("ACC开");
		strList.add("重车");
		strList.add("车辆油路断开");
		strList.add("车辆电路断开");
		strList.add("车门加锁");
		strList.add("车辆锁定");
		strList.add("已达到限制营运次数/时间");
		for (String st : state) {
			for (int i = 0; i < 15; i++) {
				if(null != strList.get(i) && null != st){
					if(!strList.get(i).equals("定位")){
						System.out.println("进入定位");
						break;
					}
					if((strList.get(i)).contains(st)){
						System.out.println("strList.get(i)) :"+strList.get(i));
						bits[7-i/4] = (byte) (1 << (i%4 == 0 ? i/4 : i%4) | bits[7-i/4]);
					}
				}
			}
		}
		System.out.println(bits[5]);*/
		return bits;
	}
	
	/**
	 * @param result
	 * @return
	 * @二进制转十六进制
	 */
	public static String toHex2(byte[] result) { 
		  StringBuffer sb = new StringBuffer(result.length * 2); 
		  for (int i = 0; i < result.length; i++) { 
		    int low = result[i] & 0x0f; 
		    sb.append(low > 9 ? (char) ((low - 10) + 'a') : (char) (low + '0')); 
		  } 
		  return sb.toString(); 
	} 
	
	/**
	 * @param str
	 * @return
	 * @base64码转中文
	 */
	public static String TransferStr(String str){
		try {
			str = new String(Base64.decode(str.toCharArray()), "GBK");
		} catch (UnsupportedEncodingException e) {
			
		}
		return  str;
	}
}
