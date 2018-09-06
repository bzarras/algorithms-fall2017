/**
 * Ben Zarras
 * 10/22/2017
 *
 * FastCollinearPoints is an implementation of a faster algorithm for
 * finding collinear points amongst a collection of points.
 */

import java.util.Arrays;
import edu.princeton.cs.algs4.Queue;

public class FastCollinearPoints {

    private final int numberOfSegments;
    private final LineSegment[] segments;
    private final Point[] points;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points != null");
        }
        this.points = Arrays.copyOf(points, points.length);
        Arrays.sort(this.points);
        Queue<LineSegment> segmentQueue = new Queue<LineSegment>();
        for (int i = 0; i < this.points.length; i++) {
            if (this.points[i] == null) {
                throw new IllegalArgumentException();
            }
            // Intentionally grabbing from original points array, because
            // this.points gets resorted every time
            Point origin = points[i];
            // sort slopes relative to current point
            Arrays.sort(this.points, origin.slopeOrder());
            // walk through points and find blocks of contiguous slopes
            double currentSlope = origin.slopeTo(this.points[0]);
            int startOfContiguousBlock = 0;
            int collinearCount = 2;
            for (int j = 1; j < this.points.length; j++) {
                if (this.points[j] == null) throw new IllegalArgumentException();
                if (origin.compareTo(this.points[j]) == 0) {
                    throw new IllegalArgumentException("duplicate points");
                }
                if (origin.slopeTo(this.points[j]) == currentSlope) {
                    collinearCount++;
                } else {
                    if (collinearCount > 3) {
                        LineSegment segment = this.createSegmentIfNeeded(this.points, origin, startOfContiguousBlock, j - 1);
                        if (segment != null) {
                            segmentQueue.enqueue(segment);
                        }
                    }
                    currentSlope = origin.slopeTo(this.points[j]);
                    startOfContiguousBlock = j;
                    collinearCount = 2;
                    continue;
                }
                if (j == this.points.length - 1) {
                    if (collinearCount > 3) {
                        LineSegment segment = this.createSegmentIfNeeded(this.points, origin, startOfContiguousBlock, j);
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

    private LineSegment createSegmentIfNeeded(Point[] points, Point origin, int start, int end) {
        Arrays.sort(points, start, end + 1);
        // only create segment if origin is at the beginning of
        // the segment
        if (origin.compareTo(points[start]) < 0) {
            return new LineSegment(origin, points[end]);
        }
        return null;
    }
}
