package com.lynch.util;

import net.sf.json.JSONObject;
import redis.clients.jedis.JedisPubSub;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by lynch on 2018/6/4. <br>
 **/
public class RedisPubSub extends JedisPubSub {
    @Override
    public void onMessage(String channel, String message) {
        System.out.println("time:" + new Date().toString() + "   channel:" + channel + "  receives messages:" + message);
        JSONObject messagejson = JSONObject.fromObject(message);
        Iterator<String> iterator = messagejson.keys();
        if (messagejson.getString("commandType").equals("Light Up")) {
            String nodeId = messagejson.getString("nodeId");
            System.out.println("通知终端"+nodeId+"开灯！");
//            LoRaMain.Light_up_down = true;

        } else if (messagejson.getString("commandType").equals("Inquire Electronic")) {
            System.out.println("查询电量！");
        } else if (messagejson.getString("commandType").equals("Inquire Water")) {
            System.out.println("查询水量！");
        } else
            System.out.println("待开发！");

//      while (iterator.hasNext()) {
//            String key = iterator.next();
//            String value = messagejson.getString(key).toString();
//            System.out.println(key+": "+value);
// }

    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {


    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println("channel:" + channel + " is been subscribed:" + subscribedChannels);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println("channel:" + channel + " is been unsubscribed:" + subscribedChannels);
    }

    @Override
    public void onPUnsubscribe(String s, int i) {

    }

    @Override
    public void onPSubscribe(String s, int i) {

    }
}
