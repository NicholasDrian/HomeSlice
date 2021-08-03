package com.company;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class ToGCode {
    StringBuilder gCode = new StringBuilder();
    double totalPrintTime = 0;
    double totalExtrusion;

    ToGCode(Map<Double, ConnectedPoly[]> map){
        //add start code
        gCode.append(Printers.ender3ProStartCode);
        //create sorted map (least to greatest based on key)
        TreeMap<Double, ConnectedPoly[]> sorted = new TreeMap<>();
        sorted.putAll(map);
        Iterator<Double> zIter = sorted.keySet().iterator();
        Iterator<ConnectedPoly[]> connectedPolys = sorted.values().iterator();
        //skip level at z = 0;
        zIter.next();
        connectedPolys.next();
        gCode.append("G0 F" + SlicerSettingsGUI.printSpeed);
        double e = 0;
        DoubleVector prev;
        while (zIter.hasNext()){
            Double z = zIter.next();
            ConnectedPoly[] polys = connectedPolys.next();
            prev = polys[0].points.get(0);
            gCode.append("G0 Z" + z + "\n");
            for (ConnectedPoly poly : polys){
                //for each polyline
                DoubleVector next = poly.points.get(0);
                double distance = DoubleVector.distance(prev, next);
                //updateTime(distance, (double)3000);
                prev = next;
                //retract
                e -= SlicerSettingsGUI.retractionDistance;
                gCode.append("G1 E" + e + " F3000" + "\n");
                //move into place
                gCode.append("G0" + pointToG0(prev));
                //un retract
                e += SlicerSettingsGUI.retractionDistance;
                gCode.append("G1 E" + e + " F" + SlicerSettingsGUI.printSpeed + "\n");
                for (int i = 1; i < poly.points.size(); i++){
                    DoubleVector point = poly.points.get(i);
                    distance = DoubleVector.distance(prev, point);
                    updateTime(distance, (double) SlicerSettingsGUI.printSpeed);
                    e += distance * SlicerSettingsGUI.extrusionRate;
                    e = DoubleVector.round(e, 4).doubleValue();
                    gCode.append("G1" + pointToG1(point, e));
                    prev = point;
                }
            }
        }
        gCode.append(Printers.ender3ProEndCode);
        totalExtrusion = DoubleVector.round(e/1000, 2).doubleValue();
        totalPrintTime = DoubleVector.round(totalPrintTime/60,2).doubleValue();
    }
    public void updateTime(Double distance, Double speed){
        //speed in mm per min
        //distance in mm
        totalPrintTime += (distance/speed);
    }

    public static String pointToG1(DoubleVector point, Double e){
        StringBuilder result = new StringBuilder();
        result.append(" X" + point.dimensions[0]);
        result.append(" Y" + point.dimensions[1]);
        result.append(" E" + e + "\n");
        return result.toString();
    }
    public static String pointToG0(DoubleVector point){
        StringBuilder result = new StringBuilder();
        result.append(" X" + point.dimensions[0]);
        result.append(" Y" + point.dimensions[1] + "\n");
        return result.toString();
    }
}
