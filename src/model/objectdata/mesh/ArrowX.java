package model.objectdata.mesh;

import transforms.Point3D;

import java.awt.*;

public class ArrowX extends Mesh {

    public ArrowX() {
        super("Arrow", "-", Color.WHITE);
        // souřadnice v NDC
        // x <-1, 1>
        // y <-1, 1>
        // z <0,1>

        Point3D p1 = new Point3D(0, 0, 0);
        Point3D p2 = new Point3D(0.8, 0, 0);
        Point3D p3 = new Point3D(0.8, 0.5, 0);
        Point3D p4 = new Point3D(0.8, -0.5, 0);
        Point3D p5 = new Point3D(0.95, 0, 0);

        vertexBuffer.add(p1);
        vertexBuffer.add(p2);
        vertexBuffer.add(p3);
        vertexBuffer.add(p4);
        vertexBuffer.add(p5);

        addIndices(
                0, 1,
                1, 2,
                2, 3,
                3, 4,
                2, 4
        );
    }
}
