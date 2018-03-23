package cn.tnar.parkingupload.task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import cn.tnar.parkingupload.model.REQUESTS;
import cn.tnar.parkingupload.model.REQ_COMM_DATA;
import cn.tnar.parkingupload.model.REQ_MSG_HDR;
import cn.tnar.parkingupload.model.Root;
import cn.tnar.parkingupload.service.impl.PakingRepositoryImpl;
import cn.tnar.parkingupload.tool.DateTool;
import cn.tnar.parkingupload.tool.HttpTool;
import cn.tnar.parkingupload.tool.IoReadOrInputOutS;
import cn.tnar.parkingupload.tool.IoReadOrInputPro;
import net.sf.json.JSONArray;

@Component
public class UploadTask {
	
	Logger logger = LoggerFactory.getLogger(UploadTask.class);

	@Value("${app.mobilefly.version}")
	private String version;

	@Value("${app.mobilefly.serviceid}")
	private String serviceId;
	
	@Value("${app.mobilefly.serviceid2}")
	private String serviceId2;
	
	@Value("${app.mobilefly.serviceid3}")
	private String serviceId3;
	
	@Value("${app.mobilefly.licensekey}")
	private String licensekey;

	@Value("${app.mobilefly.isparkingcount}")
	private boolean isPark;
	
	@Value("${app.mobilefly.requesturl}")
	private String requestUrl;

	@Autowired
	PakingRepositoryImpl respository;

	@Scheduled(cron = "${job.time.schedule}")
	public void uploadParking() {
		System.out.println("start");
		Root freeCount = new Root();
		Root inStream = new Root();
		Root outStreams = new Root();
		REQUESTS requests = freeParks();//剩余车位的请求
		List<REQUESTS> intStreamReq = inParkingStream();//查询入场流水
		if(intStreamReq!=null) {
			inStream.setREQUESTS(intStreamReq);//入场流水的请求
		}
		List<REQUESTS> list = new ArrayList<REQUESTS>();
		list.add(requests);//剩余车位
		
		List<REQUESTS> outStreamReq = outParkingStream();//查出场流水
		if(outStreamReq!=null) {
			outStreams.setREQUESTS(outStreamReq);
		}
		
		freeCount.setREQUESTS(list);//剩余车位的请求
		
		
//		variousRquests(freeCount);
//		variousRquests(inStream);
		variousRquests(outStreams);
		
	}
	
	private void variousRquests(Root root) {
		String obj = JSONObject.toJSONString(root);
		String res = "";
		try {
			res = HttpTool.sendPost(obj, requestUrl, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("request:{} answer:{}",obj,res);
	}
	
	//剩余车位
	private REQUESTS freeParks() {

		//logger.info("version:{} serviceId:{} parkCode:{} isPark:{} requestUrl:{}",version,serviceId,licensekey,isPark,requestUrl);
		int free = 0;
		if (isPark) {
			free = respository.getParkingCount();
		} else {
			int total = respository.getParkCount();                                                         
			int parking = respository.getParkingCount();
			free = total - parking;
			free = free > 0 ? free : 0;
		}
		//for test
		//free = new Random().nextInt(100);
		String now = DateTool.getDateFormater();//当前时间
		REQUESTS requests = new REQUESTS();
		REQ_MSG_HDR req_msg_hdr = new REQ_MSG_HDR();
		req_msg_hdr.setSERVICE_ID(serviceId);
		REQ_COMM_DATA req_comm_data = new REQ_COMM_DATA();
		req_comm_data.setVersion(version);
		req_comm_data.setLicensekey(licensekey);
		req_comm_data.setUptime(now);
		req_comm_data.setFreecount(Integer.toString(free));
		requests.setREQ_COMM_DATA(req_comm_data);
		requests.setREQ_MSG_HDR(req_msg_hdr);  
		return requests;
	
	}
	
	//入场流水
	private ArrayList<REQUESTS> inParkingStream() {
		REQ_MSG_HDR req_msg_hdr = new REQ_MSG_HDR();
		req_msg_hdr.setSERVICE_ID(serviceId2);
		//读取持久化的intime1（before）
		String intime1 = new IoReadOrInputPro().inPutIntime();
//		System.out.println("intime1 :"+intime1);
		if(intime1 == null) {
			intime1 = "";
		}
		//查询最大intime2（after）
		String intime = respository.getMaxIntime();
//		System.out.println(intime);
		String intime2 = intime.substring(0, 19);
//		System.out.println("intime2 :"+intime2);
		//持久化最大intime
		new IoReadOrInputPro().outPutIntime(intime2);
		//根据入场时间查询入场流水
		if(!intime1.equals(intime2)) {
			List arry = respository.getParkingStream(intime1,intime2);
//			System.out.println(arry.size());
			JSONArray ja = JSONArray.fromObject(arry);
			//创建请求集合
			ArrayList<REQUESTS> request_list = new ArrayList<REQUESTS>();	
			
			for(int i=0;i<ja.size();i++) {
				REQ_COMM_DATA req_comm_data = new REQ_COMM_DATA();
				REQUESTS requests = new REQUESTS();
				req_comm_data.setVersion(version);
				req_comm_data.setLicensekey(licensekey);
				req_comm_data.setCar_id(ja.getJSONObject(i).getString("carid"));
				req_comm_data.setCartype(ja.getJSONObject(i).getString("cartypeno"));
				req_comm_data.setCarnocolor(ja.getJSONObject(i).getString("carnocolor"));
				req_comm_data.setInserialno(ja.getJSONObject(i).getString("serialno"));
				req_comm_data.setInparktrace(ja.getJSONObject(i).getString("batchno"));
				req_comm_data.setInoperno(ja.getJSONObject(i).getString("creater"));//inoperno
				req_comm_data.setIngate_code(ja.getJSONObject(i).getString("ingateno_id"));
				req_comm_data.setIntime(ja.getJSONObject(i).getString("intime"));
				req_comm_data.setInphoto(ja.getJSONObject(i).getString("inphoto"));
				req_comm_data.setInsmallphoto(ja.getJSONObject(i).getString("insmallphoto"));
				req_comm_data.setInaccepted(ja.getJSONObject(i).getString("inaccepted"));
				req_comm_data.setInconfidence(ja.getJSONObject(i).getString("confidence"));
				req_comm_data.setRegion_code(ja.getJSONObject(i).getString("zone_id"));
			
				requests.setREQ_COMM_DATA(req_comm_data);
				requests.setREQ_MSG_HDR(req_msg_hdr); 
				
				request_list.add(requests);	
			}
				return request_list;
		}
				return null;
	}
	
	//出场流水
	private ArrayList<REQUESTS> outParkingStream(){
		REQ_MSG_HDR req_msg_hdr = new REQ_MSG_HDR();
		req_msg_hdr.setSERVICE_ID(serviceId3);
		//读取
		String outTime1 = new IoReadOrInputOutS().inPutIntime();
		if(outTime1 == null) {
			outTime1 = "";
		}
		//查询最大outTime
		String outTime = respository.getMaxOuttime();
		String outTime2 = outTime.substring(0, 19);
		//持久outTime2
		new IoReadOrInputOutS().outPutIntime(outTime2);
		if(!outTime1.equals(outTime2)) {
			List arry = respository.getParkingOutStream(outTime1, outTime2);
			
			JSONArray ja = JSONArray.fromObject(arry);
			//创建请求集合
			ArrayList<REQUESTS> request_list = new ArrayList<REQUESTS>();	
			for(int i=0;i<ja.size();i++) {
				REQ_COMM_DATA req_comm_data = new REQ_COMM_DATA();
				REQUESTS requests = new REQUESTS();
				req_comm_data.setVersion(version);//1
				req_comm_data.setLicensekey(licensekey);//1
				req_comm_data.setCar_id(ja.getJSONObject(i).getString("carid"));//1
				req_comm_data.setCartype(ja.getJSONObject(i).getString("cartypeno"));//1
				req_comm_data.setInserialno(ja.getJSONObject(i).getString("serialno"));//1
				req_comm_data.setInparktrace(ja.getJSONObject(i).getString("serialno"));
				req_comm_data.setOutoperno(ja.getJSONObject(i).getString("carid"));
				req_comm_data.setOutgate_code("01");
				req_comm_data.setIntime(ja.getJSONObject(i).getString("intime"));//1
				req_comm_data.setInphoto("");
				req_comm_data.setOutphoto("");
				req_comm_data.setInsmallphoto(ja.getJSONObject(i).getString("insmallphoto"));
				req_comm_data.setOutsmallphoto(ja.getJSONObject(i).getString("outsmallphoto"));
				req_comm_data.setOutaccepted(ja.getJSONObject(i).getString("outaccepted"));
				req_comm_data.setOutconfidence(ja.getJSONObject(i).getString("confidence"));
				req_comm_data.setRegion_code(ja.getJSONObject(i).getString("zone_id"));
				req_comm_data.setOutopertiontypename("");
				req_comm_data.setOutopertiontime("20160101230059");
				req_comm_data.setOuttype("1");
				req_comm_data.setParktime("10");//1
				req_comm_data.setPaytype(ja.getJSONObject(i).getString("paytype"));//1
				req_comm_data.setPaystate("2");
				req_comm_data.setPaydatetime("20160101230059");
				req_comm_data.setPaymoney(ja.getJSONObject(i).getString("paymoney"));//1
				req_comm_data.setOutlog_batchno("");
				req_comm_data.setChargemoney(ja.getJSONObject(i).getString("chargemoney"));//1
				req_comm_data.setOuttime(ja.getJSONObject(i).getString("outtime"));//1
				req_comm_data.setChargemanno("");
				
			
				requests.setREQ_COMM_DATA(req_comm_data);
				requests.setREQ_MSG_HDR(req_msg_hdr); 
				
				request_list.add(requests);	
			}
				return request_list;
		}
				return null;
		
	}
}
