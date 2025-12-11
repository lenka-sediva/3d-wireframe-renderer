package model.objectdata.mesh;

import transforms.Mat4Identity;
import transforms.Point3D;

import java.awt.*;

public abstract class Curve extends Mesh {

    public Curve(String name, String shortcut, Color defaultColor) {
        super(name, shortcut, defaultColor);
    }

    /**
     * Přegeneruje body křivky s danou přesností
     * @param precision počet bodů k vykreslení
     */
    public abstract void generate(int precision);

    /**
     * Pomocná metoda pro naplnění vertex bufferu a index bufferu z libovolné křivky
     * @param function funkce, která pro parametr t vrací bod na křivce
     * @param precision počet bodů k vykreslení
     */
    protected void buildCurveVerticesAndIndices(CurveFunction function, int precision) {
        // vyčistí předchozí body
        getVB().clear();
        getIB().clear();

        // generuje body křivky podle parametru t [0,1]
        for (int i = 0; i < precision; i++) {
            double t = (double) i / (precision - 1);
            getVB().add(function.compute(t));
        }

        // generuje indexy pro spojení bodů do spojité čáry
        for (int i = 0; i < getVB().size() - 1; i++) {
            getIB().add(i);
            getIB().add(i + 1);
        }

        setModelMat(new Mat4Identity());
    }

    // funkční rozhraní pro výpočet bodu křivky v závislosti na parametru t
    @FunctionalInterface
    public interface CurveFunction {
        Point3D compute(double t);
    }
}
