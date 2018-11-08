package com.lynch;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by lynch on 2018/9/4. <br>
 **/
public class EnvTest {
    public static void main(String[] args) {
        Map<String, String> map = System.getenv();
        for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext(); ) {
            String key = itr.next();
            System.out.println(key + "=" + map.get(key));
            System.out.println(System.getProperty("java.library.path"));
        }
    }

}
