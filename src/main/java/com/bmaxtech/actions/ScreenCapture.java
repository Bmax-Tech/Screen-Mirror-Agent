package com.bmaxtech.actions;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.bmaxtech.entity.Constants;
import com.bmaxtech.utility.ClientEndPoints;
import com.bmaxtech.utility.ResourceEndPoint;

public class ScreenCapture extends Thread{
	/** LOGGER **/
	final static Logger LOGGER = Logger.getLogger(ScreenCapture.class);	
	
	private static Thread thread;
	private static ActionsListener action = new ActionsListener();
	private static boolean isAlive = false;
	//private static ResourceEndPoint resourceEndPoint = new ResourceEndPoint();
	private static ClientEndPoints clientEndPoints = new ClientEndPoints();
	private static Robot robot;
	
	@Override
	public void run(){
		try {
			robot = new Robot();
		
			while(true){
				if(ScreenCapture.isAlive){
					try {
						// capture image
						this.captureImage(robot);
						
						// send Image
						JSONObject ob = new JSONObject();
						ob.put("message", "image");
						clientEndPoints.sendMessage(ob.toString());
						
						// sleeps the thread for IMAGE_SLEEP milliseconds
						Thread.sleep(Constants.IMAGE_SLEEP);
					} catch (InterruptedException e) {
						LOGGER.error("Image Capturing Thread = "+e.getMessage());
					} catch (AWTException e) {
						LOGGER.info("AWT Error = "+e.getMessage());
					}
				}
			}
		} catch (AWTException e1) {
			LOGGER.error("AWT parent error = "+e1.getMessage());
		}
	}	
	
	public void startThread(){
		if(!isAlive){
			thread = new Thread(this);
			thread.start();
		}
		isAlive = true;
	}
	
	public void pauseThread(){
		isAlive = false;
	}
	
	public void stopThread(){
		try{
		if(thread.isAlive()){
			isAlive = false;
			thread.interrupt();
		}
		}catch (Exception e) {
			LOGGER.error("Stop Thread Error = "+e.getMessage());
		}
	}
	
	public void captureImage(Robot robot) throws AWTException{		
		// Capture Desktop Image
		BufferedImage image = robot.createScreenCapture(new Rectangle(new Dimension(
				Toolkit.getDefaultToolkit().getScreenSize().width, 
				Toolkit.getDefaultToolkit().getScreenSize().height)));
		
		// Add new image
		action.addImage(image);	
	}
	
	public Robot getRobot(){
		return ScreenCapture.robot;
	}

}
