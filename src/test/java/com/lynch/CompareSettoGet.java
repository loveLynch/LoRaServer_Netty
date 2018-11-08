package com.lynch;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


/**
 * Created by lynch on 2018/10/17. <br>
 **/
public class CompareSettoGet {
    public static void main(String[] args) throws IOException {
        String[] standard = new String[gethextoten()];
        List<String> obtain = new ArrayList<>();
        List<Integer> rsnum = new ArrayList<>();
        for (int i = 0; i < standard.length; i++) {
//            standard[i] = Integer.toHexString(i);
//            standard[i] = addtab(standard[i]);
            standard[i] = addtab(Integer.toHexString(i));
        }
        Set<String> set = new HashSet<>();

        //获取读取流
        FileInputStream fis = new FileInputStream("loraserver2.txt");
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        for (int i = 0; i < standard.length; i++) {
            set.add(standard[i]);
        }
        while ((line = br.readLine()) != null) {
            String[] num = line.split("Fcnt:");
            for (int i = 0; i < num.length; i++) {
                if (i % 2 != 0) {
                    obtain.add(num[i].substring(0, 4));
                }
            }
        }

        //关闭读取流
        br.close();
        isr.close();
        fis.close();
        for (int j = 0; j < obtain.size(); j++) {
            if (set.contains(obtain.get(j))) {
                set.remove(obtain.get(j));
            }
        }
        //将十六进制转十进制
        for (int k = 0; k < set.size(); k++) {
            List<String> result = new ArrayList<>(set);
            rsnum.add(Integer.parseInt(result.get(k).replaceAll("^0[x|X]", ""), 16));
        }
        //十进制排序，list
        Collections.sort(rsnum);
        //结果输出十六进制
        for (int i = 0; i < rsnum.size(); i++) {
            String resultstr[] = new String[rsnum.size()];
            resultstr[i] = addtab(Integer.toHexString(rsnum.get(i)));
            System.out.println(resultstr[i]);

        }
        System.out.println("the lost total is " + rsnum.size());


    }

    public static String addtab(String str) {
        if (str.length() == 1)
            str = "0x0" + str;
        else
            str = "0x" + str;

        return str;

    }

    public static int gethextoten() {
        String s = "0xaa";
        int num = Integer.parseInt(s.replaceAll("^0[x|X]", ""), 16);
        return num+1;

    }

    @Test
    public void testhextoten() {
        String s = "0xaa";
        int num = Integer.parseInt(s.replaceAll("^0[x|X]", ""), 16);
        System.out.println(num);

    }


}
