package com.catenoid.dashbd.util;

import org.apache.commons.codec.binary.Base64;

public class Base64Coder {
	public static String encode(String plain) {
		return Base64.encodeBase64String(plain.getBytes());
	}
	
	public static String decode(String chiper) {		
		return new String(Base64.decodeBase64(chiper));
	}
}
