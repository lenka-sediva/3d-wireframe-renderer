package controller;

import model.objectdata.mesh.cubics.*;
import model.objectdata.mesh.*;
import model.objectdata.mesh.parametric.*;
import model.rasterdata.RasterBI;
import model.rasterops.rasterizer.*;
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
    private Mesh cube, pyramid, sphere, cylinder, torus, bezier, coons, ferguson, viviani;
    private final List<Mesh> objects = new ArrayList<>();

    private Camera view; // pozorovatel
    private Mat4 persp, ortho;

    private final double angle = 90.d; // úhel
    private final double speed = 0.5; // rychlost pozorovatele

    private int lastMouseX, lastMouseY; // myš
    private boolean dragging = false;

    private Timer timer; // pro animaci
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

        persp = new Mat4PerspRH(Math.toRadians(90), (double) panel.getHeight() / panel.getWidth(), 0.1, 100); // perspektivní matice projekce
        ortho = new Mat4OrthoRH(panel.getWidth() / 100., panel.getHeight() / 100., 0.1, 100); // ortogonální matice projekce

        liner = new LineRasterizerBresenham(panel.getRaster());
        renderer3D = new Renderer3D(liner, panel);
        renderer3D.setView(view.getViewMatrix()); // pohledová matice
        renderer3D.setProj(persp); // matice projekce

        cube = new Cube();
        axis = new Axis();
        pyramid = new Pyramid();
        sphere = new Sphere();
        cylinder = new Cylinder();
        bezier = new Bezier();
        coons = new Coons();
        ferguson = new Ferguson();
        viviani = new VivianiCurve();
        torus = new Torus();

        objects.add(cube);
        objects.add(pyramid);
        objects.add(sphere);
        objects.add(torus);
        objects.add(cylinder);
        objects.add(bezier);
        objects.add(coons);
        objects.add(ferguson);
        objects.add(viviani);

        setSelectedObject(cube); // krychle je výchozí vybraná
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

                persp = new Mat4PerspRH(Math.toRadians(90), (double) panel.getHeight() / panel.getWidth(), 0.1, 100);
                ortho = new Mat4OrthoRH(panel.getWidth() / 100.0, panel.getHeight() / 100.0, 0.1, 100);

                renderer3D.setProj(persp);
                renderScene();
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
                dragging = false; // začíná nový stisk myši
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!dragging) {
                    // vybere objekt, pokud během držení myši nedošlo k dragování
                    Mesh closest = findClosestMesh(lastMouseX, lastMouseY);
                    setSelectedObject(closest); // označí objekt nejblíž kurzoru myši za vybraný
                    renderScene();
                }
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                dragging = true; // uživatel táhne myší

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
                    // vymazání plátna kromě os XYZ
                    case KeyEvent.VK_C:
                        hardClear();
                        break;

                    // rotace podle osy X
                    case KeyEvent.VK_X:
                        transformSelected(new Mat4Rot(Math.toRadians(angle), new Vec3D(1, 0, 0))); // přenásobení maticí rotace
                        break;

                    // rotace podle osy Y
                    case KeyEvent.VK_Y:
                        transformSelected(new Mat4Rot(Math.toRadians(angle), new Vec3D(0, 1, 0)));
                        break;

                    // rotace podle osy Z
                    case KeyEvent.VK_Z:
                        transformSelected(new Mat4Rot(Math.toRadians(angle), new Vec3D(0, 0, 1)));
                        break;

                    // translace (posunutí) vlevo
                    case KeyEvent.VK_LEFT:
                        moveSelected(-0.1, 0);
                        break;

                    // translace (posunutí) vpravo
                    case KeyEvent.VK_RIGHT:
                        moveSelected(0.1, 0);
                        break;

                    // nahoru
                    case KeyEvent.VK_UP:
                        moveSelected(0, 0.1);
                        break;

                    // dolů
                    case KeyEvent.VK_DOWN:
                        moveSelected(0, -0.1);
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

                    // vrácení objektů zpátky do původního stavu
                    case KeyEvent.VK_O:
                        for (Mesh m : objects) {
                            m.setModelMat(new Mat4Identity());
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
                        renderer3D.setProj(persp);
                        break;

                    // ortogonální projekce (pravoúhlá) = vzdálenost od kamery nemění velikost objektu
                    case KeyEvent.VK_H:
                        renderer3D.setProj(ortho);
                        break;

                    // vybrání objektů
                    case KeyEvent.VK_K:
                        select(cube, "krychle");
                        break;

                    case KeyEvent.VK_M:
                        select(pyramid, "pyramida");
                        break;

                    case KeyEvent.VK_U:
                        select(sphere, "koule");
                        break;

                    case KeyEvent.VK_B:
                        select(bezier, "bezier");
                        break;

                    case KeyEvent.VK_N:
                        select(coons, "coons");
                        break;

                    case KeyEvent.VK_F:
                        select(ferguson, "ferguson");
                        break;

                    case KeyEvent.VK_T:
                        select(torus, "torus");
                        break;

                    case KeyEvent.VK_I:
                        select(viviani, "viviani");
                        break;

                    case KeyEvent.VK_V:
                        select(cylinder, "válec");
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

                    // změna pohledu kamery = 1. a 3. osoba
                    case KeyEvent.VK_SPACE:
                        view = view.withFirstPerson(!view.getFirstPerson()); // přepíná mezi režimy
                        panel.setCurrentCameraMode(view.getFirstPerson()); // aktualizace panelu
                        renderer3D.setView(view.getViewMatrix());
                        renderScene();
                        break;
                }
                renderScene();
            }
        });

        // listener pro slider přesnosti křivek
        panel.getPrecisionSlider().addChangeListener(_ -> {
            int precision = panel.getPrecision();
            for (Mesh m : objects) {
                if (m instanceof Curve curve) {
                    Mat4 currentMat = curve.getModelMat(); // uloží aktuální transformaci
                    curve.generate(precision); // znovu vytvoří body podle slideru
                    curve.setModelMat(currentMat); // obnoví transformaci
                }
            }
            renderScene();
            panel.requestFocusInWindow(); // vrátí focus panelu, aby KeyListener opět fungoval
        });
    }

    private Mesh getSelectedObject() {
        for (Mesh m : objects) {
            if (m.isSelected()) return m;
        }
        return null;
    }

    private void setSelectedObject(Mesh obj) {
        // zruší výběr předchozích objektů
        for (Mesh m : objects) {
            m.setSelected(false);
            m.setColor(m.getDefaultColor()); // vrátí výchozí barvu
        }

        // označí nový objekt
        obj.setSelected(true);
        obj.setColor(Color.YELLOW); // zvýrazní vybraný objekt žlutě
    }

    // pomocná metoda, aplikuje transformaci na vybraný objekt
    private void transformSelected(Mat4 transform) {
        Mesh sel = getSelectedObject();
        if (sel != null) {
            sel.setModelMat(sel.getModelMat().mul(transform));
        }
    }

    // posune vybraný objekt o zadané souřadnice
    private void moveSelected(double x, double y) {
        transformSelected(new Mat4Transl(x, y, 0));
    }

    // vybere objekt a předá jeho název do panelu
    private void select(Mesh obj, String name) {
        setSelectedObject(obj);
        panel.setCurrentObject(name);
    }

    // změna měřítka
    private void scale(Mesh m, double factor) { // factor = o kolik škáluju
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
        timer = new Timer(1000 / FPS, _ -> {
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

    // vrací objekt, jehož vrchol je nejblíže zadané pozici (kurzoru myši)
    private Mesh findClosestMesh(int mouseX, int mouseY) {
        Mesh closest = null;
        double minDist = Double.MAX_VALUE;

        for (Mesh m : objects) {
            for (Point3D p : m.getVB()) {
                Point3D pTransformed = p.mul(m.getModelMat()).mul(view.getViewMatrix()).mul(persp);

                // dehomogenizace
                double x = pTransformed.getX() / pTransformed.getW();
                double y = pTransformed.getY() / pTransformed.getW();

                // transformace do okna
                double screenX = (x + 1) * (panel.getWidth() - 1) / 2.0;
                double screenY = panel.getHeight() - (y + 1) * (panel.getHeight() - 1) / 2.0;

                double dist = Math.hypot(mouseX - screenX, mouseY - screenY); // počítá euklidovskou vzdálenost
                if (dist < minDist) {
                    minDist = dist;
                    closest = m;
                }
            }
        }
        return closest;
    }

    // posílá do seznamu objekty a vyrenderuje je všechny najednou + šipky
    private void renderScene() {
        panel.clear();

        renderer3D.render(axis.xAxis);
        renderer3D.render(axis.yAxis);
        renderer3D.render(axis.zAxis);

        for(Mesh m : objects) {
            renderer3D.render(m);
        }

        panel.repaint();
    }

    private void hardClear() {
        panel.clear();

        for(Mesh m : objects) {
            m.setModelMat(new Mat4());
        }

        renderScene();
    }
}
