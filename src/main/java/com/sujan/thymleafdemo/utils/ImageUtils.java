package com.sujan.thymleafdemo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.tomcat.util.codec.binary.Base64;

public class ImageUtils {
	/**
	 * decodes encoded base64 image string and save it
	 * @param encodedImage
	 * @return String
	 */
	public static String saveImage(String encodedImage) {
		String staticDir = "src/main/resources/static";
		String imgDir = staticDir+"/images";
		DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("ddmmyyyyhhmmss");
		LocalDateTime rightNow = LocalDateTime.now();
		String imgName = dtFormat.format(rightNow);
		//create directory
		if(!new File(staticDir).exists()) {
			new File(staticDir).mkdir();
		}
		if(!new File(imgDir).exists()) {
			new File(imgDir).mkdir();
		}
		File finalImg = new File(imgDir.concat(File.separator).concat(imgName).concat(".png"));
		try {
			FileOutputStream outputImg = new FileOutputStream(finalImg);
			byte[] imgByte =  Base64.decodeBase64(encodedImage);
			try {
				outputImg.write(imgByte);
				outputImg.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
		return "http://localhost:8080/images/"+imgName+".png";
	}
	
	
}
