package com.company;

import java.util.*;

public class MeshSlicer {
    Map<Double, List<DoubleLine>> lineLists = new HashMap<>();  //one list per print level
    Map<Double, PolyLine[]> polyLineArrays = new HashMap<>(); //one array per level
    Map<Double, twoDNode> pointTrees = new HashMap<>(); //one tree per level
    Map<Double, ConnectedPoly[]> connectedPolys = new HashMap<>(); //connected version of previous polyline arrays

    //sets up the
    public MeshSlicer(Mesh mesh, Double strokeHeight){
        // slice each face and put its lines in a dictionary.
        DoubleLine[] temp;
        for (Face face : mesh.faces){
            temp = FaceSlicer.sliceFace(face, strokeHeight);
            for (DoubleLine line : temp){
                //make sure line has size
                if (!DoubleVector.equals(line.start, line.end)) {
                    if (lineLists.containsKey(line.start.dimensions[2])) {
                        lineLists.get(line.start.dimensions[2]).add(line);
                    } else {
                        List<DoubleLine> lines = new ArrayList();
                        lines.add(line);
                        lineLists.put(line.start.dimensions[2], lines);
                    }
                }
            }
        }
        //build PolyLine arrays
        Iterator<List<DoubleLine>> listIter = lineLists.values().iterator();
        Iterator<Double> zIter = lineLists.keySet().iterator();
        while (zIter.hasNext()){
            List<DoubleLine> list = listIter.next();
            Iterator<DoubleLine> iter = list.iterator();
            PolyLine[] polyLineArray = new PolyLine[list.size()];
            int j = 0;
            while (iter.hasNext()){
                polyLineArray[j] = new PolyLine(iter.next());
                j++;
            }
            polyLineArrays.put(zIter.next(), polyLineArray);
        }

        //build a 2d tree per print level
        Iterator<PolyLine[]> arrayIter = polyLineArrays.values().iterator();
        zIter = polyLineArrays.keySet().iterator();
        while (zIter.hasNext()){
            PolyLine[] polyLineArray = arrayIter.next();
            twoDNode tree = new twoDNode(polyLineArray[0].start, 0);
            twoDNode tempNode;
            for (int i = 1; i < polyLineArray.length; i++){
                tempNode = new twoDNode(polyLineArray[i].start, i);
                twoDNode.addNode(tree, tempNode);
            }
            for (int i = 0; i < polyLineArray.length; i++){
                tempNode = new twoDNode(polyLineArray[i].end, i);
                twoDNode.addNode(tree, tempNode);
            }
            pointTrees.put(zIter.next(), tree);
        }
        //add index of connections to each polyline (polylines have only one segment and one possible connection per end)
        Iterator<twoDNode> trees = pointTrees.values().iterator();
        Iterator<PolyLine[]> polys = polyLineArrays.values().iterator();
        zIter = polyLineArrays.keySet().iterator();
        while (zIter.hasNext()){
            twoDNode tree = trees.next();
            List<LinePoint> linePointList = MeshSlicer.toLinePoints(tree);
            Iterator<LinePoint> linePointIterator = linePointList.listIterator();
            PolyLine[] polyArray = polys.next();
            while (linePointIterator.hasNext()) {
                LinePoint linePoint = linePointIterator.next();
                DoubleVector point = linePoint.point;
                int[] connections = linePoint.connections;
                for (int i = 0; i < connections.length - 1; i++) {
                    for (int j = i + 1; j < connections.length; j++) {
                        //for each pair of possible connections at current line point, if possible, make connection.
                        boolean pointIsStartOfI;
                        boolean pointIsStartOfJ;
                        if (DoubleVector.equals(point, polyArray[connections[i]].start)) {
                            pointIsStartOfI = true;
                        } else pointIsStartOfI = false;
                        if (DoubleVector.equals(point, polyArray[connections[j]].start)) {
                            pointIsStartOfJ = true;
                        } else pointIsStartOfJ = false;
                        boolean iIsAvailable;
                        boolean jIsAvailable;
                        if (pointIsStartOfI) {
                            if (polyArray[connections[i]].startConnection == -1) {
                                iIsAvailable = true;
                            } else {
                                iIsAvailable = false;
                            }
                        } else if (polyArray[connections[i]].endConnection == -1) {
                            iIsAvailable = true;
                        } else {
                            iIsAvailable = false;
                        }

                        if (pointIsStartOfJ) {
                            if (polyArray[connections[j]].startConnection == -1) {
                                jIsAvailable = true;
                            } else {
                                jIsAvailable = false;
                            }
                        } else if (polyArray[connections[j]].endConnection == -1) {
                            jIsAvailable = true;
                        } else {
                            jIsAvailable = false;
                        }

                        if (iIsAvailable && jIsAvailable) {
                            //connection is open! make connection!
                            if (pointIsStartOfI) {
                                polyArray[connections[i]].startConnection = connections[j];
                            } else {
                                polyArray[connections[i]].endConnection = connections[j];
                            }
                            if (pointIsStartOfJ) {
                                polyArray[connections[j]].startConnection = connections[i];
                            } else {
                                polyArray[connections[j]].endConnection = connections[i];
                            }
                        }
                    }
                }
            }
            zIter.next();
        }
        //make polyline segments directionally aligned
        //mark start and end segments
        polys = polyLineArrays.values().iterator();
        while (polys.hasNext()){
            PolyLine.align(polys.next());
        }
        polys = polyLineArrays.values().iterator();
        zIter = polyLineArrays.keySet().iterator();
        while (polys.hasNext()){
            // string together lines
            ConnectedPoly[] result = PolyLine.stringTogether(polys.next());
            connectedPolys.put(zIter.next(), result);
        }
    }

    public static List<LinePoint> toLinePoints (twoDNode tree) {

        //build a list
        List<LinePoint> result = new ArrayList<>();

        //add current node to list
        int[] links = new int[tree.linkedLines.size()];
        int i = 0;
        for (Integer lineIndex: tree.linkedLines){
            links[i] = lineIndex;
            i++;
        }
        result.add(new LinePoint(tree.point, links));

        //add recursive nodes to list
        if (tree.leftChild != null){
            result.addAll(toLinePoints(tree.leftChild));
        } if (tree.rightChild != null){
            result.addAll(toLinePoints(tree.rightChild));
        }
        return result;
    }
}
