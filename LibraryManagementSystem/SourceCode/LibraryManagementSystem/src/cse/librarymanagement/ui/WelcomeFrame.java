package cse.librarymanagement.ui;

import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {

    public WelcomeFrame() {

        setTitle("Library Management System");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));

        JLabel title = new JLabel(
                "LIBRARY MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

        JLabel subtitle = new JLabel(
                "Welcome to Our Digital Library",
                SwingConstants.CENTER
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));

        JPanel headingPanel = new JPanel(new GridLayout(2, 1));
        headingPanel.add(title);
        headingPanel.add(subtitle);

        add(headingPanel, BorderLayout.NORTH);

        JButton adminButton = new JButton("ADMIN PANEL");
        JButton studentButton = new JButton("STUDENT PANEL");

        adminButton.setFont(new Font("Arial", Font.BOLD, 18));
        studentButton.setFont(new Font("Arial", Font.BOLD, 18));

        adminButton.setPreferredSize(new Dimension(300, 65));
        studentButton.setPreferredSize(new Dimension(300, 65));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 15, 20));
        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(30, 100, 50, 100)
        );

        buttonPanel.add(adminButton);
        buttonPanel.add(studentButton);

        add(buttonPanel, BorderLayout.CENTER);

        JLabel footer = new JLabel(
                "Select your portal to continue",
                SwingConstants.CENTER
        );
        footer.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        add(footer, BorderLayout.SOUTH);

        adminButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        studentButton.addActionListener(e -> {
            new StudentLoginFrame().setVisible(true);
            dispose();
        });
    }
}
