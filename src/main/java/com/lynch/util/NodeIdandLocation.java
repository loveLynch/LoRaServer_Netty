package com.lynch.util;

import org.springframework.util.ClassUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lynch on 2018/9/18. <br>
 * 终端id与其地理位置的映射
 **/
public class NodeIdandLocation {

    HashMap<String, String> nodeandlocation = new HashMap<String, String>();
    // List<String> putlist = new ArrayList<>();
    String[] nodeidColAry = null;
    String[] latiColAry = null;
    String[] longColAry = null;

    public void idtolocation() {
        List<String> nodeidlist = new ArrayList<String>();
        List<String> latilist = new ArrayList<String>();
        List<String> longlist = new ArrayList<String>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(ClassUtils.getDefaultClassLoader().getResource("nodeid_location.txt").getFile()));

            String content = null;

            while ((content = bf.readLine()) != null) {
                String ary[] = content.trim().split("\\s+");
                nodeidlist.add(ary[0]);
                latilist.add(ary[1]);
                longlist.add(ary[2]);
            }
            bf.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        nodeidColAry = nodeidlist.toArray(new String[0]);
        latiColAry = latilist.toArray(new String[0]);
        longColAry = longlist.toArray(new String[0]);

    }


    public String getnodelati(String nodeid) {
        for (int i = 0; i < nodeidColAry.length; i++) {
            nodeandlocation.put(nodeidColAry[i], latiColAry[i]);
        }
        return nodeandlocation.get(nodeid);

    }

    public String getnodelong(String nodeid) {
        for (int i = 0; i < nodeidColAry.length; i++) {
            nodeandlocation.put(nodeidColAry[i], longColAry[i]);
        }
        return nodeandlocation.get(nodeid);

    }

    public static void main(String[] args) {
        NodeIdandLocation nodeIdandLocation = new NodeIdandLocation();
        nodeIdandLocation.idtolocation();
        System.out.print(nodeIdandLocation.getnodelati("e6:7c:e6:01"));
        System.out.print(" ");
        System.out.print(nodeIdandLocation.getnodelong("e6:7c:e6:01"));
        System.out.println();
        System.out.print(nodeIdandLocation.getnodelati("59:a6:73:01"));
        System.out.print(" ");
        System.out.print(nodeIdandLocation.getnodelong("59:a6:73:01"));
    }
}

