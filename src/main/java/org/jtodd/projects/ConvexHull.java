package org.jtodd.projects;

import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ConvexHull {

    public static Path2D calculate(Set<Point2D> points) {
        Path2D path = new Path2D.Double();
        if (points.size() == 0) {
            return path;
        }
        List<Point2D> rightmost = findRightmost(points);
        List<Point2D> partialHull = new ArrayList<>(rightmost);
        Point2D start = rightmost.get(0);
        Point2D pivotTail = null, pivotHead = null;
        if (rightmost.size() > 1) {
            pivotHead = start;
            pivotTail = rightmost.get(rightmost.size() - 1);
        } else {
            pivotTail = start;
            pivotHead = new Point2D.Double(pivotTail.getX(), pivotTail.getY() - 1);
        }
        List<Point2D> nextEdge = null;
        int numEdges = 0;
        do {
            Line2D prevEdge = new Line2D.Double(pivotHead, pivotTail);
            nextEdge = findNextEdge(points, partialHull, numEdges, prevEdge, start);
            partialHull.addAll(nextEdge);
            pivotHead = pivotTail;
            pivotTail = nextEdge.get(nextEdge.size() - 1);
            ++numEdges;
        } while (pivotTail != start);

        path.moveTo(start.getX(), start.getY());
        for (int i = 1; i < partialHull.size(); ++i) {
            pivotHead = partialHull.get(i);
            path.lineTo(pivotHead.getX(), pivotHead.getY());
        }
        return path;
    }

    /**
     * Return the rightmost (greatest X coordinate) of a set of points.
     * If more than one point shares the greatest X coordinate, return a List
     * of points sorted in increasing Y coordinate order.
     *
     * @param points The set of points to be searched
     * @return a list of Point2D that are rightmost in the set
     */
    public static List<Point2D> findRightmost(Set<Point2D> points) {
        List<Point2D> resultList = points.stream()
                     .collect(Collectors.groupingBy(p -> p.getX(), TreeMap::new, Collectors.toList()))
                     .lastEntry()
                     .getValue();
        Collections.sort(resultList, Comparator.comparingDouble(p -> p.getY()));
        return resultList;
    }

    /**
     * Return the next vertx of a convex hull. If the next edge contains more than one point
     * from the input set, a list of points is returned sorted in order of increasing distance
     * from the pivot point.
     *
     * @param points The set of points considered as candidates for next vertex
     * @param partialHull The points in the hull calculated so far
     * @param numEdges The number of edges in the hull calculated so far. Not the same as the number
     *                 of points since an edge can contain more than two collinear points.
     * @param prevEdge The previous edge of the hull
     * @param start The starting point of the convex hull. This is the only point in the currently
     *              known hull that is a candidate for the next edge, in which case the complete hull
     *              has been found.
     * @return a list of points containing the next edge of the hull
     */
    public static List<Point2D> findNextEdge(Set<Point2D> points, List<Point2D> partialHull, int numEdges, Line2D prevEdge, Point2D start) {
        Point2D pivot = prevEdge.getP2();
        List<Point2D> resultList = points.stream()
                     .filter(p -> (numEdges > 0 && p == start) || !partialHull.contains(p))
                     .collect(Collectors.groupingBy(
                                p -> Geom.vectorAngle(prevEdge, new Line2D.Double(pivot, p)),
                                TreeMap::new,
                                Collectors.toList()
                             )
                     )
                     .firstEntry()
                     .getValue();
        Collections.sort(resultList, Comparator.comparingDouble(p -> p.distance(pivot)));
        return resultList;
    }
}
