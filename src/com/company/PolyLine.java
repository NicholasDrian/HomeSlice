package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolyLine{
    boolean isAligned = false;
    boolean isStartSegment = false;
    boolean isEndSegment = false;
    List<DoubleVector> points = new ArrayList<>();
    int startConnection = -1; //-1 stands for no connection
    int endConnection = -1;
    int index;


    //from line to poly
    public static PolyLine toPoly(DoubleLine line){
        PolyLine poly = new PolyLine();
        poly.points.add(line.start);
        poly.points.add(line.end);
        return poly;
    }
    //poly and poly join
    //assumes that the two lines share a point
    //preserves the directionality of the first line.
    //CONNECTION POINT MUST BE THE END OF polyline a!
    public static PolyLine join(PolyLine a, PolyLine b){
        DoubleVector aEnd = a.points.get(0);
        PolyLine result = new PolyLine();
        //load a into result
        for (DoubleVector point : a.points){
            result.points.add(point);
        }
        if (DoubleVector.equals(aEnd, b.points.get(0))) {
            //end of first connects to start of next
            //leave out beginning of b
            for (int i = 1; i < b.points.size(); i++){
                result.points.add(b.points.get(i));
            }
        } else {
            //end of first connects to end of next
            //leave out end of b
            for (int i = b.points.size() -2; i > -1; i--){
                result.points.add(b.points.get(i));
            }
        }
        return result;
    }

    //align poly segments with neighbors
    //make start and end segments
    public static PolyLine[] align(PolyLine[] polys) {
        return polys;
    }
}
