package model.objectdata.mesh.cubics;

import model.objectdata.mesh.Curve;
import model.objectdata.mesh.Cube;
import transforms.Cubic;
import transforms.Point3D;

import java.awt.*;

public class Coons extends Curve {

    private final Point3D[] points;

    public Coons() {
        this(new Cube());
    }

    public Coons(Cube cube) {
        super("Coons", "N", Color.MAGENTA);

        // posun o -3 na ose Y pro umístění do válce
        Point3D offset = new Point3D(0, -3, 0);

        // protilehlé vrcholy krychle
        Point3D p0 = cube.getVB().get(0).add(offset); // spodní levý zadní (0)
        Point3D p1 = cube.getVB().get(6).add(offset); // horní pravý přední (6)

        // body určují tvar křivky
        Point3D p2 = new Point3D(p0.getX(), p0.getY(), p1.getZ());
        Point3D p3 = new Point3D(p1.getX(), p1.getY(), p0.getZ());

        points = new Point3D[]{p0, p2, p3, p1};

        generate(64); // výchozí počet bodů
    }

    @Override
    public void generate(int precision) {
        Cubic coons = new Cubic(Cubic.COONS, points);
        buildCurveVerticesAndIndices(coons::compute, precision);
    }
}
