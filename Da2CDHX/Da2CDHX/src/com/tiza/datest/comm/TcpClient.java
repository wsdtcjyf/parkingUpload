package com.tiza.datest.comm;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

import com.tiza.datest.DaApplication;
import com.tiza.datest.comm.UdpServer.SendThread;
import com.tiza.datest.entity.Czc;
import com.tiza.datest.entity.Gps;
import com.tiza.datest.entity.MobileEntity;
import com.tiza.datest.entity.Taxi;
import com.tiza.datest.entity.Taxi2;
import com.tiza.datest.util.DaConfig;
import com.tiza.datest.util.TimeFormate;

public class TcpClient extends Thread implements NeedReconnect {

	private Socket paramSocket;
	private InputStream paramInputStream;
	private OutputStream paramOutputStream;
	private boolean bConn;

	private Logger log;
	private String tcpIp;
	private int tcpPort;

	private String username;
	private String pass;
	private int time;
	private int dbtime;
	private Noop noop;
	private DataReaderThread dataReaderThread;
//	private SimpleDateFormat format;
    private DaConfig cfg;
    private DaApplication daApp;
    
    private long Beforetime = System.currentTimeMillis();
    
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
//    public Map<String, Czc> lastCzcMap = new ConcurrentHashMap<String, Czc>();
    
//    private ScheduledExecutorService se;
    //创建gps实例
	Gps gps = new Gps();
    
	public TcpClient(DaConfig cfg, Logger log, DaApplication daApp) {
		this.cfg = cfg;
		this.log = log;
		this.daApp = daApp;
		this.tcpIp = cfg.theproperties.getProperty("tcp_ip");
		this.tcpPort = Integer.parseInt(cfg.theproperties
				.getProperty("tcp_port"));
		this.username = cfg.theproperties.getProperty("username");
		this.pass = cfg.theproperties.getProperty("pass");
		this.time = Integer.parseInt(cfg.theproperties.getProperty("time"));
		this.dbtime = Integer.parseInt(cfg.theproperties.getProperty("dbtime"));
//		se  = Executors.newScheduledThreadPool(5);
		noop = new Noop();
		dataReaderThread = new DataReaderThread();

	}
	
	/**
	 * 心跳类
	 * @author Administrator
	 *
	 */
	class Noop implements Runnable {

		@Override
		public void run() {

			while (true) {
				if (bConn) {
					String noopStr = "CLIENTSMG-CLIENT";
					byte[] data = noopStr.getBytes();
					send(data, data.length);
					try {
						Thread.sleep(time * 1000);
					} catch (InterruptedException e) {

					}
				} else {
					log.info("wait......");
					synchronized (this) {
						try {
							wait();
						} catch (InterruptedException e) {

						}
					}
				}
			}

		}
	}
	
	class DataReaderThread implements Runnable {

		@Override
		public void run() {
//			se.scheduleAtFixedRate(new SendThread2(), 0, 5, TimeUnit.SECONDS);
			while (true) {
				if (bConn) {
					long Nowtime = System.currentTimeMillis();
					String Nowmonth = new SimpleDateFormat("yyMM").format(Nowtime);
					String Beforemonth = new SimpleDateFormat("yyMM").format(Beforetime);
					String sql = null;
					if(Nowmonth.equals(Beforemonth)){
						sql = "select * from TAXI_BUSS_SINGLE" + Nowmonth +" where utc > "+Beforetime/1000 + " and utc <= " + Nowtime/1000;
					}else{
						sql = "select * from TAXI_BUSS_SINGLE" + Nowmonth +" where utc <= "+ Nowtime/1000 +
						" union select * from TAXI_BUSS_SINGLE" + Beforemonth + " where utc > " + Beforetime/1000;
					}
					log.info("计价器数据发送时间段：" + Beforetime/1000 + "--" + Nowtime/1000);
					Beforetime = Nowtime;
					List<Taxi> t = daApp.dbObj.ReadDB(sql);
					for (Taxi test : t) {
						MobileEntity me = daApp.mapSuidme(test.suid);
						if(me != null){
							String onutc =df.format(new Date(test.onutc * 1000));       //上车时间
							String offutc =df.format(new Date(test.offutc * 1000));     //下车时间
							
							double onlonDouble = test.onlon / 1000.0d / 60 / 60;            //原先1000.0d / 60 / 60
							String onlon = String.format("%.5f", onlonDouble);          //上车经度 原先"%.6f"
							double onlatDouble = test.onlat / 1000.0d / 60 / 60;
							String onlat = String.format("%.5f", onlatDouble);          //上车纬度 原先"%.6f"
							
							double offlonDouble = test.offlon / 1000.0d / 60 / 60;
							String offlon = String.format("%.5f", offlonDouble);        //下车经度 原先"%.6f"
							double offlatDouble = test.offlat / 1000.0d / 60 / 60;
							String offlat = String.format("%.5f", offlatDouble);        //上车纬度 原先"%.6f"
							int distance = test.distance / 1000;   						//载客里程（km）
							int empty_distance = test.empty_distance / 1000;
							
							String waittime = TimeFormate.formatHHmm(test.waittime);    //等待时间（秒）
							String unitprice = Integer.toString(test.unitprice / 10);   //单价 （分） 原先没 /10
							String price = Integer.toString(test.price / 10);           //打车费金额  (分) 原先*10
							String utc = Long.toString(test.utc);                       //业务上车时间(当前车次)
							String bpos= Integer.toString(test.bpos);                   //BPOS 0-现金 1-刷卡
							
							//创建营运数据对象
							Czc czc = new Czc();
							czc.setNumber(me.vname);
							czc.setLat_on(Double.valueOf(onlat));//1
							czc.setLon_on(Double.valueOf(onlon));//1
							czc.setTime_get_on(onutc);//1
							czc.setLat_off(Double.valueOf(offlon));
							czc.setLon_off(Double.valueOf(offlat));
							czc.setTime_get_off(offutc);//1
							czc.setEmployee_id(test.managementid);//从业资格证号1
							czc.setService_eval_idx(0);//评价选项
							czc.setRun_odometer(Integer.valueOf(distance));//1
							czc.setEmpty_odometer(Integer.valueOf(empty_distance));//1
							czc.setFuel_surcharge(Integer.valueOf(unitprice));//燃油附加费1
							czc.setTime_wait(waittime);
							czc.setIncome(Integer.valueOf(price));
							czc.setIc_flag(Integer.valueOf(bpos));//1
//							lastCzcMap.put(me.vname, czc);
							
							
							JSONArray arr = new JSONArray();
							String sendValue;
							try {
								arr.put(czc.toJSON());
								arr.put(getGps().toJSON());
								log.info(me.commaddr + "_" + arr.toString());
								String arrStr = arr.toString();
								sendValue = arrStr+"END";
								byte[] data = sendValue.getBytes("UTF-8");
								send(data, data.length);
							} catch (UnsupportedEncodingException e) {
								log.info("UnsupportedEncodingException...");
							} catch (JSONException e) {
								log.info("JSONException...");
							}
						   
						}
					}
					try {
						Thread.sleep(5 * 1000);
					} catch (InterruptedException e) {
					}
				} else {
					log.info("Data wait......");
					synchronized (this) {
						try {
							wait();
						} catch (InterruptedException e) {

						}
					}
				}
			}
		}
	}
	
	/**
	 * @author Administrator
	 * @sendData 
	 * @period=20s
	 */
	/*class SendThread2 implements Runnable {
		
		@Override
		public void run() {
			StringBuilder sb = new StringBuilder();
			JSONArray jsArr = null;
			String fstr = "";
			gps = getGps();
			if(lastCzcMap.size() > 0){
				for(String key:lastCzcMap.keySet()) {
					Czc czc = lastCzcMap.get(key);
					try {
						jsArr = new JSONArray();
						jsArr.put(czc.toJSON());
						jsArr.put(gps.toJSON());
						sb = sb.append(fstr);
						sb = sb.append(jsArr.toString());
						fstr = ";";
					} catch (JSONException e) {
						log.info("-------------json exception-----------");
					}
				}
				log.info("---------czc.size = :"+lastCzcMap.size());
				String arrStr1 = sb.toString()+"END";
				try {
					byte[] tc = arrStr1.getBytes("UTF-8");
					daApp.sendToTcpServer(tc, tc.length);
					lastCzcMap.clear();
				} catch (UnsupportedEncodingException e) {
					log.info("---------------UnsupportedEncodingException ------------");
				}
			}else{
				log.info("--------------The value of gps is null-----------");
			}
		}
	}*/
	
	private final Gps getGps(){
		gps.setNumber("null");
		gps.setAlarm(-1);
		gps.setState(-1);
		gps.setLat(-1);
		gps.setLon(-1);
		gps.setVec(-1);
		gps.setDirection(-1);
		gps.setDatetime("1970-01-01 08:00:00");
		gps.setOper_state(-1);
		return gps;
	}

	@Override
	public void setConnected(Socket paramSocket, InputStream paramInputStream,
			OutputStream paramOutputStream, boolean paramBoolean) {
		this.paramSocket = paramSocket;
		this.paramInputStream = paramInputStream;
		this.paramOutputStream = paramOutputStream;
		this.bConn = paramBoolean;

		// 用户登陆
		String user = "LOGIN,client,haixinserver;";
		byte[] data = user.getBytes();
		send(data, data.length);
		
		this.log.info("set connect run :"+new String(data));
		
		synchronized (noop) {
			noop.notify();
		}
		synchronized (dataReaderThread) {
			dataReaderThread.notify();
		}
		// dataReaderThread.run();
	}

	@Override
	public void reTryToConnect() {
		TcpReconnect tcpr = new TcpReconnect(this, log, 5000);
		tcpr.strHost = tcpIp;
		tcpr.nPort = tcpPort;
		tcpr.start();

	}

	@Override
	public void run() {
		// 第一连接
		reTryToConnect();
		//		

		// 心跳启动
		new Thread(noop).start();
		new Thread(dataReaderThread).start();
		
		ByteBuffer buffer = ByteBuffer.allocate(512);
		while (true) {
			// 如果连接未成功，则阻塞当前线程，知道通知连接成功
			if (!bConn) {
				synchronized (this) {
					try {
						log.info("block this thread,Connection failed");
						this.wait();
					} catch (InterruptedException e) {

					}
				}
			} else {

				if (null != paramInputStream) {
					try {
						int b = paramInputStream.read();
						String bb = Integer.toString(b);
						if(b == 0x3B) {
							if(buffer.remaining() > bb.length()){
								buffer.put((byte) b);
								buffer.flip();
								byte[] recBytes = new byte[buffer.limit()];
								buffer.get(recBytes);
								String str = new String(recBytes, "UTF-8");
								log.info("Receive from tcpServer(0x3B):" + str);
							}else{
								log.info("BufferOverFlow...");
								buffer.clear();
							}
							
						} else if(b == -1) {// 连接断开
							log.info("disconnected ...");
							connBroken();
							continue;
						} else {
							if(buffer.remaining() > bb.length()){
								buffer.put((byte) b);
								buffer.flip();
								byte[] recBytes = new byte[buffer.limit()];
								buffer.get(recBytes);
								String str = new String(recBytes, "UTF-8");
								log.info("Receive from tcpServer(else):" + str);
							}else{
								log.info("BufferOverFlow(else)...");
								buffer.clear();
							}
						}

					} catch (Exception e) {
						log.error(e, e);
						connBroken();
					}
				}else{
					log.info("there is no data in paramInPutStream");
				}
			}
		}
	}

	/**
	 * 往Tcp服务端发送数据
	 * 
	 * @param data
	 */
	public void send(byte[] data, int len) {
		log.info("data will be send ... :"+new String(data));
		if (bConn) {
			log.info("bConn is true , data sent");
			try {
				paramOutputStream.write(data, 0, len);
				paramOutputStream.flush();
				
			} catch (Exception e) {
				log.error(e, e);
				connBroken();
			}
		}else{
			log.info("bConn is false,send method is useless");
		}
	}

	/**
	 * 连接断开，释放资源
	 */
	private void connBroken() {

		if (bConn) {
			log.info("Tcp 连接异常断开");
			bConn = false;
			try {
				paramInputStream.close();
			} catch (Exception e) {
			}

			try {
				paramOutputStream.close();
			} catch (Exception e) {
			}

			try {
				paramSocket.close();
			} catch (Exception e) {
			}
			// 断线重连
			reTryToConnect();
		}

	}

}
