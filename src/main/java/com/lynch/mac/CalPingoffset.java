package com.lynch.mac;

import com.lynch.LoRaServer;
import com.lynch.util.ByteArrayandInt;
import com.lynch.util.aes.LoRaMacCrypto;
import com.lynch.util.base64.base64__;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.lynch.LoRaServer.beacon_time;

/**
 * Created by lynch on 2018/7/9. <br>
 * 从网络时间获取beacon，它从1970年01月01日0h00m00s，System.currentTimeMillis()
 * GPS时起点为1980年1月6日0h00m00s
 **/
public class CalPingoffset {

    public static byte[] beacontime() {
        int SecondSinceEpoch1 = (int) (System.currentTimeMillis() / 1000 - 315964800);
        int mode = SecondSinceEpoch1 % 128;
        int SecondSinceEpoch2 = SecondSinceEpoch1 - mode;
        return ByteArrayandInt.intToByteArray(SecondSinceEpoch2);
    }

    public synchronized void calpingoffset(byte[] DevAddr) {
        System.out.println("----beaconTime----receive---");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println(df.format(new Date()));
//        int SecondSinceEpoch1 = (int) (System.currentTimeMillis() / 1000 - 315964800);
//        int mode = SecondSinceEpoch1 % 128;
//        int SecondSinceEpoch2 = SecondSinceEpoch1 - mode;
//        //int SecondSinceEpoch1 = 315964800;
        LoRaServer.beacon_time = beacontime();
        base64__.myprintHex(LoRaServer.beacon_time);
        byte[] Rank = LoRaMacCrypto.LoRaMacBeaconComputeOffset(LoRaMacCrypto.APPSKEY0, beacon_time, DevAddr);
        // base64__.myprintHex(Rank);
        LoRaServer.pingOffset = (Rank[0] + Rank[1] * 256) % LoRaServer.ping_period;
        System.out.println("receive pingOffset is " + LoRaServer.pingOffset);
        byte[] pingoffset = new byte[2];
        pingoffset[0] = (byte) (LoRaServer.pingOffset & 0xFF);
        pingoffset[1] = (byte) ((LoRaServer.pingOffset >> 8) & 0xFF);
        System.out.print("hex_pingoffset is ");
        base64__.myprintHex(pingoffset);
        //test
//        byte[] abspingoffset = new byte[2];
//        int absintpingoffset = Math.abs(LoRaMain.pingOffset);
//        abspingoffset[0] = (byte) (absintpingoffset & 0XFF);
//        abspingoffset[1] = (byte) ((absintpingoffset >> 8) & 0XFF);
//        base64__.myprintHex(abspingoffset);
//        long millBeacon_time = (long) (ByteArrayandInt.byteArrayToInt(beacon_time)) * 1000;
//        System.out.println(millBeacon_time);
//        System.out.println("数据：" + millBeacon_time + "   " + LoRaMain.pingOffset + "   " + LoRaMain.beacon_reserved + "     " + LoRaMain.periodicity);
//        double number = LoRaMain.beacon_period / 32;
//        for (int i = 0; i < number; i++) {
//            double time = millBeacon_time + (LoRaMain.pingOffset + i * LoRaMain.ping_period) * LoRaMain.slotLen + LoRaMain.beacon_reserved;
//            BigDecimal bigDecimal = new BigDecimal(time);
//            String strtime = bigDecimal.toPlainString();
//            System.out.println(strtime);
//            double finaltime = Double.parseDouble(strtime);
//            //System.out.println("time:" + finaltime);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
//            double cha = (double) 315964800 * 1000;
//            System.out.println(finaltime + cha);
//            String sd = sdf.format(new Date((long) (finaltime + cha)));   // 时间戳转换成时间
//            System.out.println(sd);
// }

    }
}
