package cse.librarymanagement.ui;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    // Initializes the admin dashboard.
    public AdminDashboard() {

        setTitle("Library Management System - Admin Dashboard");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Creates the top header.
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(35, 47, 62));
        header.setPreferredSize(new Dimension(1200, 65));

        JLabel title = new JLabel("  LIBRARY MANAGEMENT SYSTEM");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel adminLabel = new JLabel("Admin Panel  ");
        adminLabel.setForeground(Color.WHITE);
        adminLabel.setFont(new Font("Arial", Font.BOLD, 15));

        header.add(title, BorderLayout.WEST);
        header.add(adminLabel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Creates the sidebar.
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(9, 1, 5, 5));
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(220, 600));

        String[] menuItems = {
                "Dashboard",
                "Manage Books",
                "Student Registrations",
                "Manage Students",
                "Borrow / Return",
                "Manage Shelves",
                "Book Requests",
                "Change Admin Credentials",
                "Logout"
        };

        // Creates the content area using CardLayout.
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        JPanel dashboardPanel = new JPanel(new GridBagLayout());
        dashboardPanel.setBackground(new Color(236, 240, 241));

        JLabel welcomeLabel =
                new JLabel("Welcome to Admin Dashboard!");

        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(44, 62, 80));

        dashboardPanel.add(welcomeLabel);

        contentPanel.add(dashboardPanel, "Dashboard");

        // Adds the book management panel.
        contentPanel.add(new ManageBooksFrame(), "Manage Books");
        contentPanel.add(new StudentApprovalFrame(), "Student Registrations");
        contentPanel.add(new ManageStudentsFrame(), "Manage Students");
        contentPanel.add(new BorrowReturnFrame(), "Borrow / Return");
        contentPanel.add(new ManageShelvesFrame(), "Manage Shelves");
        contentPanel.add(new BookRequestsFrame(), "Book Requests");
        contentPanel.add(new AdminCredentialsFrame(), "Change Admin Credentials");

        // Creates sidebar buttons and their actions.
        for (String item : menuItems) {

            JButton button = new JButton(item);

            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setForeground(Color.WHITE);
            button.setBackground(new Color(52, 73, 94));
            button.setFocusPainted(false);

            button.addActionListener(e -> {

                if (item.equals("Logout")) {

                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to logout?",
                            "Logout",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        new WelcomeFrame().setVisible(true);
                        dispose();
                    }

                } else if (item.equals("Dashboard") ||
                        item.equals("Manage Books") ||
                        item.equals("Student Registrations") ||
                        item.equals("Manage Students") ||
                        item.equals("Borrow / Return") ||
                        item.equals("Manage Shelves") ||
                        item.equals("Book Requests") ||
                        item.equals("Change Admin Credentials")) {
                    cardLayout.show(contentPanel, item);

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "This section will be added to the dashboard."
                    );
                }
            });

            sidebar.add(button);
        }

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
}
