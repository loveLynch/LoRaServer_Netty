package com.lynch.domain;


import com.lynch.util.base64.base64__;

public class DownInfoC_FSK implements DownInfoForm{
	
	/**
	 * class C 的各项 JSON 数据
	 */
	private boolean imme;
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
		InfoFSKModEndForm infoFSKModEndForm = (InfoFSKModEndForm) info;
		DownInfoC_FSK downInfoC_FSK = new DownInfoC_FSK();
		downInfoC_FSK.setImme(true);
		downInfoC_FSK.setNcrc(false);
		downInfoC_FSK.setFreq(infoFSKModEndForm.getFreq());
		downInfoC_FSK.setRfch(infoFSKModEndForm.getRfch());
		downInfoC_FSK.setPowe(0);
		downInfoC_FSK.setModu("FSK");
		downInfoC_FSK.setDatr(infoFSKModEndForm.getDatr_fsk());
		downInfoC_FSK.setFdev(0);	// TODO fdev
		downInfoC_FSK.setPrea(8);
		downInfoC_FSK.setSize(infoFSKModEndForm.getSize());
		downInfoC_FSK.setData(base64__.encode(data));
		return downInfoC_FSK;
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
