package fractal;

import engine.Line;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.MyMath;

import java.util.ArrayList;
import java.util.List;

import static util.MyMath.RADIANS_TO_DEGREES;

public class Fractal {
    public static final Vector3f DEFAULT_COLOR = new Vector3f(1.0f, 0.8f, 0.5f);
    final List<Line> base;
    List<Line> lines;
    Vector3f color;

    public Fractal(Line... lines) {
        this.base = new ArrayList<>(List.of(lines));
        this.lines = new ArrayList<>(List.of(lines));
        this.color = new Vector3f((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

    public List<Line> getLines() {
        return lines;
    }

    public Fractal copy() {
        Fractal f = new Fractal(this.base.toArray(new Line[0]));
        f.lines.clear();
        for (Line l : lines) {
            f.lines.add(new Line(new Vector2f(l.getStart()), new Vector2f(l.getEnd())));
        }

        return f;
    }

    public Fractal getBase() {
        return new Fractal(this.base.toArray(new Line[0]));
    }

    public void translateTo(Vector2f pos) {
        Vector2f d = new Vector2f(pos).sub(this.lines.get(0).getStart());

        for (Line l : lines) {
            l.getStart().add(d);
            l.getEnd().add(d);
        }
    }

    public void scale(float f) {
        Vector2f fractalStart = this.lines.get(0).getStart();
        for (Line l : lines) {
            l.scale(fractalStart, f);
        }
    }

    public void rotate(Vector2f v, float theta) {
        for (Line l : lines) {
            MyMath.rotate(v, l.getStart(), theta);
            MyMath.rotate(v, l.getEnd(), theta);
        }
    }

    public void iterate() {
        List<Fractal> toBeAdded = new ArrayList<>();
        final int l = this.lines.size();

        // Generate a bunch of smaller, translated, scaled, and rotated copies of this pseudo-fractal.
        for (int i = 0; i < l; i++) {
            Fractal f = this.copy();
            Vector2f lStart = this.lines.get(i).getStart();
            Vector2f lEnd = this.lines.get(i).getEnd();

            // Translate f to line.
            f.translateTo(new Vector2f(lStart));

            Vector2f fStart = f.base.get(0).getStart();
            Vector2f fEnd = f.base.get(f.base.size()-1).getEnd();

            // Scale f to fit line.
            float factor = this.lines.get(i).lengthSquared()/new Line(fStart, fEnd).lengthSquared();
            factor = (float) Math.sqrt(factor);
            f.scale(factor);

            // Rotate f.
            float num = new Vector2f(lEnd)
                    .sub(lStart)
                    .normalize()
                    .dot(new Vector2f(1.0f, 0.0f));


            float theta = (float) Math.acos(num);
            if (lEnd.y > lStart.y) {
                theta *= -1;
            }

            f.rotate(lStart, theta*RADIANS_TO_DEGREES);

            toBeAdded.add(f);
        }

        // Add all lines of all smaller copies generated.
        this.lines.clear();

        for (Fractal fractal : toBeAdded) {
            this.lines.addAll(fractal.lines);
        }

        System.out.println(this.lines.size());
    }
}
