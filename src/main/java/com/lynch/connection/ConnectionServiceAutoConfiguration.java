package com.lynch.connection;

import com.lynch.util.RedisDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lynch on 2018/11/2. <br>
 **/
@Configuration
@EnableConfigurationProperties(ConnectionServiceProperties.class)
@ConditionalOnClass(ConnectionService.class)
@ConditionalOnProperty(prefix = "loraServer", value = "enable", matchIfMissing = true)
public class ConnectionServiceAutoConfiguration {

    @Autowired
    private ConnectionServiceProperties connectionServiceProperties;


    @Bean
    @ConditionalOnMissingBean(ConnectionService.class)
    public ConnectionService connectionService() throws InterruptedException {
        RedisDB.setPORT(connectionServiceProperties.getRedis_port());
        RedisDB.setIP(connectionServiceProperties.getRedis_address());
        ConnectionService service = new ConnectionService(connectionServiceProperties.getUp_port(),connectionServiceProperties.getDown_port());
        return service;
    }
}
