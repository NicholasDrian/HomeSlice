package com.company;

//consists of a grid of lines spaced 10 units apart.
public class PrintBed {
    public Line[] printBedThickX;
    public Line[] printBedThickY;
    public PrintBed(int xSize, int ySize){
        FloatVector origin = new FloatVector(0,0,0);
        FloatVector xDirection = new FloatVector(1,0,0);
        FloatVector yDirection = new FloatVector(0,1,0);
        Line xAxis = new Line(origin, xDirection, ySize);
        Line yAxis = new Line(origin, yDirection, xSize);
        printBedThickX = Line.makeLineArray(xAxis, yDirection, 10, Math.floorDiv(xSize, 10));
        printBedThickY = Line.makeLineArray(yAxis, xDirection, 10, Math.floorDiv(ySize, 10));
    }
}
