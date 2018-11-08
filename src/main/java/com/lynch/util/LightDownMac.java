package com.lynch.util;

import net.sf.json.JSONObject;
import redis.clients.jedis.JedisPubSub;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by lynch on 2018/9/20. <br>
 **/
public class LightDownMac extends JedisPubSub {
    public static String nodeId = "";
    public static String brightness = "";
    public static String nodeswitch = "";

    @Override
    public void onMessage(String channel, String message) {
        System.out.println("time:" + new Date().toString() + "   channel:" + channel + "  receives messages:" + message);
        JSONObject messagejson = JSONObject.fromObject(message);
        Iterator<String> iterator = messagejson.keys();
        if (messagejson.getString("commandType").equals("1")) {
            nodeId = messagejson.getString("nodeId");
            System.out.println("通知终端" + nodeId + "开灯！");
            nodeswitch = "1";
        } else if (messagejson.getString("commandType").equals("0")) {
            nodeId = messagejson.getString("nodeId");
            System.out.println("通知终端" + nodeId + "关灯！");
            nodeswitch = "0";

        } else if (messagejson.getString("commandType").equals("1,1")) {
            nodeId = messagejson.getString("nodeId");
            System.out.println("通知终端" + nodeId + "深夜模式！");
            brightness = "1";
            nodeswitch = "1";

        } else if (messagejson.getString("commandType").equals("1,2")) {
            nodeId = messagejson.getString("nodeId");
            System.out.println("通知终端" + nodeId + "节能模式！");
            brightness = "2";
            nodeswitch = "1";

        } else if (messagejson.getString("commandType").equals("1,3")) {
            nodeId = messagejson.getString("nodeId");
            System.out.println("通知终端" + nodeId + "全亮模式！");
            brightness = "3";
            nodeswitch = "1";

        }


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
