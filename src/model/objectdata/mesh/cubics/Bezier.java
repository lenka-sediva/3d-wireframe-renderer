package model.objectdata.mesh.cubics;

import model.objectdata.mesh.Mesh;
import transforms.Cubic;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.awt.*;

public class Bezier extends Mesh {

    public Bezier() {
        // řídící body křivky, koncové body v protilehlých vrcholech krychle
        Point3D p0 = new Point3D(0, 0, 0); // levý spodní roh
        Point3D p1 = new Point3D(1, 1, 1); // zakřivení nahoru
        Point3D p2 = new Point3D(0, 1, 0.3); // zakřivení dopředu
        Point3D p3 = new Point3D(1, 0, 0.7); // pravý horní roh

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
        for (int i = 0; i < getVB().size(); i ++) {
            getIB().add(i);
        }

        // barva křivky
        this.setColor(Color.MAGENTA);

        // jednotková matice
        this.setModelMat(new Mat4Identity());
    }
}
