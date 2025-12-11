package model.objectdata.mesh;

import java.awt.*;

public class Axis {

    public Mesh xAxis;
    public Mesh yAxis;
    public Mesh zAxis;

    public Axis() {
        // X osa
        xAxis = new ArrowX();
        xAxis.setColor(Color.RED);

        // Y osa
        yAxis = new ArrowY();
        yAxis.setColor(Color.GREEN);

        // Z osa
        zAxis = new ArrowZ();
        zAxis.setColor(Color.BLUE);
    }
}
