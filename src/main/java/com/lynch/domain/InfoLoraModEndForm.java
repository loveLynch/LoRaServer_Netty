package com.lynch.domain;

import java.lang.reflect.Field;
import java.util.Date;

public class InfoLoraModEndForm implements UpInfoForm {

    private String time;
    private float tmms;
    private double tmst;
    private float freq;
    private int chan;
    private int rfch;
    private int stat;
    private String modu;
    private String datr_lora;
    private String codr;
    private int rssi;
    private int lsnr;
    private int size;
    private byte[] data = null;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getTmms() {
        return tmms;
    }

    public void setTmms(float tmms) {
        this.tmms = tmms;
    }

    public void setTmst(double tmst) {
        this.tmst = tmst;
    }

    public double getTmst() {
        return tmst;
    }

    public void setTmst(int tmst) {
        this.tmst = tmst;
    }

    public float getFreq() {
        return freq;
    }

    public void setFreq(float freq) {
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

    public String getDatr_lora() {
        return datr_lora;
    }

    public void setDatr_lora(String datr_lora) {
        this.datr_lora = datr_lora;
    }

    public String getCodr() {
        return codr;
    }

    public void setCodr(String codr) {
        this.codr = codr;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getLsnr() {
        return lsnr;
    }

    public void setLsnr(int lsnr) {
        this.lsnr = lsnr;
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
        System.out.println("Lora");
    }

    @Override
    public String getSysInfo() {
        String sysInfo = "{";
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(this) != null && !"".equals(field.get(this).toString()) && (!field.getName().equals("data"))) {
                    sysInfo = sysInfo + "\"" + field.getName() + "\":\"" + field.get(this) + "\",";
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sysInfo.substring(0, sysInfo.length() - 1) + "}";
    }

    public static void main(String[] args) {
        InfoLoraModEndForm infoLoraModEndForm = new InfoLoraModEndForm();
        infoLoraModEndForm.chan = 0;
        infoLoraModEndForm.codr = "31";
        byte[] bs = {0x00, 0x01};
        infoLoraModEndForm.data = bs;
        infoLoraModEndForm.datr_lora = "31";
        infoLoraModEndForm.lsnr = 31231;
        infoLoraModEndForm.modu = "lora";
        System.out.println(infoLoraModEndForm.getSysInfo());


    }

}