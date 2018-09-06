/**
 * Ben Zarras
 * 11/1/2017
 *
 * KdTree.java
 */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
    }

    private Node root;
    private int n;

    public KdTree() {
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public int size() {
        return this.n;
    }

    public void insert(Point2D p) {
        this.checkForNull(p);
        RectHV rect = new RectHV(0, 0, 1, 1);
        this.root = this.insert(this.root, p, rect, 0);
    }

    private Node insert(Node node, Point2D point, RectHV rect, int depth) {
        this.checkForNull(point);
        if (node == null) {
            Node newNode = new Node();
            newNode.p = point;
            newNode.rect = rect;
            this.n++;
            return newNode;
        }
        if (depth % 2 == 0) { // left/right
            if (point.x() < node.p.x()) node.lb = this.insert(node.lb, point, new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax()), depth + 1);
            else if (point.x() >= node.p.x() && point.compareTo(node.p) != 0) node.rt = this.insert(node.rt, point, new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax()), depth + 1);
            else node.p = point;
        } else { // up/down
            if (point.y() < node.p.y()) node.lb = this.insert(node.lb, point, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y()), depth + 1);
            else if (point.y() >= node.p.y() && point.compareTo(node.p) != 0) node.rt = this.insert(node.rt, point, new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax()), depth + 1);
            else node.p = point;
        }
        return node;
    }

    public boolean contains(Point2D p) {
        this.checkForNull(p);
        return this.get(this.root, p, 0) != null;
    }

    private Point2D get(Node node, Point2D point, int depth) {
        this.checkForNull(point);
        if (node == null) return null;
        if (depth % 2 == 0) {
            if (point.x() < node.p.x()) return this.get(node.lb, point, depth + 1);
            else if (point.x() >= node.p.x() && !point.equals(node.p)) return this.get(node.rt, point, depth + 1);
        } else {
            if (point.y() < node.p.y()) return this.get(node.lb, point, depth + 1);
            else if (point.y() >= node.p.y() && !point.equals(node.p)) return this.get(node.rt, point, depth + 1);
        }
        return node.p;
    }

    public void draw() {
        this.draw(this.root, 0);
    }

    private void draw(Node node, int depth) {
        if (node == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();
        StdDraw.setPenRadius();
        if (depth % 2 == 0) { // vertical line
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        } else { // horizontal line
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
        this.draw(node.lb, depth + 1);
        this.draw(node.rt, depth + 1);
    }

    public Iterable<Point2D> range(RectHV rect) {
        this.checkForNull(rect);
        Queue<Point2D> q = new Queue<Point2D>();
        this.addPointToRangeQueue(this.root, rect, q);
        return q;
    }

    private void addPointToRangeQueue(Node node, RectHV queryRect, Queue<Point2D> q) {
        if (node == null) return;
        if (!queryRect.intersects(node.rect)) return;
        if (queryRect.contains(node.p)) q.enqueue(node.p);
        if (node.lb != null) this.addPointToRangeQueue(node.lb, queryRect, q);
        if (node.rt != null) this.addPointToRangeQueue(node.rt, queryRect, q);
    }

    public Point2D nearest(Point2D p) {
        this.checkForNull(p);
        if (this.root == null) return null;
        Point2D nearest = this.findNearest(this.root, this.root.p, p, 0);
        return nearest;
    }

    private Point2D findNearest(Node node, Point2D currentNearest, Point2D queryPoint, int depth) {
        if (node == null || node.rect.distanceSquaredTo(queryPoint) > currentNearest.distanceSquaredTo(queryPoint)) {
            return currentNearest;
        }
        if (node.p.distanceSquaredTo(queryPoint) < currentNearest.distanceSquaredTo(queryPoint)) {
            currentNearest = node.p;
        }
        RectHV lb;
        RectHV rt;
        if (depth % 2 == 0) {
            lb = new RectHV(node.rect.xmin(), node.rect.ymin(), node.p.x(), node.rect.ymax()); // left
            rt = new RectHV(node.p.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax()); // right
        } else {
            lb = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.p.y()); // bottom
            rt = new RectHV(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.rect.ymax()); // top
        }
        double distToLb = lb.distanceSquaredTo(queryPoint);
        double distToRt = rt.distanceSquaredTo(queryPoint);
        if (distToLb <= distToRt) {
            currentNearest = this.findNearest(node.lb, currentNearest, queryPoint, depth + 1);
            currentNearest = this.findNearest(node.rt, currentNearest, queryPoint, depth + 1);
        } else {
            currentNearest = this.findNearest(node.rt, currentNearest, queryPoint, depth + 1);
            currentNearest = this.findNearest(node.lb, currentNearest, queryPoint, depth + 1);
        }
        return currentNearest;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        KdTree tree = new KdTree();
        Queue<Point2D> pointQueue = new Queue<Point2D>();
        int count = 0;
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D point = new Point2D(x, y);
            pointQueue.enqueue(point);
            tree.insert(point);
            count++;
        }
        System.out.println("tree size " + tree.size());
        for (Point2D point : pointQueue) {
            System.out.println("checking for point " + point);
            if (!tree.contains(point)) {
                System.out.println("uh oh! didn't find that point");
            }
        }
        tree.draw();
    }

    private void checkForNull(Object o) {
        if (o == null) throw new IllegalArgumentException();
    }
}
