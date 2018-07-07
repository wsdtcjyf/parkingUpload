package com.tiza.datest.comm;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract interface NeedReconnect {
	public abstract void setConnected(Socket paramSocket, InputStream paramInputStream, OutputStream paramOutputStream,
			boolean paramBoolean);

	public abstract void reTryToConnect();
}
