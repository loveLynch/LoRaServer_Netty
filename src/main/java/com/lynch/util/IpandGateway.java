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
public class IpandGateway {

    HashMap<String, String> ipandgatewayid = new HashMap<String, String>();
    // List<String> putlist = new ArrayList<>();
    String[] ipColAry = null;
    String[] gatewayidColAry = null;
    String[] latiColAry = null;
    String[] longColAry = null;

    public void iptogateway() {
        // ipandgateway.put("/192.168.3.153", new String("AA555A0000000000"));
        List<String> iplist = new ArrayList<String>();
        List<String> gatewayidlist = new ArrayList<String>();
        List<String> latilist = new ArrayList<String>();
        List<String> longlist = new ArrayList<String>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(ClassUtils.getDefaultClassLoader().getResource("ip_gateway.txt").getFile()));

            String content = null;

            while ((content = bf.readLine()) != null) {
                String ary[] = content.trim().split("\\s+");
                iplist.add(ary[0]);
                gatewayidlist.add(ary[1]);
                latilist.add(ary[2]);
                longlist.add(ary[3]);
            }
            bf.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        ipColAry = iplist.toArray(new String[0]);
        gatewayidColAry = gatewayidlist.toArray(new String[0]);
        latiColAry = latilist.toArray(new String[0]);
        longColAry = longlist.toArray(new String[0]);
        // String[] putlistColary = new String[gatewayidColAry.length + latiColAry.length + longColAry.length];
        //       for (int i = 0; i < ipColAry.length; i++) {
//            System.arraycopy(gatewayidColAry, 0, putlistColary, 0, gatewayidColAry.length);
//            System.out.println(putlistColary[j]);
//            System.arraycopy(latiColAry, 0, putlistColary, gatewayidColAry.length, latiColAry.length);
//            System.out.println(putlistColary[j]);
//            System.arraycopy(longColAry, 0, putlistColary, gatewayidColAry.length + latiColAry.length, longColAry.length);
//            ipandgatewayid.put(ipColAry[j], putlistColary[j]);
//            ipandgatewayid.put(ipColAry[i], gatewayidColAry[i]);
//            ipandgatewayid.put(ipColAry[i], latiColAry[i]);
//            ipandgatewayid.put(ipColAry[i], longColAry[i]);
//}


    }


    public String getgatewayid(String ip) {
        for (int i = 0; i < ipColAry.length; i++) {
            ipandgatewayid.put(ipColAry[i], gatewayidColAry[i]);
        }
        return ipandgatewayid.get(ip);

    }

    public String getgatewaylati(String ip) {
        for (int i = 0; i < ipColAry.length; i++) {
            ipandgatewayid.put(ipColAry[i], latiColAry[i]);
        }
        return ipandgatewayid.get(ip);

    }

    public String getgatewaylong(String ip) {
        for (int i = 0; i < ipColAry.length; i++) {
            ipandgatewayid.put(ipColAry[i], longColAry[i]);
        }
        return ipandgatewayid.get(ip);

    }

    public HashMap<String, String> getIpandgateway() {
        return ipandgatewayid;
    }

    public void setIpandgateway(HashMap<String, String> ipandgateway) {
        this.ipandgatewayid = ipandgateway;
    }


    public static void main(String[] args) {
        List<String> outlist = new ArrayList<>();
        IpandGateway ipandGateway = new IpandGateway();
        ipandGateway.iptogateway();
        System.out.println(ipandGateway.getgatewayid("/192.168.3.153"));
        System.out.println(ipandGateway.getgatewaylati("/192.168.3.153"));
        System.out.println(ipandGateway.getgatewaylong("/192.168.3.153"));
    }
}
