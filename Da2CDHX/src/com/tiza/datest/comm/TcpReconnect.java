package com.tiza.datest.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

/**
 *  Tcp 连接、断线重连线程
 * 
 * @author  yanghj@gpssz.com
 *
 */
public class TcpReconnect extends Thread {
	private NeedReconnect needed = null;

	private Logger cat = null;

	/**
	 * 是否第一次连接
	 */
	private boolean isFirst = true;

	private int nTimeLong = 5000;

	public Socket ms = null;

	public InputStream mis = null;

	public OutputStream mos = null;

	public String strHost = null;

	public int nPort = 0;

	public boolean bRun = false;

	/**
	 * 
	 * @param vnr
	 *            tcp 连接类
	 * @param cat
	 *            日志输出类
	 * @param nTimeLong
	 *            重连间隔
	 */
	public TcpReconnect(NeedReconnect vnr, Logger cat, int nTimeLong) {
		this.needed = vnr;
		this.cat = cat;
		this.nTimeLong = nTimeLong;
		this.bRun = true;
	}

	public void tryToConnectServer() throws UnknownHostException, IOException {
		if ((this.strHost == null) || (this.strHost.compareTo("") == 0) || (this.nPort == 0)) {
			throw new UnknownHostException("error host or port");
		}

		this.ms = new Socket(this.strHost, this.nPort);
		
		this.mis = this.ms.getInputStream();
		this.mos = this.ms.getOutputStream();
		
		//返回信息
		/*if(null != mis){
			InputStreamReader is = new InputStreamReader(mis);
			BufferedReader br = new BufferedReader(is);
			String strs = br.readLine();
			this.cat.info("receive from tcp :"+strs);
		}else{
			this.cat.info("receive from tcp : null");
		}*/
		
		
		this.cat.info("connected to Server " + this.strHost + ":" + this.nPort);
	}
	
	@Override
	public void run() {
		this.cat.info("the value of bRun is :"+bRun);
		while (bRun) {
			if (!isFirst) {
				try {
					sleep(this.nTimeLong);
					this.cat.info("entry into !isFirst and will sleep");
				} catch (InterruptedException ie) {
					this.cat.warn("TcpReconnect sleep error:" + ie.getMessage());
				}
			}
			try {
				tryToConnectServer();
				this.needed.setConnected(this.ms, this.mis, this.mos, true);
				this.cat.info("entry into tryConnect set bRun : true");
				synchronized (this.needed) {
					this.needed.notify();
				}
				bRun = false;
			} catch (Exception e) {
				this.cat.warn(e.getMessage());
			} finally {
				//  设置非第一次连接
				isFirst = false;
			}
		}
	}
}
