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
    public static String ToString(DoubleVector vector) {
        StringBuilder str = new StringBuilder("{");
        for (int i = 0; i <= vector.Length - 1; i++) {
            str.append(round(vector.dimensions[i], 2) + ", ");
        }
        str.append("}");
        return str.toString();
    }
    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_DOWN);
        return bd;
    }
    public static String ToStringUnrounded(DoubleVector vector) {
        StringBuilder str = new StringBuilder("{");
        for (int i = 0; i <= vector.Length - 1; i++) {
            str.append(vector.dimensions[i] + ", ");
        }
        str.append("}");
        return str.toString();
    }
    public static boolean equals(DoubleVector a, DoubleVector b){
        for (int i = 0; i < a.Length; i++){
            if (a.dimensions[i] != b.dimensions[i]){
                return false;
            }
        } return true;
    }
}
