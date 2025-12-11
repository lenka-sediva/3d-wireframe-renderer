package controller;

import model.objectdata.mesh.cubics.*;
import model.objectdata.mesh.*;
import model.objectdata.mesh.parametric.Torus;
import model.objectdata.mesh.parametric.VivianiCurve;
import model.rasterdata.RasterBI;
import model.rasterops.rasterizer.LineRasterizer;
import model.rasterops.rasterizer.LineRasterizerBresenham;
import model.rasterops.renderer.Renderer3D;
import transforms.*;
import view.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D implements Controller {

    private final Panel panel;
    private Renderer3D renderer3D;
    private LineRasterizer liner;

    private Axis axis;
    private Mesh cube, pyramid, sphere, bezier, coons, ferguson, viviani, torus;
    private final List<Mesh> objects = new ArrayList<>();

    private Camera view; // pozorovatel
    Mat4 proj, orto;

    private final double angle = 90.d; // uhel
    private final double speed = 0.5; // rychlost pozorovatele
    private Mat4 rotationMatrix;

    private int lastMouseX, lastMouseY;

    // pro animaci
    private Timer timer;
    private boolean rotating = false;
    private static final int FPS = 60;

    public Controller3D(Panel panel) {
        this.panel = panel;

        initObjects();
        initListeners(panel);
        startTimer();
        renderScene();
    }

    @Override
    public void initObjects() {
        view = new Camera(new Vec3D(0, 0, 0), Math.PI * 1.25, Math.PI * -0.125, 5, false);

        proj = new Mat4PerspRH(Math.toRadians(90), (double) panel.getHeight() / panel.getWidth(), 0.1, 100); // perspektivní matice projekce
        orto = new Mat4OrthoRH(panel.getWidth() / 100., panel.getHeight() / 100., 0.1, 100); // orotogonální matice projekce

        liner = new LineRasterizerBresenham(panel.getRaster());
        renderer3D = new Renderer3D(liner, panel);
        renderer3D.setView(view.getViewMatrix()); // získáme pohledovou matici
        renderer3D.setProj(proj); // matice projekce

        cube = new Cube();
        axis = new Axis();
        pyramid = new Pyramid();
        sphere = new Sphere();
        bezier = new Bezier();
        coons = new Coons();
        ferguson = new Ferguson();
        viviani = new VivianiCurve();
        torus = new Torus();

        objects.add(cube);
        objects.add(pyramid);
        objects.add(sphere);
        objects.add(torus);
        objects.add(bezier);
        objects.add(coons);
        objects.add(ferguson);
        objects.add(viviani);

        selectedObject(cube); // krychle je výchozí vybraná

        panel.setObjects(objects); // předání objektů do panelu, aby se vykreslily svou barvou
    }

    @Override
    public void initListeners(Panel panel) {
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                RasterBI newRaster = new RasterBI(panel.getWidth(), panel.getHeight());
                newRaster.clear();
                panel.setRaster(newRaster);
                liner.setRaster(newRaster);

                proj = new Mat4PerspRH(Math.toRadians(90), (double) panel.getHeight() / panel.getWidth(), 0.1, 100);
                orto = new Mat4OrthoRH(panel.getWidth() / 100.0, panel.getHeight() / 100.0, 0.1, 100);

                renderer3D.setProj(proj);
                renderScene();
            }
        });

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

                    // rotace podle osy X
                    case KeyEvent.VK_X:
                        rotationMatrix = new Mat4Rot(Math.toRadians(angle), new Vec3D(1, 0, 0));
                        for (Mesh m : objects) {
                            if (m.isSelected()) {
                                m.setModelMat(m.getModelMat().mul(rotationMatrix)); // přenásobení maticí rotace
                            }
                        }
                        break;

                    // rotace podle osy Y
                    case KeyEvent.VK_Y:
                        rotationMatrix = new Mat4Rot(Math.toRadians(angle), new Vec3D(0, 1, 0));
                        for (Mesh m : objects) {
                            if (m.isSelected()) {
                                m.setModelMat(m.getModelMat().mul(rotationMatrix));
                            }
                        }
                        break;

                    // rotace podle osy Z
                    case KeyEvent.VK_Z:
                        rotationMatrix = new Mat4Rot(Math.toRadians(angle), new Vec3D(0, 0, 1));
                        for (Mesh m : objects) {
                            if (m.isSelected()) {
                                m.setModelMat(m.getModelMat().mul(rotationMatrix));
                            }
                        }
                        break;

                    // translace (posunutí) vlevo
                    case KeyEvent.VK_LEFT:
                        for (Mesh m : objects) {
                            if (m.isSelected()) {
                                m.setModelMat(m.getModelMat().mul(new Mat4Transl(-0.1, 0, 0))); // přenásobení maticí translace
                            }
                        }
                        break;

                    // translace (posunutí) vpravo
                    case KeyEvent.VK_RIGHT:
                        for (Mesh m : objects) {
                            if (m.isSelected()) {
                                m.setModelMat(m.getModelMat().mul(new Mat4Transl(0.1, 0, 0)));
                            }
                        }
                        break;

                    // nahoru
                    case KeyEvent.VK_UP:
                        for (Mesh m : objects) {
                            if (m.isSelected()) {
                                m.setModelMat(m.getModelMat().mul(new Mat4Transl(0, 0.1, 0)));
                            }
                        }
                        break;

                    // dolů
                    case KeyEvent.VK_DOWN:
                        for (Mesh m : objects) {
                            if (m.isSelected()) {
                                m.setModelMat(m.getModelMat().mul(new Mat4Transl(0, -0.1, 0)));
                            }
                        }
                        break;

                    // změna měřítka (scale)
                    case KeyEvent.VK_PLUS:
                    case KeyEvent.VK_ADD: // numerická klávesnice +
                        for (Mesh m : objects) {
                            if (m.isSelected()) {
                                scale(m, 1.1);
                            }
                        }
                        break;

                    case KeyEvent.VK_MINUS:
                    case KeyEvent.VK_SUBTRACT: // numerická klávesnice -
                        for (Mesh m : objects) {
                            if (m.isSelected()) {
                                scale(m, 0.9);
                            }
                        }
                        break;

                    // vrácení objektů zpátky
                    case KeyEvent.VK_O:
                        for (Mesh object : objects) {
                            object.setModelMat(new Mat4Identity());
                        }
                        break;

                    // pohyby pozorovatele WASD
                    case KeyEvent.VK_W:
                        view = view.forward(speed);
                        renderer3D.setView(view.getViewMatrix());
                        break;

                    case KeyEvent.VK_S:
                        view = view.backward(speed);
                        renderer3D.setView(view.getViewMatrix());
                        break;

                    case KeyEvent.VK_A:
                        view = view.left(speed);
                        renderer3D.setView(view.getViewMatrix());
                        break;

                    case KeyEvent.VK_D:
                        view = view.right(speed);
                        renderer3D.setView(view.getViewMatrix());
                        break;

                    // natočení kamery vlevo
                    case KeyEvent.VK_Q:
                        view = view.addAzimuth(-0.1);
                        renderer3D.setView(view.getViewMatrix());
                        break;

                    // natočení kamery vpravo
                    case KeyEvent.VK_E:
                        view = view.addAzimuth(0.1);
                        renderer3D.setView(view.getViewMatrix());
                        break;

                    // pohyb kamery nahoru
                    case KeyEvent.VK_SHIFT:
                        view = view.up(speed);
                        renderer3D.setView(view.getViewMatrix());
                        break;

                    // pohyb kamery dolů
                    case KeyEvent.VK_CONTROL:
                        view = view.down(speed);
                        renderer3D.setView(view.getViewMatrix());
                        break;

                    // perspektivní projekce (výchozí) = vzdálenější objekty se jeví menší
                    case KeyEvent.VK_P:
                        renderer3D.setProj(proj);
                        break;

                    // orto projekce
                    case KeyEvent.VK_H:
                        renderer3D.setProj(orto);
                        break;

                    // vybrání objektů
                    case KeyEvent.VK_K:
                        selectedObject(cube);
                        panel.setCurrentObject("krychle");
                        break;

                    case KeyEvent.VK_M:
                        selectedObject(pyramid);
                        panel.setCurrentObject("pyramida");
                        break;

                    case KeyEvent.VK_U:
                        selectedObject(sphere);
                        panel.setCurrentObject("koule");
                        break;

                    case KeyEvent.VK_B:
                        selectedObject(bezier);
                        panel.setCurrentObject("bezier");
                        break;

                    case KeyEvent.VK_N:
                        selectedObject(coons);
                        panel.setCurrentObject("coons");
                        break;

                    case KeyEvent.VK_F:
                        selectedObject(ferguson);
                        panel.setCurrentObject("ferguson");
                        break;

                    case KeyEvent.VK_T:
                        selectedObject(torus);
                        panel.setCurrentObject("torus");
                        break;

                    case KeyEvent.VK_V:
                        selectedObject(viviani);
                        panel.setCurrentObject("viviani");
                        break;

                    // animace v čase = rotace
                    case KeyEvent.VK_R:
                        if (!rotating) {
                            timer.start();
                            rotating = true;
                        } else {
                            timer.stop();
                            rotating = false;
                        }
                        break;

                    // výběr barvy aktuálně vybraného objektu
                    case KeyEvent.VK_L:
                        Mesh selected = getSelectedObject();
                        if (selected != null) {
                            Color chosen = JColorChooser.showDialog(panel, "Vyber barvu objektu", selected.getColor());

                            if (chosen != null) {
                                selected.setColor(chosen);
                                selected.setDefaultColor(chosen); // aktualizace defaultní barvy
                                renderScene();
                            }
                        }
                        break;
                }
                renderScene();
            }
        });
    }

    private Mesh getSelectedObject() {
        for (Mesh m : objects) {
            if (m.isSelected()) return m;
        }
        return null;
    }

    private void selectedObject(Mesh obj) {
        // zruší výběr předchozích objektů
        for (Mesh m : objects) {
            m.setSelected(false);
            m.setColor(m.getDefaultColor()); // vrátí výchozí barvu
        }

        // označí nový objekt
        obj.setSelected(true);
        obj.setColor(Color.YELLOW); // zvýrazní vybraný objekt žlutě
    }

    // factor = o kolik škáluju
    private void scale(Mesh m, double factor) {
        Vec3D c = m.getCenter();
        m.setModelMat(
                new Mat4Transl(c.opposite()) // opposite = záporné souřadnice
                        .mul(new Mat4Scale(factor))
                        .mul(new Mat4Transl(c))
                        .mul(m.getModelMat())
        );
    }

    // animace rotace v čase
    private void startTimer() {
        timer = new Timer(1000 / FPS, e -> {
            for (Mesh m : objects) {
                if (m.isSelected()) {
                    Vec3D c = m.getCenter();
                    m.setModelMat(
                            new Mat4Transl(c.opposite())
                                    .mul(new Mat4Rot(Math.toRadians(2), new Vec3D(0, 1, 0)))
                                    .mul(new Mat4Transl(c))
                                    .mul(m.getModelMat())
                    );
                }
            }
            renderScene();
        });
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
