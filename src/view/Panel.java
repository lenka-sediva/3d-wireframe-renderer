package view;

import model.objectdata.mesh.Mesh;
import model.rasterdata.Raster;
import model.rasterdata.RasterBI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Panel extends JPanel {

    private Raster raster;
    private String currentObjectName = "krychle";
    private List<Mesh> objects; // seznam všech objektů

    public Panel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        raster = new RasterBI(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        raster.repaint(g);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));

        // levý dolní roh = nápověda
        g.setColor(Color.ORANGE);
        g.drawString("Nápověda:", 10, getHeight() - 160);
        g.setColor(Color.WHITE);
        g.drawString("[šipky] posun objektu", 10, getHeight() - 135);
        g.drawString("[L] změna barvy objektu", 10, getHeight() - 110);
        g.drawString("[R] animace rotace objektu v čase", 10, getHeight() - 85);
        g.drawString("[+] zvětšení objektu, [-] zmenšení objektu", 10, getHeight() - 60);
        g.drawString("[C] smazání, [O] původní stav, [XYZ] rotace podle os", 10, getHeight() - 35);
        g.drawString("Aktuálně vybraný objekt: ", 10, getHeight() - 10);
        g.setColor(Color.ORANGE);
        g.drawString(currentObjectName, 205, getHeight() - 10);

        // levý horní roh = seznam objektů s jejich vlastní barvou
        int y = 25;
        for (Mesh m : objects) {
            g.setColor(m.getColor());
            g.drawString("[" + m.getShortcut() + "] " + m.getName(), 10, y);
            y += 25;
        }

        // pravý dolní roh = projekce
        g.setColor(Color.WHITE);
        g.drawString("Ortogonální projekce [H]", getWidth() - 195, getHeight() - 35);
        g.drawString("Perspektivní projekce [P]", getWidth() - 200, getHeight() - 10);

        // pravý horní roh = pozorovatel
        g.setColor(Color.ORANGE);
        g.drawString("Pozorovatel:", getWidth() - 105, 25);
        g.setColor(Color.WHITE);
        g.drawString("[WASD] pohyb", getWidth() - 120, 50);
        g.drawString("[Q] vlevo, [E] vpravo", getWidth() - 160, 75);
        g.drawString("[SHIFT] nahoru, [CTRL] dolů", getWidth() - 225, 100);
    }

    public void setCurrentObject(String name) {
        this.currentObjectName = name;
        repaint();
    }

    // předá seznam všech objektů, aby Panel mohl kreslit jejich názvy v jejich barvě
    public void setObjects(List<Mesh> objects) {
        this.objects = objects;
        repaint();
    }

    public Raster getRaster() {
        return raster;
    }

    public void clear() {
        raster.clear();
    }

    public void setRaster(RasterBI newRaster) {
        raster = newRaster;
        repaint();
    }
}
