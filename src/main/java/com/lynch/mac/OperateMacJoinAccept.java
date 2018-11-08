package com.lynch.mac;


//import random.CreateRandom;

public class OperateMacJoinAccept implements OperateMacPkt{

	/**
	 * 解析 mac 层数据
	 */
	@Override
	public	MacPktForm MacParseData(byte[] data,String gatawayId, String syscontent) {
		return null;
	}

    @Override
    public MacPktForm MacParseData(byte[] data, String gatawayId, String gatewaylati, String gatewaylong, String content) {
        return null;
    }

    @Override
	//accept 填的是默认的数据
	public MacPktForm MacConstructData(MacPktForm macpkt) {
		byte[] appnonce = {0x01,0x02,0x03};//随机
		byte[] netid = {0x00,0x00,0x60}; //devaddr 高7位  = netid 低7位        netid高17位自己定  高17位为全0
		byte[] devaddr = {(byte) 0xc0,(byte) 0xa8,0x01,0x01}; // 随机唯一 IP 192.168.1.1
		byte[] rxdelay = {0x01}; //默认1 
		byte[] cflist = null; //  
		byte[] dlsetting = {0x00};
		MacJoinAcceptForm macjoinaccept = new MacJoinAcceptForm();
		macjoinaccept.AppNonce = appnonce;
		macjoinaccept.NetId = netid;
		macjoinaccept.DevAddr = devaddr;
		
		macjoinaccept.dlset.RFU = dlsetting[0] & 0x80;
		macjoinaccept.dlset.RX1DRoffset = dlsetting[0] & 0x70;//默认为0
		macjoinaccept.dlset.Rx2DataRate = dlsetting[0] & 0x0f;//默认0
		
		macjoinaccept.RxDelay = rxdelay;
		macjoinaccept.CfList = cflist  ;  
	/*	for(byte b : macjoinaccept.MacPkt2Byte()){
			String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print("0x" + hex + " ");
		}
	*/

		return macjoinaccept;				
	}
	
	// TODO 测试
	public static void main(String[] arg){
		byte[] appnonce = {0x01,0x02,0x03};//随机
		byte[] netid = {0x00,0x00,0x60}; //devaddr 高7位  = netid 低7位        netid高17位自己定  高17位为全0
		byte[] devaddr = {(byte) 0xc0,(byte) 0xa8,0x01,0x01}; // 随机唯一 IP 192.168.1.1
		byte[] rxdelay = {0x01}; //默认1 
		byte[] cflist = null; //  
		MacJoinAcceptForm macjoinaccept = new MacJoinAcceptForm();
		//CreateRandom createrandom = new CreateRandom();
		//macjoinaccept.AppNonce = createrandom.RandomArray(8);
		macjoinaccept.AppNonce = appnonce;
		macjoinaccept.NetId = netid;
		macjoinaccept.DevAddr = devaddr;
		
		macjoinaccept.dlset.RFU = 0;
		macjoinaccept.dlset.RX1DRoffset = 0;//默认为0
		macjoinaccept.dlset.Rx2DataRate = 0;//默认0
		
		macjoinaccept.RxDelay = rxdelay;//  
		macjoinaccept.CfList = cflist  ;  
		for(byte b : macjoinaccept.MacPkt2Byte()){
			String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print("0x" + hex + " ");
		}
	}

}
