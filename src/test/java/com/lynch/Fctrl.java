package com.lynch;

/**
 * Created by lynch on 2018/7/4. <br>
 **/
public class Fctrl {
    static int ADR;
    static int ADRACKReq;
    static int ACK;
    static int RFU;
    static int FOptslen;

    byte[] ConvertToByte() {
        byte[] fctrl = new byte[1];
        fctrl[0] = (byte) 0x00;
        fctrl[0] = (byte) ((ADR << 7) | fctrl[0]);
        fctrl[0] = (byte) ((ADRACKReq << 6) | fctrl[0]);
        fctrl[0] = (byte) ((ACK << 5) | fctrl[0]);
        fctrl[0] = (byte) ((RFU << 4) | fctrl[0]);
        fctrl[0] = (byte) (FOptslen | fctrl[0]);
        return fctrl;
    }

    public static void setFctrl(byte fcrl) {
        ADR = (fcrl & 0x80) >> 7;
        ADRACKReq = (fcrl & 0x40) >> 6;
        ACK = (fcrl & 0x20) >> 5;
        RFU = (fcrl & 0x10) >> 4;
        FOptslen = (fcrl & 0x0f);
    }

    public static void main(String[] args) {
        byte ret = 0x12;
        setFctrl(ret);
        System.out.println(RFU);

    }

}
