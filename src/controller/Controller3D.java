package controller;

import model.objectdata.cubics.Bezier;
import model.objectdata.mesh.*;
import model.rasterops.rasterizer.LineRasterizer;
import model.rasterops.rasterizer.LineRasterizerBresenham;
import model.rasterops.renderer.Renderer3D;
import transforms.*;
import view.Panel;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D implements Controller {

    private final Panel panel;
    private Renderer3D renderer3D;
    private LineRasterizer liner;

    private Axis axis;
    private Mesh triangle;
    private Mesh cube;
    private Mesh sphere;
    private List<Mesh> objects = new ArrayList<>();
    private Mesh bezier;

    private Camera view; // pozorovatel
    Mat4 proj, orto;

    private double uhel = 90.d;
    private double pohyb = 0.1; // přesnější pohyb = 0.01
    private Mat4 maticeRotace;
    private Mat4 maticePohybu;

    private int lastMouseX, lastMouseY;

    public Controller3D(Panel panel) {
        this.panel = panel;

        initObjects();
        initListeners(panel);
        renderScene();
    }

    @Override
    public void initObjects() {
        // TODO změnit azimuth, aby byl pozorovatel uprostřed
        view = new Camera(new Vec3D(3, 3, 3), Math.PI, -1*(Math.PI/4), 3, true); // Vec3D = pozice, Math.PI = azimut, záporný zenit (aby se díval dolů)

        proj = new Mat4PerspRH(Math.toRadians(90), (double) panel.getHeight() / panel.getWidth(), 0.1, 100); // perspektivní matice projekce

        // ortogonální matice projekce
        double scale = 5;
        double aspect = (double) panel.getWidth() / panel.getHeight();
        orto = new Mat4OrthoRH(scale * aspect, scale, 0.1, 100);

        //orto = new Mat4OrthoRH(panel.getWidth() / 10., panel.getHeight() / 10., 0.1, 100); // orotogonální matice projekce

        liner = new LineRasterizerBresenham(panel.getRaster());
        renderer3D = new Renderer3D(liner, panel);
        renderer3D.setView(view.getViewMatrix()); // získáme pohledovou matici
        renderer3D.setProj(proj); // matice projekce

        triangle = new Triangle();
        cube = new Cube();
        axis = new Axis();
        sphere = new Sphere();
        bezier = new Bezier();

        objects.add(triangle);
        objects.add(cube);
        objects.add(sphere);
        objects.add(bezier);

        selectedObject(cube); // krychle je výchozí vybraná
    }

    @Override
    public void initListeners(Panel panel) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - lastMouseX;
                int dy = e.getY() - lastMouseY;

                double sensitivity = 0.01; // citlivost myši

                view = view.addAzimuth(dx * sensitivity).addZenith(-dy * sensitivity);

                lastMouseX = e.getX();
                lastMouseY = e.getY();

                renderer3D.setView(view.getViewMatrix());
                renderScene();
            }
        });

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
                        for (Mesh m : objects) {
                            if (m.isSelected()) {
                                m.setModelMat(m.getModelMat().mul(maticeRotace)); // přenásobení maticí rotace
                            }
                        }
                        break;

                    // pro translaci (pohyb)
                    case KeyEvent.VK_T:
                        // pohyb po ose x
                        maticePohybu = new Mat4Transl(pohyb, 0, 0);
                        for (Mesh m : objects) {
                            if (m.isSelected()) {
                                m.setModelMat(m.getModelMat().mul(maticePohybu));
                            }
                        }
                        break;

                    // TODO změna měřítka (scale)

                    // vrácení objektů zpátky
                    case KeyEvent.VK_O:
                        for (Mesh object : objects) {
                            object.setModelMat(new Mat4Identity());
                        }
                        break;

                    // pohyby pozorovatele WASD
                    case KeyEvent.VK_W:
                        view = view.forward(1);
                        renderer3D.setView(view.getViewMatrix());
                        break;

                    case KeyEvent.VK_S:
                        view = view.backward(1);
                        renderer3D.setView(view.getViewMatrix());
                        break;

                    case KeyEvent.VK_A:
                        view = view.left(1);
                        renderer3D.setView(view.getViewMatrix());
                        break;

                    case KeyEvent.VK_D:
                        view = view.right(1);
                        renderer3D.setView(view.getViewMatrix());
                        break;

                    // projekční pohled (výchozí)
                    case KeyEvent.VK_P:
                        renderer3D.setProj(proj);
                        break;

                    // orto pohled
                    case KeyEvent.VK_H:
                        renderer3D.setProj(orto);
                        break;

                    // vybrání objektů
                    case KeyEvent.VK_K:
                        selectedObject(cube);
                        panel.setCurrentObject("krychle");
                        break;

                    case KeyEvent.VK_E:
                        selectedObject(sphere);
                        panel.setCurrentObject("koule");
                        break;
                }
                renderScene();
            }
        });
    }

    private void selectedObject(Mesh obj) {
        // zruší výběr předchozích objektů
        for (Mesh m : objects) {
            m.setSelected(false);
        }

        // označí nový objekt
        obj.setSelected(true);
    }

    // posílá do seznamu objekty a vyrenderuje je všechny najednou + šipky
    private void renderScene() {
        panel.clear();

        renderer3D.render(axis.xAxis);
        renderer3D.render(axis.yAxis);
        renderer3D.render(axis.zAxis);

        for(Mesh mesh : objects) {
            renderer3D.render(mesh);
        }

        panel.repaint();
    }

    private void hardClear() {
        panel.clear();

        for(Mesh object : objects) {
            object.setModelMat(new Mat4());
        }

        renderScene();
    }
}
