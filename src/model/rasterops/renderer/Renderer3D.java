package model.rasterops.renderer;

import model.objectdata.Line;
import model.objectdata.mesh.Mesh;
import model.rasterops.rasterizer.LineRasterizer;
import transforms.Col;
import transforms.Point3D;
import transforms.Vec3D;

import java.awt.*;

public class Renderer3D {

    private final LineRasterizer liner;

    public Renderer3D(LineRasterizer liner) {
        this.liner = liner;
    }

    public void render(Mesh mesh) {
        for(int i = 0; i < mesh.getIB().size(); i += 2) {
            int index1 = mesh.getIB().get(i);
            int index2 = mesh.getIB().get(i+1);

            Point3D a = mesh.getVB().get(index1);
            Point3D b = mesh.getVB().get(index2);

            Line line = new Line((int) Math.round(a.getX()),
                                 (int) Math.round(a.getY()),
                                 (int) Math.round(b.getX()),
                                 (int) Math.round(b.getY()));

            liner.setColor(Color.WHITE);
            liner.rasterize(line);

        }
    }
/*
    // Slide 105
    private Vec3D transformToWindow() {

    }
*/

}
