import controller.Controller3D;
import view.Window;

import javax.swing.*;
import java.util.Locale;

public class App {
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH); // anglické texty i ve standardních dialozích Swingu
        SwingUtilities.invokeLater(() -> {
            Window window = new Window(1200, 800);
            new Controller3D(window.getPanel());
            window.setVisible(true); // zobrazit okno
        });
    }
}
