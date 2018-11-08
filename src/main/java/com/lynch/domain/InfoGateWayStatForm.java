package com.lynch.domain;

import java.lang.reflect.Field;
import java.util.Date;

public class InfoGateWayStatForm implements UpInfoForm {

    public String time;
    public int lati;
    public int longe;
    public int alti;
    public int rxnb;
    public int rxok;
    public int rxfw;
    public int ackr;
    public int dwnb;
    public int txnb;



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLati() {
        return lati;
    }

    public void setLati(int lati) {
        this.lati = lati;
    }

    public int getLonge() {
        return longe;
    }

    public void setLonge(int longe) {
        this.longe = longe;
    }

    public int getAlti() {
        return alti;
    }

    public void setAlti(int alti) {
        this.alti = alti;
    }

    public int getRxnb() {
        return rxnb;
    }

    public void setRxnb(int rxnb) {
        this.rxnb = rxnb;
    }

    public int getRxok() {
        return rxok;
    }

    public void setRxok(int rxok) {
        this.rxok = rxok;
    }

    public int getRxfw() {
        return rxfw;
    }

    public void setRxfw(int rxfw) {
        this.rxfw = rxfw;
    }

    public int getAckr() {
        return ackr;
    }

    public void setAckr(int ackr) {
        this.ackr = ackr;
    }

    public int getDwnb() {
        return dwnb;
    }

    public void setDwnb(int dwnb) {
        this.dwnb = dwnb;
    }

    public int getTxnb() {
        return txnb;
    }

    public void setTxnb(int txnb) {
        this.txnb = txnb;
    }

    public InfoGateWayStatForm() {
    }

    @Override
    public void saveData() {
        // TODO Auto-generated method stub
        System.out.println("GateWay");
    }

    @Override
    public byte[] getData() {
        return null;
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

    @Override
    public String getModu() {
        // TODO Auto-generated method stub
        return null;
    }


}