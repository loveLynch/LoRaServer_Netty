package com.lynch.util.base64;

import org.apache.commons.codec.binary.Base64;
public class base64__ {
    /**
     * @param base64String
     * @return
     */
    public static byte[] decode(String base64String) {
        return Base64.decodeBase64(base64String);
    }

    /**
     * 二进制数据编码为BASE64字符串
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(final byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    public static void myprintHex(byte[] inputData) {
        for (int i = 0; i < inputData.length; i++) {
            String hex = Integer.toHexString(inputData[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print("0x" + hex + " ");
        }
        System.out.println();
    }

    //辅助转为ascll
    public static String recvtohex(byte[] inputData) {
        String rstr = "";
        for (int i = 0; i < inputData.length; i++) {
            String hex = Integer.toHexString(inputData[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            rstr = rstr + hex;
        }

        return rstr;
    }

    //buffer to String (hex)
    public static String BytetoHex(byte[] buffer) {
        String nodeid = "";
        for (int i = 0; i < buffer.length; i++) {
            String hex = Integer.toHexString(buffer[i] & 0xFF);
            if (hex.length() == 1)
                hex = '0' + hex;
            if ((i != buffer.length - 1))
                hex = hex + ":";
            nodeid = nodeid + hex;
        }
        return nodeid;
    }

    //String(hex) to buffer
    public static byte[] HextoByte(String hexstr) {
        String[] strings = hexstr.split(":");
        String nodeid = "";
        for (String s : strings) {
            nodeid += s;
        }
        if (nodeid == null || nodeid.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[nodeid.length() / 2];
        for (int i = 0; i < nodeid.length() / 2; i++) {
            String subStr = nodeid.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    //to app
    public static String apptohex(byte[] inputData) {
        String rstr = "";
        for (int i = 0; i < inputData.length; i++) {
            String hex = Integer.toHexString(inputData[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            rstr = rstr + "0x" + hex + " ";
        }

        return rstr;
    }

    //redis存dev及nwk转码
    public static String toHex(byte[] inputData) {
        String rstr = "";
        for (int i = 0; i < inputData.length; i++) {
            String hex = Integer.toHexString(inputData[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            rstr = rstr + hex + ":";
        }

        return rstr;
    }

    public static void main(String[] args) {
        byte[] buffer = new byte[3];
//		  byte[] buff = new byte[13];
//		  buff[0] = 0x01;	// mhdr
//			buff[1] = 0x0f;	// DevAddr
//			buff[2] = 0x0f;
//			buff[3] = 0x0f;
//			buff[4] = 0x0f;
//			buff[5] = 0x01;	// Fctrl
//			buff[6] = 0x0f;	// Fcnt
//			buff[7] = 0x0f;
//			buff[8] = 0x0f; //Fport
//			buff[9] = 0x0f; //FramePayload
//			buff[10] = 0x0f;
//			buff[11] = 0x0f;
//			buff[12] = (byte) 0xf0;
//		  String base64String = "AQ8PDw8BDw8PDw8P8A==";
//		  buffer = base64__.decode(base64String);
//		  
//		  System.out.println(Arrays.toString(buffer));//字节数组打印
//		  base64__.myprintHex(buffer);
        buffer[0] = 0x0f;
        buffer[1] = 0x0f;
        buffer[2] = 0x0f;
        String string = "" + buffer[0] + buffer[1] + buffer[2];

        String nodeid = "";
//		  byte[] buffer = macunconfirmeddataup.DevAddr;
        for (int i = 0; i < buffer.length; i++) {
            String hex = Integer.toHexString(buffer[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
                if ((i != buffer.length - 1))
                    hex = hex + ":";
            }
            nodeid = nodeid + hex;
        }
        System.out.println(nodeid);
//		  String macJson = "gI3AeQAAAQADfQGMwPoi";
        String macJson = "QI3AeQAAAgADbOrMmLZLKs4=";
//		  String macJson = "QI3AeQAAAQADi9D2ZA1VRCQ=";
        myprintHex(decode(macJson));
//		  gI3AeQAAAQADfQGMwPoi
//		  gI3AeQAAAQADfNNNvzp6
//		  QI3AeQAAAgADmnWsTizT
    }
}
