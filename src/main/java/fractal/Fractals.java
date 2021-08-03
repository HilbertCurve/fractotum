package fractal;

import engine.Line;
import org.joml.Vector2f;

public class Fractals {
    public static final Fractal DEFAULT = new Fractal(
            new Line(new Vector2f(-100, 0), new Vector2f(0, 0)),
            new Line(new Vector2f(0, 0), new Vector2f(0, 100)));

    public static final class HilbertCurve extends Fractal {
        //FIXME
    }
}
