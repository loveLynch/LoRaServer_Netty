package com.lynch.mac;

public class MacJoinRequestForm implements MacPktForm {
	public byte[] AppEui = new byte[8];
    public byte[] DevEui = new byte[8];
    public byte[] DevNonce = new byte[2];
	public byte[] getAppEui() {
		return AppEui;
	}
	public void setAppEui(byte[] appEui) {
		AppEui = appEui;
	}
	public byte[] getDevEui() {
		return DevEui;
	}
	public void setDevEui(byte[] devEui) {
		DevEui = devEui;
	}
	public byte[] getDevNonce() {
		return DevNonce;
	}
	public void setDevNonce(byte[] devNonce) {
		DevNonce = devNonce;
	}
	@Override
	public byte[] MacPkt2Byte() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getDevAddr() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getEndType() {
		// TODO Auto-generated method stub
		return 0;
	}
    
}
