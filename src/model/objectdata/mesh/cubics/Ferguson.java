package model.objectdata.mesh.cubics;

import model.objectdata.mesh.Mesh;
import model.objectdata.mesh.Pyramid;
import transforms.Cubic;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.awt.*;

public class Ferguson extends Mesh {

    public Ferguson(Pyramid pyramid) {
        super("Ferguson", "F", Color.MAGENTA);

        // 2 koncové body ve vrcholech pyramidy
        Point3D p0 = pyramid.getVB().get(0);
        Point3D p1 = pyramid.getVB().get(4); // horní vrchol

        // 2 tečné vektory v koncových bodech
        Point3D t0 = new Point3D(0.5, 0, 0.7); // tečný vektor na spodním vrcholu
        Point3D t1 = new Point3D(-5, 0, -0.5); // tečný vektor na horním vrcholu

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

        this.setModelMat(new Mat4Identity());
    }

    public Ferguson() {
        this(new Pyramid());
    }
}
