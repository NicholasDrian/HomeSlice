package com.company;

import java.awt.*;

import static java.lang.Math.PI;

public interface NicksColors {
    Color VeryDarkBlue = new Color(0, 0, 100);
    Color DarkBlue = new Color(0, 0, 150);
    Color Blue = new Color(0, 0, 200);
    Color LightBlue = new Color(100, 100, 250);
    Color TransparntBlue = new Color(0,0,250, 50);
    Color VeryDarkRed = new Color(100, 0, 0);
    Color DarkRed = new Color(150, 0, 0);
    Color Red = new Color(200, 0, 0);
    Color BrightRed = new Color(255, 0, 0);
    Color DarkGreen = new Color(0, 100, 0);
    Color Green = new Color(0, 120, 0);
    Color LightGreen = new Color(0, 200, 0);
    Color BrightGreen = new Color(0, 255, 0);
    Color DarkYellow =  new Color(200, 200, 0);
    Color Yellow =  new Color(255, 255, 0);

    static Color next(){
        return Color.getHSBColor((float)Math.random(), 1, 0.5f);
    }

    static Color colorByNormal(FloatVector normal){
        FloatVector flatVersion = new FloatVector(normal.dimensions[0], normal.dimensions[1], 0);
        float angle = FloatVector.angleBetween(flatVersion, normal);
        if (normal.dimensions[2] < 0) { angle *= -1; }
        angle += PI/2;
        angle /= PI;
        angle *= 255;
        if (angle > 255) {angle = 255;}
        if (angle < 0) {angle = 0;}
        return new Color(70,70,(int)angle);
    }
}


