package com.lynch.mac;

public class OperateMacUnconfirmedDataDown implements OperateMacPkt{

	@Override
	public MacPktForm MacParseData(byte[] data,String gatawayId, String syscontent) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public MacPktForm MacParseData(byte[] data, String gatawayId, String gatewaylati, String gatewaylong, String content) {
        return null;
    }

    @Override
	public MacPktForm MacConstructData(MacPktForm macpkt) {
		byte[] devaddr = {0x00,0x01,0x02,0x03};
		byte[] fctrl = {0x00};
		byte[] fcnt = {0x00,0x02};
		byte[] fopts = {0x00,0x00,0x00};
		MacConfirmedDataDownForm  macconfirmed = new MacConfirmedDataDownForm();
		macconfirmed.DevAddr = devaddr;
		macconfirmed.fctrl.ADR = (fctrl[0] & 0x80 );
		macconfirmed.fctrl.RFU = (fctrl[0] & 0x40 ) ;
		macconfirmed.fctrl.ACK = (fctrl[0] & 0x20 ) ;
		macconfirmed.fctrl.FPending = (fctrl[0] & 0x10 ) ;
		macconfirmed.fctrl.FOptslen = (fctrl[0] & 0x07 ) ;
		
		macconfirmed.Fcnt = fcnt;
		macconfirmed.Fopts = fopts;
		macconfirmed.Fport = null;
		
		return macconfirmed;
	}

}
