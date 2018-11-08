package com.lynch.domain;

import java.lang.reflect.Field;

public class InfoPktErrorForm implements UpInfoForm{

	public int error;

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	@Override
	public void saveData() {
		// TODO Auto-generated method stub
		System.out.println("Error");
	}

	@Override
	public byte[] getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSysInfo() {
		String sysInfo = "{";
		try {
			Field[] fields = this.getClass().getDeclaredFields();
			for(Field field : fields){
				field.setAccessible(true);
				if(field.get(this)!=null&&!"".equals(field.get(this).toString()) && (!field.getName().equals("data"))){
		            sysInfo = sysInfo + "\""+ field.getName() + "\":" + field.get(this) + ",";
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