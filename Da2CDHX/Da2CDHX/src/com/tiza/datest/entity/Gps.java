package com.tiza.datest.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Gps {
	/**
	 * ���ƺ���
	 */
	private String number;
	
	/**
	 * ����״̬
	 */
	private int alarm;
	
	/**
	 * ����״̬
	 */
	private int state;
	
	/**
	 * γ��
	 */
	private double lat;
	
	/**
	 * ����
	 */
	private double lon;
	
	/**
	 * �ٶ�
	 */
	private int vec;
	
	/**
	 * ��ʻ����
	 */
	private int direction;
	
	/**
	 * ���Ƕ�λʱ�� 
	 */
	private String datetime;
	
	/**
	 * ����Ӫ��״̬
	 */
	private int oper_state;

	public int getOper_state() {
		return oper_state;
	}

	public void setOper_state(int oper_state) {
		this.oper_state = oper_state;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getAlarm() {
		return alarm;
	}

	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public int getVec() {
		return vec;
	}

	public void setVec(int vec) {
		this.vec = vec;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	@Override
	public String toString() {
		return "Gps [number=" + number + ", alarm=" + alarm + ", state="
				+ state + ", lat=" + lat + ", lon=" + lon + ", vec=" + vec
				+ ", direction=" + direction + ", datetime=" + datetime
				+ ", oper_state=" + oper_state + "]";
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject j = new JSONObject();
		JSONObject json = new JSONObject();
		json.put("number", number);
		json.put("alarm", alarm);
		json.put("state", state);
		json.put("lat", lat);
		json.put("lon", lon);
		json.put("vec", vec);
		json.put("direction", direction);
		json.put("datetime", datetime);
		json.put("oper_state", oper_state);
		j.put("gps", json);
		return j;
	}
}
