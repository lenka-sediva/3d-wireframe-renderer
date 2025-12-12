package model.objectdata.mesh.parametric;

import model.objectdata.mesh.Curve;
import transforms.Point3D;

import java.awt.*;

public class VivianiCurve extends Curve {

    public VivianiCurve() {
        super("Viviani", "I", Color.PINK);
        generate(50); // výchozí přesnost
    }

    @Override
    public void generate(int precision) {
        // vyčistí staré body
        getVB().clear();
        getIB().clear();

        double tStep = 2 * Math.PI / precision;

        for (int i = 0; i <= precision; i++) {
            double t = -Math.PI / 2 + i * tStep; // úhel

            // parametrická Vivianiho křivka
            double x = Math.cos(t) * Math.cos(t);
            double y = Math.cos(t) * Math.sin(t);
            double z = Math.sin(t);

            // škálování do krychle [-0.5,0.5]
            x = x - 0.5;
            y = y / 2;
            z = z / 2;

            getVB().add(new Point3D(x, y, z));

            if (i > 0) {
                getIB().add(i - 1);
                getIB().add(i);
            }
        }
    }
}
