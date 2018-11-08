package com.lynch;


import com.lynch.util.aes.LoRaMacCrypto;
import com.lynch.util.base64.base64__;

/**
 * Created by lynch on 2018/9/10. <br>
 **/
public class FRMPayloadDecode {
    public static void main(String[] args) {
        // 对于 FRMPayload 要进行解密测试
        /*
        updata:
0x40 0x59 0xa6 0x73 0x01 0x90 0x31 0x00 0x03 0x7c 0xdc 0xe5 0xb6 0xb5 0xed
1B mhdr__4B DevAddr__1B Fctrl__2B Fcnt__1B Fports__0~NB FRMPayload__4B MIC
                    （0x90)        (0~15B Fopts)

downdata:
0x60 0x59 0xa6 0x73 0x01 0x80 0x31 0x00 0x03 0x1e 0x2a 0x8a 0x71 0x5c 0x95 0xaa 0x66
1B mhdr__4B DevAddr__1B Fctrl__2B Fcnt__1B Fports__0~NB FRMPayload__4B MIC
                     (0x80)        (0~15B Fopts)
         */
        try {
            byte[] dev = new byte[]{0x59, (byte) 0xa6, 0x73, 0x01};
            byte[][] Fcnt = new byte[][]{{(byte) 0x81, 0x00}, {(byte) 0x82, 0x00}, {(byte) 0x84, 0x00}, {(byte) 0x86, 0x00}};
            byte[][] frmpayload = new byte[][]{{0x5a, (byte) 0xec, (byte) 0xfa, 0x34}, {0x7b, 0x46, (byte) 0xb9, (byte) 0x87}, {0x72}, {(byte) 0xbf, 0x28, 0x10, (byte) 0xe7}};
            byte[] fcnt = new byte[4];
            fcnt[2] = 0x00;
            fcnt[3] = 0x00;
            byte[] appSKey = {0x2b, 0x7e, 0x15, 0x16, 0x28, (byte) 0xae, (byte) 0xd2, (byte) 0xa6, (byte) 0xab, (byte) 0xf7, 0x15, (byte) 0x88, 0x09, (byte) 0xcf, 0x4f, 0x3c};
            for (int i = 0; i < 4; i++) {


                System.arraycopy(Fcnt[i], 0, fcnt, 0, 2);
                byte[] FRMPayload = LoRaMacCrypto.LoRaMacPayloadDecrypt(frmpayload[i], frmpayload[i].length,
                        appSKey, dev, (byte) 0x01, fcnt);// TOD


                System.out.print("FRMPayload:");
                base64__.myprintHex(FRMPayload);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
