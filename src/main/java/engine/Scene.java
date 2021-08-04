package engine;

import fractal.Fractal;
import fractal.Fractals;
import fractal.HilbertCurve;
import org.joml.Vector2f;

import java.util.List;

public class Scene {
    private Camera camera = new Camera(new Vector2f());
    private Fractal f = Fractals.DEFAULT;
    public List<Line> lines = f.getLines();
    public int[] iterations = {3};
    public int[] numPoints = {3};
    public int[][] pointData = new int[numPoints[0]][2];

    public void start() {
        loadResources();

        for (int i = 0; i < lines.size(); i++) {
            pointData[i][0] = (int) lines.get(i).getStart().x;
            pointData[i][1] = (int) lines.get(i).getStart().y;
            pointData[i+1][0] = (int) lines.get(i).getEnd().x;
            pointData[i+1][1] = (int) lines.get(i).getEnd().y;
        }

        generateFractal(3);

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

    public void generateFractal(int iterations) {
        Renderer.clearLines();

        /*lines.clear();

        Renderer.clearLines();

        int[][] oldPointData = pointData;

        pointData = new int[numPoints[0]][2];

        System.arraycopy(
                oldPointData, 0,
                pointData, 0,
                Math.min(pointData.length, oldPointData.length)
        );

        for (int i = 0; i < numPoints[0] - 1; i++) {
            lines.add(new Line(
                    new Vector2f(pointData[i][0], pointData[i][1]),
                    new Vector2f(pointData[i+1][0], pointData[i+1][1])
            ));
        }

        f = new Fractal(lines);
        f.getColor().set(Fractals.DEFAULT_COLOR);*/

        HilbertCurve h = new HilbertCurve();

        for (int i = 0; i < iterations; i++) {
            h.iterate();
        }

        for (Line l : h.getLines())
            Renderer.addLine(l.getStart(), l.getEnd());
    }

    public Camera getCamera() {
        return camera;
    }
}
