package com.company;

public interface TestTriangles {
    FloatVector one = new FloatVector(0,0,0);
    FloatVector two = new FloatVector(1,0,0);
    FloatVector three = new FloatVector(1,1,0);
    FloatVector four = new FloatVector(0,1,0);
    FloatVector five = new FloatVector(0,0,1);
    FloatVector six = new FloatVector(1,0,1);
    FloatVector seven = new FloatVector(1,1,1);
    FloatVector eight = new FloatVector(0,1,1);
    Face allFlat = new Face(new FloatVector[]{one, two, three}, five);
    Face flatBottom = new Face(new FloatVector[]{one, two, five}, four);
}
