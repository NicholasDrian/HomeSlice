package com.company;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.Map;

public class SlicePreviewGUI extends JFrame implements ActionListener {

    SlicerSettingsGUI settings;

    private static Container c;
    private static JLabel title;
    private static JButton backToSettings;
    private static JButton previewMesh;
    private static JButton previewPrintPath;
    private static JButton downloadGCode;
    private static JButton slice;
    private static boolean meshParsed = false;
    private static boolean meshSliced = false;
    private static Mesh mesh;
    private static Map<Double, PolyLine[]> printPath;
    private static JTextArea userFeedback;
    private static JTextArea sliceFeedback;
    private static ToGCode gCode;
    private static File finalGCode;
    private static Border YellowLine = BorderFactory.createLineBorder(NicksColors.Yellow);
    private static String instructions1 = "Instructions:\n\nPreview your mesh to ensure proper positioning, then click HomeSlice to generate your GCode!\n\nOr drag and drop a new .stl file anywhere.\n\n";
    private static String instructions2 = "Instructions:\n\nPreview your print path, then click Download GCode to start your print!\n\nOr drag and drop a new .stl file anywhere.\n\n";
    private static String wrongFilePath = "Error: Invalid File Path";
    private static boolean fileDownloadError = false;
    private static double printTime;
    private static double totalExtrusion;

    SlicePreviewGUI(File file, SlicerSettingsGUI Settings, Rectangle screen){
        setTitle("HomeSlice -> Slice Preview");
        setBounds(screen);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setDropTarget(createDrop());

        settings = Settings;

        c = getContentPane();
        c.setLayout(null);
        c.setBackground(NicksColors.VeryDarkBlue);

        title = new JLabel("Processing...");
        title.setFont(new Font("Arial", Font.PLAIN, 60));
        title.setSize(600, 55);
        title.setLocation(20, 20);
        title.setForeground(NicksColors.Yellow);
        c.add(title);


        backToSettings = new JButton("Back to Settings");
        backToSettings.setFont(new Font("Arial", Font.PLAIN, 25));
        backToSettings.setSize(280, 60);
        backToSettings.setLocation(30, 480);
        backToSettings.addActionListener(this);
        backToSettings.setBackground(NicksColors.Yellow);
        backToSettings.setForeground(NicksColors.VeryDarkBlue);
        c.add(backToSettings);

        previewMesh = new JButton("Preview Mesh");
        previewMesh.setFont(new Font("Arial", Font.PLAIN, 25));
        previewMesh.setSize(250, 60);
        previewMesh.setLocation(320, 480);
        previewMesh.addActionListener(this);
        previewMesh.setBackground(NicksColors.DarkYellow);
        c.add(previewMesh);

        previewPrintPath = new JButton("Preview Print Path");
        previewPrintPath.setFont(new Font("Arial", Font.PLAIN, 25));
        previewPrintPath.setSize(260, 60);
        previewPrintPath.setLocation(580, 480);
        previewPrintPath.addActionListener(this);
        previewPrintPath.setBackground(NicksColors.Yellow);
        c.add(previewPrintPath);

        userFeedback = new JTextArea(instructions1);
        userFeedback.setFont(new Font("Arial", Font.PLAIN, 20));
        userFeedback.setSize(400, 300);
        userFeedback.setLocation(30, 100);
        userFeedback.setBackground(NicksColors.DarkBlue);
        userFeedback.setForeground(NicksColors.Yellow);
        userFeedback.setLineWrap(true);
        userFeedback.setWrapStyleWord(true);
        userFeedback.setBorder(YellowLine);
        userFeedback.setDropTarget(createDrop());
        c.add(userFeedback);

        sliceFeedback = new JTextArea();
        sliceFeedback.setFont(new Font("Arial", Font.PLAIN, 20));
        sliceFeedback.setSize(400, 300);
        sliceFeedback.setLocation(440, 100);
        sliceFeedback.setBackground(NicksColors.DarkBlue);
        sliceFeedback.setForeground(NicksColors.Yellow);
        sliceFeedback.setLineWrap(true);
        sliceFeedback.setWrapStyleWord(true);
        sliceFeedback.setBorder(YellowLine);
        sliceFeedback.setDropTarget(createDrop());
        setSliceFeedback();
        c.add(sliceFeedback);

        downloadGCode = new JButton("Download GCode");
        downloadGCode.setFont(new Font("Arial", Font.PLAIN, 30));
        downloadGCode.setSize(400, 60);
        downloadGCode.setLocation(440, 410);
        downloadGCode.setBackground(NicksColors.Yellow);
        downloadGCode.setForeground(NicksColors.VeryDarkBlue);
        downloadGCode.addActionListener(this);
        c.add(downloadGCode);

        slice = new JButton("Almost ready to slice!");
        slice.setFont(new Font("Arial", Font.PLAIN, 30));
        slice.setSize(400, 60);
        slice.setLocation(30, 410);
        slice.setBackground(NicksColors.DarkYellow);
        slice.addActionListener(this);
        c.add(slice);

        setVisible(true);

        //for debug if file hasn't been chosen yet.
        if (file != null){
            try {
                mesh = STLParser.parseSTLFile(file);
                title.setText("Mesh Processed!");
                slice.setText("HomeSlice!");
                meshParsed = true;
                slice.setBackground(NicksColors.BrightGreen);
                previewMesh.setBackground(NicksColors.BrightGreen);
                setSliceFeedback();
                updateUserFeedback();
            } catch(Exception e) {
                System.out.println("couldn't parse file");
            }
        }
    }

    private DropTarget createDrop() {
        DropTarget drop = (new DropTarget() {
            public synchronized void drop(DropTargetDropEvent event) {
                meshSliced = false;
                try {
                    event.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable transferable = event.getTransferable();
                    DataFlavor[] flavors = transferable.getTransferDataFlavors();
                    for (DataFlavor flavor : flavors) {
                        try {
                            if (flavor.isFlavorJavaFileListType()) {
                                java.util.List<File> files = (List<File>) transferable.getTransferData(flavor);
                                for (File file : files) {
                                    new SlicePreviewGUI(file, settings, getBounds());
                                    dispose();
                                }
                            }
                        } catch (Exception ex){
                        }
                    }
                } catch (Exception ex){
                }
            }
        });
        return drop;
    }

    private static void setSliceFeedback() {
        String s = "";
        if (mesh != null){
            s = mesh.name + " contains " + mesh.faces.length + " faces.\n\n";
        }
        if (meshSliced){
            s = s + "Total print time is roughly " + printTime + " hours.\n\n";
            s = s + "Total extrusion distance is about " + totalExtrusion + " meters.\n\n";
        }
        if (mesh != null && mesh.faces.length > 20000){
            s = s + "Warning:\nthis mesh is quite large and may be slow to display.\n\n";
        }
        sliceFeedback.setText(s);
    }

    private static void updateUserFeedback(){
        String s = "";
        if (mesh != null && printPath == null){
            //mesh parsed but not sliced
            s = instructions1;
        } else if(printPath != null){
            //mesh sliced
            s = instructions2;
        }
        if(fileDownloadError){
            s = s + wrongFilePath;
        }
        userFeedback.setText(s);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backToSettings){
            settings.setBounds(getBounds());
            settings.setVisible(true);
            dispose();
        } else if (e.getSource() == previewMesh) {
            if (meshParsed){
                new MeshPreviewGUI(this, mesh, getBounds());
                setVisible(false);
            }
        } else if (e.getSource() == slice){
            if (meshParsed){
                slice.setText("Slicing!");
                MeshSlicer slicer = new MeshSlicer(mesh, (double)SlicerSettingsGUI.layerThickness); //GET REAL STROKE HEIGHT!
                meshSliced = true;
                slice.setText("Sliced!");
                title.setText("Mesh Sliced!");
                printPath = slicer.polyLineArrays;
                Map connectedLines = slicer.connectedPolys;
                gCode = new ToGCode(connectedLines);
                previewPrintPath.setBackground(NicksColors.BrightGreen);
                downloadGCode.setBackground(NicksColors.BrightGreen);
                totalExtrusion = gCode.totalExtrusion;
                printTime = gCode.totalPrintTime;
                setSliceFeedback();
            }
            updateUserFeedback();
        } else if (e.getSource() == previewPrintPath){
            if (meshSliced){
                new PathPreviewGUI(this, mesh, printPath, getBounds());
                setVisible(false);
            }
        } else if (e.getSource() == downloadGCode){
            if (meshSliced){
                JFileChooser pathSelector = new JFileChooser();
                pathSelector.setDialogType(JFileChooser.SAVE_DIALOG);
                pathSelector.setDialogTitle("Specify File Destination");
                pathSelector.setAcceptAllFileFilterUsed(false);
                pathSelector.setCurrentDirectory(pathSelector.getFileSystemView().getParentDirectory(new File("C:\\")));
                int userSelection = pathSelector.showSaveDialog(new JFrame());
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File file = pathSelector.getSelectedFile();
                    if (!file.toString().toLowerCase().endsWith(".gcode")) {
                        file = new File(file + ".gcode");
                    }
                    finalGCode = file;
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(finalGCode))) {
                        writer.write(gCode.gCode.toString());
                        fileDownloadError = false;
                    } catch (FileNotFoundException notFound) {
                        fileDownloadError = true;
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        fileDownloadError = true;
                    }
                    if(!fileDownloadError){
                        downloadGCode.setText("Downloaded!");
                    }
                }
                updateUserFeedback();
            }
        }
    }
}
