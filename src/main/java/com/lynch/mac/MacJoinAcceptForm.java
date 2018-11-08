package com.lynch.mac;

import java.lang.reflect.Field;

public class MacJoinAcceptForm implements MacPktForm {
	public byte[] AppNonce = new byte[3];
    public byte[] NetId = new byte[3];
    public byte[] DevAddr = new byte[4];
    public Dlsetting dlset = new Dlsetting();
    byte[] RxDelay = new byte[1];
    byte[] CfList;
//    int mtype;
    
    public class Dlsetting{
    	int RFU;
    	int RX1DRoffset;
    	int Rx2DataRate;
    	
    	byte[] ConvertToByte(){
    		byte[] dlsetting = new byte[1];
    		dlsetting[0] = (byte) 0x00;
    		dlsetting[0] = (byte) ((RFU<<7) | dlsetting[0]);
    		dlsetting[0] = (byte) ((RX1DRoffset<<4) | dlsetting[0]);
    		dlsetting[0] = (byte) (Rx2DataRate | dlsetting[0]);
    		return dlsetting;
    	}
    	int getLength(){
    		return 1;
    	}
    }

	@Override
	public byte[] MacPkt2Byte() {
		int i = 0;
		byte[] output = new byte[this.getLength()];
		try {
			Field[] fields = this.getClass().getDeclaredFields();
			for(Field field : fields){
				field.setAccessible(true);
				if(field.get(this)!=null&&!"".equals(field.get(this).toString())){
		            if((field.get(this)) instanceof Dlsetting){
		            	System.arraycopy(((Dlsetting)field.get(this)).ConvertToByte(),
		            			0, output, i,
		            			((Dlsetting)field.get(this)).getLength());
		            	i = i + ((Dlsetting)field.get(this)).getLength();
		            	
		            } else if (field.get(this) == null ){
						continue;
					} else {
		            	System.arraycopy(field.get(this), 
		            			0, output, i, 
		            			((byte[])field.get(this)).length);
		            	i = i + ((byte[])field.get(this)).length;
		            }
//		            System.out.println("change to byte[]..."+ field.getName());
		        }
			}	
//			System.out.println("change to byte[] success!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}


	@Override
	public int getLength() {
		return (this.AppNonce.length + this.DevAddr.length + 
				this.NetId.length + this.dlset.getLength() 
				+ this.RxDelay.length + (this.CfList == null ? 0 : this.CfList.length));
	}


	@Override
	public byte[] getDevAddr() {
		return DevAddr;
	}


	@Override
	public int getEndType() {
		// TODO Auto-generated method stub
		return 0;
	}
    
}
