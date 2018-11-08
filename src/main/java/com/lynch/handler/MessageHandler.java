package com.lynch.handler;

import com.lynch.LoRaServer;
import com.lynch.domain.DownInfoForm;
import com.lynch.util.ConstructJson;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lynch on 2018/11/2. <br>
 **/
public class MessageHandler extends MessageToMessageDecoder<DatagramPacket> {
    private static final byte PKT_PUSH_DATA = 0x00;
    private static final byte PKT_PUSH_ACK = 0x01;
    private static final byte PKT_PULL_DATA = 0x02;
    private static final byte PKT_PULL_RESP = 0x03;
    private static final byte PKT_PULL_ACK = 0x04;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
/*
    public void run() {
        System.out.println("|******************************************************|");
        System.out.println("|********************* hello lora *********************|");
        System.out.println("|******************************************************|");
        while (true) {

            byte[] buffer = new byte[8192];
            recv_pkt = new DatagramPacket(buffer, buffer.length);
//			System.out.println("********************************");
            try {
                dsock.receive(recv_pkt);
                //String recv_info;
                DatagramPacket send_pkt;
                switch (buffer[3]) {
                    case PKT_PUSH_DATA:
                        recv_info = new String(recv_pkt.getData(), 12, recv_pkt.getLength() - 12);
                        System.out.println("PUSH_DATA" + recv_info);
                        // 先回 ack
                        buffer[3] = PKT_PUSH_ACK;
                        send_pkt = new DatagramPacket(
                                buffer, 4,
                                recv_pkt.getAddress(), recv_pkt.getPort());
                        dsock.send(send_pkt);
                        // 存到 queueUp
                        ParseJson parseJson = new ParseJson();
                        parseJson.parseOfJson(recv_info);
                        break;
                    default:
//	                  System.out.println(", unexpected command");
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
 */
/*
    @Override
    public void run() {

        while (true) {
            byte[] buffer = new byte[8192];
            DatagramPacket recv_pkt = new DatagramPacket(buffer, buffer.length);
//			System.out.println("********************************");
            try {
                dsock.receive(recv_pkt);
                DatagramPacket send_pkt;
                switch (buffer[3]) {
                    case PKT_PULL_DATA:
                        buffer[3] = PKT_PULL_ACK;
                        send_pkt = new DatagramPacket(
                                buffer, 4,
                                recv_pkt.getAddress(), recv_pkt.getPort());
                        dsock.send(send_pkt);

                        DownInfoForm info;
                        byte[] down;
                        buffer[3] = PKT_PULL_RESP;

                        for (int i = 0; i < 8; i++) {
                            synchronized (LoRaMain.queueDown) {
                                info = LoRaMain.queueDown.pull();
                            }
                            if (info == null) {
                                continue;
                            }
                            // 构造 JSON 数据
                         down = (ConstructJson.ToJsonStr(info)).getBytes();
                            byte[] phyDown = new byte[down.length + 4];
                            System.arraycopy(buffer, 0, phyDown, 0, 4);
                            System.arraycopy(down, 0, phyDown, 4, down.length);

                            send_pkt = new DatagramPacket(
                                    phyDown, phyDown.length,
                                    recv_pkt.getAddress(), recv_pkt.getPort());
//						System.out.println("asdasdasdsadasdasd");
//						base64__.myprintHex(phyDown);
                            dsock.send(send_pkt);
                        }

                        break;
                    case PKT_PULL_ACK:
                        break;
                    default:
//	                  System.out.println(", unexpected command");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 */
//        System.out.println(">>>>>>>received package:");
//        //打印收到的报文内容
//        System.out.println(ByteBufUtil.hexDump(datagramPacket.content()));
        switch (datagramPacket.content().getByte(3)) {
            case PKT_PUSH_DATA:
                System.out.println("PUSH");
                //回应ACK
                ByteBuf byteBuf = datagramPacket.content().copy(0, 4);
                try {
                    byteBuf.setByte(3, PKT_PUSH_ACK);
                    channelHandlerContext.channel().writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(byteBuf), new InetSocketAddress(datagramPacket.sender().getHostString(), datagramPacket.sender().getPort())));

                    //去掉头部12字节
                    list.add(new String(datagramPacket.content().toString(CharsetUtil.UTF_8).substring(6)));
                } finally {
                    byteBuf.release();
                }
                break;

            case PKT_PULL_DATA:
                ByteBuf byteBuf1 = datagramPacket.content().copy(0, 4);
                try {
                    byteBuf1.setByte(3, PKT_PULL_ACK);
                    channelHandlerContext.channel().writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(byteBuf1), new InetSocketAddress(datagramPacket.sender().getHostString(), datagramPacket.sender().getPort())));
                    byte[] buffer = new byte[4];
                    //如果是堆缓冲区
                    if (datagramPacket.content().hasArray()) {
                        buffer = datagramPacket.content().array();
                    } else {
                        //如果是直接缓冲区
                        datagramPacket.content().getBytes(0, buffer);
                    }
                    DownInfoForm info = null;
                    byte[] down;
                    buffer[3] = PKT_PULL_RESP;

                    for (int i = 0; i < 8; i++) {
                        info = (DownInfoForm) LoRaServer.queueDown.poll(1, TimeUnit.MICROSECONDS);

                        if (info == null) {
                            break;
//                        throw new Exception("info == null");
                        }
                        // 构造 JSON 数据
                        down = (ConstructJson.ToJsonStr(info)).getBytes();
                        byte[] phyDown = new byte[down.length + 4];
                        System.arraycopy(buffer, 0, phyDown, 0, 4);
                        System.arraycopy(down, 0, phyDown, 4, down.length);
                        channelHandlerContext.channel().writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(phyDown), new InetSocketAddress(datagramPacket.sender().getHostString(), datagramPacket.sender().getPort())));

                    }
                    channelHandlerContext.flush();
                } finally {
                    byteBuf1.release();
                }
                break;
            case PKT_PULL_ACK:
                break;
            default:
//                System.out.println(", unexpected command");

        }


    }
}
