package com.bmaxtech.utility;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.bmaxtech.actions.ActionsListener;
import com.bmaxtech.actions.MouseListener;
import com.bmaxtech.actions.ScreenCapture;
import com.bmaxtech.entity.Constants;

@ClientEndpoint
public class ClientEndPoints {
//	private static CountDownLatch latch;
	private ActionsListener action = new ActionsListener();
//	public static Session mSession;
//	
//	private Logger LOGGER = Logger.getLogger(this.getClass().getName());
//	
//	@OnOpen
//	public void onOpen(Session session) {
//		// same as above
//		LOGGER.info("New Session session = "+session.hashCode());
//	}
//	
//	@OnMessage
//	public String onMessage(String message, Session session) {
//		// same as above
//		LOGGER.info("Message = "+message);
//		// perform action
//		this.sendMessage(this.doAction(message));
//		return message;
//	}
//	
//	@OnClose
//	public void onClose(Session session, CloseReason closeReason) {
//		LOGGER.info(String.format("Session %s close because of %s", session.getId(), closeReason));
//		latch.countDown();
//	}
//
//	
//	
//	public static void main( String[] args ) throws IOException
//	{
//		latch = new CountDownLatch(1);
//		ClientManager client = ClientManager.createClient();
//		try {
//			client.connectToServer(Main.class, new URI("ws://"+Constants.HOST_NAME+":"+Constants.PORT_NO));
//			latch.await();
//		} catch (DeploymentException e){
//			throw new RuntimeException(e);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		System.in.read();
//	}
//	
//	
//	
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
	
//	/**
//	 * Sends the message
//	 * @param message
//	 */
//	private void sendMessage(String message){
//		if(userSession != null){
//			try{
//				//mSession.getRemote().sendString(message);
//				userSession.getAsyncRemote().sendText(message);
//			}catch (Exception e) {
//				// TODO: handle exception
//				userSession.error("Send Message Error = "+e.getMessage());
//			}
//		}
//	}
	
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
	
	
	private static Session userSession = null;
	private static MessageHandler messageHandler;
	
	public ClientEndPoints(){}
	
	public ClientEndPoints(URI endpointURI) {
		try {
			WebSocketContainer container = ContainerProvider
			.getWebSocketContainer();
			container.connectToServer(this, endpointURI);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	     * Callback hook for Connection open events.
	     * 
	     * @param userSession
	     *            the userSession which is opened.
	     */
	@SuppressWarnings("static-access")
	@OnOpen
	public void onOpen(Session userSession) {
		this.userSession = userSession;
	}
	/**
	     * Callback hook for Connection close events.
	     * 
	     * @param userSession
	     *            the userSession which is getting closed.
	     * @param reason
	     *            the reason for connection close
	     */
	@SuppressWarnings("static-access")
	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		this.userSession = null;
	}
	/**
	     * Callback hook for Message Events. This method will be invoked when a
	     * client send a message.
	     * 
	     * @param message
	     *            The text message
	     */
	@SuppressWarnings("static-access")
	@OnMessage
	public void onMessage(String message) {
		if (this.userSession != null)
			doAction(message);
		//this.messageHandler.handleMessage(message);
	}
	/**
	     * register message handler
	     * 
	     * @param message
	     */
	@SuppressWarnings("static-access")
	public void addMessageHandler(MessageHandler msgHandler) {
		this.messageHandler = msgHandler;
	}
	/**
	     * Send a message.
	     * 
	     * @param user
	     * @param message
	     */
	@SuppressWarnings("static-access")
	public void sendMessage(String message) {
		this.userSession.getAsyncRemote().sendText(doAction(message));
	}
	/**
	     * Message handler.
	     * 
	     * @author Jiji_Sasidharan
	     */
	public static interface MessageHandler {
		public void handleMessage(String message);
	}
	
}
