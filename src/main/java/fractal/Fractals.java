package fractal;

import engine.Line;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Fractals {
    public static final Fractal DEFAULT = new Fractal(
            new Line(new Vector2f(-100, 0), new Vector2f(0, 0), new Vector3f(1.0f, 1.0f, 1.0f)),
            new Line(new Vector2f(0, 0), new Vector2f(0, 100), new Vector3f(1.0f, 1.0f, 1.0f)));

    public static final class HilbertCurve extends Fractal {
        //FIXME
    }
}
