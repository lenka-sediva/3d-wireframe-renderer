package view;

import model.objectdata.mesh.Mesh;
import model.rasterdata.Raster;
import model.rasterdata.RasterBI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Panel extends JPanel {

    private Raster raster;
    private String currentObjectName = "cube";
    private List<Mesh> objects; // seznam všech objektů
    private String currentCameraMode = "3rd person"; // výchozí
    private final JSlider precisionSlider; // slider pro přesnost křivek
    private final JLabel sliderLabel; // popisek nad sliderem

    public Panel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setLayout(null); // absolutní pozicování komponent
        raster = new RasterBI(width, height);

        // slider
        precisionSlider = new JSlider(10, 300, 100);
        add(precisionSlider);

        // popisek nad sliderem
        sliderLabel = new JLabel("Curve rendering precision", SwingConstants.CENTER);
        sliderLabel.setForeground(Color.WHITE);
        sliderLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        add(sliderLabel);

        // listener pro resize = centrování slideru a popisku
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                centerSlider();
            }
        });
    }

    // přesune slider a popisek doprostřed panelu
    private void centerSlider() {
        int sliderWidth = 300;
        int sliderHeight = 40;
        int center = (getWidth() - sliderWidth) / 2;
        precisionSlider.setBounds(center, 40, sliderWidth, sliderHeight);
        sliderLabel.setBounds(center, 10, sliderWidth, 20); // popisek pro slider
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        raster.repaint(g);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));

        // levý dolní roh = nápověda
        g.setColor(Color.ORANGE);
        g.drawString("Help:", 10, getHeight() - 160);
        g.setColor(Color.WHITE);
        g.drawString("[arrows] move object", 10, getHeight() - 135);
        g.drawString("[L] change object color", 10, getHeight() - 110);
        g.drawString("[R] animate object rotation", 10, getHeight() - 85);
        g.drawString("[+] scale object up, [-] scale object down", 10, getHeight() - 60);
        g.drawString("[C] clear, [O] reset, [XYZ] rotate around axes", 10, getHeight() - 35);
        String selectedLabel = "Selected object: ";
        g.drawString(selectedLabel, 10, getHeight() - 10);
        g.setColor(Color.ORANGE);
        g.drawString(currentObjectName, 10 + g.getFontMetrics().stringWidth(selectedLabel), getHeight() - 10);

        // levý horní roh = seznam objektů s jejich vlastní barvou
        int y = 25;
        for (Mesh m : objects) {
            g.setColor(m.getColor());
            g.drawString("[" + m.getShortcut() + "] " + m.getName(), 10, y);
            y += 25;
        }

        // pravý dolní roh = projekce
        g.setColor(Color.WHITE);
        drawStringRight(g, "Orthographic projection [H]", getHeight() - 35);
        drawStringRight(g, "Perspective projection [P]", getHeight() - 10);

        // pravý horní roh = pozorovatel
        g.setColor(Color.ORANGE);
        drawStringRight(g, "Observer:", 25);
        g.setColor(Color.WHITE);
        drawStringRight(g, "[WASD] move", 50);
        drawStringRight(g, "[Q] left, [E] right", 75);
        drawStringRight(g, "[SHIFT] up, [CTRL] down", 100);
        drawStringRight(g, currentCameraMode + " [SPACE]", 125);
    }

    // vykreslí text zarovnaný k pravému okraji panelu
    private void drawStringRight(Graphics g, String text, int y) {
        int width = g.getFontMetrics().stringWidth(text);
        g.drawString(text, getWidth() - width - 10, y);
    }

    public void setCurrentObject(String name) {
        this.currentObjectName = name;
        repaint();
    }

    public void setObjects(List<Mesh> objects) {
        this.objects = objects;
        repaint();
    }

    public void setCurrentCameraMode(boolean firstPerson) {
        this.currentCameraMode = firstPerson ? "1st person" : "3rd person";
        repaint();
    }

    public int getPrecision() {
        return precisionSlider.getValue();
    }

    public JSlider getPrecisionSlider() {
        return precisionSlider;
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
