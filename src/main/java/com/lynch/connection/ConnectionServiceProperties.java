package com.lynch.connection;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by lynch on 2018/11/2. <br>
 **/
@ConfigurationProperties(prefix = "loraServer")
public class ConnectionServiceProperties {
    public static final int CONNECTION_UPPORT = 1780;
    public static final int CONNECTION_DOWNPORT = 1782;
    public static final String REDIS_ADDRESS = "127.0.0.1";
    public static final int REDIS_PORT = 6379;

    private int up_port = CONNECTION_UPPORT;
    private int down_port = CONNECTION_DOWNPORT;

    public String getRedis_address() {
        return redis_address;
    }

    public void setRedis_address(String redis_address) {
        this.redis_address = redis_address;
    }

    public int getRedis_port() {
        return redis_port;
    }

    public void setRedis_port(int redis_port) {
        this.redis_port = redis_port;
    }

    private String redis_address = REDIS_ADDRESS;
    private int redis_port = REDIS_PORT;

    public int getUp_port() {
        return up_port;
    }

    public void setUp_port(int up_port) {
        this.up_port = up_port;
    }

    public int getDown_port() {
        return down_port;
    }

    public void setDown_port(int down_port) {
        this.down_port = down_port;
    }
}
