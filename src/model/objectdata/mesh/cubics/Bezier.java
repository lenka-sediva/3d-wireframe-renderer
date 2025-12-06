package model.objectdata.mesh.cubics;

import model.objectdata.mesh.Mesh;
import transforms.Cubic;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.awt.*;

public class Bezier extends Mesh {

    public Bezier() {
        // řídící body křivky + koncové body ve 2 protilehlých vrcholech krychle
        Point3D p0 = new Point3D(-0.5, -0.5, -0.5); // začátek (levý spodní zadní roh)
        Point3D p1 = new Point3D(-0.3, 0.4, 0.1); // řídící bod
        Point3D p2 = new Point3D(0.4, -0.2, 0.3); // řídící bod
        Point3D p3 = new Point3D(0.5, 0.5, 0.5); // konec (pravý horní přední roh)

        Point3D[] points = new Point3D[] {p0, p1, p2, p3};

        Cubic bezier = new Cubic(Cubic.BEZIER, points);

        // počet bodů pro křivku
        int samples = 64;

        // vypočítá body na křivce
        for (int i = 0; i < samples; i++) {
            double t = (double) i / (samples - 1);
            getVB().add(bezier.compute(t));
        }

        // indexBuffer pro spojitou čáru
        for (int i = 0; i < getVB().size() - 1; i ++) {
            getIB().add(i);
            getIB().add(i + 1);
        }

        // barva křivky
        this.setColor(Color.MAGENTA);

        // jednotková matice
        this.setModelMat(new Mat4Identity());
    }
}
