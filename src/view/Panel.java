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
        g.drawString("Nápověda:", 10, getHeight() - 60);
        g.drawString("[WASD] pohyb", 10, getHeight() - 35);
        g.drawString("Aktuálně vybraný objekt: " + currentObject, 10, getHeight() - 10);
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
