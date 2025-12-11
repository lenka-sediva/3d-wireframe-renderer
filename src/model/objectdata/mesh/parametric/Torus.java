package model.objectdata.mesh.parametric;

import model.objectdata.mesh.Mesh;
import transforms.Point3D;

import java.awt.*;
import java.util.List;

public class Torus extends Mesh {

    private static final double R = 0.75; // velký poloměr
    private static final double r = 0.25; // malý poloměr
    private static final int uSteps = 50; // počet segmentů kolem velkého kruhu
    private static final int vSteps = 30; // počet segmentů kolem malého kruhu

    // konstruktor s možností nastavit pozici
    public Torus(double centerX, double centerY, double centerZ) {
        super("Torus", "T", Color.CYAN);

        List<Point3D> vertexBuffer = getVB();
        List<Integer> indexBuffer = getIB();

        // generování bodů
        for (int i = 0; i <= uSteps; i++) {
            double u = 2 * Math.PI * i / uSteps;

            for (int j = 0; j <= vSteps; j++) {
                double v = 2 * Math.PI * j / vSteps;

                double x = centerX + (R + r * Math.cos(v)) * Math.cos(u);
                double y = centerY + (R + r * Math.cos(v)) * Math.sin(u);
                double z = centerZ + r * Math.sin(v);

                vertexBuffer.add(new Point3D(x, y, z));

                // hrana v rámci malého kruhu
                if (j > 0) {
                    int current = i * (vSteps + 1) + j;
                    int prev = current - 1;
                    indexBuffer.add(prev);
                    indexBuffer.add(current);
                }

                // hrana v rámci velkého kruhu
                if (i > 0 && j > 0) {
                    int current = i * (vSteps + 1) + j;
                    int prevU = (i - 1) * (vSteps + 1) + j;
                    indexBuffer.add(prevU);
                    indexBuffer.add(current);
                }
            }
        }
    }

    // výchozí konstruktor – torus bude vlevo od počátku
    public Torus() {
        this(-3, 0, 0);
    }
}
