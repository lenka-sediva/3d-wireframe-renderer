package model.objectdata.mesh.cubics;

import model.objectdata.mesh.Curve;
import model.objectdata.mesh.Pyramid;
import transforms.Cubic;
import transforms.Point3D;

import java.awt.*;

public class Coons extends Curve {

    private final Point3D[] controlPoints;

    public Coons() {
        this(new Pyramid());
    }

    public Coons(Pyramid pyramid) {
        super("Coons", "N", Color.MAGENTA);

        // vrcholy pyramidy
        Point3D p0 = pyramid.getVB().get(0); // spodní levý
        Point3D p1 = pyramid.getVB().get(4); // horní vrchol
        Point3D p2 = pyramid.getVB().get(2); // spodní pravý
        Point3D p3 = pyramid.getVB().get(3); // spodní zadní levý

        // řídící body pro Coons – uvnitř pyramidy
        controlPoints = new Point3D[]{
                p0,
                new Point3D((p0.getX() + p1.getX()) / 2, (p0.getY() + p1.getY()) / 2, p1.getZ() - 0.3),
                new Point3D((p2.getX() + p3.getX()) / 2, (p2.getY() + p3.getY()) / 2, p2.getZ() + 0.3),
                p1
        };

        generate(64); // výchozí počet bodů
    }

    @Override
    public void generate(int precision) {
        Cubic coons = new Cubic(Cubic.COONS, controlPoints);
        buildCurveVerticesAndIndices(coons::compute, precision);
    }
}
