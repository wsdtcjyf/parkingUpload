package com.tiza.datest.entity;

public class Taxi {

	public int suid;    //ƽ̨SUID
    public long utc;     //ҵ���ϳ�ʱ��(��ǰ����)
    public long onlon;   //�ϳ�����
    public long onlat;   //�ϳ�γ��
    public long onutc;   //�ϳ�ʱ��
    public long offlon;  //�³�����
    public long offlat;  //�³�γ��
    public long offutc;  //�³�ʱ��
    public int unitprice;//���� ���֣�
    public int waittime; //�ȴ�ʱ�䣨�룩
    public int distance; //�ؿ����(����)
    public int empty_distance;  //�������(����)
    public int price;    //�򳵷ѽ��  (��)
    public int bpos;     //BPOS 0-�ֽ� 1-ˢ��
    public String managementid;//��ҵ�ʸ�֤��
    public int serviceevalidx;//����ѡ��
    public int fuelsurcharge;//ȼ�͸��ӷ�
    
}
