package model.objectdata.mesh;

import transforms.Point3D;

public class Pyramid extends Mesh {

    public Pyramid(double x, double y, double z, double size) {

        double half = size / 2.0;

        // spodní vrcholy
        Point3D p1 = new Point3D(x - half, y - half, z - half);
        Point3D p2 = new Point3D(x + half, y - half, z - half);
        Point3D p3 = new Point3D(x + half, y + half, z - half);
        Point3D p4 = new Point3D(x - half, y + half, z - half);

        // horní vrchol
        Point3D p5 = new Point3D(x, y, z + half);

        vertexBuffer.add(p1); // 0
        vertexBuffer.add(p2); // 1
        vertexBuffer.add(p3); // 2
        vertexBuffer.add(p4); // 3
        vertexBuffer.add(p5); // 4

        // spodní hrany
        addIndices(0, 1, 1, 2, 2, 3, 3, 0);

        // boční hrany ke špičce
        addIndices(0, 4, 1, 4, 2, 4, 3, 4);
    }

    public Pyramid() {
        this(0, 3, 0, 1);
    }
}
