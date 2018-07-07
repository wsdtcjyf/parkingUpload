package com.tiza.datest;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.tiza.datest.util.DaConfig;

public class DaMain {
	public static Logger log = Logger.getLogger(DaMain.class);

	public static void main(String[] paramArrayOfString) {
		String str = null;
		DaConfig localciConfig = null;
		DaApplication app = null;

		if ((paramArrayOfString.length < 2) || (!paramArrayOfString[0].equalsIgnoreCase("-f"))) {
			System.out.println("Usage: DaMain -f ConfFilePath");
			return;
		}
		str = paramArrayOfString[1];
//		str="Da2OtherPT.xml";
		try {
			// 读取配置文件
			localciConfig = new DaConfig(str);
			localciConfig.load();
			localciConfig.closeCfg();
			PropertyConfigurator.configure(localciConfig.theproperties);
			// 启动管理线程0
			app = new DaApplication(localciConfig, log);
			try {
				app.setName("DaApp");
				app.start();
			} catch (Exception localciException) {
				log.error("无法启动" + localciException.getMessage());
				System.exit(-1);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
