package cn.tnar.parkingupload.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class IoReadOrInputPro {
	private String path = "F:\\JZ\\";
	private String file_Name = "inPutStream.txt";
	
	//读,写文件的方法
	//输入流
	@Test
	public String inPutIntime() {
		try {
			File file = new File(path);//创建文件
			if(!file.exists()) {
				System.out.println("进入创建");
				file.mkdirs();//创建文件夹
				String fileName = file_Name;
				File target = new File(file,fileName);
				target.createNewFile();
			}else if(file.length()==0) {
//				file = new File(path+file_Name);
				String fileName = file_Name;
				File target = new File(file,fileName);
				target.createNewFile();
			}
			InputStream in = new FileInputStream(path+file_Name);//读
			byte[] data = new byte[(int) file.length()];//创建字节数组
			in.read(data);
			String str = new String(data);//读到的结果
//			System.out.println(str.length());
//			if(str.length()>0){
//				System.out.println(str);
//			}
			in.close();
			return str;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return null;
				
	}
	
	//输出流
	public void outPutIntime(String intime) {
		OutputStream ot;
		try {
			ot = new FileOutputStream(path+file_Name);
			String conts = intime;
			byte[] words = conts.getBytes();
			ot.write(words, 0, words.length);
			ot.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
