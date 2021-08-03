package com.company;

import java.util.ArrayList;
import java.util.List;

public class ConnectedPoly {
    List<DoubleVector> points = new ArrayList<>();

    public ConnectedPoly(PolyLine poly){
        points.add(poly.start);
        points.add(poly.end);
    }
    public void addPoly(ConnectedPoly conPoly, PolyLine poly){
        conPoly.points.add(poly.end);
    }
}
