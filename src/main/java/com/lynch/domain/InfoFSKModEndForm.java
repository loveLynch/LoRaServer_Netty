package com.lynch.domain;

import java.lang.reflect.Field;
import java.util.Date;

public class InfoFSKModEndForm implements UpInfoForm {

    private String time;
    private float tmms;
    private double tmst;
    private int freq;
    private int chan;
    private int rfch;
    private int stat;
    private String modu;
    private int datr_fsk;
    private int rssi;
    private int size;
    private byte[] data = null;


    public InfoFSKModEndForm() {

    }

    public float getTmms() {
        return tmms;
    }

    public void setTmms(float tmms) {
        this.tmms = tmms;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTmst() {
        return tmst;
    }

    public void setTmst(int tmst) {
        this.tmst = tmst;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public int getChan() {
        return chan;
    }

    public void setChan(int chan) {
        this.chan = chan;
    }

    public int getRfch() {
        return rfch;
    }

    public void setRfch(int rfch) {
        this.rfch = rfch;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public String getModu() {
        return modu;
    }

    public void setModu(String modu) {
        this.modu = modu;
    }

    public int getDatr_fsk() {
        return datr_fsk;
    }

    public void setDatr_fsk(int datr_fsk) {
        this.datr_fsk = datr_fsk;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public byte[] getData() {
        return this.data;
    }

    @Override
    public void saveData() {
        // TODO Auto-generated method stub
        System.out.println("FSK");
    }

    @Override
    public String getSysInfo() {
        String sysInfo = "{";
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(this) != null && !"".equals(field.get(this).toString()) && (!field.getName().equals("data"))) {
                    sysInfo = sysInfo + "\"" + field.getName() + "\":" + field.get(this) + ",";
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sysInfo.substring(0, sysInfo.length() - 1) + "}";
    }

}