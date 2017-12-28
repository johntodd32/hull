package org.jtodd.projects;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
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
        if (points.size() > 0) {
            Iterator<Point2D> pi = points.iterator();
            Point2D start = pi.next();
            path.moveTo(start.getX(), start.getY());
            while (pi.hasNext()) {
                Point2D p = pi.next();
                path.lineTo(p.getX(), p.getY());
            }
            path.closePath();
        }
        return path;
    }

    /**
     * Return the rightmost (greatest X coordinate) of a set of points.
     * If more than one point shares the greatest X coordinate, return a List
     * of points sorted in increasing Y coordinate order.
     *
     * @param Set of Point2D
     * @return List of Point2D
     */
    public static List<Point2D> findRightmost(Set<Point2D> points) {
        List<Point2D> resultList = points.stream()
                     .collect(Collectors.groupingBy(p -> p.getX(), TreeMap::new, Collectors.toList()))
                     .lastEntry()
                     .getValue();
        Collections.sort(resultList, Comparator.comparingDouble(p -> p.getY()));
        return resultList;
    }
}
