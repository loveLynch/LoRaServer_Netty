package com.lynch.util.phy;

public class Mhdr {
	private int Mtype;
	private int RFU;
	private int Major;
	
	public Mhdr(int mtype, int rFu, int major) {
		this.setMtype(mtype);
		this.setRFU(rFu);
		this.setMajor(major);
	}
	
	byte[] MhdrPktToByte() {
		byte[]  mhdr = new byte[1];
		mhdr[0] = (byte) 0x00 ;
		mhdr[0] = (byte) ((this.getMtype() << 5) | mhdr[0]);
		mhdr[0] = (byte) ((this.getRFU() << 2) | mhdr[0]);
		mhdr[0] = (byte) ((this.getMajor()) | mhdr[0]);
		return mhdr;
	}

	/*
	byte[] MacPktToByte() {
		int i = 0;
		byte[] output = new byte[this.getLength()];
		try {
			Field[] fields = this.getClass().getDeclaredFields();
			for(Field field : fields){
				field.setAccessible(true);
				if(field.get(this)!=null&&!"".equals(field.get(this).toString())){
		        {
		            	System.arraycopy((byte[])field.get(this), 
		            			0, output, i, 
		            			((byte[])field.get(this)).length);
		            	i = i + ((byte[])field.get(this)).length;
		            }
		            System.out.println(field.getName());
		        }
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}*/

	int getLength(){
		return 1 ;
	}

	public int getMtype() {
		return Mtype;
	}

	public void setMtype(int mtype) {
		Mtype = mtype;
	}

	public int getRFU() {
		return RFU;
	}

	public void setRFU(int rFU) {
		RFU = rFU;
	}

	public int getMajor() {
		return Major;
	}

	public void setMajor(int major) {
		Major = major;
	}
    
	public static void main(String[] args) {
		Mhdr mhdr = new Mhdr(5, 0, 0);
	}
	
}
