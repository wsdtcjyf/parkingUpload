package com.tiza.datest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import org.apache.log4j.Logger;

import com.lingtu.crypt.Base64;
import com.tiza.datest.comm.TcpClient;
import com.tiza.datest.comm.TcpClient2;
import com.tiza.datest.comm.UdpServer;
import com.tiza.datest.database.DataBaseFunction;
import com.tiza.datest.entity.MobileEntity;
import com.tiza.datest.entity.Taxi2;
import com.tiza.datest.util.DaConfig;

public class DaApplication extends Thread {

	public static String strVersion = "";
	public DaConfig cfg;
	public Logger log;
	// tcp 客户端
	private TcpClient tc;
	private TcpClient2 tc2;

	// 数据库连接类
	public DataBaseFunction dbObj;
	// 手机号码映射关系表
	public Vector<MobileEntity> vMac;
	public long lWaitTime;
	private String tel1;
	private String tel2;
	private String tel3;
	private String tel4;
	private String tel5;
	private String tel6;
	private String information;
	private int i = 0;
	private int messageNum;

	public DaApplication(DaConfig cfg, Logger log) {
		this.cfg = cfg;
		this.log = log;
		this.lWaitTime = Long.parseLong(cfg.theproperties.getProperty("UpdWaitTime"));
		this.tel1 = cfg.theproperties.getProperty("tel1");
		this.tel2 = cfg.theproperties.getProperty("tel2");
		this.tel3 = cfg.theproperties.getProperty("tel3");
		this.tel4 = cfg.theproperties.getProperty("tel4");
		this.tel5 = cfg.theproperties.getProperty("tel5");
		this.tel6 = cfg.theproperties.getProperty("tel6");
		this.information = cfg.theproperties.getProperty("information");
		this.messageNum = Integer.parseInt(cfg.theproperties.getProperty("messageNum"));
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void initApplication() throws Exception {
		log.info("===============实时数据转发服务" + strVersion + "================");
		// 可以在这里初始化数据库连接等等
		dbObj = new DataBaseFunction(this);
		if (dbObj.initDB1()) {
			this.getSimSerial2CommAddr();
		}
		if (lWaitTime != 0) {
			new ThreadMapReset(this, this.log, lWaitTime).start();
		}

	}

	/**
	 * 获得手机号码隐射表
	 */
	public void getSimSerial2CommAddr() {
		this.vMac = dbObj.getSimSerial2CommAddr();
	}

	/**
	 * 根据sim卡号获得映射对象
	 * 
	 * @param sim
	 * @return
	 */
	public MobileEntity mapSim2me(String sim) {
		if(this.vMac == null) return null;
		MobileEntity me = null;
		for (MobileEntity mm : vMac) {
			if (mm.commaddr.equals(sim)) {
				me = mm;
				break;
			}
		}
		return me;
	}


	/**
	 * 根据suid卡号获得映射对象
	 * 
	 * @param sim
	 * @return
	 */
	public MobileEntity mapSuidme(int suid) {
		MobileEntity me = null;
		for (MobileEntity mm : vMac) {
			if (mm.suid == suid) {
				me = mm;
				break;
			}
		}
		return me;
	}

	/**
	 * 转发数据
	 * 
	 * @param data
	 */
	public void sendToTcpServer(byte[] data, int len) {
		tc.send(data, len);
	}

	/**
	 * 转发数据
	 * 
	 * @param data
	 */
	public void sendToTcpServer2(String commaddr) {
		MobileEntity me = mapSim2me(commaddr);
		if (i < messageNum) {
			if (me != null) {
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String dateNowStr = sdf.format(date);
				String newStr1 = information.replaceAll("DataTime", dateNowStr);
				String newStr2 = newStr1.replaceAll("vname", me.vname);
				String SendMessage = newStr2.replaceAll("commaddr", me.commaddr);
				byte[] inform = SendMessage.getBytes();
				char[] info = Base64.encode(inform);
				String in = String.valueOf(info);

				if (me.sgid == 84) {
					String[] telephone = tel1.split("#");
					for (int a = 0; a < telephone.length; a++) {
						String TcpClient2 = "SBMT 0 " + telephone[a] + " " + in + " 0 \r\n";
						byte[] out2 = TcpClient2.getBytes();
						tc2.send(out2, out2.length);
						i++;
						log.info("短信条数_" + i + "  " + "短信内容:" + SendMessage);
					}
				} else if (me.sgid == 105) {
					String[] telephone = tel2.split("#");
					for (int b = 0; b < telephone.length; b++) {
						String TcpClient2 = "SBMT 0 " + telephone[b] + " " + in + " 0 \r\n";
						byte[] out2 = TcpClient2.getBytes();
						tc2.send(out2, out2.length);
						i++;
						log.info("短信条数_" + i + "  " + "短信内容:" + SendMessage);
					}
				} else if (me.sgid == 124) {
					String[] telephone = tel3.split("#");
					for (int b = 0; b < telephone.length; b++) {
						String TcpClient2 = "SBMT 0 " + telephone[b] + " " + in + " 0 \r\n";
						byte[] out2 = TcpClient2.getBytes();
						tc2.send(out2, out2.length);
						i++;
						log.info("短信条数_" + i + "  " + "短信内容:" + SendMessage);
					}
				} else if (me.sgid == 224) {
					String[] telephone = tel4.split("#");
					for (int b = 0; b < telephone.length; b++) {
						String TcpClient2 = "SBMT 0 " + telephone[b] + " " + in + " 0 \r\n";
						byte[] out2 = TcpClient2.getBytes();
						tc2.send(out2, out2.length);
						i++;
						log.info("短信条数_" + i + "  " + "短信内容:" + SendMessage);
					}
				} else if (me.sgid == 144) {
					String[] telephone = tel5.split("#");
					for (int b = 0; b < telephone.length; b++) {
						String TcpClient2 = "SBMT 0 " + telephone[b] + " " + in + " 0 \r\n";
						byte[] out2 = TcpClient2.getBytes();
						tc2.send(out2, out2.length);
						i++;
						log.info("短信条数_" + i + "  " + "短信内容:" + SendMessage);
					}
				} else if (me.sgid == 164) {
					String[] telephone = tel6.split("#");
					for (int b = 0; b < telephone.length; b++) {
						String TcpClient2 = "SBMT 0 " + telephone[b] + " " + in + " 0 \r\n";
						byte[] out2 = TcpClient2.getBytes();
						tc2.send(out2, out2.length);
						i++;
						log.info("短信条数_" + i + "  " + "短信内容:" + SendMessage);
					}
				}
			}
		} else {
			log.info("超过短信发送条数上限");
		}

	}

	@Override
	public void run() {

		try {
			// 初始化一些内容
			initApplication();
			// 开启Udp侦听
			UdpServer ds = new UdpServer(cfg, log, this);
			ds.setName("DaService");
			ds.start();

			// 发起Tcp连接
			tc = new TcpClient(cfg, log, this);
			tc.start();

			// 发起Tcp连接
//			tc2 = new TcpClient2(cfg, log);
//			tc2.start();

			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					break;
				}

				String[] time = "23:55".split(":");
				Calendar ca = Calendar.getInstance();
				ca.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
				ca.set(Calendar.MINUTE, Integer.parseInt(time[1]));
				ca.set(Calendar.SECOND, 0);
				long resetTime = ca.getTimeInMillis() / 1000;
				long currentTime = System.currentTimeMillis() / 1000;
				if (resetTime == currentTime) {
					Date date = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
					String dateNowStr = sdf.format(date);
					log.info(dateNowStr + "发送短信的最大条数：" + i);
					i = 0;
				}
			}

		} catch (Exception e1) {
			log.error("init error :" + e1);
			e1.printStackTrace();
		}

	}

}
