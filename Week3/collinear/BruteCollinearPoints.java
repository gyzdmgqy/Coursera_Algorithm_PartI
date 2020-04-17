import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> segmentList;

    public BruteCollinearPoints(Point[] points) // finds all line segments containing 4 points
    {
        if (points == null) throw new IllegalArgumentException();
        int pSize = points.length;
        for (int i = 0; i < pSize; ++i) if (points[i] == null) throw new IllegalArgumentException();
        Point[] localPoints = points.clone();
        Arrays.sort(localPoints);
        for (int p = 0; p < pSize - 1; ++p) {
            if (localPoints[p].compareTo(localPoints[p + 1]) == 0)
                throw new IllegalArgumentException();
        }
        segmentList = new ArrayList<LineSegment>();

        for (int p = 0; p < pSize - 3; ++p) {
            for (int q = p + 1; q < pSize - 2; ++q) {
                double slopePQ = localPoints[p].slopeTo(localPoints[q]);
                for (int r = q + 1; r < pSize - 1; ++r) {
                    double slopePR = localPoints[p].slopeTo(localPoints[r]);
                    if (Double.compare(slopePQ, slopePR) != 0) continue;
                    for (int s = r + 1; s < pSize; ++s) {
                        double slopePS = localPoints[p].slopeTo(localPoints[s]);
                        if (Double.compare(slopePQ, slopePS) == 0) {
                            segmentList.add(new LineSegment(localPoints[p], localPoints[s]));
                        }

                    }
                }
            }
        }

    }

    public int numberOfSegments()        // the number of line segments
    {
        return segmentList.size();
    }

    public LineSegment[] segments()                // the line segments
    {
        return segmentList.toArray(new LineSegment[numberOfSegments()]);

    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
