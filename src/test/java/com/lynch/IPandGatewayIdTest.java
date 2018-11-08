package com.lynch;

import org.springframework.util.ClassUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lynch on 2018/9/12. <br>
 **/
public class IPandGatewayIdTest {


    public static void main(String[] args) {
//        ClassUtils.getDefaultClassLoader().getResource("ip_gatewayid.txt").getFile());

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

        String[] firstColAry = iplist.toArray(new String[0]);
        String[] secondColAry = gatewayidlist.toArray(new String[0]);

        System.out.println("The item in the array is: ");
        for (int i = 0; i < firstColAry.length; i++) {
            System.out.println(firstColAry[i] + "\t" + secondColAry[i]);
        }

    }


}
