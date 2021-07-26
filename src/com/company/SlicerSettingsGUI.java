package com.company;


        import javax.swing.*;
        import java.awt.*;
        import java.awt.datatransfer.DataFlavor;
        import java.awt.datatransfer.Transferable;
        import java.awt.dnd.DnDConstants;
        import java.awt.dnd.DropTarget;
        import java.awt.dnd.DropTargetDropEvent;
        import java.awt.event.*;
        import java.io.File;
        import java.util.List;

class SlicerSettingsGUI
        extends JFrame
        implements ActionListener {


    private static String defaultUserFeedback = "Instructions:\n" +
            "-Input custom settings\n" +
            "-Or select an autofill option\n" +
            "-Then drag and drop your stl";

    private static Container c;
    private static JLabel title;
    private static JComboBox<String> selectYourPrinter;
    private static String[] printers = {"Select Autofill Option", "Ender 3 Pro", "Concrete Printer", "Cley Printer"};

    private static JLabel layerThicknessQ;
    private static JTextField layerThicknessA;
    public static Float layerThickness;

    private static JLabel extrusionRateQ;
    private static JTextField extrusionRateA;
    public static Float extrusionRate;

    private static JLabel printSpeedQ;
    private static JTextField printSpeedA;
    public static Float printSpeed;

    private static JLabel retractionDistanceQ;
    private static JTextField retractionDistanceA;
    public static Float retractionDistance;

    private static JLabel printVolumeQ;
    private static JLabel xQ;
    private static JTextField xA;
    public static int x;
    private static JLabel yQ;
    private static JTextField yA;
    public static int y;
    private static JLabel zQ;
    private static JTextField zA;
    public static int z;

    private static JButton slice;
    private static JButton reset;
    private static JTextArea out;

    private static JButton stlHere;
    private static File stl;

    private static JButton backToHome;


    // constructor, to initialize the components
    // with default values.
    public SlicerSettingsGUI()
    {
        setTitle("HomeSlice -> Settings");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);


        c = getContentPane();
        c.setLayout(null);
        c.setBackground(NicksColors.VeryDarkBlue);

        title = new JLabel("Slicer Settings");
        title.setFont(new Font("Arial", Font.PLAIN, 60));
        title.setSize(450, 70);
        title.setLocation(430, 10);
        title.setForeground(Color.white);
        c.add(title);

        selectYourPrinter = new JComboBox<String>(printers);
        selectYourPrinter.setFont(new Font("Arial", Font.PLAIN, 20));
        selectYourPrinter.setSize(290, 40);
        selectYourPrinter.setLocation(80, 30);
        selectYourPrinter.addActionListener(this);
        selectYourPrinter.setBackground(Color.white);
        c.add(selectYourPrinter);

        layerThicknessQ = new JLabel("Layer Thickness (mm):");
        layerThicknessQ.setFont(new Font("Arial", Font.PLAIN, 20));
        layerThicknessQ.setSize(300, 30);
        layerThicknessQ.setLocation(50, 90);
        layerThicknessQ.setForeground(Color.white);
        c.add(layerThicknessQ);

        layerThicknessA = new JTextField();
        layerThicknessA.setFont(new Font("Arial", Font.PLAIN, 15));
        layerThicknessA.setSize(150, 20);
        layerThicknessA.setLocation(70, 125);
        layerThicknessA.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String s = layerThicknessA.getText();
                char[] chars = s.toCharArray();
                for (char character : chars){
                    if (character >= '0' && character <= '9' || character == '.'){
                        layerThickness = Float.valueOf(layerThicknessA.getText());
                    } else {
                        layerThicknessA.setText("");
                        break;
                    }
                }
            }
        });
        c.add(layerThicknessA);

        extrusionRateQ = new JLabel("Extrusion Rate:");
        extrusionRateQ.setFont(new Font("Arial", Font.PLAIN, 20));
        extrusionRateQ.setSize(300, 30);
        extrusionRateQ.setLocation(50, 150);
        extrusionRateQ.setForeground(Color.white);
        c.add(extrusionRateQ);

        extrusionRateA = new JTextField();
        extrusionRateA.setFont(new Font("Arial", Font.PLAIN, 15));
        extrusionRateA.setSize(150, 20);
        extrusionRateA.setLocation(70, 185);
        extrusionRateA.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String s = extrusionRateA.getText();
                char[] chars = s.toCharArray();
                for (char character : chars){
                    if (character >= '0' && character <= '9' || character == '.'){
                        extrusionRate = Float.valueOf(extrusionRateA.getText());
                    } else {
                        extrusionRateA.setText("");
                        break;
                    }
                }
            }
        });
        c.add(extrusionRateA);

        printSpeedQ = new JLabel("Print Speed:");
        printSpeedQ.setFont(new Font("Arial", Font.PLAIN, 20));
        printSpeedQ.setSize(300, 30);
        printSpeedQ.setLocation(50, 210);
        printSpeedQ.setForeground(Color.white);
        c.add(printSpeedQ);

        printSpeedA = new JTextField();
        printSpeedA.setFont(new Font("Arial", Font.PLAIN, 15));
        printSpeedA.setSize(150, 20);
        printSpeedA.setLocation(70, 245);
        printSpeedA.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String s = printSpeedA.getText();
                char[] chars = s.toCharArray();
                for (char character : chars){
                    if (character >= '0' && character <= '9' || character == '.'){
                        printSpeed = Float.valueOf(printSpeedA.getText());
                    } else {
                        printSpeedA.setText("");
                        break;
                    }
                }
            }
        });
        c.add(printSpeedA);

        retractionDistanceQ = new JLabel("Retraction Distance (mm):");
        retractionDistanceQ.setFont(new Font("Arial", Font.PLAIN, 20));
        retractionDistanceQ.setSize(300, 30);
        retractionDistanceQ.setLocation(50, 270);
        retractionDistanceQ.setForeground(Color.white);
        c.add(retractionDistanceQ);

        retractionDistanceA = new JTextField();
        retractionDistanceA.setFont(new Font("Arial", Font.PLAIN, 15));
        retractionDistanceA.setSize(150, 20);
        retractionDistanceA.setLocation(70, 305);
        retractionDistanceA.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String s = retractionDistanceA.getText();
                char[] chars = s.toCharArray();
                for (char character : chars){
                    if (character >= '0' && character <= '9' || character == '.'){
                        retractionDistance = Float.valueOf(retractionDistanceA.getText());
                    } else {
                        retractionDistanceA.setText("");
                        break;
                    }
                }
            }
        });
        c.add(retractionDistanceA);


        printVolumeQ = new JLabel("Maximum Print Volume (mm):");
        printVolumeQ.setFont(new Font("Arial", Font.PLAIN, 20));
        printVolumeQ.setSize(300, 30);
        printVolumeQ.setLocation(50, 330);
        printVolumeQ.setForeground(Color.white);
        c.add(printVolumeQ);
        xQ = new JLabel("X Size:");
        xQ.setFont(new Font("Arial", Font.PLAIN, 20));
        xQ.setSize(100, 30);
        xQ.setLocation(70, 360);
        xQ.setForeground(Color.white);
        c.add(xQ);
        yQ = new JLabel("Y Size:");
        yQ.setFont(new Font("Arial", Font.PLAIN, 20));
        yQ.setSize(100, 30);
        yQ.setLocation(70, 390);
        yQ.setForeground(Color.white);
        c.add(yQ);
        zQ = new JLabel("Z Size:");
        zQ.setFont(new Font("Arial", Font.PLAIN, 20));
        zQ.setSize(100, 30);
        zQ.setLocation(70, 420);
        zQ.setForeground(Color.white);
        c.add(zQ);

        xA = new JTextField();
        xA.setFont(new Font("Arial", Font.PLAIN, 15));
        xA.setSize(150, 20);
        xA.setLocation(150, 365);
        xA.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String s = xA.getText();
                char[] chars = s.toCharArray();
                for (char character : chars){
                    if (character >= '0' && character <= '9'){
                        x = Integer.valueOf(xA.getText());
                    } else {
                        xA.setText("");
                        break;
                    }
                }
            }
        });
        c.add(xA);

        yA = new JTextField();
        yA.setFont(new Font("Arial", Font.PLAIN, 15));
        yA.setSize(150, 20);
        yA.setLocation(150, 395);
        yA.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String s = yA.getText();
                char[] chars = s.toCharArray();
                for (char character : chars){
                    if (character >= '0' && character <= '9'){
                        y = Integer.valueOf(yA.getText());
                    } else {
                        yA.setText("");
                        break;
                    }
                }
            }
        });
        c.add(yA);

        zA = new JTextField();
        zA.setFont(new Font("Arial", Font.PLAIN, 15));
        zA.setSize(150, 20);
        zA.setLocation(150, 425);
        zA.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String s = zA.getText();
                char[] chars = s.toCharArray();
                for (char character : chars){
                    if (character >= '0' && character <= '9'){
                        z = Integer.valueOf(zA.getText());
                    } else {
                        zA.setText("");
                        break;
                    }
                }
            }
        });
        c.add(zA);

        stlHere = new JButton("Drop .stl file here");
        stlHere.setFont(new Font("Arial", Font.PLAIN, 30));
        stlHere.setSize(510, 100);
        stlHere.setLocation(350, 340);
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
                                    stl = file;
                                    updateUserFeedback();
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

        slice = new JButton("HomeSlice!");
        slice.setFont(new Font("Arial", Font.PLAIN, 20));
        slice.setSize(200, 50);
        slice.setLocation(380, 460);
        slice.setBackground(NicksColors.LightGreen);
        slice.addActionListener(this);
        c.add(slice);

        reset = new JButton("Reset");
        reset.setFont(new Font("Arial", Font.PLAIN, 20));
        reset.setSize(200, 50);
        reset.setLocation(640, 460);
        reset.setBackground(NicksColors.Yellow);
        reset.addActionListener(this);
        c.add(reset);

        out = new JTextArea(defaultUserFeedback);
        out.setFont(new Font("Arial", Font.PLAIN, 14));
        out.setWrapStyleWord(true);
        out.setSize(250, 230);
        out.setLocation(605, 90);
        out.setLineWrap(true);
        out.setEditable(false);
        out.setBackground(Color.white);
        out.setForeground(NicksColors.VeryDarkBlue);
        c.add(out);

        backToHome = new JButton("Back to Home Screen");
        backToHome.setFont(new Font("Arial", Font.PLAIN, 20));
        backToHome.setSize(270, 50);
        backToHome.setLocation(50, 460);
        backToHome.addActionListener(this);
        backToHome.setBackground(NicksColors.DarkBlue);
        backToHome.setForeground(Color.white);
        c.add(backToHome);

        setVisible(true);


    }

    public static void setLayerThickness(Float num){
        layerThicknessA.setText(num.toString());
        layerThickness = num;
    }
    public static void setPrintSpeed(Float num){
        printSpeedA.setText(num.toString());
        printSpeed = num;
    }
    public static void setExtrusionRate(Float num){
        extrusionRateA.setText(num.toString());
        extrusionRate = num;
    }
    public static void setRetractionDistance(Float num){
        retractionDistanceA.setText(num.toString());
        retractionDistance = num;
    }
    public static void setPrintVolume(int X, int Y, int Z){
        x = X; y = Y; z = Z;
        xA.setText(Integer.toString(x)); yA.setText(Integer.toString(y)); zA.setText(Integer.toString(z));
    }
    private static boolean updateUserFeedback(){
        boolean bool = true;
        String s = "";
        if (layerThickness == null){ s = s + "Layer Thickness: \n    None Selected"; bool = false; } else { s = s + "Layer Thickness: \n    " + layerThickness; }
        if (extrusionRate == null){ s = s + "\n" + "Extrusion Rate: \n    None Selected"; bool = false; } else { s = s + "\n" + "Extrusion Rate: \n    " + extrusionRate; }
        if (printSpeed == null){ s = s + "\n" + "Print Speed: \n    None Selected"; bool = false; } else { s = s + "\n" + "Print Speed: \n    " + printSpeed; }
        if (retractionDistance == null){ s = s + "\n" + "Retraction Distance: \n    None Selected"; bool = false; } else { s = s + "\n" + "Retraction Distance: \n    " + retractionDistance; }
        if (x == 0||y == 0|| z == 0){ s = s + "\n" + "Print Volume: \n    None Selected"; bool = false; } else { s = s + "\n" + "Print Volume: \n    " + x + " x " + y + " x " + z; }
        if (stl == null){
            s = s + "\n" + "STL File: \n    None Selected"; bool = false;
        } else {
            if (VerifyFile.IsStl(stl)) { s = s + "\n" + "File: \n    " + stl.getName();} else {s = s + "\n\n" + "STL File: \n    Wrong Type Of File!"; bool = false;}
        }
        if (bool){ s = s + "\nReady!"; out.setForeground(NicksColors.LightGreen);} else { s = s + "\nEnter missing or wrong information."; out.setForeground(NicksColors.DarkRed);}
        out.setText(s);
        return bool;

    }

    public static void resetStoredVariables(){
        layerThickness = null;
        extrusionRate = null;
        printSpeed = null;
        retractionDistance = null;
        stl = null;
        x = 0;
        y = 0;
        z = 0;
        layerThicknessA.setText("");
        extrusionRateA.setText("");
        printSpeedA.setText("");
        retractionDistanceA.setText("");
        xA.setText("");
        yA.setText("");
        zA.setText("");
        out.setText(defaultUserFeedback);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == reset) {
            resetStoredVariables();
        } else if (e.getSource() == slice) {
            if (updateUserFeedback()){
                setVisible(false);
                new SlicePreviewGUI(stl, this);
            }
        } else if (e.getSource() == backToHome) {
            new HomeScreenGUI();
            dispose();
            resetStoredVariables();
        } else if (e.getSource() == selectYourPrinter) {
            String s = (String) selectYourPrinter.getSelectedItem();
            new Printers(s);
            updateUserFeedback();
        }
    }
}
