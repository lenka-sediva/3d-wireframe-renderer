package model.objectdata.mesh;

import transforms.Point3D;

import java.awt.*;

public class Sphere extends Mesh {

    /**
     * Vytvoří kouli
     * @param centerX střed X
     * @param centerY střed Y
     * @param centerZ střed Z
     * @param radius poloměr
     * @param latSegments počet horizontálních segmentů
     * @param lonSegments počet vertikálních segmentů
     */
    public Sphere(double centerX, double centerY, double centerZ, double radius, int latSegments, int lonSegments) {
        super("Koule", "U", Color.CYAN);

        // generování vrcholů
        for (int i = 0; i <= latSegments; i++) {
            double theta = Math.PI * i / latSegments; // od 0 do PI
            double sinTheta = Math.sin(theta);
            double cosTheta = Math.cos(theta);

            for (int j = 0; j <= lonSegments; j++) {
                double phi = 2 * Math.PI * j / lonSegments; // od 0 do 2PI
                double sinPhi = Math.sin(phi);
                double cosPhi = Math.cos(phi);

                double x = centerX + radius * sinTheta * cosPhi;
                double y = centerY + radius * cosTheta;
                double z = centerZ + radius * sinTheta * sinPhi;

                vertexBuffer.add(new Point3D(x, y, z));
            }
        }

        // generování hran (indices)
        for (int i = 0; i < latSegments; i++) {
            for (int j = 0; j < lonSegments; j++) {
                int first = i * (lonSegments + 1) + j;
                int second = first + lonSegments + 1;

                // horizontální hrany
                addIndices(first, first + 1);
                addIndices(second, second + 1);

                // vertikální hrany
                addIndices(first, second);
                addIndices(first + 1, second + 1);
            }
        }
    }

    public Sphere() {
        this(3, 0, 0, 1, 20, 20); // 20x20 segmentů
    }
}
