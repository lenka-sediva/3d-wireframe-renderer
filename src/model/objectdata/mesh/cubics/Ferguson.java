package model.objectdata.mesh.cubics;

import model.objectdata.mesh.Curve;
import model.objectdata.mesh.Pyramid;
import transforms.Cubic;
import transforms.Point3D;

import java.awt.*;

public class Ferguson extends Curve {

    private final Point3D[] points;

    public Ferguson() {
        this(new Pyramid());
    }

    public Ferguson(Pyramid pyramid) {
        super("Ferguson", "F", Color.MAGENTA);

        // koncové body ve vrcholech pyramidy
        Point3D p0 = pyramid.getVB().get(0);
        Point3D p1 = pyramid.getVB().get(4);

        // tečné vektory v koncových bodech
        Point3D t0 = new Point3D(0.5, 0, 0.7);
        Point3D t1 = new Point3D(-5, 0, -0.5);

        points = new Point3D[]{p0, p1, t0, t1};

        generate(64); // výchozí počet bodů
    }

    @Override
    public void generate(int precision) {
        Cubic ferguson = new Cubic(Cubic.FERGUSON, points);
        buildCurveVerticesAndIndices(ferguson::compute, precision);
    }
}
