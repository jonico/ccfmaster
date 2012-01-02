package com.collabnet.ccf.ccfmaster.util;

import org.apache.commons.codec.binary.Base64;


public class Obfuscator {

	public static final String OBFUSCATED_PASSWORD_PREFIX = "OBF:"; //$NON-NLS-1$
	
	public static String encodePassword(String password) {
		String encodedPassword = password;
		if (encodedPassword != null && encodedPassword.length() > 0) {
			encodedPassword = OBFUSCATED_PASSWORD_PREFIX + encode(encodedPassword);
		}
		return encodedPassword;
	}	
	
	public static String decodePassword(String password) {
		String decodedPassword = password;
		if (decodedPassword != null && decodedPassword.startsWith(OBFUSCATED_PASSWORD_PREFIX)) {
			if (decodedPassword.length() > 4) {
				decodedPassword = decode(decodedPassword.substring(4));
			} else {
				decodedPassword = "";
			}
		}
		return decodedPassword;
	}
	
	public static String encode(String string) {
		if (string == null) {
			return null;
		}
		return Obfuscator.obfuscateString(string);
	}	
	
	private static String decode(String string) {
		if (string == null) {
			return null;
		}
		return Obfuscator.deObfuscateString(string);
	}
	
	
	
	public static String obfuscateString(String string) {
		byte[] bytes = string.getBytes();
		for (int i =0;i<bytes.length;++i) {
			bytes[i] = cyclicShiftBitsRight(bytes[i], 4);
		}
		
		return new String(Base64.encodeBase64(bytes));
	}
	
	private static byte cyclicShiftBitsRight(int b, int i) {
		if (b < 0) {
			b+=256;
		}
		int j = (((b >>> i) % 256) | (b << (8-i)));
		if (j > 127) {
			j-=256;
		}
		return (byte) j;
	}
	
	private static byte cyclicShiftBitsLeft(int b, int i) {
		if (b < 0) {
			b+=256;
		}
		int j = ((b << i) | ((b >>> (8-i)) % 256));
		if (j > 127) {
			j-=256;
		}
		return (byte) j;
	}

	public static String deObfuscateString(String string) {
		byte[] bytes;
		bytes = Base64.decodeBase64(string.getBytes());
		for (int i =0;i<bytes.length;++i) {
			bytes[i] = cyclicShiftBitsLeft(bytes[i], 4);
		}
		return new String(bytes);
	}
	
}
