package com.company;

import static java.lang.Math.*;

public class Camera {

    FloatVector position;
    FloatVector DirectionNormalized;
    FloatVector bottomLeft;
    FloatVector topLeft;
    FloatVector bottomRight;
    FloatVector target;
    float lensLength;
    int res;

    float verticalRotation = 0;
    float horizontalRotation = 0;
    float scaleFactor = 1;
    FloatVector motion = new FloatVector(0,0,0);
    Plane screenPlane;

    public Camera(){
        position = CameraDefault.position;
        DirectionNormalized = CameraDefault.DirectionNormalized;
        bottomLeft = CameraDefault.bottomLeft;
        topLeft = CameraDefault.topLeft;
        bottomRight = CameraDefault.bottomRight;
        target = CameraDefault.target;
        lensLength = CameraDefault.lensLength;
        res = CameraDefault.res;
        screenPlane = new Plane(DirectionNormalized, bottomLeft);
    }

    public static void initCamera(Camera camera, float xSize, float ySize, float zSize){
        FloatVector newTarget = new FloatVector(xSize/2, ySize/2, zSize/6);
        camera.motion = newTarget;
        camera.scaleFactor = 1/(xSize+ySize);
        camera.verticalRotation = (float) -PI/6;
        Camera.updateCamera(camera);
    }

    public static FloatVector cameraScreenIntersection(Camera camera, FloatVector point) {
        Plane plane = new Plane(camera.DirectionNormalized, camera.bottomLeft);
        FloatVector direction = FloatVector.Subtract(camera.position, point);
        return projectPointOntoPlane(plane, point, direction);
    }
    public static FloatVector projectPointOntoPlane(Plane plane, FloatVector point, FloatVector direction){

        FloatVector diff = FloatVector.Subtract(point, plane.origin);
        float prod1 = FloatVector.DotProduct(diff, plane.normalNormal);
        float prod2 = FloatVector.DotProduct(direction, plane.normalNormal);
        float prod3 = prod1 / prod2;
        FloatVector result = FloatVector.Subtract(point, FloatVector.Multiply(direction, prod3));
        return result;
    }
    public static FloatVector fromRealToScreenSpace(Camera camera, FloatVector point){
        FloatVector posVec = FloatVector.Subtract(point, camera.bottomLeft);
        FloatVector screenXVec = FloatVector.Subtract(camera.bottomRight, camera.bottomLeft);
        float thetaPosVec = FloatVector.angleBetween(posVec, screenXVec);
        if (point.dimensions[2] < camera.bottomLeft.dimensions[2]){
            thetaPosVec *= -1;
        }
        float magnitudePosVectReal = FloatVector.Magnitude(posVec) * camera.res;
        float screenX = (float) ((cos(thetaPosVec) * magnitudePosVectReal));
        float screenY = (float) (camera.res - (sin(thetaPosVec) * magnitudePosVectReal));
        return new FloatVector(screenX, screenY);
    }


    public static float distanceSquaredToPolyCenter(Camera camera, FloatVector[] polyPoints){
        FloatVector average = new FloatVector(0,0, 0);
        for (FloatVector point : polyPoints){
            for (int i = 0; i < point.dimensions.length; i++){
                average.dimensions[i] += point.dimensions[i];
            }
        }
        average = FloatVector.Scale(average, (float)1/3);
        float distance = FloatVector.Magnitude(FloatVector.Subtract(camera.position, average));
        return distance;
    }

    public static Camera updateVerticalRotation(Camera camera, float theta){
        camera.verticalRotation += theta;
        if (camera.verticalRotation > PI/2 || camera.verticalRotation < -PI/2){
            camera.verticalRotation -= theta;
        }
        return camera;
    }
    public static Camera updateHorizontalRotation(Camera camera, float theta){
        camera.horizontalRotation += theta;
        return camera;
    }
    public static Camera updateScale(Camera camera, float factor){
        camera.scaleFactor *= factor;
        return camera;
    }
    public static Camera updatePosition(Camera camera, FloatVector motion){
        camera.motion = FloatVector.Subtract(camera.motion, FloatVector.Scale(motion, 1/camera.scaleFactor));
        return camera;
    }
    public static Camera updateCamera(Camera camera){
        //move target into place
        camera.target = FloatVector.Add(CameraDefault.target, camera.motion);
        //rotate camera normal
        FloatVector verticalRotationAxis = FloatVector.Subtract(CameraDefault.bottomRight, CameraDefault.bottomLeft);
        FloatVector horizontalRotationAxis = new FloatVector(0,0,1);
        camera.DirectionNormalized = FloatVector.rotateVector(CameraDefault.DirectionNormalized, verticalRotationAxis, camera.verticalRotation);
        camera.DirectionNormalized = FloatVector.rotateVector(camera.DirectionNormalized, horizontalRotationAxis, -camera.horizontalRotation);
        //place camera position
        float magnitude = -(((1/camera.scaleFactor) * CameraDefault.distanceFromTarget) + CameraDefault.lensLength);
        FloatVector motion = FloatVector.Scale(camera.DirectionNormalized, magnitude);
        camera.position = FloatVector.Add(camera.target, motion);
        //find center of screen
        motion = FloatVector.Scale(camera.DirectionNormalized, - (CameraDefault.distanceFromTarget * (1/camera.scaleFactor)));
        FloatVector center = FloatVector.Add(camera.target, motion);
        //find screen right and screen up direction vectors
        FloatVector screenRight = new FloatVector(
                camera.DirectionNormalized.dimensions[0],
                camera.DirectionNormalized.dimensions[1],
                0);
        screenRight = FloatVector.rotateVector(screenRight, horizontalRotationAxis, (float) -PI/2);
        FloatVector screenVert = FloatVector.rotateVector(screenRight, camera.DirectionNormalized, (float) -PI/2);
        //find Origin
        FloatVector origin = FloatVector.Move(center, screenRight, -CameraDefault.lensXSize/2);
        camera.bottomLeft = FloatVector.Move(origin, screenVert, -CameraDefault.lensYSize/2);
        //find bottom right
        camera.bottomRight = FloatVector.Move(camera.bottomLeft, screenRight, CameraDefault.lensXSize);
        //find top left
        camera.topLeft = FloatVector.Move(camera.bottomLeft, screenVert, CameraDefault.lensYSize);
        //update screenPlane
        camera.screenPlane = new Plane(camera.DirectionNormalized, camera.bottomLeft);

        return camera;
    }

}
