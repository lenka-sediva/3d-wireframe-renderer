package view;

import model.rasterdata.Raster;
import model.rasterdata.RasterBI;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {

    private final Raster raster;
    private String currentObject = "krychle";

    public Panel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        raster = new RasterBI(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        raster.repaint(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        // levý dolní roh
        g.drawString("Nápověda:", 10, getHeight() - 85);
        g.drawString("[WASD] pohyb pozorovatele", 10, getHeight() - 60);
        g.drawString("[šipky] posunutí objektů", 10, getHeight() - 35);
        g.drawString("Aktuálně vybraný objekt: " + currentObject, 10, getHeight() - 10);
        // levý horní roh
        g.drawString("[K] krychle", 10, 25);
        g.drawString("[E] koule", 10, 50);
        g.drawString("[B] bezier", 10, 75);
        g.drawString("[N] coons", 10, 100);
        g.drawString("[F] ferguson", 10, 125);
    }

    public void setCurrentObject(String currentObject) {
        this.currentObject = currentObject;
        repaint();
    }

    public Raster getRaster() {
        return raster;
    }

    public void clear() {
        raster.clear();
    }
}
