package com.bmaxtech.actions;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class ActionsListener {
	/** Image List **/
	private static LinkedList<BufferedImage> imageList = new LinkedList<BufferedImage>();
	
	public void addImage(BufferedImage image){
		imageList.add(image);
	}
	
	public int getImageListSize(){
		return imageList.size();
	}
	
	public BufferedImage getPendingImage(){
		BufferedImage image = imageList.getFirst();
		imageList.removeFirst();
		return image;
	}
	
	public void clearImageList(){
		imageList.clear();
	}
}
