package com.tiza.datest.comm;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.tiza.datest.util.DaConfig;


	public class TcpClient2 extends Thread implements NeedReconnect {

		private Socket paramSocket;
		private InputStream paramInputStream;
		private OutputStream paramOutputStream;
		private boolean bConn;

		private Logger log;
		private String tcpIp2;
		private int tcpPort2;


		private String username2;
		private String pass2;

		
		public TcpClient2(DaConfig cfg, Logger log) {
			this.log = log;
			this.tcpIp2 = cfg.theproperties.getProperty("tcp_ip2");
			this.tcpPort2 = Integer.parseInt(cfg.theproperties.getProperty("tcp_port2"));
			this.username2 = cfg.theproperties.getProperty("username2");
			this.pass2 = cfg.theproperties.getProperty("pass2");
		}


		
		@Override
		public void setConnected(Socket paramSocket, InputStream paramInputStream, OutputStream paramOutputStream,
				boolean paramBoolean) {
			this.paramSocket = paramSocket;
			this.paramInputStream = paramInputStream;
			this.paramOutputStream = paramOutputStream;
			this.bConn = paramBoolean;
			
			// �û���½
			String user = "AUTH"+" "+username2+" "+pass2+" \r\n";
			byte[] data = user.getBytes();
			send(data, data.length);

		}

		@Override
		public void reTryToConnect() {
			TcpReconnect tcpr = new TcpReconnect(this, log, 5000);
			tcpr.strHost = tcpIp2;
			tcpr.nPort = tcpPort2;
			tcpr.start();
		}

		@Override
		public void run() {
			// ��һ����
			reTryToConnect();
			
			byte[] buffer = new byte[512];
			while (true) {
				// �������δ�ɹ�����������ǰ�̣߳�֪��֪ͨ���ӳɹ�
				if (!bConn) {
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {

						}
					}
				} else {

					if (null != paramInputStream) {

						try {
							int n = paramInputStream.read(buffer);
							// ���ӶϿ�
							if (-1 == n) {
								connBroken();
								continue;
							}
							//
							String str = new String(buffer, 0, n);
							log.info("Receive from tcpServer:" + str);

						} catch (Exception e) {
							log.error(e, e);
							connBroken();
						}
					}
				}
			}
		}

		/**
		 * ��Tcp����˷�������
		 * 
		 * @param data
		 */
		public void send(byte[] data, int len) {
			if (bConn) {
				try {
					paramOutputStream.write(data, 0, len);
					paramOutputStream.flush();
				} catch (Exception e) {
					log.error(e, e);
					connBroken();
				}
			}
		}

		/**
		 * ���ӶϿ����ͷ���Դ
		 */
		private void connBroken() {

			if (bConn) {
				log.info("Tcp �����쳣�Ͽ�");
				bConn = false;
				try {
					paramInputStream.close();
				} catch (Exception e) {
				}

				try {
					paramOutputStream.close();
				} catch (Exception e) {
				}

				try {
					paramSocket.close();
				} catch (Exception e) {
				}
				// ��������
				reTryToConnect();
			}

		}

	

}
