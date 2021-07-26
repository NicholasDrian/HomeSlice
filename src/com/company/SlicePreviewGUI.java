package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;

public class SlicePreviewGUI extends JFrame implements ActionListener {

    SlicerSettingsGUI settings;

    private static Container c;
    private static JLabel title;
    private static JButton backToSettings;
    private static JButton previewMesh;
    private static JButton previewPrintPath;
    private static JButton stlHere;
    private static JButton slice;
    private static boolean meshParsed = false;
    private static boolean meshSliced = false;
    private static Mesh mesh;
    private static Map<Double, PolyLine[]> printPath;
    private static JTextArea usereFeedback;
    private static String userFeedbackString = "Parsing Stl...";


    SlicePreviewGUI(File file, SlicerSettingsGUI Settings){
        setTitle("HomeSlice -> Slice Preview");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        settings = Settings;

        c = getContentPane();
        c.setLayout(null);
        c.setBackground(NicksColors.VeryDarkBlue);

        title = new JLabel("Processing...");
        title.setFont(new Font("Arial", Font.PLAIN, 50));
        title.setSize(400, 55);
        title.setLocation(30, 30);
        title.setForeground(Color.WHITE);
        c.add(title);


        backToSettings = new JButton("Back to Settings");
        backToSettings.setFont(new Font("Arial", Font.PLAIN, 15));
        backToSettings.setSize(260, 30);
        backToSettings.setLocation(30, 490);
        backToSettings.addActionListener(this);
        backToSettings.setBackground(NicksColors.DarkBlue);
        backToSettings.setForeground(Color.WHITE);
        c.add(backToSettings);

        previewMesh = new JButton("Preview STL");
        previewMesh.setFont(new Font("Arial", Font.PLAIN, 15));
        previewMesh.setSize(200, 30);
        previewMesh.setLocation(320, 490);
        previewMesh.addActionListener(this);
        previewMesh.setBackground(NicksColors.DarkYellow);
        c.add(previewMesh);

        previewPrintPath = new JButton("Preview Print Path");
        previewPrintPath.setFont(new Font("Arial", Font.PLAIN, 15));
        previewPrintPath.setSize(200, 30);
        previewPrintPath.setLocation(550, 490);
        previewPrintPath.addActionListener(this);
        previewPrintPath.setBackground(NicksColors.DarkYellow);
        c.add(previewPrintPath);

        usereFeedback = new JTextArea(userFeedbackString);
        usereFeedback.setFont(new Font("Arial", Font.PLAIN, 15));
        usereFeedback.setSize(400, 300);
        usereFeedback.setLocation(30, 100);
        usereFeedback.setBackground(NicksColors.VeryDarkBlue);
        usereFeedback.setForeground(Color.WHITE);
        c.add(usereFeedback);


        stlHere = new JButton("Drop new .stl file here");
        stlHere.setFont(new Font("Arial", Font.PLAIN, 30));
        stlHere.setSize(400, 60);
        stlHere.setLocation(30, 410);
        stlHere.setBackground(Color.WHITE);
        stlHere.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent event) {
                try {
                    event.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable transferable = event.getTransferable();
                    DataFlavor[] flavors = transferable.getTransferDataFlavors();
                    for (DataFlavor flavor : flavors) {
                        try {
                            if (flavor.isFlavorJavaFileListType()) {
                                java.util.List<File> files = (List<File>) transferable.getTransferData(flavor);
                                for (File file : files) {
                                    dispose();
                                    new SlicePreviewGUI(file, settings);
                                }
                            }
                        } catch (Exception ex){

                        }
                    }
                } catch (Exception ex){

                }
            }
        });
        c.add(stlHere);

        slice = new JButton("Almost ready to slice!");
        slice.setFont(new Font("Arial", Font.PLAIN, 30));
        slice.setSize(400, 60);
        slice.setLocation(440, 410);
        slice.setBackground(NicksColors.DarkYellow);
        slice.addActionListener(this);
        c.add(slice);

        setVisible(true);

        //for debug if file hasn't been chosen yet.
        if (file != null){
            try {
                mesh = STLParser.parseSTLFile(file);
                updateUserFeedback();
                title.setText("File Processed");
                slice.setText("Slice it!");
                meshParsed = true;
                slice.setBackground(NicksColors.LightGreen);
                previewMesh.setBackground(NicksColors.LightGreen);
            } catch(Exception e) {
                System.out.println("couldn't parse file");
            }
        }
    }

    private static void updateUserFeedback(){
        String s = "";
        if (mesh != null){
            s = s + "STL Parsed!\n\n";
            s = s + mesh.name + " contains " + mesh.faces.length + " mesh faces.\n\n";
            s = s + "Press HomeSlice to begin slicing!";
            usereFeedback.setText(s);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backToSettings){
            settings.setVisible(true);
            dispose();
        } else if (e.getSource() == previewMesh) {
            if (meshParsed){
                new MeshPreviewGUI(this, mesh);
                setVisible(false);
            }
        } else if (e.getSource() == slice){
            if (meshParsed){
                slice.setText("Slicing!");
                MeshSlicer slicer = new MeshSlicer(mesh, (double)SlicerSettingsGUI.layerThickness); //GET REAL STROKE HEIGHT!
                meshSliced = true;
                slice.setText("Sliced!");
                printPath = slicer.polyLineArrays; //update this to the connected version of print path
                previewPrintPath.setBackground(NicksColors.LightGreen);
            }
        } else if (e.getSource() == previewPrintPath){
            if (meshSliced){
                new PathPreviewGUI(this, mesh, printPath);
                setVisible(false);
            }
        }
    }
}
