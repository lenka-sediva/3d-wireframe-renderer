package controller;

import model.objectdata.mesh.Arrow;
import model.objectdata.mesh.Mesh;
import model.objectdata.mesh.Triangle;
import model.rasterops.rasterizer.LineRasterizer;
import model.rasterops.rasterizer.LineRasterizerBresenham;
import model.rasterops.renderer.Renderer3D;
import view.Panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller3D implements Controller {

    private final Panel panel;
    private Renderer3D renderer3D;
    private LineRasterizer liner;

    private Mesh triangle;
    private Mesh arrow;

    public Controller3D(Panel panel) {
        this.panel = panel;

        initObjects();
        renderScene();
    }

    @Override
    public void initObjects() {
        liner = new LineRasterizerBresenham(panel.getRaster());
        renderer3D = new Renderer3D(liner);

        triangle = new Triangle();
        arrow = new Arrow();
    }

    @Override
    public void initListeners(Panel panel) {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // na klávesu C vymazat plátno
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    // TODO
                }
            }
        });
    }

    // TODO posílat do seznamu objekty a vyrenderovat je všechny najednou

    private void renderScene() {
        panel.clear();

        renderer3D.render(triangle);
        renderer3D.render(arrow);

        panel.repaint();
    }

    private void hardClear() {
        panel.clear();
        // TODO
    }
}
