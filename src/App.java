import controller.Controller3D;
import view.Window;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Window window = new Window(1200, 800);
            new Controller3D(window.getPanel());
            window.setVisible(true); // zobrazit okno
        });
    }
}
