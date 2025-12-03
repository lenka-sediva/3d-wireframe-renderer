package model.objectdata.mesh;

import transforms.Mat4Identity;
import transforms.Mat4Rot;
import transforms.Vec3D;

import java.awt.*;

public class Axis {

    public Mesh xAxis;
    public Mesh yAxis;
    public Mesh zAxis;

    public Axis() {
        // X osa = červená
        xAxis = new Arrow();
        xAxis.setModelMat(new Mat4Identity());
        xAxis.setColor(Color.RED);

        // Y osa = zelená
        yAxis = new Arrow();
        yAxis.setModelMat(new Mat4Rot(Math.toRadians(90), new Vec3D(0, 0, 1)));
        yAxis.setColor(Color.GREEN);

        // Z osa = modrá
        zAxis = new Arrow();
        zAxis.setModelMat(new Mat4Rot(Math.toRadians(-90), new Vec3D(0, 1, 0)));
        zAxis.setColor(Color.BLUE);
    }
}
