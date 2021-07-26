package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Map;

public class PathPreviewGUI
        extends JFrame
        implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

    private static JFrame previousFrame;
    private static Container c;
    private static JButton backToSlicePreview;
    private static JLabel faceCount;
    private static JLabel frameRate;
    public static float frameDuration;
    private static JLabel bedX;
    private static JLabel bedY;
    private static JLabel bedZ;
    private static JTextArea instructions;
    private static Image image;
    private static JLabel screen;
    private static Camera camera;
    private static Mesh mesh;
    boolean meshVis = false;
    private static Map<Double, PolyLine[]> printPath;
    boolean pathVis = true;
    private static PrintBed printBed;
    private static FloatVector previousDragPosition;
    private static boolean shiftPressed; //for panning
    private static boolean ctrlPressed; //for scaling

    PathPreviewGUI(JFrame previous, Mesh Mesh, Map map) {
        previousFrame = previous;
        setTitle("HomeSlice -> Mesh Preview");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        mesh = Mesh;
        printPath = map;
        addKeyListener(this);
        setFocusable(true);
        c = getContentPane();
        c.setLayout(null);
        c.setBackground(NicksColors.VeryDarkBlue);

        printBed = new PrintBed(SlicerSettingsGUI.x, SlicerSettingsGUI.y);

        camera = new Camera();
        Camera.initCamera(camera, SlicerSettingsGUI.x, SlicerSettingsGUI.y, SlicerSettingsGUI.z);

        backToSlicePreview = new JButton("Back to Slice Preview");
        backToSlicePreview.setFont(new Font("Arial", Font.PLAIN, 15));
        backToSlicePreview.setSize(260, 30);
        backToSlicePreview.setLocation(30, 490);
        backToSlicePreview.addActionListener(this);
        backToSlicePreview.setBackground(NicksColors.DarkBlue);
        backToSlicePreview.setForeground(Color.WHITE);
        c.add(backToSlicePreview);

        faceCount = new JLabel("Mesh face Count: " + mesh.faces.length);
        faceCount.setFont(new Font("Arial", Font.PLAIN, 15));
        faceCount.setSize(300, 30);
        faceCount.setLocation(20, 20);
        faceCount.setForeground(Color.white);
        c.add(faceCount);

        frameRate = new JLabel("Frame Rate (fps): " + 1 / frameDuration);
        frameRate.setFont(new Font("Arial", Font.PLAIN, 15));
        frameRate.setSize(300, 30);
        frameRate.setLocation(20, 40);
        frameRate.setForeground(Color.white);
        c.add(frameRate);

        bedX = new JLabel("Print bed X size (mm): " + SlicerSettingsGUI.x);
        frameRate.setFont(new Font("Arial", Font.PLAIN, 15));
        bedX.setSize(300, 30);
        bedX.setLocation(20, 60);
        bedX.setForeground(Color.white);
        c.add(bedX);

        bedY = new JLabel("Print bed Y size (mm): " + SlicerSettingsGUI.y);
        frameRate.setFont(new Font("Arial", Font.PLAIN, 15));
        bedY.setSize(300, 30);
        bedY.setLocation(20, 80);
        bedY.setForeground(Color.white);
        c.add(bedY);

        bedZ = new JLabel("Print bed Z size (mm): " + SlicerSettingsGUI.z);
        frameRate.setFont(new Font("Arial", Font.PLAIN, 15));
        bedZ.setSize(300, 30);
        bedZ.setLocation(20, 100);
        bedZ.setForeground(Color.white);
        c.add(bedZ);

        screen = new JLabel();
        screen.setLocation(350, 20);
        screen.setSize(camera.res, camera.res);
        screen.addMouseMotionListener(this);
        screen.addMouseListener(this);
        updateRender();
        c.add(screen);

        instructions = new JTextArea("" +
                "Click and drag to rotate,\n" +
                "Hold shift to pan.\n" +
                "Hold control to zoom.");
        instructions.setFont(new Font("Arial", Font.PLAIN, 15));
        instructions.setSize(300, 90);
        instructions.setLocation(20, 140);
        instructions.setBackground(NicksColors.VeryDarkBlue);
        instructions.setForeground(Color.white);
        c.add(instructions);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backToSlicePreview) {
            dispose();
            camera = new Camera();
            previousFrame.setVisible(true);
        }
    }

    public void updateRender() {
        boolean drawBedFirst = camera.position.dimensions[2] > 0;
        image = Draw.createBackground(camera);
        if (drawBedFirst) {
            image = Draw.drawPrintBed(camera, printBed, image);
        }
        if (meshVis) {
            image = Draw.drawMesh(camera, mesh, image);
        }
        if (pathVis) {
            //draw path
            Iterator<Double> zIter = printPath.keySet().iterator();
            Iterator<PolyLine[]> polys = printPath.values().iterator();
            while (zIter.hasNext()){
                image = Draw.drawLines(camera, polys.next(), image, Color.BLACK, zIter.next());
            }
        }
        if (!drawBedFirst) {
            image = Draw.drawPrintBed(camera, printBed, image);
        }
    ImageIcon icon = new ImageIcon(image);
            screen.setIcon(icon);
            frameRate.setText("Frame Rate : " + 1 / frameDuration + "fps");
    }

    public void updateCamera(int x, int y) {
        if (previousDragPosition == null) {
            previousDragPosition = new FloatVector(x, y);
        } else {
            Float deltaX = x - previousDragPosition.dimensions[0];
            if (deltaX != 0) {
                if (shiftPressed) { //panning
                    FloatVector motion = FloatVector.Subtract(camera.bottomRight, camera.bottomLeft);
                    motion = FloatVector.Normalize(motion);
                    motion = FloatVector.Scale(motion, deltaX / 500);
                    camera = Camera.updatePosition(camera, motion);
                } else if (ctrlPressed) { //scaling
                    //do nothing
                } else { //rotating
                    camera = Camera.updateHorizontalRotation(camera, deltaX / 360);
                }
                previousDragPosition.dimensions[0] = x;
            }
            Float deltaY = y - previousDragPosition.dimensions[1];
            if (deltaY != 0) {
                if (shiftPressed) { //panning
                    FloatVector motion = FloatVector.Subtract(camera.topLeft, camera.bottomLeft);
                    motion = FloatVector.Normalize(motion);
                    motion = FloatVector.Scale(motion, deltaY / 500);
                    camera = Camera.updatePosition(camera, motion);
                } else if (ctrlPressed) { //scaling
                    camera = Camera.updateScale(camera, 1 + (deltaY / 500));
                } else { //rotating
                    camera = Camera.updateVerticalRotation(camera, deltaY / 360);
                }
                previousDragPosition.dimensions[1] = y;
            }
            camera = Camera.updateCamera(camera);
            updateRender();
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        updateCamera(e.getX(), -e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            ctrlPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shiftPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            ctrlPressed = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        previousDragPosition = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}