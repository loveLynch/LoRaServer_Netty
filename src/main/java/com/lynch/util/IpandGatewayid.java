package com.lynch.util;

import org.springframework.util.ClassUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lynch on 2018/9/12. <br>
 **/
public class IpandGatewayid {
    //可创建一个list，将收到的接收数据的ip与网关一一对应，这有两个前提：
    //1.网关名字与ip一一对应且固定
    //2.网关与ip对应关系需提前告知服务器，服务器只能经过ip判断此次数据来自哪个网关
    HashMap<String, String> ipandgatewayid = new HashMap<String, String>();

    public void iptogateway() {
        // ipandgateway.put("/192.168.3.153", new String("AA555A0000000000"));
        List<String> iplist = new ArrayList<String>();
        List<String> gatewayidlist = new ArrayList<String>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(ClassUtils.getDefaultClassLoader().getResource("ip_gatewayid.txt").getFile()));

            String content = null;

            while ((content = bf.readLine()) != null) {
                String ary[] = content.trim().split("\\s+");

                iplist.add(ary[0]);
                gatewayidlist.add(ary[1]);
            }
            bf.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] ipColAry = iplist.toArray(new String[0]);
        String[] gatewayidColAry = gatewayidlist.toArray(new String[0]);
        for (int i = 0; i < ipColAry.length; i++) {
            ipandgatewayid.put(ipColAry[i], gatewayidColAry[i]);
        }


    }

    public String getgateway(String ip) {
        return ipandgatewayid.get(ip);

    }

    public HashMap<String, String> getIpandgateway() {
        return ipandgatewayid;
    }

    public void setIpandgateway(HashMap<String, String> ipandgateway) {
        this.ipandgatewayid = ipandgateway;
    }

}
