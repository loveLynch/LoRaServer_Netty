package com.lynch;


import com.lynch.util.ByteArrayandInt;
import com.lynch.util.aes.LoRaMacCrypto;
import com.lynch.util.base64.base64__;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by lynch on 2018/6/26. <br>
 **/

/**
 * ********Upstream JSON data structure
 * time | string | UTC time of pkt RX, us precision, ISO 8601 'compact' format
 * tmms | number | GPS time of pkt RX, number of milliseconds since 06.Jan.1980
 * tmst | number | Internal timestamp of "RX finished" event (32b unsigned)
 * *******Downstream protocol
 * imme | bool   | Send packet immediately (will ignore tmst & time)
 * tmst | number | Send packet on a certain timestamp value (will ignore time)
 * tmms | number | Send packet at a certain GPS time (GPS synchronization required)
 */
class PrintpingOffset extends TimerTask {
    byte[] beacon_time;
    int pingOffset = 0;
    int ping_period = 1024;
    int beacon_reserved = 2120;
    int periodicity = 32;
    int slotLen = 30;
    int beacon_period = 128;

    @Override

    public void run() {
        System.out.println("----beaconTime----receive---");
        int SecondSinceEpoch1 = (int) (System.currentTimeMillis() / 1000 - 315964800);
        int mode = SecondSinceEpoch1 % 128;
        int SecondSinceEpoch2 = SecondSinceEpoch1 - mode;
        beacon_time = new byte[4];
        //低位在前，高位在后
        beacon_time[0] = (byte) (SecondSinceEpoch2 & 0xFF);
        beacon_time[1] = (byte) ((SecondSinceEpoch2 >> 8) & 0xFF);
        beacon_time[2] = (byte) ((SecondSinceEpoch2 >> 16) & 0xFF);
        beacon_time[3] = (byte) ((SecondSinceEpoch2 >> 24) & 0xFF);
        base64__.myprintHex(beacon_time);
        byte[] dev = {0x59, (byte) 0xa6, 0x73, 0x01};
        try {
            byte[] Rank = LoRaMacCrypto.LoRaMacBeaconComputeOffset(LoRaMacCrypto.APPSKEY0, beacon_time, dev);
            base64__.myprintHex(Rank);
            pingOffset = (Rank[0] + Rank[1] * 256) % ping_period;
            System.out.println("receive pingOffset is " + pingOffset);
            byte[] pingoffset = new byte[2];
            pingoffset[0] = (byte) (pingOffset & 0xFF);
            pingoffset[1] = (byte) ((pingOffset >> 8) & 0xFF);
            base64__.myprintHex(pingoffset);
            long millBeacon_time = (long) (ByteArrayandInt.byteArrayToInt(beacon_time)) * 1000;
            System.out.println(millBeacon_time);


            System.out.println("数据：" + millBeacon_time + "   " + pingOffset + "   " + beacon_reserved + "     " + periodicity);
            double number = beacon_period / 32;
            for (int i = 0; i < number; i++) {
                double time = millBeacon_time + (pingOffset + i * ping_period) * slotLen + beacon_reserved;
                BigDecimal bigDecimal = new BigDecimal(time);
                String strtime = bigDecimal.toPlainString();
                System.out.println(strtime);
                double finaltime = Double.parseDouble(strtime);
                //System.out.println("time:" + finaltime);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                double cha = (double) 315964800 * 1000;
                System.out.println(finaltime + cha);
                String sd = sdf.format(new Date((long) (finaltime + cha)));   // 时间戳转换成时间
                System.out.println(sd);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Timer timer = new Timer();
        timer.schedule(new PrintpingOffset(), new Date(), 128000);


    }
}