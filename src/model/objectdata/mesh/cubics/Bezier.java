package model.objectdata.mesh.cubics;

import model.objectdata.mesh.Curve;
import transforms.Cubic;
import transforms.Point3D;

import java.awt.*;

public class Bezier extends Curve {

    private final Point3D[] controlPoints; // řídící body

    public Bezier() {
        super("Bezier", "B", Color.MAGENTA);

        // koncové body a řídící body křivky
        controlPoints = new Point3D[]{
                new Point3D(-0.5, -0.5, -0.5), // začátek
                new Point3D(-0.3, 0.4, 0.1),   // řídící bod
                new Point3D(0.4, -0.2, 0.3),   // řídící bod
                new Point3D(0.5, 0.5, 0.5)     // konec
        };

        generate(64); // výchozí počet bodů
    }

    @Override
    public void generate(int precision) {
        Cubic bezier = new Cubic(Cubic.BEZIER, controlPoints);
        buildCurveVerticesAndIndices(bezier::compute, precision);
    }
}
