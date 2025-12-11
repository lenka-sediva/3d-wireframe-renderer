package model.objectdata.mesh.parametric;

import model.objectdata.mesh.Mesh;
import transforms.Point3D;

import java.awt.*;
import java.util.List;

public class VivianiCurve extends Mesh {
    private static final int steps = 100; // počet bodů k vykreslení křivky

    public VivianiCurve() {
        super("Viviani", "V", Color.PINK);

        double tStep = 2 * Math.PI / steps; // krok mezi body křivky

        List<Point3D> vertexBuffer = getVB();
        List<Integer> indexBuffer = getIB();

        // přidávání bodů do vertexBufferu
        for (int i = 0; i <= steps; i++) {
            double t = -Math.PI / 2 + i * tStep;

            // parametrická Vivianiho křivka
            double x = Math.cos(t) * Math.cos(t);
            double y = Math.cos(t) * Math.sin(t);
            double z = Math.sin(t);

            // škálování do krychle [-0.5,0.5]
            x = x - 0.5; // x = [-0.5, 0.5]
            y = y / 2; // y = [-0.25, 0.25]
            z = z / 2; // z = [-0.5, 0.5]

            vertexBuffer.add(new Point3D(x, y, z));

            // spojení s předchozím bodem pomocí indexBufferu
            if (i > 0) {
                indexBuffer.add(i - 1);
                indexBuffer.add(i);
            }
        }
    }
}
