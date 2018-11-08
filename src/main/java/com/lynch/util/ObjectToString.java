package com.lynch.util;

import java.util.ArrayList;

public class ObjectToString {

	public static String ArrayToStr(ArrayList<String> list)
	{
		String ret = "";
		for (String s : list)
		{
			ret = ret + s;
		}

		return ret;

	}
	public static String byteToStr(byte[] buffer)
	{
		String ret = "";
		for (int i = 0; i < buffer.length; i++) {   
	        String hex = Integer.toHexString(buffer[i] & 0xFF);   
	        if (hex.length() == 1 )   
	                hex = '0' + hex;
		    ret = ret + hex;
		}  	
		return ret;
	}
	
	public static String byteToStrWith(byte[] buffer) {
		String ret = "";
		for (int i = 0; i < buffer.length; i++) {   
	        String hex = Integer.toHexString(buffer[i] & 0xFF);   
	        if (hex.length() == 1 )   
	                hex = '0' + hex;
	        if((i != buffer.length - 1))
				hex = hex + ":";
		    ret = ret + hex;
		}  	
		return ret;
	}
	
	public static void main(String[] args) {
		byte[] aBlock = {
				0x01, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
		System.out.println(byteToStr(aBlock));
	}
	
}
