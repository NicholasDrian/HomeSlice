package com.company;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeScreenGUI extends JFrame implements ActionListener {

    private Container c;
    private JLabel logo;
    private JLabel brand;
    private JLabel author;
    private JButton runHomeSlice;
    private JButton instructions;
    private Font titleFont = new Font("Arial", Font.BOLD, 60);
    private Font regularFont = new Font("Arial", Font.PLAIN, 40);
    private Font smallFont = new Font("Arial", Font.PLAIN, 25);

    public HomeScreenGUI(Rectangle screen) {
        setTitle("HomeSlice");
        setBounds(screen);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        c = getContentPane();
        c.setLayout(null);
        c.setBackground(NicksColors.VeryDarkBlue);

        logo = new JLabel("Welcome to");
        logo.setFont(regularFont);
        logo.setSize(400, 50);
        logo.setLocation(190, 30);
        logo.setForeground(NicksColors.Yellow);
        c.add(logo);

        brand = new JLabel("HomeSlice");
        brand.setFont(titleFont);
        brand.setSize(400, 50);
        brand.setLocation(410, 30);
        brand.setForeground(NicksColors.Yellow);
        c.add(brand);

        author = new JLabel("By Nick Drian");
        author.setFont(smallFont);
        author.setSize(400,100);
        author.setLocation(360, 60);
        author.setForeground(NicksColors.Yellow);
        c.add(author);

        Border YellowLine = BorderFactory.createLineBorder(NicksColors.Yellow);

        runHomeSlice = new JButton("Click Here To Begin Slicing!");
        runHomeSlice.setFont(titleFont);
        runHomeSlice.setSize(820, 150);
        runHomeSlice.setLocation(40, 180);
        runHomeSlice.addActionListener(this);
        runHomeSlice.setBackground(NicksColors.DarkBlue);
        runHomeSlice.setForeground(NicksColors.Yellow);
        runHomeSlice.setBorder(YellowLine);
        runHomeSlice.setFocusable(false);
        c.add(runHomeSlice);

        instructions = new JButton("Click Here For Instructions!");
        instructions.setFont(smallFont);
        instructions.setSize(520, 50);
        instructions.setLocation(190, 380);
        instructions.addActionListener(this);
        instructions.setBackground(NicksColors.DarkBlue);
        instructions.setForeground(NicksColors.Yellow);
        instructions.setBorder(YellowLine);
        instructions.setFocusable(false);
        c.add(instructions);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == runHomeSlice){
            new SlicerSettingsGUI(getBounds());
            dispose();
        }
    }

    //private static void UpdateFeedback
}

