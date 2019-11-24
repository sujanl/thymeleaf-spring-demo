package com.sujan.thymleafdemo.utils;

import java.security.SecureRandom;

public class PasswordUtils {
	private static final String ALLCHARS ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*(){}_+=-<>?[]'.,/";
	private static SecureRandom sr = new SecureRandom();
	
	/**
	 * generates a string(ie, password) for the given size
	 * @param size
	 * @return String
	 */
	public String generatePassword(int size) {
		StringBuilder sb = new StringBuilder(size);
		for(int i = 0; i < size; i++) {
			sb.append(ALLCHARS.charAt(sr.nextInt(ALLCHARS.length())));
		}
		return sb.toString();
	}

}
