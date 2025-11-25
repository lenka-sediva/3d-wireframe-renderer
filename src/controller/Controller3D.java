package controller;

import model.objectdata.mesh.Arrow;
import model.objectdata.mesh.Cube;
import model.objectdata.mesh.Mesh;
import model.objectdata.mesh.Triangle;
import model.rasterops.rasterizer.LineRasterizer;
import model.rasterops.rasterizer.LineRasterizerBresenham;
import model.rasterops.renderer.Renderer3D;
import transforms.*;
import view.Panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller3D implements Controller {

    private final Panel panel;
    private Renderer3D renderer3D;
    private LineRasterizer liner;

    private Mesh triangle;
    private Mesh arrow;
    private Mesh cube;
    private List<Mesh> objects = new ArrayList<>();

    private double uhel = 90.d;
    private double pohyb = 0.1; // přesnější pohyb = 0.01
    private Mat4 maticeRotace;
    private Mat4 maticePohybu;

    public Controller3D(Panel panel) {
        this.panel = panel;

        initObjects();
        initListeners(panel);
        renderScene();
    }

    @Override
    public void initObjects() {
        liner = new LineRasterizerBresenham(panel.getRaster());
        renderer3D = new Renderer3D(liner, panel);

        triangle = new Triangle();
        arrow = new Arrow();
        cube = new Cube();

        objects.add(triangle);
        objects.add(arrow);
        //objects.add(cube);
    }

    @Override
    public void initListeners(Panel panel) {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    // vymazání plátna
                    case KeyEvent.VK_C:
                        hardClear();
                        break;

                    // pro rotaci
                    case KeyEvent.VK_R:
                        maticeRotace = new Mat4Rot(Math.toRadians(uhel), new Vec3D(0, 0, 1));
                        arrow.setModelMat(arrow.getModelMat().mul(maticeRotace)); // přenásobení maticí rotace
                        break;

                    // pro translaci (pohyb)
                    case KeyEvent.VK_T:
                        // pohyb po ose x
                        maticePohybu = new Mat4Transl(pohyb, 0, 0);
                        arrow.setModelMat(arrow.getModelMat().mul(maticePohybu));
                        break;

                    case KeyEvent.VK_O:
                        arrow.setModelMat(new Mat4Identity());
                        break;
                }
                renderScene();
            }
        });
    }

    // posílá do seznamu objekty a vyrenderuje je všechny najednou
    private void renderScene() {
        panel.clear();

        for(Mesh mesh : objects) {
            renderer3D.render(mesh);
        }

        panel.repaint();
    }

    private void hardClear() {
        // TODO
        panel.clear(); // vymaže obrazovku
        for(Mesh object : objects) {
            object.setModelMat(new Mat4()); // reset identitou
        }
        panel.repaint();
        panel.repaint();
    }
}
