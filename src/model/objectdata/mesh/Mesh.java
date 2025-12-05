package model.objectdata.mesh;

import transforms.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mesh {

    List<Point3D> vertexBuffer = new ArrayList<>();
    List<Integer> indexBuffer = new ArrayList<>();
    Mat4 modelMat  = new Mat4Identity(); // jednotková matice, původní stav tělesa
    Color color = Color.WHITE; // výchozí barva
    boolean isSelected;

    protected void addIndices(Integer... indices) {
        indexBuffer.addAll(Arrays.asList(indices));
    }

    public List<Point3D> getVB() {
        return vertexBuffer;
    }

    public List<Integer> getIB() {
        return indexBuffer;
    }

    public Mat4 getModelMat() {
        return modelMat;
    }

    public void setModelMat(Mat4 modelMat) {
        this.modelMat = modelMat;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    // vypočítá střed objektu pro správný scale
    public Vec3D getCenter() {
        double x = 0, y = 0, z = 0;

        for (Point3D p : vertexBuffer) {
            x += p.getX();
            y += p.getY();
            z += p.getZ();
        }

        int size = vertexBuffer.size();
        return new Vec3D(x / size, y / size, z / size);
    }
}
