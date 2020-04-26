/* *****************************************************************************
 *  Name:gyzdmgqy
 *  Date:4/25/2020
 *  Description:Brute-force implementation. Write a mutable data type PointSET.java that represents a set of points in the unit square. Implement the following API by using a red–black BST:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> bst;

    public PointSET()                               // construct an empty set of points
    {
        bst = new TreeSet<>();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return bst.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return bst.size();
    }

    public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
        if (p == null) throw new IllegalArgumentException();
        bst.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException();
        return bst.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        for (Point2D p : bst) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(
            RectHV rect) {     // all points that are inside the rectangle (or on the boundary)
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> list = new ArrayList<>();
        for (Point2D p : bst) {
            if (rect.contains(p)) list.add(p);
        }
        return list;
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Point2D nearestPoint = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (Point2D q : bst) {
            double distance = q.distanceSquaredTo(p);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestPoint = q;
            }
        }
        return nearestPoint;
    }

    public static void main(
            String[] args)                  // unit testing of the methods (optional)
    {
        int a = 0;
        return;
    }
}
