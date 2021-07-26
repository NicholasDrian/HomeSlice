package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeScreenGUI extends JFrame implements ActionListener {

    private Container c;
    private JLabel logo;
    private JLabel brand;
    private JLabel author;
    private JButton runHomeSlice;
    private Font titleFont = new Font("Arial", Font.BOLD, 60);
    private Font regularFont = new Font("Arial", Font.PLAIN, 40);
    private Font smallFont = new Font("Arial", Font.PLAIN, 30);

    public HomeScreenGUI() {
        setTitle("HomeSlice");
        setBounds(300, 90, 900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);
        c.setBackground(NicksColors.VeryDarkBlue);

        logo = new JLabel("Welcome to");
        logo.setFont(regularFont);
        logo.setSize(400, 50);
        logo.setLocation(190, 30);
        logo.setForeground(NicksColors.DarkYellow);
        c.add(logo);

        brand = new JLabel("HomeSlice");
        brand.setFont(titleFont);
        brand.setSize(400, 50);
        brand.setLocation(410, 30);
        brand.setForeground(NicksColors.DarkYellow);
        c.add(brand);

        author = new JLabel("By Nick Drian");
        author.setFont(smallFont);
        author.setSize(400,100);
        author.setLocation(360, 60);
        author.setForeground(NicksColors.DarkYellow);
        c.add(author);

        runHomeSlice = new JButton("Click Here To Begin Slicing!");
        runHomeSlice.setFont(regularFont);
        runHomeSlice.setSize(800, 300);
        runHomeSlice.setLocation(50, 180);
        runHomeSlice.addActionListener(this);
        runHomeSlice.setBackground(NicksColors.DarkYellow);
        runHomeSlice.setForeground(NicksColors.DarkBlue);
        c.add(runHomeSlice);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == runHomeSlice){
            dispose();
            new SlicerSettingsGUI();
        }
    }

    //private static void UpdateFeedback
}

