package com.lynch.mac;

public interface OperateMacPkt {

    /**
     * @param data      :phypayload
     * @param gatawayId
     * @param content
     * @return macpayload 对象, 不含 mhdr、mic
     */
    MacPktForm MacParseData(byte[] data, String gatawayId, String content);

    /**
     * @param data
     * @param gatawayId
     * @param gatewaylati
     * @param gatewaylong
     * @param content
     * @return
     */
    MacPktForm MacParseData(byte[] data, String gatawayId, String gatewaylati, String gatewaylong, String content);

    /**
     * @param macpkt： 上行的 macpayload 对象
     * @return 下行的 macpayload 对象
     */
    MacPktForm MacConstructData(MacPktForm macpkt);

    //MacPktForm MacConstructData(MacPktForm macpkt, String str);
}
