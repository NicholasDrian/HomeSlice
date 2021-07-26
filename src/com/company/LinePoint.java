package com.company;

public class LinePoint {
    public DoubleVector point;
    public int[] connections;
    LinePoint(DoubleVector Point, int[] Connections){
        point = Point;
        connections = Connections;
    }
}
