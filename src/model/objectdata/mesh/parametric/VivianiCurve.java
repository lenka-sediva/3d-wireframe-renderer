package model.objectdata.mesh.parametric;

import model.objectdata.mesh.Mesh;
import transforms.Point3D;

import java.awt.*;
import java.util.List;

public class VivianiCurve extends Mesh {
    private static final int steps = 100;

    public VivianiCurve() {
        super("Viviani", "V", Color.ORANGE);

        double tStep = 2 * Math.PI / steps;

        List<Point3D> vertexBuffer = getVB();
        List<Integer> indexBuffer = getIB();

        // přidávání bodů do vertexBufferu
        for (int i = 0; i <= steps; i++) {
            double t = i * tStep;

            // umístění do krychle [-0.5, 0.5]
            double x = (1 + Math.cos(t)) / 2 - 0.5;
            double y = Math.sin(t) / 2;
            double z = Math.sin(t) / 2;

            vertexBuffer.add(new Point3D(x, y, z));

            // spojení s předchozím bodem pomocí indexBufferu
            if (i > 0) {
                indexBuffer.add(i - 1);
                indexBuffer.add(i);
            }
        }
    }
}
