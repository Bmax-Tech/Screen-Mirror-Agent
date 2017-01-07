package com.bmaxtech.screenmirror;

import java.net.URI;

import com.bmaxtech.utility.ClientEndPoints;

/**
 * Hello world!
 *
 */
public class Main 
{
//	 private static CountDownLatch latch;
//
//	 private Logger logger = Logger.getLogger(this.getClass().getName());
//
//	 @OnOpen
//	 public void onOpen(Session session) {
//	     // same as above
//	 }
//
//	 @OnMessage
//	 public String onMessage(String message, Session session) {
//	     // same as above
//		 return message;
//	 }
//
//	 @OnClose
//	 public void onClose(Session session, CloseReason closeReason) {
//	     logger.info(String.format("Session %s close because of %s", session.getId(), closeReason));
//	     latch.countDown();
//	 }
//
//	
//	
//    public static void main( String[] args ) throws IOException
//    {
////          new WebSocketServer();
//    	latch = new CountDownLatch(1);
//    	ClientManager client = ClientManager.createClient();
//        try {
//            client.connectToServer(Main.class, new URI("ws://localhost:7788"));
//            latch.await();
//
//        } catch (DeploymentException e){
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//          
//          System.in.read();
//    }
	
	/**
     * main
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new ClientEndPoints(new URI("ws://localhost:7788"));
//        clientEndPoint.addMessageHandler(new ClientEndPoints.MessageHandler() {
//                    public void handleMessage(String message) {
//                        JSONObject jsonObject = new JSONObject(message);
//                        String userName = jsonObject.getString("user");
//                        if (!"bot".equals(userName)) {
//                            clientEndPoint.sendMessage(getMessage("Hello " + userName +", How are you?"));
//                            // other dirty bot logic goes here.. :)
//                        }
//                    }
//                });
// 
//        while (true) {
//            clientEndPoint.sendMessage(getMessage("Hi There!!"));
//            Thread.sleep(30000);
//        }
        System.in.read();
    }
 
//    /**
//     * Create a json representation.
//     * 
//     * @param message
//     * @return
//     */
//    private static String getMessage(String message) {
//        JSONObject data = new JSONObject();
//        data.put("user", "bot");
//        data.put("message", message);
//    	
//    	return data.toString();
//    }
	
}
