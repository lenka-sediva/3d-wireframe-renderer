package model.objectdata.mesh.cubics;

import model.objectdata.mesh.Mesh;
import model.objectdata.mesh.Pyramid;
import transforms.Cubic;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.awt.*;

public class Coons extends Mesh {

    public Coons(Pyramid pyramid) {
        super("Coons", "N", Color.MAGENTA);

        // vrcholy pyramidy
        Point3D p0 = pyramid.getVB().get(0); // spodní levý
        Point3D p1 = pyramid.getVB().get(4); // horní vrchol
        Point3D p2 = pyramid.getVB().get(2); // spodní pravý
        Point3D p3 = pyramid.getVB().get(3); // spodní levý zadní

        // 4 řídící body pro Coons – aby křivka byla uvnitř pyramidy
        Point3D g1 = p0;
        Point3D g2 = new Point3D((p0.getX() + p1.getX()) / 2, (p0.getY() + p1.getY()) / 2, p1.getZ() - 0.3); // mírně dolů od vrcholu
        Point3D g3 = new Point3D((p2.getX() + p3.getX()) / 2, (p2.getY() + p3.getY()) / 2, p2.getZ() + 0.3); // mírně nahoru od spodní hrany
        Point3D g4 = p1;

        Point3D[] points = new Point3D[]{g1, g2, g3, g4};

        Cubic coons = new Cubic(Cubic.COONS, points);

        // počet bodů pro křivku
        int samples = 64;

        // vypočítá body na křivce
        for (int i = 0; i < samples; i++) {
            double t = (double) i / (samples - 1);
            getVB().add(coons.compute(t));
        }

        // indexBuffer pro spojitou čáru
        for (int i = 0; i < getVB().size() - 1; i++) {
            getIB().add(i);
            getIB().add(i + 1);
        }

        this.setModelMat(new Mat4Identity());
    }

    public Coons() {
        this(new Pyramid());
    }
}
