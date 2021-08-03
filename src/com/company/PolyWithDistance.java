package com.company;

import java.awt.*;

import static java.lang.Math.PI;
 //Packages a Polygon with a few of its key attributes.
public class PolyWithDistance {
    Polygon poly;
    float distanceSquared;
    FloatVector normal;
    public PolyWithDistance(Polygon Poly, float Distance, FloatVector Normal){
        poly = Poly;
        distanceSquared = Distance;
        normal = Normal;
    }
    public float getNegativeDistance(){
        return -distanceSquared;
    }
    public Polygon  getPoly(){
        return poly;
    }
    public FloatVector getNormal(){ return normal;}
}
