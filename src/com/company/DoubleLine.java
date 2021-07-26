package com.company;

import java.util.ArrayList;
import java.util.List;

public class DoubleLine {
    DoubleVector start;
    DoubleVector end;
    boolean usedInGCode = false;
    boolean hasNext = false;
    List<Integer> connections = new ArrayList<>();

    public DoubleLine(DoubleVector Start, DoubleVector End) {
        start = Start;
        end = End;
    }
    public DoubleLine(FloatVector Start, FloatVector End) {
        DoubleVector newStart = FloatVector.toDoubleVector(Start);
        DoubleVector newEnd = FloatVector.toDoubleVector(End);
        start = newStart;
        end = newEnd;
    }
    public static void sortPoints(DoubleLine line){
        if (line.start.dimensions[2] < line.end.dimensions[2]){
            DoubleLine.flipPoints(line);
        }
    }
    public static void flipPoints(DoubleLine line){
        DoubleVector tempStart = new DoubleVector(line.start.dimensions);
        line.start = line.end;
        line.end = tempStart;
    }
    public static String toString(DoubleLine line){
        StringBuilder result = new StringBuilder();
        result.append(DoubleVector.ToString(line.start));
        result.append("\t");
        result.append(DoubleVector.ToString(line.end));
        return result.toString();
    }
    public static String toStringUnrounded(DoubleLine line){
        StringBuilder result = new StringBuilder();
        result.append(DoubleVector.ToStringUnrounded(line.start));
        result.append("\t");
        result.append(DoubleVector.ToStringUnrounded(line.end));
        return result.toString();
    }

}
