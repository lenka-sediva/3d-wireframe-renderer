package model.objectdata.mesh;

import transforms.Point3D;

import java.awt.*;

public class Cylinder extends Mesh {
    private static final int steps = 50; // počet segmentů kolem kruhu

    public Cylinder(double centerX, double centerY, double centerZ, double radius, double height) {
        super("Válec", "V", Color.CYAN);

        double halfHeight = height / 2.0;

        // generování vrcholů válce
        for (int i = 0; i < steps; i++) {
            double t = 2 * Math.PI * i / steps; // úhel pro aktuální segment

            double x = centerX + radius * Math.cos(t);
            double z = centerZ + radius * Math.sin(t);

            // horní vrchol
            vertexBuffer.add(new Point3D(x, centerY + halfHeight, z));
            // dolní vrchol
            vertexBuffer.add(new Point3D(x, centerY - halfHeight, z));
        }

        // generování hran
        for (int i = 0; i < steps; i++) {
            // horní a dolní vrcholy aktuálního a následujícího segmentu
            int p1 = i * 2; // horní vrchol aktuálního segmentu
            int p2 = i * 2 + 1; // dolní vrchol aktuálního segmentu
            int p3 = ((i + 1) % steps) * 2; // horní vrchol dalšího segmentu
            int p4 = ((i + 1) % steps) * 2 + 1; // dolní vrchol dalšího segmentu

            // svislé hrany
            indexBuffer.add(p1);
            indexBuffer.add(p2);

            // hrany horní kružnice
            indexBuffer.add(p1);
            indexBuffer.add(p3);

            // hrany dolní kružnice
            indexBuffer.add(p2);
            indexBuffer.add(p4);
        }
    }

    public Cylinder() {
        this(0, -3, 0, 0.5, 1);
    }
}
