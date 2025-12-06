package model.objectdata.mesh.cubics;

import model.objectdata.mesh.Mesh;
import transforms.Cubic;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.awt.*;

public class Coons extends Mesh {

    public Coons() {
        // 4 řídící body – konvexní obal křivky
        Point3D g1 = new Point3D(-4, -1, -1);
        Point3D g2 = new Point3D(-4, 1, 0.5);
        Point3D g3 = new Point3D(-2, -1, 0.5);
        Point3D g4 = new Point3D(-4, 1, 1);

        Point3D[] points = new Point3D[] {g1, g2, g3, g4};

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

        this.setColor(Color.WHITE);

        this.setModelMat(new Mat4Identity());
    }
}
