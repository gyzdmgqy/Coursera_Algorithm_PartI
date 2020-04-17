import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> localSegments;

    public FastCollinearPoints(Point[] points)// finds all line segments containing 4 or more points
    {
        double[] slopesArr;
        ArrayList<Point> currentFullSegment = new ArrayList<>();
        double currentSlope = 0;
        double nextSlope = 0;
        if (points == null) throw new IllegalArgumentException();
        int pSize = points.length;
        for (int i = 0; i < pSize; ++i)
            if (points[i] == null) throw new IllegalArgumentException();
        Point[] localPoints = points.clone();
        Arrays.sort(localPoints);
        for (int i = 0; i < pSize - 1; ++i)
            if (localPoints[i].compareTo(localPoints[i + 1]) == 0)
                throw new IllegalArgumentException();
        localSegments = new ArrayList<LineSegment>();
        for (int p = 0; p < pSize - 3; ++p) {
            Point[] slopeOrderedPoints = localPoints.clone();
            Arrays.sort(slopeOrderedPoints, localPoints[p].slopeOrder());
            int numEqualSlopes = 1;
            Point tail = null;
            Point minPoint = null;
            slopesArr = new double[pSize];
            for (int j = 0; j < pSize; ++j)
                slopesArr[j] = localPoints[p].slopeTo(slopeOrderedPoints[j]);
            for (int j = 0; j < pSize - 1; ++j) {
                Point currentPoint = slopeOrderedPoints[j];
                currentSlope = localPoints[p].slopeTo(currentPoint);
                nextSlope = localPoints[p].slopeTo(slopeOrderedPoints[j + 1]);
                if (Double.compare(currentSlope, nextSlope) == 0) {
                    numEqualSlopes++;
                    if (minPoint == null) minPoint = currentPoint;
                    currentFullSegment.add(currentPoint);
                }
                else {
                    if (numEqualSlopes >= 3) {
                        tail = slopeOrderedPoints[j];
                        if (minPoint.compareTo(localPoints[p]) > 0) {
                            LineSegment newSeg = new LineSegment(localPoints[p], tail);
                            localSegments.add(newSeg);

                        }
                    }
                    numEqualSlopes = 1;
                    currentFullSegment.clear();
                    minPoint = null;
                }
            }
            if (numEqualSlopes >= 3) {
                tail = slopeOrderedPoints[pSize - 1];
                if (minPoint.compareTo(localPoints[p]) > 0) {
                    LineSegment newSeg = new LineSegment(localPoints[p], tail);
                    localSegments.add(newSeg);
                }
            }
        }
    }

    public int numberOfSegments()        // the number of line segments
    {
        return localSegments.size();
    }

    public LineSegment[] segments()                // the line segments
    {
        return localSegments.toArray(new LineSegment[numberOfSegments()]);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
