package com.lynch.connection;


import com.lynch.handler.MessageHandler;
import com.lynch.handler.MacDataHandler;
import com.lynch.handler.ParseJsonHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by lynch on 2018/11/2. <br>
 **/
public class ConnectionService {

    private NioEventLoopGroup eventLoopGroup = null;
    private Bootstrap bootstrap = null;
    private int  up_port;
    private int  down_port;


    public ConnectionService(int up_port,int down_port) {
        this.up_port = up_port;
        this.down_port = down_port;
        this.eventLoopGroup = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
    }

    public void start() throws InterruptedException {
        bootstrap.channel(NioDatagramChannel.class)
                .group(eventLoopGroup)
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel nioDatagramChannel) throws Exception {
                        ChannelPipeline pipeline = nioDatagramChannel.pipeline();
                        pipeline.addLast("message", new MessageHandler())
                                .addLast(new ParseJsonHandler())
                                .addLast(new MacDataHandler());

                    }
                });
        List<Integer> ports = Arrays.asList(up_port, down_port);
        Collection<Channel> channels = new ArrayList<>(ports.size());
        for (int port : ports) {
            Channel serverChannel = bootstrap.bind(port).sync().channel();
            channels.add(serverChannel);
        }
        for (Channel ch : channels) {
            ch.closeFuture().sync();
        }
//        ChannelFuture future = bootstrap.bind(port).sync();
//        future.channel().closeFuture().sync();
    }

    public void stop() {
        this.eventLoopGroup.shutdownGracefully();
    }

}
