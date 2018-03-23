package cn.tnar.parkingupload.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

public class Root {
	
	@JSONField(name = "REQUESTS")
	private List<REQUESTS> REQUESTS;

	public void setREQUESTS(List<REQUESTS> REQUESTS) {
		this.REQUESTS = REQUESTS;
	}

	public List<REQUESTS> getREQUESTS() {
		return this.REQUESTS;
	}
}
