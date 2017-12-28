package org.jtodd.projects;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ConvexHull {

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
