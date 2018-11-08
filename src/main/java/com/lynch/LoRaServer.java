package com.lynch;

import com.lynch.connection.ConnectionService;
import com.lynch.util.ClassMod;
import com.lynch.util.ConSolePrint;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by lynch on 2018/11/1. <br>
 **/
@SpringBootApplication
public class LoRaServer implements ApplicationContextAware {
    private static ApplicationContext context;

    public static LinkedBlockingQueue queueDown = new LinkedBlockingQueue();
    public static LinkedBlockingQueue<String> OrderQueue = new LinkedBlockingQueue<>();
    public static HashMap<String, Integer> map = new HashMap<>();
    public static HashMap<Integer, String> reverse_map = new HashMap<>();

    public static final String LORAMAC_STATUS_OK = "LoRa Status Ok";
    public static final String LORAMAC_STATUS_BUSY = "LoRa Status Busy";

    public static final byte DeviceTimeReq = 0x0D; //终端设备从网络请求当前网络的日期和时间
    public static final byte DeviceModeInd = 0x20; //终端用来指示当前的操作模式（A或C）
    public static final byte LinkCheckReq = 0x02; //用于终端验证网络连接
    public static final byte LinkADRReq = 0x03; //请求终端改变数据率、传输功率、接收率或者信道
    public static final byte DutyCycleReq = 0x04; //设置设备的最大总发射占空比
    public static final byte RXParamSetupReq = 0x05; //设置接收时隙相关参数
    public static final byte DevStatusReq = 0x06; //请求终端状态,电量和解调情况

    public static final byte pingSlotInfoReq = 0x10; //终端使用，告知服务器ping单播通信的时隙速率和周期
    public static final byte PingSlotChannelReq = 0x11; //服务器使用，设置终端的单播ping通信信道

    //    public static boolean Light_up_down = false;
    public static long start = 0;
    public static long end = 0;


    public static ClassMod classMod = ClassMod.Class_A;

    public static byte[] beacon_time = null;
    public static int pingNb = 4; //ping slot数量,必须为2^k幂次方，0<=k<=7
    public static int ping_period = (int) (Math.pow(2, 12) / pingNb);
    public static int beacon_reserved = 2120;//2.12s=2120ms
    public static float beacon_guard = (float) 3.000;
    //beacon_window=becon_period - beacon_reserved -beacon_guard
    public static int pingOffset = 0;
    public static int periodicity = 32;
    public static int Delay = 0;
    public static int slotLen = 30;   //一个ping slot时长，30ms
    private byte[] Frequence = new byte[3];

    public byte[] getFrequence() {
        return Frequence;
    }

    public void setFrequence(byte[] frequence) {
        Frequence = frequence;
    }


    private static final int DR0 = 0;
    private static final int DR1 = 1;
    private static final int DR2 = 2;
    private static final int DR3 = 3;
    private static final int DR4 = 4;
    private static final int DR5 = 5;

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        map.put("SF12BW125", DR0);
        map.put("SF11BW125", DR1);
        map.put("SF10BW125", DR2);
        map.put("SF9BW125", DR3);
        map.put("SF8BW125", DR4);
        map.put("SF7BW125", DR5);


        reverse_map.put(DR0, "SF12BW125");
        reverse_map.put(DR1, "SF11BW125");
        reverse_map.put(DR2, "SF10BW125");
        reverse_map.put(DR3, "SF9BW125");
        reverse_map.put(DR4, "SF8BW125");
        reverse_map.put(DR5, "SF7BW125");

        SpringApplication.run(LoRaServer.class, args);
        context.getBean(ConnectionService.class).start();
        //将控制台消息输入到文本
//              ConSolePrint conSolePrint = new ConSolePrint();
//              conSolePrint.consoleprint();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
