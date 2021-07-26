package com.company;

import java.util.HashMap;


public class Printers {
    HashMap<String, Float[]> printers = new HashMap<String, Float[]>();

    public Printers(String s){
        if (s == "Ender 3 Pro") {
            setEnder();
        } else {
            setEnder();
        }
    }
    public static void setEnder(){
        SlicerSettingsGUI.setLayerThickness(0.2f);
        SlicerSettingsGUI.setPrintSpeed(4000f);
        SlicerSettingsGUI.setExtrusionRate(5f);
        SlicerSettingsGUI.setRetractionDistance(2f);
        SlicerSettingsGUI.setPrintVolume(400,400,400);
    }
}
