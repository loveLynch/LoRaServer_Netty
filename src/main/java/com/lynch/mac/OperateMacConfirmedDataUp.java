package com.lynch.mac;

public class OperateMacConfirmedDataUp implements OperateMacPkt{

	/**
	 * 
	 */
	@Override
	public MacPktForm MacParseData(byte[] data,String gatawayId, String syscontent) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public MacPktForm MacParseData(byte[] data, String gatawayId, String gatewaylati, String gatewaylong, String content) {
        return null;
    }

    /**
	 * 返回 MacConfirmedDataDownForm, 其中的 FRMPayload 已加密
	 */
	@Override
	public MacPktForm MacConstructData(MacPktForm macpkt) {
		// TODO Auto-generated method stub
		return null;
	}

}
