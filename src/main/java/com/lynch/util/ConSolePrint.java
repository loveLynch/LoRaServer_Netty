package com.lynch.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by lynch on 2018/9/28. <br>
 **/
public class ConSolePrint {
    public static void consoleprint() throws FileNotFoundException {
        File f = new File("loraserver2.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(f, true);
        PrintStream printStream = new PrintStream(fileOutputStream);
        System.setOut(printStream);

    }
}
