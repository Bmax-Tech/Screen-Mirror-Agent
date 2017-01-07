package com.bmaxtech.utility;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.json.JSONObject;

import com.bmaxtech.actions.ActionsListener;
import com.bmaxtech.actions.MouseListener;
import com.bmaxtech.actions.ScreenCapture;
import com.bmaxtech.entity.Constants;

@WebSocket
public class ResourceEndPoint extends WebSocketHandler{
	/** LOGGER **/
	final static Logger LOGGER = Logger.getLogger(ResourceEndPoint.class);	
	
	private ActionsListener action = new ActionsListener();
	private static Session mSession;
	private static ArrayList<ResourceEndPoint> sessions = new ArrayList<ResourceEndPoint>();
	public static ArrayList<ResourceEndPoint> getAllSessions(){
		return sessions;
	}
	
	
	@OnWebSocketClose
	public void onClose(int statusCode, String reason){
		sessions.remove(this);
		LOGGER.info("ClosedL statusCode = "+statusCode+", reason = "+ reason);
	}
	
	@OnWebSocketError
	public void onError(Throwable t){
		LOGGER.error("Error = "+t.getMessage());
	}
	
	@OnWebSocketConnect
	public void onConnect(Session session){
		mSession = session;
		sessions.add(this);
		LOGGER.info("New Session session = "+session.hashCode());
	}
	
	@OnWebSocketMessage
	public void onMessage(String message){
		LOGGER.info("Message = "+message);
		// perform action
		this.sendMessage(this.doAction(message));
	}

	@Override
	public void configure(WebSocketServletFactory arg0) {
		// TODO Auto-generated method stub
		arg0.register(ResourceEndPoint.class);
	}
	
	
	
	/**
	 * ------------------------------------
	 *  Custom Methods
	 * ------------------------------------ 
	 */
	
	private String doAction(String data){
		JSONObject ob = new JSONObject(data);
		if(ob.getString("message").equals("image")){
			// if Message request image
			if(action.getImageListSize() > 0){
				return this.ImageToBase64(action.getPendingImage(), Constants.IMAGE_FORMAT);
			}
		} else if (ob.getString("message").equals("startCapturing")) {
			// start capturing images
			new ScreenCapture().startThread();
			return "STARTED";
		} else if (ob.getString("message").equals("pauseCapturing")) {
			// start capturing images
			new ScreenCapture().pauseThread();
			return "PAUSED";
		} else if (ob.getString("message").equals("stopCapturing")) {
			// start capturing images
			new ScreenCapture().stopThread();
			return "STOPED";
		} else if (ob.getString("message").equals("mouseClick")) {
			// start capturing images
			new MouseListener().performAction(ob.getJSONObject("action"));
			return "ACTION PERFORMED";
		}
		return null;
	}
	
	/**
	 * Sends the message
	 * @param message
	 */
	private void sendMessage(String message){
		if(mSession != null){
			try{
				mSession.getRemote().sendString(message);
			}catch (Exception e) {
				// TODO: handle exception
				LOGGER.error("Send Message Error = "+e.getMessage());
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public String ImageToBase64(BufferedImage image,String type)
	{
		String imageString = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
 
        try {
            ImageIO.write(image, type, output);
            byte[] imageBytes = output.toByteArray();
 
            Base64 encoder = new Base64();
            imageString = encoder.encodeBase64String(imageBytes);
 
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
	}
}
