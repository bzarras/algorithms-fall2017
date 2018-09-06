/**
 * Ben Zarras
 * 10/21/2017
 *
 * BruteCollinearPoints is a class that implements a brute force algorithm
 * for finding all the collinear points in a collection of points.
 */

import java.util.Arrays;
import edu.princeton.cs.algs4.Queue;

public class BruteCollinearPoints {

    private final int numberOfSegments;
    private final LineSegment[] segments;
    private final Point[] points;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points != null");
        }
        this.points = Arrays.copyOf(points, points.length);
        Arrays.sort(this.points);
        int n = this.points.length;
        Queue<LineSegment> segmentQueue = new Queue<LineSegment>();
        for (int i = 0; i < n; i++) {
            if (this.points[i] == null) throw new IllegalArgumentException();
            // check duplicates
            if (i > 0) {
                Point p0 = this.points[i - 1];
                Point p1 = this.points[i];
                if (p0.compareTo(p1) == 0) {
                    throw new IllegalArgumentException("dupe point");
                }
            }
            for (int j = i + 1; j < n; j++) {
                if (this.points[j] == null) throw new IllegalArgumentException();
                for (int k = j + 1; k < n; k++) {
                    if (this.points[k] == null) throw new IllegalArgumentException();
                    for (int x = k + 1; x < n; x++) {
                        if (this.points[x] == null) throw new IllegalArgumentException();
                        Point[] pointArray = {this.points[i], this.points[j], this.points[k], this.points[x]};
                        LineSegment segment = collinear(pointArray);
                        if (segment != null) {
                            segmentQueue.enqueue(segment);
                        }
                    }
                }
            }
        }
        this.numberOfSegments = segmentQueue.size();
        this.segments = new LineSegment[segmentQueue.size()];
        for (int i = 0; i < this.segments.length; i++) {
            this.segments[i] = segmentQueue.dequeue();
        }
    }

    public int numberOfSegments() {
        return this.numberOfSegments;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(this.segments, this.numberOfSegments);
    }

    private LineSegment collinear(Point[] points) {
        Point smallest = points[0];
        double slope1 = smallest.slopeTo(points[1]);
        double slope2 = smallest.slopeTo(points[2]);
        double slope3 = smallest.slopeTo(points[3]);
        if (slope1 == slope2 && slope1 == slope3) {
            return new LineSegment(smallest, points[3]);
        }
        return null;
    }
}
