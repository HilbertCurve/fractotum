package fractal;

import engine.Line;
import engine.Renderer;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.MyMath;

import java.util.ArrayList;
import java.util.List;

import static util.MyMath.RADIANS_TO_DEGREES;

public class Fractal {
    protected List<Line> base;
    protected List<Line> lines;
    protected Vector3f color;
    protected Fractal original;

    // imgui stuff (that's why they're arrays)
    protected boolean dirtyFlag;
    protected int[][] pointData;
    protected int[][] baseData;
    protected int[] numPoints;
    protected int[] iterations;

    public Fractal() {
        this.base = new ArrayList<>();
        this.lines = new ArrayList<>();

        this.init();
    }

    public Fractal(Line... lines) {
        this.base = new ArrayList<>(List.of(lines));
        this.lines = new ArrayList<>(List.of(lines));

        this.init();
    }

    public Fractal(List<Line> lines) {
        this.base = lines;
        this.lines = lines;

        this.init();
    }

    private void init() {
        this.color = new Vector3f();
        Fractals.DEFAULT_COLOR.get(this.color);

        this.dirtyFlag = true;
        this.numPoints = new int[]{lines.size() + 1};
        this.iterations = new int[]{3};

        this.baseData = new int[this.base.size() + 1][2];

        for (int i = 0; i < numPoints[0] - 1; i++) {
            baseData[i][0] = (int) base.get(i).getStart().x;
            baseData[i][1] = (int) base.get(i).getStart().y;
            baseData[i + 1][0] = (int) base.get(i).getEnd().x;
            baseData[i + 1][1] = (int) base.get(i).getEnd().y;
        }
    }

    public Fractal getBase() {
        return new Fractal(this.base);
    }

    public List<Line> getLines() {
        return lines;
    }

    public Vector3f getColor() {
        return this.color;
    }

    public int[] getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = new int[]{iterations};
    }

    public int[][] getBaseData() {
        if (dirtyFlag) {
            int[][] oldData = baseData.clone();

            baseData = new int[numPoints[0]][2];

            System.arraycopy(oldData, 0, baseData, 0, Math.min(oldData.length, baseData.length));

            base.clear();

            for (int i = 0; i < numPoints[0] - 1; i++) {
                base.add(new Line(
                        new Vector2f(baseData[i][0], baseData[i][1]),
                        new Vector2f(baseData[i+1][0], baseData[i+1][1])
                ));
            }
        }

        return baseData;
    }

    public int[][] getPointData() {
        if (dirtyFlag) {
            pointData = new int[lines.size() + 1][2];

            for (int i = 0; i < lines.size(); i++) {
                pointData[i][0] = (int) lines.get(i).getStart().x;
                pointData[i][1] = (int) lines.get(i).getStart().y;
                pointData[i + 1][0] = (int) lines.get(i).getEnd().x;
                pointData[i + 1][1] = (int) lines.get(i).getEnd().y;
            }
        }

        return pointData;
    }

    public int[] getNumPoints() {
        return numPoints;
    }

    public Fractal copy() {
        Fractal f = new Fractal();
        f.base = new ArrayList<>();
        f.lines = new ArrayList<>();
        for (Line l : base) {
            f.base.add(new Line(new Vector2f(l.getStart()), new Vector2f(l.getEnd())));
        }
        for (Line l : lines) {
            f.lines.add(new Line(new Vector2f(l.getStart()), new Vector2f(l.getEnd())));
        }

        f.color = this.color;

        return f;
    }

    public void translateBy(Vector2f dPos) {
        for (Line l : lines) {
            l.getStart().add(dPos);
            l.getEnd().add(dPos);
        }
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

    public void scale(Vector2f pos, float f) {
        for (Line l : lines) {
            l.scale(pos, f);
        }
    }

    public void rotate(Vector2f v, float theta) {
        for (Line l : lines) {
            MyMath.rotate(v, l.getStart(), theta);
            MyMath.rotate(v, l.getEnd(), theta);
        }
    }

    public void reset() {
        this.lines.clear();

        getBaseData();

        if (numPoints[0] - 1 > this.base.size()) {
            this.base.add(new Line(new Vector2f(0, 0), new Vector2f(0, 0)));
        }

        this.lines.addAll(this.base);

        this.original = this.copy();
    }

    public void generateFractal() {
        this.dirtyFlag = true;

        this.reset();

        for (int i = 0; i < iterations[0]; i++)
            this.iterate();

        for (Line l : this.lines)
            Renderer.addLine(l.getStart(), l.getEnd());

        this.dirtyFlag = false;
    }

    protected void iterate() {
        List<Fractal> toBeAdded = new ArrayList<>();
        final int l = this.lines.size();

        // Generate a bunch of smaller, translated, scaled, and rotated copies of this pseudo-fractal's base.
        for (int i = 0; i < l; i++) {
            Fractal f = original.copy();
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

            float theta2 = (float) Math.acos(new Vector2f(fEnd)
                    .sub(fStart)
                    .normalize()
                    .dot(new Vector2f(1.0f, 0.0f)));

            if (fEnd.y > fStart.y) {
                theta2 *= -1;
            }

            theta -= theta2;

            f.rotate(lStart, theta*RADIANS_TO_DEGREES);

            toBeAdded.add(f);
        }

        // Add all lines of all smaller copies generated.
        this.lines.clear();

        for (Fractal fractal : toBeAdded) {
            for (Line line : fractal.lines) {
                line.getColor().set(this.color);
            }
            this.lines.addAll(fractal.lines);
        }
    }
}
