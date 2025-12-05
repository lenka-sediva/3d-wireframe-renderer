package model.rasterops.renderer;

import model.objectdata.Line;
import model.objectdata.mesh.Mesh;
import model.rasterops.rasterizer.LineRasterizer;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;
import view.Panel;

public class Renderer3D {

    private final LineRasterizer liner;
    private final Panel panel;

    private Mat4 proj, view; // projekční a pohledová matice

    public Renderer3D(LineRasterizer liner, Panel panel) {
        this.liner = liner;
        this.panel = panel;
    }

    public void render(Mesh mesh) {
        // MVP (Model-View-Projection) matice
        Mat4 mvp = new Mat4(mesh.getModelMat()).mul(view).mul(proj);

        for(int i = 0; i < mesh.getIB().size(); i += 2) {
            int index1 = mesh.getIB().get(i);
            int index2 = mesh.getIB().get(i + 1);

            Point3D a = mesh.getVB().get(index1);
            Point3D b = mesh.getVB().get(index2);

            // pro všechny objekty v NDC
            a = a.mul(mvp);
            b = b.mul(mvp);

            //test(a,b);

            // 4. KROK grafické pipeline
            // podmínka pro ořezání
            if (isInView(a, b)) {
                // 5. KROK grafické pipeline
                // dehomogenizace (vydělení souřadnic w)
                Vec3D aDehomog = new Vec3D(a.getX() / a.getW(), a.getY() / a.getW(), a.getZ() / a.getW());
                Vec3D bDehomog = new Vec3D(b.getX() / b.getW(), b.getY() / b.getW(), b.getZ() / b.getW());

                // transformace do okna obrazovky
                Vec3D aToWindow = transformToWindow(aDehomog);
                Vec3D bToWindow = transformToWindow(bDehomog);

                Line line = new Line((int) Math.round(aToWindow.getX()),
                        (int) Math.round(aToWindow.getY()),
                        (int) Math.round(bToWindow.getX()),
                        (int) Math.round(bToWindow.getY()),
                        mesh.getColor().getRGB()
                );
                liner.rasterize(line);
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
     * Prezentace F -> Slide 105
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

    // metoda pro testování ořezání
    private void test(Point3D a, Point3D b) {
        boolean tooLeft = (a.getX() < -a.getW());
        boolean tooRight = (a.getX() > a.getW());
        boolean tooUp = (a.getY() > a.getW());
        boolean tooDown = (a.getY() < -a.getW());
        boolean tooBehind = (a.getZ() < 0);
        boolean tooFar = (a.getZ() > a.getW());

        System.out.println("--- FOR POINT A ---");
        System.out.println("Too left? " + tooLeft + ". A.x is: " + a.getX()+ ", should be >= -A.w: "+ (-a.getW()));
        System.out.println("Too right? " +tooRight + ". A.x is: " + a.getX() + ", should be <= A.w: "+ a.getW());
        System.out.println("Too down? " + tooDown + ". A.y is: " + a.getY() + ", should be >= A.w: "+ -a.getW());
        System.out.println("Too up? " + tooUp + ". A.y is: " + a.getY() + ", should be <= A.w: " + a.getW());
        System.out.println("Too behind? " + tooBehind + ". A.z is: " + a.getZ() + ", should be >= 0");
        System.out.println("Too far? " + tooFar + ". A.z is: " + a.getZ() + ", should be <= A.w: " + a.getW());

        tooLeft = (b.getX() < -b.getW());
        tooRight = (b.getX() > b.getW());
        tooUp = (b.getY() > b.getW());
        tooDown = (b.getY() < -b.getW());
        tooBehind = (b.getZ() > b.getW());
        tooFar = (b.getZ() < 0);

        System.out.println("--- FOR POINT B ---");
        System.out.println("Too left? " + tooLeft + ". B.x is: " + b.getX()+ ", should be >= -A.w: "+ (-b.getW()));
        System.out.println("Too right? " +tooRight + ". B.x is: " + b.getX() + ", should be <= A.w: "+ b.getW());
        System.out.println("Too down? " + tooDown + ". B.y is: " + b.getY() + ", should be >= A.w: "+ (-b.getW()));
        System.out.println("Too up? " + tooUp + ". B.y is: " + b.getY() + ", should be <= A.w: " + b.getW());
        System.out.println("Too behind? " + tooBehind + ". B.z is: " + b.getZ() + ", should be >= 0");
        System.out.println("Too far? " + tooFar + ". B.z is: " + b.getZ() + ", should be <= A.w: " + b.getW());

        System.out.println("=====================");
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }

    public void setView(Mat4 view) {
        this.view = view;
    }
}
