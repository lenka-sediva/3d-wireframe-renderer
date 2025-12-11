package model.objectdata.mesh;

import transforms.Point3D;

import java.awt.*;

public class Triangle extends Mesh {

    public Triangle() {
        super("Trojúhelník", "T", Color.WHITE);
        // body v double
                                // device/window coordinates
        Point3D p1 = new Point3D(890., 200., 1.);
        Point3D p2 = new Point3D(950., 280., 1.);
        Point3D p3 = new Point3D(820., 330., 1.);

        vertexBuffer.add(p1); // index 0
        vertexBuffer.add(p2); // index 1
        vertexBuffer.add(p3); // index 2

        addIndices(0, 1, // 1. hrana
                   1, 2, // 2. hrana
                   2, 0  // 3. hrana
        );
    }
}
