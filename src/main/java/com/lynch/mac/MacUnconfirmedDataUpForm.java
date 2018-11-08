package com.lynch.mac;

import java.lang.reflect.Field;

public class MacUnconfirmedDataUpForm implements MacPktForm {
	public byte[] DevAddr = new byte[4];
	public Fctrl fctrl = new Fctrl();
	public byte[] Fcnt = new byte[2];
	public byte[] Fopts;
	public byte[] Fport = new byte[1];
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
		int ADRACKReq;
		int ACK;
		int RFU;
		int FOptslen;
		byte[] ConvertToByte(){
			byte[]  fctrl = new byte[1];
			fctrl[0] = (byte) 0x00 ;
			fctrl[0] = (byte) ((ADR<<7) | fctrl[0]);
			fctrl[0] = (byte) ((ADRACKReq<<6) | fctrl[0]);
			fctrl[0] = (byte) ((ACK<<5) | fctrl[0]);
			fctrl[0] = (byte) ((RFU<<4) | fctrl[0]);
			fctrl[0] = (byte) (FOptslen | fctrl[0]);
			return fctrl;
		}

		public void setFctrl(byte fcrl) {
            ADR = (fcrl & 0x80) >> 7;
            ADRACKReq = (fcrl & 0x40) >> 6;
            ACK = (fcrl & 0x20) >> 5;
            RFU = (fcrl & 0x10) >> 4;
            FOptslen = (fcrl & 0x0f);
		}

		int getLength(){
			return 1 ;
		}
	}
//new add 2018.6.5
    public class Fopts
    {
        int RFU;
        int Perioditity;
        int DataRate;
        byte[] ConvertToByte()
        {
            byte[]  fopts = new byte[1];
            fopts[0] = 0x00;
            fopts[0] = (byte) (RFU<<7 | fopts[0]);
            fopts[0] = (byte) (Perioditity<<4 | fopts[0]);
            fopts[0] = (byte) (DataRate | fopts[0]);
            return fopts;
        }


        public void setFopts(byte opts)
        {
            DataRate = opts & 0x0f;

            Perioditity = (opts & 0x70) >> 4;

            RFU = (opts & 0x80) >>7;
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
