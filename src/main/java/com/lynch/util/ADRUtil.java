package com.lynch.util;


import com.lynch.LoRaServer;

/**
 * Created by lynch on 2018/11/1. <br>
 **/
public class ADRUtil {

    public static String adr(String datr, int rssi_new, int lsnr_new, int rssi_old, int lsnr_old) {
        int dr = LoRaServer.map.get(datr);

        int diff_rssi = rssi_new - rssi_old;
        int diff_lsnr = lsnr_new - lsnr_old;

        if (diff_rssi >= 0 && diff_lsnr >= 0) {
            dr = dr + 2;
        }
        if (diff_rssi < 0 && diff_lsnr < 0) {
            dr = dr - 1;
        }

        String dataRate = LoRaServer.reverse_map.get(dr);

        return dataRate;
    }
}
