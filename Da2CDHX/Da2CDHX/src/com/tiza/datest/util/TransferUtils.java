package com.tiza.datest.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.lingtu.crypt.Base64;

public class TransferUtils {
	
	/**
	 * @param alarms
	 * @return byte[]
	 * @��������ת����bit
	 */
	public static byte[] trslarm2Bit(String[] alarms){
		byte[] ait = new byte[8];
		List<String> strList = new ArrayList<String>();
		strList.add("��������");
		strList.add("Ԥ��");
		strList.add("���Ƕ�λģ�鷢������");
		strList.add("���Ƕ�λ����δ�ӻ򱻼���");
		strList.add("���Ƕ�λ���߶�·");
		strList.add("ISU����ԴǷѹ");
		strList.add("ISU����Դ����");
		strList.add("Һ��(LCD)��ʾISU����");
		strList.add("�����ϳ�(TTS)ģ�����");
		strList.add("����ͷ����");
		strList.add("�Ƽ�������");
		strList.add("��������������(ǰ����)");
		strList.add("LED���������");
		strList.add("Һ��(LCD)��ʾ������");
		strList.add("��ȫ����ģ�����");
		strList.add("LED���ƹ���");
		strList.add("������ʻ��ʱ");
		strList.add("�����ۼƼ�ʻ��ʱ");
		strList.add("��ʱͣ��");
		strList.add("��������/·��");
		strList.add("·����ʻʱ�䲻��/����");
		strList.add("����·����ʻ");
		strList.add("���ٴ���������");
		strList.add("�����Ƿ����");
		strList.add("�����Ƿ�λ��");
		strList.add("ISU�洢�쳣");
		strList.add("¼���豸����");
		strList.add("�Ƽ���ʵʱʱ�ӳ����涨����Χ");
		
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
	 * @��װbit����
	 */
	public static byte[] transerState2Bit(String[] state){
		byte[] bits = new byte[8];
		int res = 0;
		for (String str : state) {
			if(str.equals("δ��λ")){
				res = 1 | 0;
				bits[7] = (byte) (bits[7] | res);
			}else if(str.equals("��Ӫ��")){
				res = res | (1 << 3);
				bits[7] = (byte) res;
			}else if(str.equals("ACC��")){
				res = 1 << 0;
				bits[5] = (byte) (bits[5] | res) ;
			}else if(str.equals("�س�")){
				res = res | ( 1 << 1 );
				bits[5] = (byte) (bits[5] |res) ;
			}
		}
	/*	List<String> strList = new ArrayList<String>();
		strList.add("δ���Ƕ�λ");
		strList.add("��γ");
		strList.add("����");
		strList.add("ͣ��״̬");
		strList.add("ԤԼ(����)");
		strList.add("��ת��");
		strList.add("��ת��");
		strList.add("0");
		strList.add("ACC��");
		strList.add("�س�");
		strList.add("������·�Ͽ�");
		strList.add("������·�Ͽ�");
		strList.add("���ż���");
		strList.add("��������");
		strList.add("�Ѵﵽ����Ӫ�˴���/ʱ��");
		for (String st : state) {
			for (int i = 0; i < 15; i++) {
				if(null != strList.get(i) && null != st){
					if(!strList.get(i).equals("��λ")){
						System.out.println("���붨λ");
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
	 * @������תʮ������
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
	 * @base64��ת����
	 */
	public static String TransferStr(String str){
		try {
			str = new String(Base64.decode(str.toCharArray()), "GBK");
		} catch (UnsupportedEncodingException e) {
			
		}
		return  str;
	}
}
