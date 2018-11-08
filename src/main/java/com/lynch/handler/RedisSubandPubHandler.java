package com.lynch.handler;

import com.lynch.util.RedisDB;
import com.lynch.util.RedisPubSub;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * Created by lynch on 2018/11/2. <br>
 **/
@Component
public class RedisSubandPubHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        RedisDB redisDB = new RedisDB();
        //redis Subscribe and Publish
        // Subscribe
        redisDB.setup();
        System.out.println("****Redis Subscribe Start****");
        Jedis jedis = redisDB.jedisPool.getResource();
        // LightDownMac listener = new LightDownMac();
        RedisPubSub listener = new RedisPubSub();
        jedis.subscribe(listener, "CommandChannel");
        //Publish
        jedis.publish("ACKChannel", "OK!");
    }
}
