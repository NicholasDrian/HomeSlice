package com.company;

import java.util.Arrays;
import java.util.Comparator;

public class Face{
    FloatVector[] points;
    FloatVector normal;
    short flatEdgeCount;
    
    public Face(FloatVector[] Points, FloatVector Normal){
        points = Points;
        normal = Normal;
    }

    public Face(FloatVector a, FloatVector b, FloatVector c){
        points = new FloatVector[]{a,b,c};
    }

    public static void setFlatEdgeCount(Face face){
        float z0 = face.points[0].dimensions[2];
        float z1 = face.points[1].dimensions[2];
        float z2 = face.points[2].dimensions[2];
        short flatEdgeCounter = 0;
        if (z0 == z1){ flatEdgeCounter ++;}
        if (z1 == z2){ flatEdgeCounter ++;}
        if (z2 == z0){ flatEdgeCounter ++;}
        face.flatEdgeCount = flatEdgeCounter;
    }

    public static void sortPoints(Face face) {
        Arrays.sort(face.points, Comparator.comparing(FloatVector::getZ));
    }

    public static void printFace(Face face) {
        System.out.println("Face");
        for (FloatVector point : face.points){
            System.out.println(FloatVector.ToString(point));
        }
    }
}