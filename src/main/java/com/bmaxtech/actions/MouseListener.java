package com.bmaxtech.actions;

import java.awt.Robot;
import java.awt.event.InputEvent;

import org.json.JSONObject;

public class MouseListener {
	private static Robot robot;
	
	public MouseListener(){
		if(robot == null){
			robot = new ScreenCapture().getRobot();
		}
	}
	
	/**
	 * Perform Mouse Action
	 * @param action
	 */
	public void performAction(JSONObject action){
		robot.mouseMove(action.getInt("x"), action.getInt("y"));
		if(action.getString("type").equals("LEFT")){
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		} else if(action.getString("type").equals("RIGHT")){
			robot.mousePress(InputEvent.BUTTON3_MASK);
			robot.mouseRelease(InputEvent.BUTTON3_MASK);
		}
	}
}
