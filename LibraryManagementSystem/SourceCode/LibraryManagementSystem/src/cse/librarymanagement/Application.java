package cse.librarymanagement;

import cse.librarymanagement.ui.WelcomeFrame;

import javax.swing.SwingUtilities;

public class Application {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            WelcomeFrame welcomeFrame = new WelcomeFrame();
            welcomeFrame.setVisible(true);
        });
    }
}
