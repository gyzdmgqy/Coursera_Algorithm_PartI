/* *****************************************************************************
 *  Name:gyzdmgqy
 *  Date:4/25/2020
 *  Description:2d-tree implementation. Write a mutable data type KdTree.java that uses a 2d-tree to implement the same API (but replace PointSET with KdTree). A 2d-tree is a generalization of a BST to two-dimensional keys. The idea is to build a BST with points in the nodes, using the x- and y-coordinates of the points as keys in strictly alternating sequence.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class KdTree {
    private Node root;
    private int count;

    public KdTree()                               // construct an empty set of points
    {
        root = null;
        count = 0;
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return root == null;
    }

    public int size()                         // number of points in the set
    {
        return count;
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException();
        boolean[] success = new boolean[1];
        success[0] = false;
        root = Node.insert(root, p, false, 0, 0, 1, 1, success);
        if (success[0]) count++;
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException();
        Node current = root;
        boolean isHorizontal = false;
        while (current != null) {
            if (current.p.equals(p)) return true;
            if (isHorizontal) {
                if (current.p.y() > p.y()) current = current.lb;
                else current = current.rt;
            }
            else {
                if (current.p.x() > p.x()) current = current.lb;
                else current = current.rt;
            }
            isHorizontal = !isHorizontal;
        }
        return false;
    }

    public void draw()                         // draw all points to standard draw
    {
        Node.draw(root, false);
    }

    public Iterable<Point2D> range(
            RectHV rect) {     // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException();
        LinkedList<Point2D> set = new LinkedList<>();
        Node.insertRange(set, root, rect);
        return set;
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Point2D nearestPoint = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node current = stack.pop();
            double distanceRECT = current.rect.distanceSquaredTo(p);
            if (distanceRECT >= nearestDistance) continue;
            double distance = current.p.distanceSquaredTo(p);
            //System.out.println(current.p.toString());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestPoint = current.p;
            }
            double lbDistance = Double.POSITIVE_INFINITY;
            double rtDistance = Double.POSITIVE_INFINITY;
            if (current.lb != null) lbDistance = current.lb.rect.distanceSquaredTo(p);
            if (current.rt != null) rtDistance = current.rt.rect.distanceSquaredTo(p);
            double smallerDistance;
            double largerDistance;
            Node smallerNode;
            Node largerNode;
            if (lbDistance < rtDistance) {
                smallerNode = current.lb;
                largerNode = current.rt;
                smallerDistance = lbDistance;
                largerDistance = rtDistance;
            }
            else {
                smallerNode = current.rt;
                largerNode = current.lb;
                smallerDistance = rtDistance;
                largerDistance = lbDistance;
            }
            if (smallerDistance >= nearestDistance) continue;
            if (largerDistance < nearestDistance) stack.push(largerNode);
            stack.push(smallerNode);
        }
        return nearestPoint;
    }


    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D point, RectHV rectangle) {
            p = point;
            rect = rectangle;
            lb = null;
            rt = null;
        }

        public static void draw(Node node, boolean isHorizontal) {
            if (node == null) return;
            node.p.draw();
            if (isHorizontal) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            }
            else {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            }
            draw(node.lb, !isHorizontal);
            draw(node.rt, !isHorizontal);
        }

        public static void insertRange(LinkedList<Point2D> set, Node node, RectHV rect) {
            if (node == null) return;
            if (!node.rect.intersects(rect)) return;
            if (rect.contains(node.p)) set.add(node.p);
            insertRange(set, node.lb, rect);
            insertRange(set, node.rt, rect);
        }

        public static Node insert(Node node, Point2D newPoint, boolean isHorizontal, double minX,
                                  double minY, double maxX, double maxY, boolean[] success) {
            if (node == null) {
                success[0] = true;
                return new Node(newPoint, new RectHV(minX, minY, maxX, maxY));
            }
            if (!node.rect.contains(newPoint)) throw new IllegalArgumentException();
            if (node.p.equals(newPoint)) {
                success[0] = false;
                return node;
            }
            if (isHorizontal) {
                if (newPoint.y() >= node.p.y()) {
                    node.rt = insert(node.rt, newPoint, false, node.rect.xmin(), node.p.y(),
                                     node.rect.xmax(), node.rect.ymax(), success);
                }
                else {
                    node.lb = insert(node.lb, newPoint, false, node.rect.xmin(), node.rect.ymin(),
                                     node.rect.xmax(), node.p.y(), success);
                }
            }
            else {
                if (newPoint.x() >= node.p.x()) {
                    node.rt = insert(node.rt, newPoint, true, node.p.x(), node.rect.ymin(),
                                     node.rect.xmax(), node.rect.ymax(), success);
                }
                else {
                    node.lb = insert(node.lb, newPoint, true, node.rect.xmin(), node.rect.ymin(),
                                     node.p.x(), node.rect.ymax(), success);
                }
            }
            return node;
        }
    }

    public static void main(
            String[] args)                  // unit testing of the methods (optional)
    {
        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            StdOut.println("size:" + kdtree.size());
        }
        StdOut.println(kdtree.contains(new Point2D(0.2, 0.3)));
        StdOut.println(kdtree.nearest(new Point2D(0.06, 0.96)));
    }
}
