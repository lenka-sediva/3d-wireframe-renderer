package model.objectdata.mesh.cubics;

import transforms.Point3D;

import java.awt.*;

public class VivianiCurve extends Curve {

    public VivianiCurve() {
        super("Viviani", "I", Color.PINK);
        generate(50); // výchozí přesnost (počet bodů tvořících křivku)
    }

    @Override
    public void generate(int precision) {

        buildCurveVerticesAndIndices(t -> {
            // t je v rozsahu [0, 1], převedeme ho na interval [-π/2, 3π/2]
            double angle = -Math.PI / 2 + t * (2 * Math.PI);

            // parametrická rovnice
            double x = Math.cos(angle) * Math.cos(angle);
            double y = Math.cos(angle) * Math.sin(angle);
            double z = Math.sin(angle);

            // škálování do krychle
            x = x - 0.5;
            y = y / 2;
            z = z / 2;

            return new Point3D(x, y, z);
        }, precision);
    }
}
