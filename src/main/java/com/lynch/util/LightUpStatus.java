package com.lynch.util;

/**
 * Created by lynch on 2018/9/19. <br>
 **/
public class LightUpStatus {
    private String nodeswitch;
    private String nodebrightness;

    public String judgeswitch(String frampayload) {
        //开深夜
        if (frampayload.equals("SUBF"))
            nodeswitch = "1";
            //开节能
        else if (frampayload.equals("SUBS")) {
            nodeswitch = "1";
        }
        //开全亮
        else if (frampayload.equals("SUBT")) {
            nodeswitch = "1";
        }
        //关
        else if (frampayload.equals("SD")) {
            nodeswitch = "0";
        }
        //故障
        else if (frampayload.equals("SUBB")) {
            nodeswitch = "1";
        }
        return nodeswitch;


    }

    public String judgebrightness(String frampayload) {
        //开深夜
        if (frampayload.equals("SUBF"))
            nodebrightness = "1";
        //开节能
        else if (frampayload.equals("SUBS")) {
            nodebrightness = "2";
        }
        //开全亮
        else if (frampayload.equals("SUBT")) {
            nodebrightness = "3";
        }
        //关
        else if (frampayload.equals("SD")) {
            nodebrightness = "0";
        }
        //故障
        else if (frampayload.equals("SUBB")) {
            nodebrightness = "0";
        }
        return nodebrightness;
    }
}
