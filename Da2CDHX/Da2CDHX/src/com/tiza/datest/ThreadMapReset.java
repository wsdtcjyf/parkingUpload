package com.tiza.datest;

import org.apache.log4j.Logger;



public class ThreadMapReset extends Thread {

	// 数据等待间隔(ms)[1分钟]
	long waittime = 60000;
	// 命令处理类
	DaApplication proto = null;
	Logger cat = null;

	public ThreadMapReset(DaApplication daApp, Logger acat, long awaittime) {
		this.proto = daApp;
		this.cat = acat;
		this.waittime = awaittime * 1000;
	}

	public void run() {
		while (true) {
			try {
				sleep(waittime);
			} catch (InterruptedException iex) {
				cat.error("ThreadMapReset出错：",iex);
			}

			// 判断数据库连接是否正常
			if (!proto.dbObj.getAlive1()) {
				cat.info("数据库连接中断 正在重新连接...");
				proto.dbObj.closeDB1();
				if (proto.dbObj.initDB1())
					cat.info("数据库重新连接成功");
				else
					continue;
			}

			proto.getSimSerial2CommAddr();
			cat.info("手机号码映射数据库更新完毕！");
			
		}
	}
}
