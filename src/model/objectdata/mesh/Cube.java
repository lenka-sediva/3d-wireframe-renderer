package model.objectdata.mesh;

import transforms.Point3D;

import java.awt.*;

public class Cube extends Mesh {

    public Cube(double x, double y, double z, double size) {
        super("Krychle", "K", Color.WHITE);

        double half = size / 2.0;

        // spodní vrcholy
        Point3D p1 = new Point3D(x - half, y - half, z - half);
        Point3D p2 = new Point3D(x + half, y - half, z - half);
        Point3D p3 = new Point3D(x + half, y + half, z - half);
        Point3D p4 = new Point3D(x - half, y + half, z - half);

        // horní vrcholy
        Point3D p5 = new Point3D(x - half, y - half, z + half);
        Point3D p6 = new Point3D(x + half, y - half, z + half);
        Point3D p7 = new Point3D(x + half, y + half, z + half);
        Point3D p8 = new Point3D(x - half, y + half, z + half);

        vertexBuffer.add(p1); // 0
        vertexBuffer.add(p2); // 1
        vertexBuffer.add(p3); // 2
        vertexBuffer.add(p4); // 3
        vertexBuffer.add(p5); // 4
        vertexBuffer.add(p6); // 5
        vertexBuffer.add(p7); // 6
        vertexBuffer.add(p8); // 7

        // spodní hrany
        addIndices(0, 1, 1, 2, 2, 3, 3, 0);

        // horní hrany
        addIndices(4, 5, 5, 6, 6, 7, 7, 4);

        // vertikální hrany
        addIndices(0, 4, 1, 5, 2, 6, 3, 7);
    }

    // výchozí krychle se středem v (0,0,0) a velikostí 1
    public Cube() {
        this(0, 0, 0, 1);
    }
}
