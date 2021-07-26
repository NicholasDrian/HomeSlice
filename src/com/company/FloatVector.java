package com.company;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import static java.lang.Math.*;

public class FloatVector {
    float[] dimensions;
    int Length;
    public FloatVector(int dimensionCount) {
        dimensions = new float[dimensionCount];
        Length = dimensions.length;
    }
    public FloatVector(float[] dimensionsList) {
        dimensions = dimensionsList;
        Length = dimensions.length;
    }
    public FloatVector(float a, float b, float c) {
        dimensions = new float[] {a, b, c};
        Length = 3;
    }
    public FloatVector(float a, float b) {
        dimensions = new float[] {a, b};
        Length = 2;
    }
    public static FloatVector Add(FloatVector a, FloatVector b){
        float[] result = new float[3];
        for (int i = 0; i < result.length; i++){
            result[i] = a.dimensions[i] + b.dimensions[i];
        }
        return new FloatVector(result);
    }
    public static FloatVector Subtract(FloatVector a, FloatVector b){
        float[] result = new float[3];
        for (int i = 0; i < result.length; i++){
            result[i] = a.dimensions[i] - b.dimensions[i];
        }
        return new FloatVector(result);
    }
    public static FloatVector Multiply(FloatVector a, float b){
        float[] result = new float[3];
        for (int i = 0; i < result.length; i++){
            result[i] = a.dimensions[i] * b;
        }
        return new FloatVector(result);
    }

    public static float DotProduct(FloatVector a, FloatVector b) {
        float result = 0;
        for (int i = 0; i < a.Length; i++) {
            result += a.dimensions[i] * b.dimensions[i];
        }
        return result;
    }

    public static FloatVector CrossProduct(FloatVector a, FloatVector b) {
        float[] result = new float[a.Length];
        for (int i = 0; i < a.Length; i++) {
            result[i] = a.dimensions[(i + 1) % a.Length] * b.dimensions[(i + 2) % a.Length];
            result[i] -= a.dimensions[(i + 2) % a.Length] * b.dimensions[(i + 1) % a.Length];
        }
        return new FloatVector(result);
    }

    public static String ToString(FloatVector vector) {
        StringBuilder str = new StringBuilder("{");
        for (int i = 0; i <= vector.Length - 1; i++) {
            str.append(round(vector.dimensions[i], 2) + ", ");
        }
        str.append("}");
        return str.toString();
    }
    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_DOWN);
        return bd;
    }

    public static float Magnitude(FloatVector vector) {
        float result = 0;
        for (float i : vector.dimensions) {
            result += pow(i, 2);
        }
        return (float) pow(result, .5);
    }

    public static float DistanceSquared(FloatVector a, FloatVector b){
        float result = 0;
        FloatVector c = FloatVector.Subtract(a,b);
        for (float i : c.dimensions) {
            result += pow(i, 2);
        }
        return result;
    }

    public static FloatVector[] sortByDimension(int Dimension, FloatVector[] Vectors){
        if (Vectors.length == 1 || Vectors.length == 0) {
            return Vectors;
        } else {
            int mid = (Vectors.length/2);
            FloatVector[] firstHalf = sortByDimension(Dimension, Arrays.copyOfRange(Vectors, 0 , mid ));
            FloatVector[] secondHalf = sortByDimension(Dimension, Arrays.copyOfRange(Vectors, mid , Vectors.length-1 ));
            FloatVector[] sorted = new FloatVector[Vectors.length];
            if (firstHalf[0].dimensions[Dimension] >= secondHalf[0].dimensions[Dimension]){
                sorted = FloatVector.Merge(secondHalf, firstHalf);
            } else {
                sorted = FloatVector.Merge(firstHalf, secondHalf);
            }
            return sorted;
        }
    }

    private static FloatVector[] Merge(FloatVector[] firstHalf, FloatVector[] secondHalf) {
        int totalLength = firstHalf.length + secondHalf.length;
        FloatVector[] result = new FloatVector [totalLength];
        for (int i = 0; i < firstHalf.length; i ++){
            result[i] = firstHalf[i];
        }
        for (int i = 0; i < secondHalf.length; i ++){
            result[i+firstHalf.length] = secondHalf[i];
        }
        return result;
    }

    public static FloatVector Normalize(FloatVector vect) {
        float scaleFactor = 1 / FloatVector.Magnitude(vect);
        for (int i = 0; i < vect.Length; i ++){
            vect.dimensions[i] *= scaleFactor;
        }
        return vect;
    }

    public static FloatVector Scale(FloatVector vec, float num){
        FloatVector ans = new FloatVector(new float[vec.Length]);
        for (int i = 0; i < vec.Length; i++){
            ans.dimensions[i] = num * vec.dimensions[i];
        }
        return ans;
    }

    public static FloatVector ScaleCentered(FloatVector vect, FloatVector center, float num) {
        for (int i = 0; i < vect.Length; i++) {
            vect.dimensions[i] = vect.dimensions[i] + ((vect.dimensions[i] - center.dimensions[i]) * (num - 1));
        }
        return vect;
    }

    public static FloatVector rotateVector(FloatVector vec, FloatVector axis, float theta) {
        axis = FloatVector.Normalize(axis);
        float x, y, z;
        float u, v, w;
        x = vec.dimensions[0]; y = vec.dimensions[1]; z = vec.dimensions[2];
        u = axis.dimensions[0]; v = axis.dimensions[1]; w = axis.dimensions[2];
        float dot = u * x + v * y + w * z;
        float xPrime = (float) (u * dot * (1d - Math.cos(theta))
                + x * Math.cos(theta)
                + (-w * y + v * z) * Math.sin(theta));
        float yPrime = (float) (v * dot * (1d - Math.cos(theta))
                + y * Math.cos(theta)
                + (w * x - u * z) * Math.sin(theta));
        float zPrime = (float) (w * dot * (1d - Math.cos(theta))
                + z * Math.cos(theta)
                + (-v * x + u * y) * Math.sin(theta));
        FloatVector result = new FloatVector(xPrime, yPrime, zPrime);
        return result;
    }

    public static float angleBetween(FloatVector a, FloatVector b){
        float aMagnitude = FloatVector.Magnitude(a);
        float bMagnitude = FloatVector.Magnitude(b);
        float thetaPosVect = (float) acos((FloatVector.DotProduct(a, b))/(aMagnitude * bMagnitude));
        if (FloatVector.DotProduct(a, b) == 0){
            thetaPosVect = (float) PI / 2;
        }
        return thetaPosVect;
    }

    public static FloatVector Move(FloatVector vec, FloatVector direction, float distance){
        FloatVector result;
        FloatVector motion = FloatVector.Scale(FloatVector.Normalize(direction), distance);
        result = FloatVector.Add(vec, motion);
        return result;
    }

    public static boolean isInFrontOf (FloatVector point, Plane plane){
        float projection = FloatVector.DotProduct(FloatVector.Subtract(point, plane.origin), plane.normalNormal);
        if ( projection > 0 ){
            return true;
        } return false;
    }
    //rotate a 2d point half way around another 2d point
    public static FloatVector rotate180In2D(FloatVector point, FloatVector center){
        float newX = center.dimensions[0] - (point.dimensions[0] - center.dimensions[0]);
        float newY = center.dimensions[1] - (point.dimensions[1] - center.dimensions[1]);
        return new FloatVector(newX, newY);
    }

    public float getZ(){
        return dimensions[2];
    }

    public static DoubleVector toDoubleVector(FloatVector floatVector){
        return new DoubleVector(floatVector.dimensions[0], floatVector.dimensions[1], floatVector.dimensions[2]);
    }

}