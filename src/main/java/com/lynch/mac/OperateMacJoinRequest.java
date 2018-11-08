package com.lynch.mac;



import com.lynch.util.ObjectToString;
import com.lynch.util.RedisDB;
import com.lynch.util.aes.LoRaMacCrypto;
import com.lynch.util.base64.base64__;

import java.util.Arrays;

public class OperateMacJoinRequest implements OperateMacPkt {


	public	MacPktForm MacParseData(byte[] data, String gatawayId, String syscontent) {
		byte[] datain = Arrays.copyOfRange(data, 0, 19);		
		byte[] micComputed = LoRaMacCrypto.LoRaMacJionRequestComputeMic(datain, datain.length, LoRaMacCrypto.APPKEY);
		byte[] micReceived = new byte[4];
		System.arraycopy(data, 19, micReceived, 0, 4);
		base64__.myprintHex(micComputed);
		base64__.myprintHex(micReceived);
		if(!ObjectToString.byteToStr(micComputed).equals(ObjectToString.byteToStr(micReceived)))
			return null;
		MacJoinRequestForm macjoinrequest = new MacJoinRequestForm();
		System.arraycopy(data, 1, macjoinrequest.AppEui, 0, 8);
		System.arraycopy(data, 9, macjoinrequest.DevEui, 0, 8);
		System.arraycopy(data, 17, macjoinrequest.DevNonce,0 ,2);
		System.out.print("DevEui: ");
		base64__.myprintHex(macjoinrequest.DevEui);
		System.out.println(ObjectToString.byteToStr(macjoinrequest.DevEui));
//		Date currentTime = new Date();
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH:mm:ss");
//		String dateString = formatter.format(currentTime);
//		float temperatur = (float) (22.0 + 0.5 * Math.random());
//		RedisDB redisDB = new RedisDB();
//		redisDB.setup();
//		String userInfo = "{\"lati\":30.75461,"
//				+ "\"long\":103.92750,\"alti\":531," + "\"temperature\":" + temperatur + "," + "\"gatewayID\":\"AA555A0000000000\"}";
//
//		System.out.println("rrrrrrrrrrrrrrwwwwwwwwwwwwwwwwww");
//		try {
//			RedisDB.operateRequest(gatawayId, ObjectToString.byteToStr(macjoinrequest.DevEui), dateString, syscontent);
//			RedisDB.operateUp(gatawayId, ObjectToString.byteToStr(macjoinrequest.DevEui), dateString,
//					userInfo, syscontent);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//

		return macjoinrequest;
	}

    @Override
    public MacPktForm MacParseData(byte[] data, String gatawayId, String gatewaylati, String gatewaylong, String content) {
        return null;
    }

    /**
	 * 返回 Accept 帧
	 */
	@Override
	public MacPktForm MacConstructData(MacPktForm macpkt) {
		byte[] appnonce = {0x01,0x02,0x03};//随机
		byte[] netid = {0x00,0x00,0x65}; //devaddr 高7位  = netid 低7位        netid高17位自己定  高17位为全0
		byte[] devaddr = {(byte) 0xca,(byte) 0x67,0x73,0x02}; // 随机唯一 IP 192.168.1.1
		byte[] rxdelay = {0x01}; //默认1 
		byte[] cflist = null; //  
		byte[] dlsetting = {0x00};
		MacJoinAcceptForm macjoinaccept = new MacJoinAcceptForm();
		//CreateRandom createrandom = new CreateRandom();
		//macjoinaccept.AppNonce = createrandom.RandomArray(8);
		macjoinaccept.AppNonce = appnonce;
		macjoinaccept.NetId = netid;
		macjoinaccept.DevAddr = devaddr;
		
		macjoinaccept.dlset.RFU = 0;
		macjoinaccept.dlset.RX1DRoffset = 0x01;
		macjoinaccept.dlset.Rx2DataRate = 0x02;
		
		macjoinaccept.RxDelay = rxdelay;
		macjoinaccept.CfList = cflist;
        MacUnconfirmedDataUpForm macunconfirmeddataup = new MacUnconfirmedDataUpForm();
		MacJoinRequestForm macJoinRequestForm = (MacJoinRequestForm) macpkt;
		byte[] appSKey = LoRaMacCrypto.LoRaMacJoinComputeAppSKey(LoRaMacCrypto.APPKEY, appnonce, macJoinRequestForm.DevNonce, netid);
		byte[] nwkSKey = LoRaMacCrypto.LoRaMacJoinComputeNwkSKey(LoRaMacCrypto.APPKEY, appnonce, macJoinRequestForm.DevNonce, netid);
		try {
			RedisDB.saveKeyByNode(ObjectToString.byteToStrWith(devaddr), nwkSKey, appSKey);
			RedisDB.saveKeyByNode(ObjectToString.byteToStrWith(macJoinRequestForm.DevEui), nwkSKey, appSKey);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 加密放在组装 phy 数据时再进行计算,同时便于 MIC 值的提取
		/*byte[] output;
		
		try {
			// 加密
			output = AesEncrypt.Encrypt(AesEncrypt.APPKEY,
				ParseByte2HexStr.Byte2HexStr(macjoinaccept.MacPkt2Byte()));			
			return output;
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return macjoinaccept;
				
	}
}
