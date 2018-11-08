package com.lynch;

import redis.clients.jedis.JedisPubSub;

import java.util.Date;

/**
 * Created by lynch on 2018/9/10. <br>
 **/
public class WebTestPubSub extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        System.out.println("time:" + new Date().toString() + "   channel:" + channel + "  receives messages:" + message);

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
