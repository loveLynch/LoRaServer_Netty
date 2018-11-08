package com.lynch.util.phy;


import com.lynch.mac.MacPktForm;
import com.lynch.util.aes.LoRaMacCrypto;
import com.lynch.util.base64.base64__;

public class PhyConstruct {

    private static final int MIC_LENGTH = 4;

    /**
     * @param macpktform : mac 层数据
     * @param type       : mhdr 的 mac 类型, 用于生成相对应的 mhdr 对象
     * @return : byte[] phy 层数据，包含加密及 MIC 的计算
     */
    public static byte[] PhyPkt2Byte(MacPktForm macpktform, int type) {
        byte[] phy = new byte[macpktform.getLength() + 5];
        //byte[] phy = new byte[macpktform.getLength() + 8];
        //byte[] webMac = new byte[]{0x12, 0x23, 0x34};
        type = type < 5 ? type + 1 : type;
        // 封装 mhdr
        Mhdr mhdr = new Mhdr(type, 0, 0);
        // 分情况：
        // 1、对于 accept 数据帧，需要先计算 mic, 再连同 mic 一起加密
        // 2、对于数据帧，直接计算 mic 值, 不需加密
        System.arraycopy(mhdr.MhdrPktToByte(), 0, phy, 0, mhdr.getLength());    // head
        System.arraycopy(macpktform.MacPkt2Byte(), 0, phy, mhdr.getLength(), macpktform.getLength());    //
        // System.arraycopy(webMac, 0, phy, mhdr.getLength() + macpktform.getLength(), 3);
//        System.arraycopy(MicCaculate.MicCaculate(macpktform, type),
//                0, phy,
//                mhdr.getLength() + macpktform.getLength() + 3, MIC_LENGTH);
        System.arraycopy(MicCaculate.MicCaculate(macpktform, type),
                0, phy,
                mhdr.getLength() + macpktform.getLength(), MIC_LENGTH);
        if (type == 1) {
            // 如果是 accept 数据帧, 则还需连同 MIC 加密, 使用秘钥 AppKey
            System.out.println("=====accept encrypt=====");
            byte[] data_in = new byte[phy.length - 1];
            System.arraycopy(phy, 1, data_in, 0, phy.length - 1);
            base64__.myprintHex(data_in);
            System.arraycopy(LoRaMacCrypto.LoRaMacAcceptEncrypt(data_in, data_in.length, LoRaMacCrypto.APPKEY),
                    0, phy, mhdr.getLength(), macpktform.getLength() + MIC_LENGTH);    //
        }
        base64__.myprintHex(phy);
        return phy;
    }
}
