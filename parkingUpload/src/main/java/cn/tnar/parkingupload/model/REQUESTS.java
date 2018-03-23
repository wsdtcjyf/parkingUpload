package cn.tnar.parkingupload.model;

import com.alibaba.fastjson.annotation.JSONField;

public class REQUESTS {
	
	@JSONField(name = "REQ_MSG_HDR")
	private REQ_MSG_HDR REQ_MSG_HDR;

	@JSONField(name = "REQ_COMM_DATA")
	private REQ_COMM_DATA REQ_COMM_DATA;

	public void setREQ_MSG_HDR(REQ_MSG_HDR REQ_MSG_HDR) {
		this.REQ_MSG_HDR = REQ_MSG_HDR;
	}

	public REQ_MSG_HDR getREQ_MSG_HDR() {
		return this.REQ_MSG_HDR;
	}

	public void setREQ_COMM_DATA(REQ_COMM_DATA REQ_COMM_DATA) {
		this.REQ_COMM_DATA = REQ_COMM_DATA;
	}

	public REQ_COMM_DATA getREQ_COMM_DATA() {
		return this.REQ_COMM_DATA;
	}
}