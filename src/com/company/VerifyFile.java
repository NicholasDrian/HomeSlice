package com.company;

import java.io.File;

public class VerifyFile {
    public static boolean IsStl(File file){
        String fileName = file.getName();
        boolean bool = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase().equals("stl");
        return bool;
    }
}
