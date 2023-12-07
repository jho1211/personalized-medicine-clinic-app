package ui;

import javax.swing.*;
import java.awt.*;

public abstract class ComponentUtils {
    // EFFECTS: Creates a dialog box with the specified message
    public void showDialogBox(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    // EFFECTS: Creates a dialog box with the specified message
    // and returns the user's selection (yes = 0, no = 1, cancel = 2)
    public int showConfirmationBox(String msg) {
        return JOptionPane.showConfirmDialog(null, msg);
    }

    // MODIFIES: frame
    // EFFECTS: Takes in a JFrame and a title and initializes a new JFrame
    // with the given title and a default size. The JFrame is closed by default
    public void drawPopupFrame(JFrame frame, int width, int height) {
        frame.setDefaultCloseOperation(frame.HIDE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(width, height));
        frame.setVisible(false);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
    }
}
