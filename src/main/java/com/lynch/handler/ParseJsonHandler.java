package com.lynch.handler;

import com.lynch.domain.InfoFSKModEndForm;
import com.lynch.domain.InfoGateWayStatForm;
import com.lynch.domain.InfoLoraModEndForm;
import com.lynch.domain.UpInfoForm;
import com.lynch.util.base64.base64__;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lynch on 2018/11/2. <br>
 **/
@Component
public class ParseJsonHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null)
            System.out.println("msg=null");
        System.out.println(">>>>>>ParseJSON:");
        String jsonstr = (String) msg;
        jsonstr = jsonstr.substring(jsonstr.indexOf("{"),jsonstr.lastIndexOf("}")+1);
        System.out.println(jsonstr);

        Map<String, UpInfoForm> upInfoFormMap = new HashMap<>(3);
        InfoLoraModEndForm loraendpkt = new InfoLoraModEndForm();
        InfoFSKModEndForm fskendpkt = new InfoFSKModEndForm();
        InfoGateWayStatForm gwstat = new InfoGateWayStatForm();

        try {
            JSONObject json = new JSONObject(jsonstr);
            if (!json.isNull("rxpk")) {
                JSONArray rxpk_arry = json.getJSONArray("rxpk");
                for (int i = 0; i < rxpk_arry.length(); i++) {
                    if (rxpk_arry.getJSONObject(i).getString("modu").equals("LORA")) {
//						loraendpkt.setTime(rxpk_arry.getJSONObject(i).getString("time"));
                        loraendpkt.setTmst(rxpk_arry.getJSONObject(i).getDouble("tmst"));
                        if (!rxpk_arry.getJSONObject(i).isNull("tmms")) {
                            loraendpkt.setTmms(rxpk_arry.getJSONObject(i).getInt("tmms"));
                        }
                        loraendpkt.setChan(rxpk_arry.getJSONObject(i).getInt("chan"));
                        loraendpkt.setRfch(rxpk_arry.getJSONObject(i).getInt("rfch"));
                        loraendpkt.setFreq((float) rxpk_arry.getJSONObject(i).getDouble("freq"));
                        loraendpkt.setStat(rxpk_arry.getJSONObject(i).getInt("stat"));
                        loraendpkt.setModu(rxpk_arry.getJSONObject(i).getString("modu"));
                        loraendpkt.setDatr_lora(rxpk_arry.getJSONObject(i).getString("datr"));
                        loraendpkt.setCodr(rxpk_arry.getJSONObject(i).getString("codr"));
                        loraendpkt.setRssi(rxpk_arry.getJSONObject(i).getInt("rssi"));
                        loraendpkt.setLsnr(rxpk_arry.getJSONObject(i).getInt("lsnr"));
                        loraendpkt.setSize(rxpk_arry.getJSONObject(i).getInt("size"));
                        loraendpkt.setData(base64__.decode(rxpk_arry.getJSONObject(i).getString("data")));
                    } else {
                        fskendpkt.setTime(rxpk_arry.getJSONObject(i).getString("time"));
                        fskendpkt.setTmst(rxpk_arry.getJSONObject(i).getInt("tmst"));
                        if (!rxpk_arry.getJSONObject(i).isNull("tmms")) {
                            fskendpkt.setTmms(rxpk_arry.getJSONObject(i).getInt("tmms"));
                        }
                        fskendpkt.setChan(rxpk_arry.getJSONObject(i).getInt("chan"));
                        fskendpkt.setRfch(rxpk_arry.getJSONObject(i).getInt("rfch"));
                        fskendpkt.setFreq(rxpk_arry.getJSONObject(i).getInt("freq"));
                        fskendpkt.setStat(rxpk_arry.getJSONObject(i).getInt("stat"));
                        fskendpkt.setModu(rxpk_arry.getJSONObject(i).getString("modu"));
                        fskendpkt.setDatr_fsk(rxpk_arry.getJSONObject(i).getInt("datr"));
                        fskendpkt.setRssi(rxpk_arry.getJSONObject(i).getInt("rssi"));
                        fskendpkt.setSize(rxpk_arry.getJSONObject(i).getInt("size"));
                        fskendpkt.setData(base64__.decode(rxpk_arry.getJSONObject(i).getString("data")));
                    }
                }
            }
            if (!json.isNull("stat")) {
                JSONObject stat = json.getJSONObject("stat");
                gwstat.time = stat.getString("time");
//				gwstat.lati = stat.getInt("lati");
//				gwstat.longe = stat.getInt("long");
//				gwstat.alti = stat.getInt("alti");
                gwstat.rxnb = stat.getInt("rxnb");
                gwstat.rxok = stat.getInt("rxok");
                gwstat.rxfw = stat.getInt("rxfw");
                gwstat.ackr = stat.getInt("ackr");
                gwstat.dwnb = stat.getInt("dwnb");
                gwstat.txnb = stat.getInt("txnb");
                if (!stat.isNull("error")) {
                    gwstat.ackr = stat.getInt("ackr");
//					gwstat.alti = stat.getInt("alti");
                    gwstat.dwnb = stat.getInt("dwnb");
//					gwstat.lati = stat.getInt("lati");
//					gwstat.longe = stat.getInt("long");
                    gwstat.rxfw = stat.getInt("rxfw");
                    gwstat.rxnb = stat.getInt("rxnb");
                    gwstat.rxok = stat.getInt("rxok");
                    gwstat.time = stat.getString("time");
                    gwstat.txnb = stat.getInt("txnb");
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            upInfoFormMap.put("loraendpkt", loraendpkt);
            upInfoFormMap.put("fskendpkt", fskendpkt);
            upInfoFormMap.put("gwstat", gwstat);
            ctx.fireUserEventTriggered(loraendpkt);
        }
    }
}
