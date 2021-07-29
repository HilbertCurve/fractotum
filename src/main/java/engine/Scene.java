package engine;

import fractal.Fractal;
import org.joml.Vector2f;

public class Scene {
    private Camera camera = new Camera(new Vector2f());

    public void start() {
        loadResources();

        Fractal f = new Fractal(
                new Line(new Vector2f(-100, 0), new Vector2f(0, 50)),
                new Line(new Vector2f(0, 50), new Vector2f(100, 0))
        );

        f.iterate();
        f.iterate();
        f.iterate();
        f.iterate();

        for (Line l : f.getLines())
            Renderer.addLine(l.getStart(), l.getEnd());

        this.camera = new Camera(new Vector2f());

        System.out.println("Scene started");
    }

    public void loadResources() {
        // do nothing right now, but if there were any
        // textures to grab, we'd do it here.
    }

    public void update(float dt) {


        camera.update();
    }

    public Camera getCamera() {
        return camera;
    }
}
