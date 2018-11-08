package com.lynch;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lynch on 2018/10/8. <br>
 **/
public class DataObtain {
    public static void main(String[] args) throws IOException {
        //获取读取流
        FileInputStream fis = new FileInputStream("loraserver2.txt");
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String line = "";
        while ((line = br.readLine()) != null) {
            String[] num = line.split("Fcnt:");
            String[] time = line.split("用时1：");
            for (int i = 0; i < num.length; i++) {
                if (i % 2 != 0) {
                    System.out.println(num[i].substring(0, 4));
                    //System.out.print(" ");
                }


            }

            for (int i = 0; i < time.length; i++) {
                if (i % 2 != 0) {
                    System.out.println(time[i]);
                }

            }

        }
        //关闭读取流
        br.close();
        isr.close();
        fis.close();

    }
}
