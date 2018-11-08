package com.lynch;


import com.lynch.util.base64.base64__;

import java.util.Arrays;


/**
 * Created by lynch on 2018/9/21. <br>
 **/
public class ByteandHex {
    public static void main(String[] args) {
        byte ints[] = {-26, 124, -26, 1};
        String node = "e6:7c:e6:01";

        System.out.println(base64__.BytetoHex(ints));
        System.out.println(Arrays.toString(base64__.HextoByte(node)));


    }

}