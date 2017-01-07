package com.bmaxtech.utility;

import org.eclipse.jetty.server.Server;

import com.bmaxtech.entity.Constants;

public class WebSocketServer{	
	
	public WebSocketServer() {
		try {
			System.out.println("OK");
			
        	Server server = new Server(Constants.PORT_NO);
            server.setHandler(new ResourceEndPoint());
            server.setStopTimeout(0);
			server.start(); 
			//server.join();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
}
