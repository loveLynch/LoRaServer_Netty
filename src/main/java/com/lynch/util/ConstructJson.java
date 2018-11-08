package com.lynch.util;



import com.lynch.domain.DownInfoA_LoRa;
import com.lynch.domain.DownInfoForm;
import net.sf.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


public class ConstructJson {
	
	public static String ToJsonStr(DownInfoForm info){
		JSONObject jsonObj = new JSONObject();
		Map <String, Object> element = new HashMap <String, Object>();
		try {
			Field[] fields = (info.getClass()).getDeclaredFields();
			for(Field field : fields){
				field.setAccessible(true);
				if(field.get(info)!=null&&!"".equals(field.get(info).toString())){
					element.put(field.getName(), field.get(info));
//		            System.out.print(field.getName() + " ");
		        }
			}		
			System.out.println();
			// 由于下行 json 字段只有一种类型，这里可以将其写死成 txpk
			jsonObj.put("txpk", element);
			return jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception{
		// TODO 测试 json 字符串的封装
		DownInfoA_LoRa A = new DownInfoA_LoRa();
		A.setImme(true);
		A.setFreq((float) 864.123456);
		A.setRfch(0);
		A.setPowe(14);
		A.setModu("LoRa");
		A.setDatr("SF11BW125");
		A.setCodr("4/6");
		A.setIpol(false);
		A.setSize(32);
		A.setData("H3P3N2i9qc4yt7rK7ldqoeCVJGBybzPY5h1Dd7P7p8v");
		
		String str = ConstructJson.ToJsonStr(A);
		System.out.println(str);
		System.out.println();
		
	}
}
