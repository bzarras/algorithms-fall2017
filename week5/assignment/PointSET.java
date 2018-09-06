/**
 * Ben Zarras
 * 11/1/2017
 *
 * PointSET.java
 */

import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

public class PointSET {
    private TreeSet<Point2D> setOfPoints;

    public PointSET() {
        this.setOfPoints = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return this.setOfPoints.isEmpty();
    }

    public int size() {
        return this.setOfPoints.size();
    }

    public void insert(Point2D p) {
        this.checkForNull(p);
        this.setOfPoints.add(p);
    }

    public boolean contains(Point2D p) {
        this.checkForNull(p);
        return setOfPoints.contains(p);
    }

    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : this.setOfPoints) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        this.checkForNull(rect);
        Queue<Point2D> q = new Queue<Point2D>();
        for (Point2D p : this.setOfPoints) {
            if (rect.contains(p)) {
                q.enqueue(p);
            }
        }
        return q;
    }

    public Point2D nearest(Point2D p) {
        this.checkForNull(p);
        Point2D nearest = this.setOfPoints.first();
        for (Point2D otherP : this.setOfPoints) {
            if (p.distanceSquaredTo(otherP) < p.distanceSquaredTo(nearest)) {
                nearest = otherP;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        PointSET ps = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            ps.insert(new Point2D(x, y));
        }
        ps.draw();
    }

    private void checkForNull(Object o) {
        if (o == null) throw new IllegalArgumentException();
    }
}
