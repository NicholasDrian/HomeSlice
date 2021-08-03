package com.company;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Line {
    FloatVector start;
    FloatVector end;

    public Line(FloatVector Start, FloatVector End){
        start = Start;
        end = End;
    }
    public Line(FloatVector Start, FloatVector direction, float length){
        start = Start;
        end = FloatVector.Move(start, direction, length);
    }

    public static void flipPoints(Line line){
        FloatVector tempStart = new FloatVector(line.start.dimensions);
        line.start = line.end;
        line.end = tempStart;
    }

    // returns points of intersection of line in top to bottom order.
    public static DoubleVector[] intersection(Line line, double strokeHeight) {
        DoubleLine dummyLine = new DoubleLine(line.start, line.end);
        DoubleLine.sortPoints(dummyLine); //top to bottom
        double startZ = dummyLine.start.dimensions[2];
        double endZ = dummyLine.end.dimensions[2];
        double deltaX = dummyLine.end.dimensions[0] - dummyLine.start.dimensions[0];
        double deltaY = dummyLine.end.dimensions[1] - dummyLine.start.dimensions[1];
        double deltaZ = endZ - startZ;
        double xStep = -(deltaX/deltaZ) * strokeHeight;
        double yStep = -(deltaY/deltaZ) * strokeHeight;
        double zStep = -strokeHeight;
        List<DoubleVector> intersections = new ArrayList<>();
        // if start is not an intersection, move to first intersection
        double verticalDistanceToFirstIntersection = ((startZ/strokeHeight) % 1)*strokeHeight;
        if (verticalDistanceToFirstIntersection != 0){
            double newX = dummyLine.start.dimensions[0] + (xStep * (verticalDistanceToFirstIntersection/strokeHeight));
            double newY = dummyLine.start.dimensions[1] + (yStep * (verticalDistanceToFirstIntersection/strokeHeight));
            double newZ = dummyLine.start.dimensions[2] + (zStep * (verticalDistanceToFirstIntersection/strokeHeight));
            dummyLine.start = new DoubleVector(newX, newY, newZ);
        }
        while (dummyLine.start.dimensions[2] >= dummyLine.end.dimensions[2]){
            //System.out.println(DoubleVector.ToStringUnrounded(dummyLine.start));
            intersections.add(dummyLine.start);
            double newX = dummyLine.start.dimensions[0] + xStep;
            double newY = dummyLine.start.dimensions[1] + yStep;
            double newZ = dummyLine.start.dimensions[2] + zStep;
            dummyLine.start = new DoubleVector(newX, newY, newZ);
        }
        Iterator<DoubleVector> iter = intersections.listIterator();
        DoubleVector[] result = new DoubleVector[intersections.size()];
        int n = 0;
        while (iter.hasNext()){
            result[n] = iter.next();
            n++;
        }
        for (int i = 0; i < result.length; i++){
            for (int j = 0; j < 3; j++){
                result[i].dimensions[j] = round(result[i].dimensions[j], 4).doubleValue();
            }
        }
        return result;
    }

    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_DOWN);
        return bd;
    }

   public static Line[] makeLineArray(Line line, FloatVector direction, float spacing, int count){
        FloatVector start = line.start;
        FloatVector end = line.end;
        Line[] result = new Line[count + 1];
        for (int i = 0; i <= count; i++){
            result[i] = new Line(start, end);
            start = FloatVector.Move(start, direction, spacing);
            end = FloatVector.Move(end, direction, spacing);
        }
        return result;
   }
}
