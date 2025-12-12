package model.objectdata.mesh.cubics;

import model.objectdata.mesh.Cube;
import model.objectdata.mesh.Curve;
import transforms.Cubic;
import transforms.Point3D;

import java.awt.*;

public class Bezier extends Curve {

    private final Point3D[] points;

    public Bezier() {
        this(new Cube());
    }

    public Bezier(Cube cube) {
        super("Bezier", "B", Color.MAGENTA);

        // koncové body a řídící body křivky
        points = new Point3D[]{
                cube.getVB().getFirst(), // začátek
                new Point3D(-0.3, 0.4, 0.1), // řídící bod
                new Point3D(0.4, -0.2, 0.3), // řídící bod
                cube.getVB().get(6), // konec
        };

        generate(64); // výchozí počet bodů, které tvoří křivku
    }

    @Override
    public void generate(int precision) {
        Cubic bezier = new Cubic(Cubic.BEZIER, points);
        buildCurveVerticesAndIndices(bezier::compute, precision);
    }
}
