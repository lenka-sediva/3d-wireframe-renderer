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
        g.setColor(Color.ORANGE);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));

        // levý dolní roh
        g.drawString("Nápověda:", 10, getHeight() - 110);
        g.setColor(Color.WHITE);
        g.drawString("[šipky] posunutí objektů", 10, getHeight() - 85);
        g.drawString("[+] zvětšení objektu, [-] zmenšení objektu", 10, getHeight() - 60);
        g.drawString("[C] smazání, [O] původní stav, [R] rotace", 10, getHeight() - 35);
        g.drawString("Aktuálně vybraný objekt: ", 10, getHeight() - 10);
        g.setColor(Color.ORANGE);
        g.drawString(currentObject, 205, getHeight() - 10);

        // levý horní roh
        g.setColor(Color.WHITE);
        g.drawString("[K] krychle", 10, 25);
        g.drawString("[U] koule", 10, 50);
        g.drawString("[B] bezier", 10, 75);
        g.drawString("[N] coons", 10, 100);
        g.drawString("[F] ferguson", 10, 125);
        g.drawString("[T] torus", 10, 150);
        g.drawString("[V] viviani", 10, 175);

        // pravý dolní roh
        g.drawString("Ortogonální projekce [H]", getWidth() - 195, getHeight() - 35);
        g.drawString("Perspektivní projekce [P]", getWidth() - 200, getHeight() - 10); // výchozí

        // pravý horní roh
        g.setColor(Color.ORANGE);
        g.drawString("Pozorovatel:", getWidth() - 105, 25);
        g.setColor(Color.WHITE);
        g.drawString("[WASD] pohyb", getWidth() - 120, 50);
        g.drawString("[Q] vlevo, [E] vpravo", getWidth() - 160, 75);
        g.drawString("[SHIFT] nahoru, [CTRL] dolů", getWidth() - 225, 100);
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
