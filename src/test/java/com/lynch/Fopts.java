package com.lynch;

/**
 * Created by lynch on 2018/7/4. <br>
 **/
public class Fopts {
    static int RFU;
    static int Perioditity;
    static int DataRate;

    byte[] ConvertToByte() {
        byte[] fopts = new byte[1];
        fopts[0] = 0x00;
        fopts[0] = (byte) (RFU << 7 | fopts[0]);
        fopts[0] = (byte) (Perioditity << 4 | fopts[0]);
        fopts[0] = (byte) (DataRate | fopts[0]);
        return fopts;
    }


    public static void setFopts(byte opts) {
        DataRate = opts & 0x0f;

        Perioditity = (opts & 0x70) >> 4;

        RFU = (opts & 0x80) >> 7;
    }

    public static void main(String[] args) {
        byte ret = 0x7f;
        setFopts(ret);
        System.out.println(RFU);
        System.out.println(DataRate);
        System.out.println(Perioditity);
    }
}

