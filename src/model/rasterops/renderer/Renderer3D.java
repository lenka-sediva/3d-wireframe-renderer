package model.rasterops.renderer;

import model.objectdata.Line;
import model.objectdata.mesh.Mesh;
import model.objectdata.mesh.Triangle;
import model.rasterops.rasterizer.LineRasterizer;
import transforms.Point3D;
import transforms.Vec3D;
import view.Panel;

import java.awt.*;

public class Renderer3D {

    private final LineRasterizer liner;
    private final Panel panel;

    public Renderer3D(LineRasterizer liner, Panel panel) {
        this.liner = liner;
        this.panel = panel;
    }

    public void render(Mesh mesh) {
        for(int i = 0; i < mesh.getIB().size(); i += 2) {
            int index1 = mesh.getIB().get(i);
            int index2 = mesh.getIB().get(i+1);

            Point3D a = mesh.getVB().get(index1);
            Point3D b = mesh.getVB().get(index2);

            if (mesh instanceof Triangle) { // pro modely v DC
                Line line = new Line((int) Math.round(a.getX()),
                        (int) Math.round(a.getY()),
                        (int) Math.round(b.getX()),
                        (int) Math.round(b.getY()));

                liner.setColor(Color.RED);
                liner.rasterize(line);
            } else { // pro modely v NDC
                a = a.mul(mesh.getModelMat());
                b = b.mul(mesh.getModelMat());

                // podmínka pro ořezání
                if (isInView(a, b)) {
                    // 5. KROK grafické pipeline
                    // dehomogenizace (vydělení souřadnic w)
                    Vec3D aDehomog = new Vec3D(a.getX() / a.getW(), a.getY() / a.getW(), a.getZ() / a.getW());
                    Vec3D bDehomog = new Vec3D(b.getX() / b.getW(), b.getY() / b.getW(), b.getZ() / b.getW());

                    // transformace do okna
                    Vec3D aToWindow = transformToWindow(aDehomog);
                    Vec3D bToWindow = transformToWindow(bDehomog);

                    Line line = new Line((int) Math.round(aToWindow.getX()),
                            (int) Math.round(aToWindow.getY()),
                            (int) Math.round(bToWindow.getX()),
                            (int) Math.round(bToWindow.getY()));

                    liner.setColor(Color.WHITE);
                    liner.rasterize(line);
                }
            }
        }
    }

    /**
     * 4. KROK grafické pipeline
     * Ořezání v homogenních souřadnicích
     * Prezentace F -> Slide 76
     *
     * @param a první bod hrany
     * @param b druhý bod hrany
     * @return true, pokud se nachází v prostoru průmětny
     */
    private boolean isInView(Point3D a, Point3D b) {
        // homogenní ořezání – před dehomogenizací
        boolean aInside = a.getX() >= -a.getW() && a.getX() <= a.getW() &&
                a.getY() >= -a.getW() && a.getY() <= a.getW() &&
                a.getZ() >= 0 && a.getZ() <= a.getW();

        boolean bInside = b.getX() >= -b.getW() && b.getX() <= b.getW() &&
                b.getY() >= -b.getW() && b.getY() <= b.getW() &&
                b.getZ() >= 0 && b.getZ() <= b.getW();

        // úsečka je v zorném poli, pokud je alespoň 1 bod uvnitř
        return aInside && bInside;
    }

    /**
     * 7. KROK  grafické pipeline
     * Transformace do okna obrazovky
     * Prezentace F -> SLide 105
     *
     * @param p vektor bodu pro transformaci
     * @return vektor výsledné transformace
     */
    private Vec3D transformToWindow(Vec3D p) {
        int w = panel.getWidth();
        int h = panel.getHeight();

        // 1) invert Y
        Vec3D r = p.mul(new Vec3D(1, -1, 1));

        // 2) posun z [-1,1] na [0,2]
        r = r.add(new Vec3D(1, 1, 0));

        // 3) scaling do pixelů
        r = r.mul(new Vec3D((w - 1) / 2.0, (h - 1) / 2.0, 1));

        return r;
    }
}
