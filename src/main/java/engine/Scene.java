package engine;

import fractal.Fractal;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private Camera camera = new Camera(new Vector2f());
    private Fractal f1 = new Fractal(
            new Line(new Vector2f(-100, 0), new Vector2f(0, 0), new Vector3f(1.0f, 1.0f, 1.0f)),
            new Line(new Vector2f(0, 0), new Vector2f(0, 100), new Vector3f(1.0f, 1.0f, 1.0f)));;
    private Fractal f2 = new Fractal(
            new Line(new Vector2f(-100, 0), new Vector2f(0, 0), new Vector3f(1.0f, 1.0f, 1.0f)),
            new Line(new Vector2f(0, 0), new Vector2f(0, 100), new Vector3f(1.0f, 1.0f, 1.0f)));;
    private Fractal f3 = new Fractal(
            new Line(new Vector2f(-100, 0), new Vector2f(0, 0), new Vector3f(1.0f, 1.0f, 1.0f)),
            new Line(new Vector2f(0, 0), new Vector2f(0, 100), new Vector3f(1.0f, 1.0f, 1.0f)));;
    private final List<Fractal> fractals = new ArrayList<>();

    public void start() {
        loadResources();

        fractals.add(f1);
        fractals.add(f2);
        fractals.add(f3);

        for (Fractal f : fractals) {
            f.setIterations(3);
            f.generateFractal();
        }

        this.camera = new Camera(new Vector2f());
    }

    public void loadResources() {
        // do nothing right now, but if there were any
        // textures to grab, we'd do it here.
    }

    public void update(float dt) {
        camera.update();
    }

    public void updateFractal(Fractal f, int iterations) {
        Renderer.clearLines();

        f.setIterations(iterations);

        // TODO: this is a dum dum move
        for (Fractal fractal : fractals) {
            fractal.generateFractal();
        }
    }

    public List<Fractal> getFractals() {
        return this.fractals;
    }

    public Camera getCamera() {
        return camera;
    }
}
