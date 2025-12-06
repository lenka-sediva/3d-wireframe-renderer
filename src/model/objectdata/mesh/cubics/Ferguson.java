package model.objectdata.mesh.cubics;

import model.objectdata.mesh.Mesh;
import transforms.Cubic;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.awt.*;

public class Ferguson extends Mesh {

    public Ferguson() {
        // 2 koncové body
        Point3D p0 = new Point3D(-3, -1, -1); // počáteční bod
        Point3D p1 = new Point3D(-3, 1, 1);   // koncový bod

        // 2 tečné vektory v koncových bodech
        Point3D t0 = new Point3D(1, 2, 0);    // tečný vektor na začátku
        Point3D t1 = new Point3D(1, -2, 0);   // tečný vektor na konci

        Point3D[] points = new Point3D[] {p0, p1, t0, t1};

        Cubic ferguson = new Cubic(Cubic.FERGUSON, points);

        // počet bodů pro křivku
        int samples = 64;

        // vypočítá body na křivce
        for (int i = 0; i < samples; i++) {
            double t = (double) i / (samples - 1);
            getVB().add(ferguson.compute(t));
        }

        // indexBuffer pro spojitou čáru
        for (int i = 0; i < getVB().size() - 1; i++) {
            getIB().add(i);
            getIB().add(i + 1);
        }

        this.setColor(Color.ORANGE);

        this.setModelMat(new Mat4Identity());
    }
}
