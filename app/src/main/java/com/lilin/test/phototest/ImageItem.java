package com.lilin.test.phototest;

import android.graphics.Bitmap;

import java.io.IOException;
import java.io.Serializable;


public class ImageItem implements Serializable {
	public String imagePath;
	private Bitmap bitmap;

	public Bitmap getBitmap() {
		if(bitmap == null){
			try {
				bitmap = BitmapManager.revitionImageSize(imagePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	
	
}
