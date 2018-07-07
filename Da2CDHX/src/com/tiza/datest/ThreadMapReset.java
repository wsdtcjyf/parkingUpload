package com.tiza.datest;

import org.apache.log4j.Logger;



public class ThreadMapReset extends Thread {

	// ���ݵȴ����(ms)[1����]
	long waittime = 60000;
	// �������
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
				cat.error("ThreadMapReset����",iex);
			}

			// �ж����ݿ������Ƿ�����
			if (!proto.dbObj.getAlive1()) {
				cat.info("���ݿ������ж� ������������...");
				proto.dbObj.closeDB1();
				if (proto.dbObj.initDB1())
					cat.info("���ݿ��������ӳɹ�");
				else
					continue;
			}

			proto.getSimSerial2CommAddr();
			cat.info("�ֻ�����ӳ�����ݿ������ϣ�");
			
		}
	}
}
