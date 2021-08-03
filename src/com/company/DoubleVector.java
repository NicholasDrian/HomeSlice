package com.company;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleVector {
    double[] dimensions;
    int Length;
    public DoubleVector(int dimensionCount) {
        dimensions = new double[dimensionCount];
        Length = dimensions.length;
    }
    public DoubleVector(double[] dimensionsList) {
        dimensions = dimensionsList;
        Length = dimensions.length;
    }
    public DoubleVector(double a, double b, double c) {
        dimensions = new double[] {a, b, c};
        Length = 3;
    }
    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_DOWN);
        return bd;
    }
    public static boolean equals(DoubleVector a, DoubleVector b){
        for (int i = 0; i < a.Length; i++){
            if (a.dimensions[i] != b.dimensions[i]){
                return false;
            }
        } return true;
    }
    public static Double distance (DoubleVector a, DoubleVector b){
        double result = 0;
        for (int i = 0; i < a.Length; i++){
            result += Math.pow(a.dimensions[i] - b.dimensions[i], 2);
        }
        return Math.sqrt(result);
    }
}
