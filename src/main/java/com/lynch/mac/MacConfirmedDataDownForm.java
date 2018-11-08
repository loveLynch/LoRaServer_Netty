package com.lynch.mac;

import java.lang.reflect.Field;

public class MacConfirmedDataDownForm implements MacPktForm{
	public byte[] DevAddr = new byte[4];
	public Fctrl fctrl = new Fctrl();
	public byte[] Fcnt = new byte[2];
	public byte[] Fopts;
	public byte[] Fport;
	public byte[] FRMPayload;
	@Override
	public byte[] MacPkt2Byte() {
		int i = 0;
		byte[] output = new byte[this.getLength()];
		try {
			Field[] fields = this.getClass().getDeclaredFields();
			for(Field field : fields){
				field.setAccessible(true);
				if(field.get(this)!=null&&!"".equals(field.get(this).toString())){
		            if((field.get(this)) instanceof Fctrl){
		            	System.arraycopy(((Fctrl)field.get(this)).ConvertToByte(),
		            			0, output, i,
		            			((Fctrl)field.get(this)).getLength());
		            	i = i + ((Fctrl)field.get(this)).getLength();
		            	
		            } else{
		            	System.arraycopy(field.get(this), 
		            			0, output, i, 
		            			((byte[])field.get(this)).length);
		            	i = i + ((byte[])field.get(this)).length;
		            }
//		            System.out.println(field.getName());
		        }
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	
	
	@Override
	public int getLength() {
		return (this.DevAddr.length + this.fctrl.getLength() + 
				this.Fcnt.length + this.Fopts.length 
				+ this.Fport.length + this.FRMPayload.length);
	}
	
	public class Fctrl{
		int ADR;
		int RFU;
		int ACK;
		int FPending;
		int FOptslen;
		byte[] ConvertToByte(){
			byte[]  fctrl = new byte[1];
			fctrl[0] = (byte) 0x00 ;
			fctrl[0] = (byte) ((ADR<<7) | fctrl[0]);
			fctrl[0] = (byte) ((RFU<<6) | fctrl[0]);
			fctrl[0] = (byte) ((ACK<<5) | fctrl[0]);
			fctrl[0] = (byte) ((FPending<<4) | fctrl[0]);
			fctrl[0] = (byte) (FOptslen | fctrl[0]);
			return fctrl;
		}
		int getLength(){
			return 1 ;
		}
	}

	@Override
	public byte[] getDevAddr() {
		return DevAddr;
	}



	@Override
	public int getEndType() {
		// TODO Auto-generated method stub
		return fctrl.RFU;
	};

}
