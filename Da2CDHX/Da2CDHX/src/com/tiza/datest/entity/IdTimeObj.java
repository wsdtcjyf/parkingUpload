package com.tiza.datest.entity;

public class IdTimeObj {

	
	private String id;
	private Long time;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	@Override
	public boolean equals(Object obj) {
		IdTimeObj o = (IdTimeObj)obj;
		if(this.id.equals(o.id)&&((this.time-o.time)<1800*1000)&&((this.time-o.time)>-1800*1000)){
			return true;
		}
		return false;
	}
}
