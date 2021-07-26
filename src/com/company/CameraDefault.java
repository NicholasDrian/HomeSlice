package com.company;

//"unit camera"
public interface CameraDefault {
    FloatVector position = new FloatVector(-2f,0f,0f);
    FloatVector DirectionNormalized = new FloatVector(1f,0f,0f);
    FloatVector bottomLeft = new FloatVector(0f, 0.5f, -0.5f);
    FloatVector topLeft = new FloatVector(0f, 0.5f, 0.5f);
    FloatVector bottomRight = new FloatVector(0f, -0.5f, -0.5f);
    FloatVector target = new FloatVector(1f,0f,0f);
    float distanceFromTarget = 1;
    float lensLength = 2;
    float lensXSize = 1;
    float lensYSize = 1;
    int res = 500;
}
