package fractal;

import engine.Line;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static fractal.Fractals.DEFAULT_COLOR;

public class HilbertCurve extends Fractal {
    public HilbertCurve() {
        this.lines = new ArrayList<>();
        this.lines.add(new Line(
                new Vector2f(-50, -50),
                new Vector2f(-50, 50))
        );

        this.lines.add(new Line(
                new Vector2f(-50, 50),
                new Vector2f(50, 50))
        );

        this.lines.add(new Line(
                new Vector2f(50, 50),
                new Vector2f(50, -50))
        );

        this.base = new ArrayList<>();
        this.base.addAll(lines);

        this.color = DEFAULT_COLOR;
    }

    @Override
    public HilbertCurve copy() {
        HilbertCurve h = new HilbertCurve();
        h.base = new ArrayList<>();
        h.lines = new ArrayList<>();
        for (Line l : base) {
            h.base.add(new Line(new Vector2f(l.getStart()), new Vector2f(l.getEnd())));
        }
        for (Line l : lines) {
            h.lines.add(new Line(new Vector2f(l.getStart()), new Vector2f(l.getEnd())));
        }

        h.color = this.color;

        return h;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void iterate() {
        List<HilbertCurve> toBeAdded = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            /* Where to move successive pseudo-hilbert curves:
             *
             *     1       2
             *
             *
             *     0       3
             *
             */

            HilbertCurve h = this.copy();
            h.scale(new Vector2f(0, 0), 1/2f);
            switch (i) {
                case 0: {
                    h.translateBy(new Vector2f(-50, -50));

                    // Reflect over line y = x
                    for (Line l : h.getLines()) {
                        float xOld = l.getEnd().x;
                        l.getEnd().x = l.getEnd().y;
                        l.getEnd().y = xOld;

                        xOld = l.getStart().x;
                        l.getStart().x = l.getStart().y;
                        l.getStart().y = xOld;
                    }

                    break;
                } case 1: {
                    h.translateBy(new Vector2f(-50, 50));
                    break;
                } case 2: {
                    h.translateBy(new Vector2f(50, 50));

                    break;
                } case 3: {
                    h.translateBy(new Vector2f(50, -50));

                    // Reflect over line y = -x
                    for (Line l : h.getLines()) {
                        float xOld = -l.getEnd().x;
                        l.getEnd().x = -l.getEnd().y;
                        l.getEnd().y = xOld;

                        xOld = -l.getStart().x;
                        l.getStart().x = -l.getStart().y;
                        l.getStart().y = xOld;
                        System.out.println(xOld);
                    }
                    break;
                }
            }
            toBeAdded.add(h);
        }

        this.lines.clear();

        for (int i = 0; i < toBeAdded.size(); i++) {
            Fractal fractal = toBeAdded.get(i);
            for (Line line : fractal.lines) {
                line.getColor().set(this.color);
            }
            this.lines.addAll(fractal.lines);

            if (i != toBeAdded.size() - 1) {
                this.lines.add(new Line(
                        fractal.lines.get(fractal.lines.size() - 1).getEnd(),
                        toBeAdded.get(i + 1).lines.get(0).getStart()
                ));
            }
        }
    }
}
