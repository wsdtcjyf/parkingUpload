package cn.tnar.parkingupload.model;

import com.alibaba.fastjson.annotation.JSONField;

public class REQ_MSG_HDR {
	@JSONField(name = "SERVICE_ID")
	private String SERVICE_ID;

	public void setSERVICE_ID(String SERVICE_ID) {
		this.SERVICE_ID = SERVICE_ID;
	}

	public String getSERVICE_ID() {
		return this.SERVICE_ID;
	}
}
