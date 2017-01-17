package com.cosmos.utils.network.tcp;

import java.io.IOException;

import com.cosmos.utils.network.tcp.TcpServer.TcpProcessor;

public class DefaultTcpProcessor implements TcpProcessor{

	@Override
	public void process(TcpClient client) {
		try {
//			try {
//				int readInt32 = client.getProcessor().readInt32();
//				System.out.println(readInt32);
//			} catch (SignedTypeFormatException e) {
//				e.printStackTrace();
//			}
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
