package com.lynch.handler;

import com.lynch.LoRaServer;
import com.lynch.domain.DownInfoForm;
import com.lynch.domain.InfoLoraModEndForm;
import com.lynch.domain.UpInfoForm;
import com.lynch.mac.MacPktForm;
import com.lynch.mac.OperateMacPkt;
import com.lynch.util.base64.base64__;
import com.lynch.util.phy.PhyConstruct;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lynch on 2018/11/2. <br>
 **/
public class MacDataHandler extends ChannelInboundHandlerAdapter {
    private static List<String> OPMACLISTNAME = new ArrayList<String>();
    private static List<String> JSONFORMLIST = new ArrayList<String>();

    public MacDataHandler() {
        OPMACLISTNAME.add("com.lynch.mac.OperateMacJoinRequest");
        OPMACLISTNAME.add("com.lynch.mac.OperateMacJoinAccept");
        OPMACLISTNAME.add("com.lynch.mac.OperateMacUnconfirmedDataUp");
        OPMACLISTNAME.add("com.lynch.mac.OperateMacConfirmedDataUp");
        OPMACLISTNAME.add("com.lynch.mac.OperateMacUnconfirmedDataDown");
        OPMACLISTNAME.add("com.lynch.mac.OperateMacConfirmedDataDown");

        JSONFORMLIST.add("com.lynch.domain.DownInfoA_LoRa");
        JSONFORMLIST.add("com.lynch.domain.DownInfoB_LoRa");
        JSONFORMLIST.add("com.lynch.domain.DownInfoC_LoRa");
        JSONFORMLIST.add("com.lynch.domain.DownInfoA_FSK");
        JSONFORMLIST.add("com.lynch.domain.DownInfoB_FSK");
        JSONFORMLIST.add("com.lynch.domain.DownInfoC_FSK");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        UpInfoForm upInfo = (InfoLoraModEndForm) evt;
        DownInfoForm DownInfo = null;
        byte[] upMacData;        // 解 base64 但未解密的 phy 数据
        byte[] downMacData;        // 加密但未 base64 编码
        MacPktForm macPktFormUp;
        OperateMacPkt upOperateMacPkt;
        byte upMhdr;
        upMacData = upInfo.getData();
        if (upMacData == null) {
            System.out.println("upMacData == null!!!");
            return;
        }
        upMhdr = upMacData[0];
        try {
            // 通过反射创建不同的对象,用于不同的 Mtype 类型
            Class<?> cls = Class.forName(OPMACLISTNAME.get(((upMhdr & 0xff) >> 5)));
            Constructor<?> ctr = cls.getConstructor();
            upOperateMacPkt = (OperateMacPkt) ctr.newInstance();


            String sysInfo = upInfo.getSysInfo();
            // 6 种不同类型的 Mtype 会调用不同的 解析和构造 操作
            // MacParseData: 先将 byte[] 解密后解析并生成 macPktFormUp 对象.
            // macPktFormUp 含 mac 层各数据的对象
            /*
             * 存 用户信息和系统信息，问题是终端节点devEui 和 devaddr 的映射
             */
            macPktFormUp = upOperateMacPkt.MacParseData(upMacData, "AA555A0000000000", sysInfo);
            System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
            if (macPktFormUp == null) {
                System.out.println("macPktFormUp == null");
                return;
            }
            /**
             * 构造 phy 层数据, 即 downMacData
             * MacConstructData(macPktFormUp):
             * 		先构造回复的对象 macPktFormDown
             * 		accept 帧不加密, 数据帧需要加密 frame 部分
             *
             * 再通过 PhyPkt2Byte() 函数, 将其转为 byte[]
             * 		accept 帧的加密操作在 Phypkt2byte() 中完成，便于 MIC 的生成
             *
             * 非确认帧的 downMacData 应该为 null
             */
            MacPktForm macPktFormDown = upOperateMacPkt.MacConstructData(macPktFormUp);
            System.out.println("oooooooooooooooooooooooooo");
            if (macPktFormDown == null) {
                System.out.println("macPktFormDown == null");
                return;
            }
            downMacData = PhyConstruct.PhyPkt2Byte(macPktFormDown, ((upMhdr & 0xff) >> 5));
            System.out.print("send to End-Node: ");
            base64__.myprintHex(downMacData);
            if (downMacData == null) {
//                throw new Exception("downMacData == null");
                return;
            }

            /**
             * 构造回送的 DownInfoForm, 并加到 downQueue 中
             * 如何调用不同的 ConstructDownInfo 以构造出不同类型的 DownInfo
             * 		有没有能够识别是 class A\B\C 的字段
             * 		需要根据 downMacData 是否为 null , 判断是否需要构造 downInfo
             */
            int downjsonformType = 0;    // 0: A_LoRa 1:B_LoRa 2:C_LoRa 3:A_FSK 4:B_FSK 5:C_FSK
            if (upInfo.getModu().equals("LORA")) {
                downjsonformType = macPktFormUp.getEndType() + downjsonformType;
            } else {
                downjsonformType = macPktFormUp.getEndType() + 3;
            }
            Class<?> clsInfo = Class.forName(JSONFORMLIST.get(downjsonformType));// TODO A/B/C
            Constructor<?> ctrInfo = clsInfo.getConstructor();
            DownInfo = (DownInfoForm) ctrInfo.newInstance();
            synchronized (LoRaServer.queueDown) {
                LoRaServer.queueDown.add(DownInfo.ConstructDownInfo(upInfo, downMacData, ((upMhdr & 0xff) >> 5)));
                System.out.println("PUSH-------------Scucsacas");
            }
            LoRaServer.end = System.currentTimeMillis();
            System.out.println("用时："+(LoRaServer.end - LoRaServer.start));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
