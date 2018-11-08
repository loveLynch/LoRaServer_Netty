package com.lynch.domain;


import com.lynch.LoRaServer;
import com.lynch.util.ClassMod;
import com.lynch.util.base64.base64__;

public class DownInfoA_FSK implements DownInfoForm{

	/**
	 * class A 的各项 JSON 数据
	 */
	private boolean imme;
	private double tmst;
	private boolean ncrc;
	private float freq;
	private int rfch;
	private int powe;
	private String modu;
	private int datr;
	private int fdev;
	private int prea;
	private int size;
	private String data;

	@Override
	public DownInfoForm ConstructDownInfo(UpInfoForm info, byte[] data, int type) {
		InfoLoraModEndForm infoLoraModEndForm = (InfoLoraModEndForm) info;
		DownInfoA_LoRa downInfoA_LoRa = new DownInfoA_LoRa();
		downInfoA_LoRa.setImme(false);
		downInfoA_LoRa.setData(base64__.encode(data));
		if (type == 0) {
			System.out.println("\n\nJoinRequest\n\n");
			downInfoA_LoRa.setTmst(infoLoraModEndForm.getTmst() + 5000000);
			downInfoA_LoRa.setNcrc(false);
			downInfoA_LoRa.setFreq(infoLoraModEndForm.getFreq());
			downInfoA_LoRa.setRfch(0);
			downInfoA_LoRa.setPowe(0);
			downInfoA_LoRa.setModu("LORA");
			downInfoA_LoRa.setDatr(infoLoraModEndForm.getDatr_lora());
			downInfoA_LoRa.setCodr(infoLoraModEndForm.getCodr());
			downInfoA_LoRa.setIpol(false);
			downInfoA_LoRa.setPrea(8);
			downInfoA_LoRa.setSize(data.length);
			return downInfoA_LoRa;


		} else {
			downInfoA_LoRa.setTmst(infoLoraModEndForm.getTmst() + 1000000);
			downInfoA_LoRa.setNcrc(false);
			downInfoA_LoRa.setFreq(infoLoraModEndForm.getFreq());
			downInfoA_LoRa.setRfch(0);
			downInfoA_LoRa.setPowe(0);
			downInfoA_LoRa.setModu("LORA");
			downInfoA_LoRa.setDatr(infoLoraModEndForm.getDatr_lora());
			downInfoA_LoRa.setCodr(infoLoraModEndForm.getCodr());
			downInfoA_LoRa.setIpol(false);
			downInfoA_LoRa.setPrea(8);
			downInfoA_LoRa.setSize(data.length);
			if (LoRaServer.classMod == ClassMod.Class_C) {
				downInfoA_LoRa.setFreq((float) 434.665);
				downInfoA_LoRa.setDatr("SF12BW125");
			}
			return downInfoA_LoRa;
		}

	}

	public boolean isImme() {
		return imme;
	}

	public void setImme(boolean imme) {
		this.imme = imme;
	}

	public double getTmst() {
		return tmst;
	}

	public void setTmst(double tmst) {
		this.tmst = tmst;
	}

	public boolean isNcrc() {
		return ncrc;
	}

	public void setNcrc(boolean ncrc) {
		this.ncrc = ncrc;
	}

	public float getFreq() {
		return freq;
	}

	public void setFreq(float freq) {
		this.freq = freq;
	}

	public int getRfch() {
		return rfch;
	}

	public void setRfch(int rfch) {
		this.rfch = rfch;
	}

	public int getPowe() {
		return powe;
	}

	public void setPowe(int powe) {
		this.powe = powe;
	}

	public String getModu() {
		return modu;
	}

	public void setModu(String modu) {
		this.modu = modu;
	}

	public int getDatr() {
		return datr;
	}

	public void setDatr(int datr) {
		this.datr = datr;
	}

	public int getFdev() {
		return fdev;
	}

	public void setFdev(int fdev) {
		this.fdev = fdev;
	}

	public int getPrea() {
		return prea;
	}

	public void setPrea(int prea) {
		this.prea = prea;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}


}
