package com.tiza.datest.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Administrator
 *
 */
public class Czc {
	
	/**
	 * 主键
	 */
	private String id;
	
	/**
	 * 车牌号码
	 */
	private String number;
	
	/**
	 * 上车地点纬度
	 */
	private double lat_on;
	
	/**
	 * 上车地点经度
	 */
	private double lon_on;
	
	/**
	 * 乘客上车时间
	 */
	private String time_get_on;
	
	/**
	 * 下车地点纬度
	 */
	private double lat_off;
	
	/**
	 * 下车地点经度
	 */
	private double lon_off;
	
	/**
	 * 乘客下车时间
	 */
	private String time_get_off;
	
	/**
	 * 从业资格证号
	 */
	private String employee_id;
	
	/**
	 * 评价选项
	 */
	private int service_eval_idx;
	
	/**
	 * 营运里程
	 */
	private int run_odometer;
	
	/**
	 * 空驶里程
	 */
	private int empty_odometer;
	
	/**
	 * 燃油附加费
	 */
	private int fuel_surcharge;
	
	/**
	 * 等候时间
	 */
	private String time_wait;
	
	/**
	 * 交易金额
	 */
	private int income;
	
	/**
	 * 交易类型代码
	 */
	private int ic_flag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public double getLat_on() {
		return lat_on;
	}

	public void setLat_on(double lat_on) {
		this.lat_on = lat_on;
	}

	public double getLon_on() {
		return lon_on;
	}

	public void setLon_on(double lon_on) {
		this.lon_on = lon_on;
	}

	public String getTime_get_on() {
		return time_get_on;
	}

	public void setTime_get_on(String time_get_on) {
		this.time_get_on = time_get_on;
	}

	public double getLat_off() {
		return lat_off;
	}

	public void setLat_off(double lat_off) {
		this.lat_off = lat_off;
	}

	public double getLon_off() {
		return lon_off;
	}

	public void setLon_off(double lon_off) {
		this.lon_off = lon_off;
	}

	public String getTime_get_off() {
		return time_get_off;
	}

	public void setTime_get_off(String time_get_off) {
		this.time_get_off = time_get_off;
	}

	public String getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}

	public int getService_eval_idx() {
		return service_eval_idx;
	}

	public void setService_eval_idx(int service_eval_idx) {
		this.service_eval_idx = service_eval_idx;
	}

	public int getRun_odometer() {
		return run_odometer;
	}

	public void setRun_odometer(int run_odometer) {
		this.run_odometer = run_odometer;
	}

	public int getEmpty_odometer() {
		return empty_odometer;
	}

	public void setEmpty_odometer(int empty_odometer) {
		this.empty_odometer = empty_odometer;
	}

	public int getFuel_surcharge() {
		return fuel_surcharge;
	}

	public void setFuel_surcharge(int fuel_surcharge) {
		this.fuel_surcharge = fuel_surcharge;
	}

	public String getTime_wait() {
		return time_wait;
	}

	public void setTime_wait(String time_wait) {
		this.time_wait = time_wait;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public int getIc_flag() {
		return ic_flag;
	}

	public void setIc_flag(int ic_flag) {
		this.ic_flag = ic_flag;
	}

	@Override
	public String toString() {
		return "Czc [id=" + id + ", number=" + number + ", lat_on=" + lat_on
				+ ", lon_on=" + lon_on + ", time_get_on=" + time_get_on
				+ ", lat_off=" + lat_off + ", lon_off=" + lon_off
				+ ", time_get_off=" + time_get_off + ", employee_id="
				+ employee_id + ", service_eval_idx=" + service_eval_idx
				+ ", run_odometer=" + run_odometer + ", empty_odometer="
				+ empty_odometer + ", fuel_surcharge=" + fuel_surcharge
				+ ", time_wait=" + time_wait + ", income=" + income
				+ ", ic_flag=" + ic_flag + "]";
	}
	
	/**
	 * @return JSONObject
	 * @throws JSONException
	 * 转成JSONObject
	 */
	public JSONObject toJSON() throws JSONException {
		JSONObject j = new JSONObject();
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("number", number);
		json.put("lat_on", lat_on);
		json.put("lon_on", lon_on);
		json.put("time_get_on", time_get_on);
		json.put("lat_off", lat_off);
		json.put("lon_off", lon_off);
		json.put("time_get_off", time_get_off);
		json.put("employee_id", employee_id);
		json.put("service_eval_idx", service_eval_idx);
		json.put("run_odometer", run_odometer);
		json.put("empty_odometer", empty_odometer);
		json.put("fuel_surcharge", fuel_surcharge);
		json.put("time_wait", time_wait);
		json.put("income", income);
		json.put("ic_flag", ic_flag);
		j.put("czc", json);
		return j;
	}
	
}
