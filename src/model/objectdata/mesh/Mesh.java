package model.objectdata.mesh;

import model.objectdata.Line;
import transforms.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mesh {

    List<Point3D> vertexBuffer = new ArrayList<>();
    List<Integer> indexBuffer = new ArrayList<>();
    Mat4 modelMat  = new Mat4Identity(); // jednotková matice
    // color
    // boolean isSelected

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
}
