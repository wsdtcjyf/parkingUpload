package com.tiza.datest.comm;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

import com.tiza.datest.DaApplication;
import com.tiza.datest.entity.Czc;
import com.tiza.datest.entity.Gps;
import com.tiza.datest.entity.MobileEntity;
import com.tiza.datest.entity.Taxi2;
import com.tiza.datest.util.DaConfig;
import com.tiza.datest.util.TransferUtils;

/**
 * Udp 侦听服务
 * 
 * @author 杨宏杰 yanghj@gpssz.com
 *
 */
public class UdpServer extends Thread {
	private Logger log;
	private DaApplication daApp;
	// 主侦听socket
	private DatagramSocket socket;

	private int udpPort;
	
	private DateFormat dfCD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Czc czc = new Czc();
	
	public Map<String, Gps> lastGpsMap = new ConcurrentHashMap<String, Gps>();
	
	private ScheduledExecutorService se;
	
	String[] state = new String[4];
	
	String[] str2 = new String[4];
	
	public UdpServer(DaConfig cfg, Logger log, DaApplication daApp) {
		this.log = log;
		se  = Executors.newScheduledThreadPool(5);
		this.daApp = daApp;
		this.udpPort = Integer.parseInt(cfg.theproperties.getProperty("comm_udpport"));
	}

	/**
	 * 初始化侦听
	 * 
	 * @throws SocketException
	 */
	private void StartListenUdpPort() throws SocketException {
		socket = new DatagramSocket(udpPort);
		log.info("UDP 侦听开启 (" + udpPort + ")");
	}
	
	@Override
	public void run() {
		log.info("===============UDP server分割线=================");
		try {
			StartListenUdpPort();
		} catch (SocketException e1) {
			log.error("Udp port " + udpPort + "开启失败:" + e1);
			return;
		}

		byte[] buffer = new byte[512];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
		se.scheduleAtFixedRate(new SendThread(), 20, 20, TimeUnit.SECONDS);
		
		while (true) {
			try {
				socket.receive(packet);
				String content = new String(packet.getData(), 0, packet.getLength());
				log.info("Receive From UdpServer:" + content);

				String[] datas = content.split(" ");
				String sourceMAC = datas[2];
				// String destUid = datas[3];
				String all = datas[4];

				String[] mac = sourceMAC.split("\\:");
				// String sourceMAC1 = mac[0];
				String sourceMAC2 = mac[1];

				String[] datass = all.split("\\|");
				String UTCSeconds = datass[0];
				// String priceCount = datass[1];
				String latitude = datass[2];
				String longitude = datass[3];
				// String altitude = datas[4];
				String heading = datass[5];
				String speeding = datass[6];
				String standardStatus = datass[7];
				String car = datass[9];

				String[] carf = car.split("\\;");
				String carfi = carf[1];
				String[] carfir = carfi.split("\\,");
				String carfire = carfir[0];
//				log.info("standardStatus : "+standardStatus+"car :"+car+" datass[8]"+datass[8]);
				//创建state和alarm状态数组
				
				for (int i = 0; i < 4; i++ ) {
					if(null == carfir[3]){
						break;
					}
					str2[i] = TransferUtils.TransferStr(carfir[i]);
			 	}
				
				//得到state中文数组state[]
				if(null == carfir[3]){
					System.arraycopy(str2, 0, state, 0, 3);
				}else{
					System.arraycopy(str2, 0, state, 0, 4);
				}
				//创建接收alarm中文数组
				String[] alas = new String[carfir.length-4];
				for(int i = 4; i < carfir.length; i++){
					alas[i-4] = TransferUtils
							.TransferStr(carfir[i]);
				}
				
				//得到alarm的bit数组
				byte[] alastr = TransferUtils.trslarm2Bit(alas);
				//得到state的bit数组
				byte[] stastr = TransferUtils.transerState2Bit(state);
				//得到state的int值
				Integer stateInt = Integer.parseInt(
						TransferUtils.toHex2(stastr).toString(),16); 
				Integer alarmInt = Integer.parseInt(
						TransferUtils.toHex2(alastr).toString(),16);
				log.info("stateHex16 ---:"+TransferUtils.toHex2(stastr).toString());
				log.info("alarmHex16 ---:"+TransferUtils.toHex2(alastr).toString());
				log.info("stateInt ---:"+stateInt+"-----"+"alarmInt ---:"+alarmInt);
				double latitudeDouble = Long.parseLong(latitude, 16) / 1000.0d / 60 / 60;
				String latitudeString = String.format("%.5f", latitudeDouble);

				double longitudeDouble = Long.parseLong(longitude, 16) / 1000.0d / 60 / 60;
				String longitudeString = String.format("%.5f", longitudeDouble);

				double headingDouble = Long.parseLong(heading, 16) / 60.0d / 60;
				String headingString = String.format("%.0f", headingDouble);

				double speedingDouble = Long.parseLong(speeding, 16) * 3.6 / 1000*10;//原先*10
				String speedingString = String.format("%.0f", speedingDouble);//"%.2f"

				Long UTCSecondsLong = Long.parseLong(UTCSeconds, 16) * 1000;
				String CdUTCSecondsString = dfCD.format(new Date(UTCSecondsLong));
				MobileEntity me = daApp.mapSim2me(sourceMAC2);
				if(me == null) continue;
				Gps gps = new Gps();
				gps.setNumber(me.vname);
				gps.setAlarm((int)alarmInt);
				gps.setState((int)stateInt);
				gps.setLat(Double.valueOf(latitudeString));
				gps.setLon(Double.valueOf(longitudeString));
				gps.setVec(Integer.valueOf(speedingString));
				gps.setDirection(Integer.valueOf(headingString));
				gps.setDatetime(CdUTCSecondsString);
				gps.setOper_state(2);
				
				lastGpsMap.put(sourceMAC2, gps);

//				
			} catch (Exception e) {
				log.error("从TL接收数据失败:" + e.getMessage(), e);
			}
		}
	}
	
	public static void main(String[] args) {
		
		//tqjOuw==,1tiztQ==,1MvTqg==,tefV2Q==,ts/TzQ==,t8e3qLXju/A=,wrzS9LnK1c8=,vMa828b30tHL+A==,U0S/qNLss6M=,we+ztbGovq8=,srm0qw==

		/*String[] carfir = new String[]{"tqjOuw==","1tiztQ==","1MvTqg==","tefV2Q==","ts/TzQ==","t8e3qLXju/A==","wrzS9LnK1c8==","Ma828b30tHL+A==","U0S/qNLss6M==","we+ztbGovq8==","srm0qw=="};
		String[] str2 = new String[carfir.length]; 
			for (int i = 0; i < carfir.length; i++ ) {
				str2[i] = TransferUtils.TransferStr(carfir[i]);
				System.out.println(str2[i]);
			}
			
			
			//创建接收alarm中文数组
			String[] alas = new String[carfir.length];
			for(int i = 0; i < carfir.length; i++){
				alas[i] = TransferUtils
						.TransferStr(carfir[i]);
			}
			
			//得到alarm的bit数组
			byte[] alastr = TransferUtils.trslarm2Bit(alas);
			//得到state的bit数组
			byte[] stastr = TransferUtils.transerState2Bit(str2);
			//得到state的int值
			Integer stateInt = Integer.parseInt(
					TransferUtils.toHex2(stastr).toString(),16); 
			Integer alarmInt = Integer.parseInt(
					TransferUtils.toHex2(alastr).toString(),16);
			System.out.println("stateHex16 ---:"+TransferUtils.toHex2(stastr).toString());
			System.out.println("alarmHex16 ---:"+TransferUtils.toHex2(alastr).toString());
			System.out.println("stateInt ---:"+stateInt+"-----"+"alarmInt ---:"+alarmInt);
			System.out.println((char)(18+'a'));
			System.out.println(1%4);*/
		
			
	}
	
	
	/**
	 * @author Administrator
	 * @sendData 
	 * @period=20s
	 */
	class SendThread implements Runnable {
		
		@Override
		public void run() {
			StringBuilder sb = new StringBuilder();
			JSONArray jsArr = null;
			String fstr = "";
			czc = getCzc();
			if(lastGpsMap.size() > 0){
				for(String key:lastGpsMap.keySet()) {
					Gps gps = lastGpsMap.get(key);
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
				String arrStr1 = sb.toString()+"END";
				try {
					byte[] tc = arrStr1.getBytes("UTF-8");
//					daApp.sendToTcpServer(tc, tc.length);
					lastGpsMap.clear();
				} catch (UnsupportedEncodingException e) {
					log.info("---------------UnsupportedEncodingException ------------");
				}
			}else{
				log.info("--------------The value of gps is null-----------");
			}
		}
	}
	
	/**
	 * @return
	 * @get czc
	 */
	private final Czc getCzc(){
		czc.setNumber("null");
		czc.setLat_on(-1);
		czc.setLon_on(-1);
		czc.setTime_get_on("1970-01-01 08:00:00");
		czc.setLat_off(-1);
		czc.setLon_off(-1);
		czc.setTime_get_off("1970-01-01 08:00:00");
		czc.setEmployee_id("null");
		czc.setService_eval_idx(-1);
		czc.setRun_odometer(-1);
		czc.setEmpty_odometer(-1);
		czc.setFuel_surcharge(-1);
		czc.setTime_wait("null");
		czc.setIncome(-1);
		czc.setIc_flag(-1);
		return czc;
	}
}
