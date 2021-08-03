package com.company;

import java.util.HashMap;


public class Printers {

    static String ender3ProStartCode = """
        M140 S60
        M104 S200
        M105
        M190 S60
        M109 S200
        M82 ;absolute extrusion mode
        G92 E0 ; Reset Extruder
        G28 ; Home all axes
        G1 Z2.0 F3000 ; Move Z Axis up little to prevent scratching of Heat Bed
        G1 X0.1 Y20 Z0.3 F5000.0 ; Move to start position
        G1 X0.1 Y200.0 Z0.3 F1500.0 E15 ; Draw the first line
        G1 X0.4 Y200.0 Z0.3 F5000.0 ; Move to side a little
        G1 X0.4 Y20 Z0.3 F1500.0 E30 ; Draw the second line
        G92 E0 ; Reset Extruder
        G1 Z2.0 F3000 ; Move Z Axis up little to prevent scratching of Heat Bed
        G1 X5 Y20 Z0.3 F5000.0 ; Move over to prevent blob squish
        G92 E0
        G92 E0
        """;

    static String ender3ProEndCode = """
        M140 S0
        M107
        G91 ;Relative positioning
        G1 E-2 F2700 ;Retract a bit
        G1 E-2 Z0.2 F2400 ;Retract and raise Z
        G1 X5 Y5 F3000 ;Wipe out
        G1 Z10 ;Raise Z more
        G90 ;Absolute positionning         
        G1 X0 Y235 ;Present print
        M106 S0 ;Turn-off fan
        M104 S0 ;Turn-off hotend
        M140 S0 ;Turn-off bed       
        M84 X Y E ;Disable all steppers but Z       
        M82 ;absolute extrusion mode
        M104 S0
        """;

    public Printers(String s) {
        if (s == "Ender 3 Pro") {
            setEnder();
        } else if (s == "Select Autofill Option") {
            // do nothing
        } else {
            setEnder();
        }
    }

    public static void setEnder() {
        SlicerSettingsGUI.setLayerThickness(0.2f);
        SlicerSettingsGUI.setPrintSpeed(800f);
        SlicerSettingsGUI.setExtrusionRate(.1f);
        SlicerSettingsGUI.setRetractionDistance(5f);
        SlicerSettingsGUI.setPrintVolume(400, 400, 400);
    }
}
