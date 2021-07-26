package com.company;

import com.gui.TimeToSliceGUI;


public class Main {



    private static void testTimeToSliceGUI(){
        new TimeToSliceGUI();
    }
    private static void testHomeScreenGUI() {
        new HomeScreenGUI();
    }
    private static void testSlicerSettingsGUI(){
        new SlicerSettingsGUI();
    }
    private static void testLineIntersection() {
        //test is
        FloatVector a = new FloatVector(1,1,1.1f);
        FloatVector b = new FloatVector(0,0,0);
        Line line = new Line(a,b);
        DoubleVector[] points = Line.intersection(line, 0.2);
        for (DoubleVector point : points) {
            System.out.println(DoubleVector.ToStringUnrounded(point));
        }
    }
    private static void testFaceSlicer(){
        FloatVector A = new FloatVector(0,0,0);
        FloatVector B = new FloatVector(1,0,0);
        FloatVector C = new FloatVector(0,0,2);
        Face faceA = new Face(A,B,C);
        FaceSlicer.sliceFace(faceA, 0.2);
    }
    public static void testMeshSlicer(){
    }
    public static void main(String[] args) {
        //testTimeToSliceGUI();
        testHomeScreenGUI();
        //testSlicerSettingsGUI();
        //testLineIntersection();
        //testFaceSlicer();
    }



}


