package com.lynch;

import redis.clients.jedis.Jedis;


/**
 * Created by lynch on 2018/9/10. <br>
 **/
public class TestWebSub {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        WebTestPubSub listener = new WebTestPubSub();
        jedis.subscribe(listener, "ACKChannel");

    }

}
