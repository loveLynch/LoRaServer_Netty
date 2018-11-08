package com.lynch.mac;

public interface MacPktForm {
	
	public byte[] MacPkt2Byte();
	public int getLength();
	public byte[] getDevAddr();
	
	int getEndType();
}
