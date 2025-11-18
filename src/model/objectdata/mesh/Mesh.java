package model.objectdata.mesh;

import model.objectdata.Line;
import transforms.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mesh {

    List<Point3D> vertexBuffer = new ArrayList<>();
    List<Integer> indexBuffer = new ArrayList<>();
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
}
