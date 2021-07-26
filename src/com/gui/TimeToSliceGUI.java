package com.gui;

import com.company.Line;
import com.company.Mesh;
import com.company.STLParser;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class TimeToSliceGUI {


    private Dictionary<String, String> userFeedback = new Hashtable();
    private JFrame frame = new JFrame();
    private JPanel panel = new JPanel();
    private static JTextArea text = new JTextArea("Drop STL Here");

    public TimeToSliceGUI() {
        frame.setDropTarget(new DropTarget(){
            public synchronized void drop(DropTargetDropEvent event) {
                try {
                    event.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable transferable = event.getTransferable();
                    DataFlavor[] flavors = transferable.getTransferDataFlavors();
                    for (DataFlavor flavor : flavors) {
                        try {
                            if (flavor.isFlavorJavaFileListType()) {
                                List<File> files = (List<File>) transferable.getTransferData(flavor);
                                for (File file :  files) {
                                    String fileName = file.getName();
                                    if (fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase().equals("stl")){
                                        panel.remove(text);
                                        text = new JTextArea("Parsing STL:\n\t" + fileName + "\nAt Location:\n\t" + file.getPath());
                                        panel.add(text);
                                        frame.pack();
                                        List<Line[]> lines = new ArrayList();
                                        Mesh mesh = STLParser.parseSTLFile(file);
                                        Mesh.printMesh(mesh);
                                        /*int inc = 0;
                                        for (Face face : mesh.faces){
                                            lines.add(FaceSlicer.sliceFace(face, .2f));
                                            inc++;
                                            panel.remove(text);
                                            text = new JTextArea("Parsing STL:\n\t" + fileName + "\nAt Location:\n\t" + file.getPath() +
                                                    inc + " of " + mesh.faces.length + "complete");
                                            panel.add(text);
                                            frame.pack();
                                        }


                                        //ToGCode gCode = new ToGCode(lines);
                                        //System.out.println("GCode Contains " + gCode.polyLines.size() + "slice levels");
                                        //for (Float fl : gCode.polyLines.keySet()){
                                        //   System.out.println("\tLevel " + fl);
                                        //    for (PolyLine poly : gCode.polyLines.get(fl)) {
                                        //        System.out.println("\t\tNew Poly:");
                                        //        PolyLine.printPoly(poly);
                                        //    }
                                        //}
*/
                                        panel.remove(text);
                                        text = new JTextArea(fileName + "\nhas been HomeSliced");
                                        panel.add(text);
                                        frame.pack();
                                    } else {
                                        panel.remove(text);
                                        text = new JTextArea(fileName + " is the wrong type of file.\n\nSTLs Only!");
                                        panel.add(text);
                                        frame.pack();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        panel.setLayout(new GridLayout(0, 1));
        frame.add(panel, BorderLayout.CENTER);
        panel.add(text);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("HomeSlice");
        frame.pack();
        frame.setVisible(true);
    }
}
