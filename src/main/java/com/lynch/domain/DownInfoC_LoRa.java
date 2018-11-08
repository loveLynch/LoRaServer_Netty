package com.lynch.domain;


import com.lynch.util.base64.base64__;

public class DownInfoC_LoRa implements DownInfoForm{
	
	/**
	 * class C 的各项 JSON 数据
	 */
	private boolean imme;
	private boolean ncrc;
	private float freq;
	private int rfch;
	private int powe;
	private String modu;
	private String datr;
	private String codr;
	private boolean ipol;
	private int prea;
	private int size;
	private String data;	
	@Override
    public DownInfoForm ConstructDownInfo(UpInfoForm info, byte[] data, int type) {
		InfoLoraModEndForm infoLoraModEndForm = (InfoLoraModEndForm) info;
		DownInfoC_LoRa downInfoC_LoRa = new DownInfoC_LoRa();
		downInfoC_LoRa.setImme(true);
		downInfoC_LoRa.setNcrc(false);
		downInfoC_LoRa.setFreq(infoLoraModEndForm.getFreq());
		downInfoC_LoRa.setRfch(infoLoraModEndForm.getRfch());
		downInfoC_LoRa.setPowe(0);
		downInfoC_LoRa.setModu("LORA");
		downInfoC_LoRa.setDatr(infoLoraModEndForm.getDatr_lora());
		downInfoC_LoRa.setIpol(false);
		downInfoC_LoRa.setPrea(8);
		downInfoC_LoRa.setSize(infoLoraModEndForm.getSize());
		downInfoC_LoRa.setData(base64__.encode(data));
		return downInfoC_LoRa;
	}
	public boolean isImme() {
		return imme;
	}
	public void setImme(boolean imme) {
		this.imme = imme;
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
	public String getDatr() {
		return datr;
	}
	public void setDatr(String datr) {
		this.datr = datr;
	}
	public String getCodr() {
		return codr;
	}
	public void setCodr(String codr) {
		this.codr = codr;
	}
	public boolean isIpol() {
		return ipol;
	}
	public void setIpol(boolean ipol) {
		this.ipol = ipol;
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
