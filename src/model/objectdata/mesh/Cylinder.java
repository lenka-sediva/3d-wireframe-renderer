package model.objectdata.mesh;

import transforms.Point3D;

import java.awt.*;

public class Cylinder extends Mesh {
    private static final int steps = 50; // počet segmentů kolem kruhu

    public Cylinder(double centerX, double centerY, double centerZ, double radius, double height) {
        super("Válec", "V", Color.CYAN);

        double halfHeight = height / 2.0;

        // vrcholy
        for (int i = 0; i < steps; i++) {
            double t = 2 * Math.PI * i / steps; // uhel

            double x = centerX + radius * Math.cos(t);
            double z = centerZ + radius * Math.sin(t);

            // horní vrchol
            vertexBuffer.add(new Point3D(x, centerY + halfHeight, z));
            // dolní vrchol
            vertexBuffer.add(new Point3D(x, centerY - halfHeight, z));
        }

        // hrany
        for (int i = 0; i < steps; i++) {
            int topCurrent = i * 2;
            int bottomCurrent = i * 2 + 1;
            int topNext = ((i + 1) % steps) * 2;
            int bottomNext = ((i + 1) % steps) * 2 + 1;

            // svislé hrany
            indexBuffer.add(topCurrent);
            indexBuffer.add(bottomCurrent);

            // hrany horní kružnice
            indexBuffer.add(topCurrent);
            indexBuffer.add(topNext);

            // hrany dolní kružnice
            indexBuffer.add(bottomCurrent);
            indexBuffer.add(bottomNext);
        }
    }

    // výchozí válec
    public Cylinder() {
        this(0, -3, 0, 0.5, 1);
    }
}
