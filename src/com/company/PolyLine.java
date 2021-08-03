package com.company;

import java.util.ArrayList;
import java.util.List;

public class PolyLine{
    boolean endSegmentsFound = false;
    boolean isAligned = false;
    boolean isStartSegment = false;
    boolean isEndSegment = false;
    DoubleVector start;
    DoubleVector end;
    int startConnection = -1; //-1 stands for no connection
    int endConnection = -1;

    static int debug = 0;
    static int debug1 = 0;
    static int debug2 = 0;
    static int debug3 = 0;

    //DEBUG
    static int lineCount = 0;


    //from line to poly
    public PolyLine(DoubleLine line){
        start = line.start;
        end = line.end;
    }

    //align poly segments with neighbors
    //make start and end segments
    public static PolyLine[] align(PolyLine[] polys) {
        for (PolyLine poly : polys){
            if (!poly.isAligned) {
                PolyLine.recursiveAlign(poly, polys);
            }
        }
        for (PolyLine poly : polys){
            if (!poly.endSegmentsFound){
                PolyLine.findEnd(poly, polys);
                PolyLine.findStart(poly, polys);
            }
        }
        PolyLine.debug = 0;
        PolyLine.debug1 = 0;
        PolyLine.debug2 = 0;
        PolyLine.debug3 = 0;
        return polys;
    }

    private static void findEnd(PolyLine poly, PolyLine[] polys) {
        poly.endSegmentsFound = true;
        if (poly.endConnection == -1 || polys[poly.endConnection].endSegmentsFound){
            //poly is start segment
            poly.isEndSegment = true;


            debug2 += 1;
            if (poly.endConnection == -1){
                debug3 += 1;
            }

        } else {
            //time for recursion!
            findEnd(polys[poly.endConnection], polys);
        }
    }

    private static void findStart(PolyLine poly, PolyLine[] polys) {
        poly.endSegmentsFound = true;
        if (poly.startConnection == -1 || polys[poly.startConnection].endSegmentsFound){
            //poly is end segment
            poly.isStartSegment = true;


            debug += 1;
            if (poly.startConnection == -1){
                debug1 += 1;
            }


        } else {
            //time for recursion!
            findEnd(polys[poly.startConnection], polys);
        }
    }

    private static void recursiveAlign(PolyLine poly, PolyLine[] polys){
        poly.isAligned = true;
        if (poly.startConnection != -1){
            //poly has previous
            if (!polys[poly.startConnection].isAligned) {
                // previous is not yet aligned
                if (!DoubleVector.equals(poly.start, polys[poly.startConnection].end)) {
                    //previous must be flipped
                    polys[poly.startConnection].flip();
                }
                recursiveAlign(polys[poly.startConnection], polys);
            }
        }
        if (poly.endConnection != -1){
            //poly has next
            if (!polys[poly.endConnection].isAligned) {
                // next is not yet aligned
                if (!DoubleVector.equals(poly.end, polys[poly.endConnection].start)) {
                    //next must be flipped
                    polys[poly.endConnection].flip();
                }
                recursiveAlign(polys[poly.endConnection], polys);
            }
        }
    }

    public static ConnectedPoly[] stringTogether(PolyLine[] polys) {
        List<ConnectedPoly> result = new ArrayList<>();
        for (PolyLine poly : polys){
            if (poly.isStartSegment){
                //string em up
                ConnectedPoly temp = new ConnectedPoly(poly);
                while (!poly.isEndSegment) {
                    poly = polys[poly.endConnection];
                    temp.addPoly(temp, poly);
                }
                result.add(temp);
            }
        }
        ConnectedPoly[] finalResult = new ConnectedPoly[result.size()];
        int i = 0;
        for (ConnectedPoly p : result){
            finalResult[i] = p;
            i++;
        }
        return finalResult;
    }

    private void flip() {
        int tempConnection = startConnection;
        DoubleVector tempPoint = start;
        startConnection = endConnection;
        start = end;
        endConnection = tempConnection;
        end = tempPoint;
    }
}
