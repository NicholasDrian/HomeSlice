package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Draw {

    public static Image createBackground(Camera camera) {
        BufferedImage bufferedImage = new BufferedImage(camera.res, camera.res, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, camera.res, camera.res); //create white image
        g2d.setColor(Color.gray);
        FloatVector realHorizon = Camera.projectPointOntoPlane(
                camera.screenPlane,
                camera.position,
                new FloatVector(camera.DirectionNormalized.dimensions[0], camera.DirectionNormalized.dimensions[1], 0));
        int screenHorizonHeight = (int) Camera.fromRealToScreenSpace(camera, realHorizon).dimensions[1];
        if (screenHorizonHeight < camera.res && screenHorizonHeight > 0) {
            g2d.drawLine(0, screenHorizonHeight, camera.res, screenHorizonHeight);
            g2d.setColor(NicksColors.TransparntBlue);
            g2d.fillRect(0, 0, camera.res, screenHorizonHeight);
            g2d.setColor(NicksColors.DarkGreen);
            g2d.fillRect(0, screenHorizonHeight, camera.res, camera.res - screenHorizonHeight);
        } else if (screenHorizonHeight < 0) {
            g2d.setColor(NicksColors.DarkGreen);
            g2d.fillRect(0, 0, camera.res, camera.res);
        } else {
            g2d.setColor(NicksColors.TransparntBlue);
            g2d.fillRect(0, 0, camera.res, camera.res);
        }
        g2d.dispose();
        return bufferedImage;
    }

    public static Image drawMesh (Camera camera, Mesh mesh, Image backGround) {
        Timer timer = new Timer();
        timer.start = System.nanoTime();
        Graphics g2d = backGround.getGraphics();
        g2d.setColor(Color.white);
        Polygon[] buffer = new Polygon[mesh.faces.length];
        float[] distanceSquaredBuffer = new float[mesh.faces.length];
        FloatVector[] normalBuffer = new FloatVector[mesh.faces.length];
        boolean[] isVisible = new boolean[mesh.faces.length];
        Polygon temp;
        for (int i = 0; i < mesh.faces.length; i++) {
            boolean isInCameraView = true;
            int[] xCoordinates = new int[3];
            int[] yCoordinates = new int[3];
            FloatVector[] pointBuffer = new FloatVector[3];
            for (int j = 0; j < 3; j ++){

                if (!FloatVector.isInFrontOf(mesh.faces[i].points[j], camera.screenPlane)){
                    isInCameraView = false;
                }

                FloatVector intersection = Camera.cameraScreenIntersection(camera, mesh.faces[i].points[j]);
                //System.out.println("real intersection : " + FloatVector.ToString(intersection));
                pointBuffer[j] = mesh.faces[i].points[j];
                FloatVector screenPoint = Camera.fromRealToScreenSpace(camera, intersection);
                //System.out.println("screen intersection : " + FloatVector.ToString(screenPoint));
                xCoordinates[j] = (int) screenPoint.dimensions[0];
                yCoordinates[j] = (int) screenPoint.dimensions[1];
            }
            temp = new Polygon(xCoordinates, yCoordinates, 3);
            buffer[i] = temp;
            distanceSquaredBuffer[i] = Camera.distanceSquaredToPolyCenter(camera, pointBuffer);
            normalBuffer[i] = mesh.faces[i].normal;

            isVisible[i] = isInCameraView;
        }
        // create ordered list of polygons
        List<PolyWithDistance> sortedPolys = new ArrayList<>();
        for (int i = 0; i < buffer.length; i++) {
            PolyWithDistance poly = new PolyWithDistance(buffer[i], distanceSquaredBuffer[i], normalBuffer[i]);
            //for later
            //poly = PolyWithDistance.alignNormalToVec(poly, camera.DirectionNormalized);
            if (isVisible[i]) {
                sortedPolys.add(poly);
            }
        }
        Collections.sort(sortedPolys, Comparator.comparing(PolyWithDistance::getNegativeDistance));
        /*for (PolyWithDistance poly : sortedPolys){
            System.out.println(poly.getDistance());
        }*/
        g2d.setColor(NicksColors.TransparntBlue);
        //iterate using sorted list instead
        for (int i = 0; i < sortedPolys.size(); i++){
            g2d.setColor(NicksColors.colorByNormal(sortedPolys.get(i).getNormal()));
            g2d.fillPolygon(sortedPolys.get(i).getPoly());
        }
        timer.end = System.nanoTime();
        MeshPreviewGUI.frameDuration = Timer.getInterval(timer);
        g2d.dispose();
        return backGround;
    }

    public static Image drawPrintBed (Camera camera, PrintBed printBed, Image backGround){
        drawLines(camera, printBed.printBedThickX, backGround, Color.black);
        drawLines(camera, printBed.printBedThickY, backGround, Color.black);
        return backGround;
    }

    public static Image drawLines (Camera camera, PolyLine[] lines, Image background, Color color, double z){
        Line[] newLines = new Line[lines.length];
        for (int i = 0; i < lines.length; i++){
            FloatVector start = new FloatVector(
                    (float)lines[i].points.get(0).dimensions[0],
                    (float)lines[i].points.get(0).dimensions[1],
                    (float)z);
            FloatVector end = new FloatVector(
                    (float)lines[i].points.get(1).dimensions[0],
                    (float)lines[i].points.get(1).dimensions[1],
                    (float)z);
            newLines[i] = new Line(start, end);
        }
        return drawLines(camera, newLines, background, color);
    }

    public static Image drawLines (Camera camera, Line[] lines, Image backGround, Color color){
        Line[] screenLines = new Line[lines.length];
        boolean[] isVisible = new boolean[lines.length];
        int length = lines.length;
        for (int i = 0; i < length; i ++){
            FloatVector start = lines[i].start;
            FloatVector end = lines[i].end;
            isVisible[i] = true;
            if (FloatVector.isInFrontOf(start, camera.screenPlane)){
                if (FloatVector.isInFrontOf(end, camera.screenPlane)){
                    // line is in front of camera, do nothing
                } else {
                    // only the end is behind camera
                    FloatVector direction = FloatVector.Subtract(end, start);
                    end = Camera.projectPointOntoPlane(camera.screenPlane, start, direction);
                }
            } else {
                if (FloatVector.isInFrontOf(end, camera.screenPlane)){
                    // only the start is behind camera
                    FloatVector direction = FloatVector.Subtract(start, end);
                    start = Camera.projectPointOntoPlane(camera.screenPlane, end, direction);
                } else {
                    // entire line is behind camera
                    isVisible[i] = false;
                }
            }
            start = Camera.cameraScreenIntersection(camera, start);
            start = Camera.fromRealToScreenSpace(camera, start);
            end = Camera.cameraScreenIntersection(camera, end);
            end = Camera.fromRealToScreenSpace(camera, end);
            screenLines[i] = new Line(start, end);
        }
        Graphics2D g2d = (Graphics2D) backGround.getGraphics();
        g2d.setColor(color);
        for (int i = 0; i < lines.length; i ++){
            if (isVisible[i]) {
                g2d.drawLine(
                        (int) screenLines[i].start.dimensions[0],
                        (int) screenLines[i].start.dimensions[1],
                        (int) screenLines[i].end.dimensions[0],
                        (int) screenLines[i].end.dimensions[1]);
            }
        }
        return backGround;
    }
}