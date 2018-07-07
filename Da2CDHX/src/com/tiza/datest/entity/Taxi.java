package com.tiza.datest.entity;

public class Taxi {

	public int suid;    //平台SUID
    public long utc;     //业务上车时间(当前车次)
    public long onlon;   //上车经度
    public long onlat;   //上车纬度
    public long onutc;   //上车时间
    public long offlon;  //下车经度
    public long offlat;  //下车纬度
    public long offutc;  //下车时间
    public int unitprice;//单价 （分）
    public int waittime; //等待时间（秒）
    public int distance; //载客里程(公里)
    public int empty_distance;  //空载里程(公里)
    public int price;    //打车费金额  (角)
    public int bpos;     //BPOS 0-现金 1-刷卡
    public String managementid;//从业资格证号
    public int serviceevalidx;//评价选项
    public int fuelsurcharge;//燃油附加费
    
}
