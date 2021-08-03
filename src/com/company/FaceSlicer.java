package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface FaceSlicer {
    static DoubleLine[] sliceFace(Face face, Double strokeHeight){
        Face.setFlatEdgeCount(face);
        Face.sortPoints(face);
        List<DoubleVector> pointsA = new ArrayList<>();
        List<DoubleVector> pointsB = new ArrayList<>();
        if (face.flatEdgeCount == 0){
            //Slice face with sorted points and no flat edges
            //create edges
            Line longEdge = new Line(face.points[0], face.points[2]);
            Line shortEdgeTop = new Line(face.points[1], face.points[2]);
            Line shortEdgeBottom = new Line(face.points[0], face.points[1]);
            //intersect edges with stroke height
            DoubleVector[] longEdgeIntersections = Line.intersection(longEdge, strokeHeight);
            //System.out.println("long" + longEdgeIntersections.length);
            DoubleVector[] shortEdgeTopIntersections = Line.intersection(shortEdgeTop, strokeHeight);
            //System.out.println("short1" + shortEdgeTopIntersections.length);
            DoubleVector[] shortEdgeBottomIntersections = Line.intersection(shortEdgeBottom, strokeHeight);
            //System.out.println("short2" + shortEdgeBottomIntersections.length);
            //load intersections into their respective buffers
            for (DoubleVector point : longEdgeIntersections) {pointsA.add(point);}
            for (DoubleVector point : shortEdgeTopIntersections) {pointsB.add(point);}
            if (shortEdgeTopIntersections.length > 0 && shortEdgeBottomIntersections.length > 0){
                // if both top an bottom parts intersect
                if (shortEdgeTopIntersections[shortEdgeTopIntersections.length - 1].dimensions[2] == shortEdgeBottomIntersections[0].dimensions[2]) {
                    //if last item of first list is repeat of first item of second list.
                    for (int i = 1; i < shortEdgeBottomIntersections.length; i++) {
                        pointsB.add(shortEdgeBottomIntersections[i]);
                    }
                } else {
                    for (DoubleVector point : shortEdgeBottomIntersections) {
                        pointsB.add(point);
                    }
                }
            } else {
                for (DoubleVector point : shortEdgeBottomIntersections) {
                    pointsB.add(point);
                }
            }
        } else if (face.flatEdgeCount == 1){
            //Slice face with sorted points and one flat edge
            Line edge1;
            Line edge2;
            if (face.points[0].dimensions[2] == face.points[1].dimensions[2]){
                //edge between 0 and 1 is flat
                edge1 = new Line(face.points[1], face.points[2]);
                edge2 = new Line(face.points[0], face.points[2]);
            } else {
                //edge between 1 and 2 is flat
                edge1 = new Line(face.points[0], face.points[1]);
                edge2 = new Line(face.points[0], face.points[2]);
            }
            DoubleVector[] edge1Intersections = Line.intersection(edge1, strokeHeight);
            DoubleVector[] edge2Intersections = Line.intersection(edge2, strokeHeight);
            for (DoubleVector point : edge1Intersections) {pointsA.add(point);}
            for (DoubleVector point : edge2Intersections) {pointsB.add(point);}
        }
        Iterator<DoubleVector> pointsAI = pointsA.iterator();
        Iterator<DoubleVector> pointsBI = pointsB.iterator();
        int length = pointsA.size();
        DoubleLine[] result = new DoubleLine[length];
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                result[i] = new DoubleLine(pointsAI.next(), pointsBI.next());
            }
        }
        return result;
    }
}
