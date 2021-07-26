package com.company;


import java.util.ArrayList;
import java.util.List;

//Each KDNode stores a unique point and the indices of all connecting lines.
public class twoDNode {
    public DoubleVector point;
    public int depth;
    public List<Integer> linkedLines = new ArrayList<>();
    public twoDNode leftChild;
    public twoDNode rightChild;

    public twoDNode(DoubleVector Point, int lineIndex){
        point = Point;
        linkedLines.add(lineIndex);
        depth = 0;
    }
    //assumes node has no children
    public static void addNode(twoDNode tree, twoDNode node){
        if (DoubleVector.equals(tree.point, node.point)){
            //if point already exists in tree
            tree.linkedLines.add(node.linkedLines.get(0));
        } else {
            //put point into tree
            node.depth += 1;
            int comparativeIndex = tree.depth % 2;
            if (node.point.dimensions[comparativeIndex] < tree.point.dimensions[comparativeIndex]){
                //node goes to the left of tree
                if (tree.leftChild == null){
                    tree.leftChild = node;
                } else {
                    addNode(tree.leftChild, node);
                }
            } else {
                //node goes to the right of tree
                if (tree.rightChild == null){
                    tree.rightChild = node;
                } else {
                    addNode(tree.rightChild, node);
                }
            }
        }
    }





}
