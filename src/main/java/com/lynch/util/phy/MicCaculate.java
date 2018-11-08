package com.lynch.util.phy;



import com.lynch.util.ObjectToString;
import com.lynch.util.RedisDB;
import com.lynch.util.aes.LoRaMacCrypto;
import com.lynch.util.base64.base64__;
import com.lynch.mac.*;

import javax.xml.bind.DatatypeConverter;

public class MicCaculate {

    /**
     * @param macpktform: mac 层数据, 不含 mhdr, 这里需要根据 type 创建 mhdr
     * @param type:       用于判断 是否为 Accept 帧
     * @return 4 字节的 MIC
     */
    public static byte[] MicCaculate(MacPktForm macpktform, int type) {
        byte[] macbyte = macpktform.MacPkt2Byte();
        byte[] mic = new byte[4];
        byte[] data = new byte[macpktform.getLength() + 1];
        base64__.myprintHex(macbyte);
        Mhdr mhdr = new Mhdr(type, 0, 0);
        System.arraycopy(mhdr.MhdrPktToByte(), 0, data, 0, mhdr.getLength());
        System.arraycopy(macbyte, 0, data, mhdr.getLength(), macbyte.length);


        RedisDB redisDB = new RedisDB();
        redisDB.setup();
        byte[] nwkSKey = new byte[16];
        byte[] appSKey = new byte[16];

        // MIC 计算
        switch (type) {
            case 0:        // Join Request
                byte[] requestb = new byte[data.length];
                System.arraycopy(data, 0, requestb, 0, data.length - 4);
                mic = LoRaMacCrypto.LoRaMacJionRequestComputeMic(requestb, requestb.length, LoRaMacCrypto.APPKEY);
                break;
            case 1:        // Join Accept
                mic = LoRaMacCrypto.LoRaMacJoinAcceptComputeMic(data, data.length, LoRaMacCrypto.APPKEY);
                break;
            case 2:        // UnConfirmed Data Up
                nwkSKey = DatatypeConverter.parseHexBinary(redisDB.jedisPool.getResource().get(ObjectToString.byteToStrWith(macpktform.getDevAddr()) + ":NwkSKey"));
//				appSKey = DatatypeConverter.parseHexBinary(RedisDB.jedisPool.getResource().get(ObjectToString.byteToStrWith(macpktform.getDevAddr()) + ":AppSKey"));
                byte[] unConfirmedUpb = new byte[data.length - 4];
                System.arraycopy(data, 0, unConfirmedUpb, 0, data.length - 4);
                MacUnconfirmedDataUpForm macUnconfirmedDataUpForm = (MacUnconfirmedDataUpForm) macpktform;
                mic = LoRaMacCrypto.LoRaMacComputeMic(unConfirmedUpb, unConfirmedUpb.length, nwkSKey,
                        macUnconfirmedDataUpForm.DevAddr, (byte) 0x00,
                        macUnconfirmedDataUpForm.Fcnt);
                break;
            case 3:        // UnConfirmed Data Down
                MacUnconfirmedDataDownForm macUnconfirmedDataDownForm = (MacUnconfirmedDataDownForm) macpktform;
                base64__.myprintHex(data);
                nwkSKey = DatatypeConverter.parseHexBinary(redisDB.jedisPool.getResource().get(ObjectToString.byteToStrWith(macpktform.getDevAddr()) + ":NwkSKey"));
                base64__.myprintHex(nwkSKey);
                System.out.println("ndajkshdjkashdjkash");
//				appSKey = DatatypeConverter.parseHexBinary(RedisDB.jedisPool.getResource().get(ObjectToString.byteToStrWith(macpktform.getDevAddr()) + ":AppSKey"));
                mic = LoRaMacCrypto.LoRaMacComputeMic(data, data.length, nwkSKey,
                        macUnconfirmedDataDownForm.DevAddr, (byte) 0x01,
                        macUnconfirmedDataDownForm.Fcnt);
                break;
            case 4:        // Confirmed Data Up
                byte[] confirmedUpb = new byte[data.length];
                System.arraycopy(data, 0, confirmedUpb, 0, data.length - 4);
                nwkSKey = DatatypeConverter.parseHexBinary(redisDB.jedisPool.getResource().get(ObjectToString.byteToStrWith(macpktform.getDevAddr()) + ":NwkSKey"));
//				appSKey = DatatypeConverter.parseHexBinary(RedisDB.jedisPool.getResource().get(ObjectToString.byteToStrWith(macpktform.getDevAddr()) + ":AppSKey"));
                MacConfirmedDataUpForm macConfirmedDataUpForm = (MacConfirmedDataUpForm) macpktform;
                mic = LoRaMacCrypto.LoRaMacComputeMic(confirmedUpb, confirmedUpb.length, nwkSKey,
                        macConfirmedDataUpForm.DevAddr, (byte) 0x00,
                        macConfirmedDataUpForm.Fcnt);
                break;
            case 5:        // Confirmed Data Down
                MacConfirmedDataDownForm macConfirmedDataDownForm = (MacConfirmedDataDownForm) macpktform;
                nwkSKey = DatatypeConverter.parseHexBinary(redisDB.jedisPool.getResource().get(ObjectToString.byteToStrWith(macpktform.getDevAddr()) + ":NwkSKey"));
//				appSKey = DatatypeConverter.parseHexBinary(RedisDB.jedisPool.getResource().get(ObjectToString.byteToStrWith(macpktform.getDevAddr()) + ":AppSKey"));
                mic = LoRaMacCrypto.LoRaMacComputeMic(data, data.length, nwkSKey, macConfirmedDataDownForm.DevAddr, (byte) 0x01, macConfirmedDataDownForm.Fcnt);
                break;
            case 6:        // RFU

                break;
            case 7:        // Proprietary

                break;
            default:
                mic = null;
                nwkSKey = DatatypeConverter.parseHexBinary(redisDB.jedisPool.getResource().get(ObjectToString.byteToStrWith(macpktform.getDevAddr()) + ":NwkSKey"));
                appSKey = DatatypeConverter.parseHexBinary(RedisDB.jedisPool.getResource().get(ObjectToString.byteToStrWith(macpktform.getDevAddr()) + ":AppSKey"));
                break;
        }
        return mic;
    }

}


