package org.jtodd.projects;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class TestConvexHull {

    @Test
    public void testFindRightmostPointInASetOfPoints() {
        Point2D p1 = new Point2D.Double(1, -1);
        Point2D p2 = new Point2D.Double(0.5, 0.5);

        Set<Point2D> s = new HashSet<>();
        s.add(p1);
        s.add(p2);
        Assert.assertArrayEquals(new Point2D [] {p1}, ConvexHull.findRightmost(s).toArray());
    }

    @Test
    public void testFindSetOfRightmostPointsInCorrectOrder() {
        Point2D p1 = new Point2D.Double(1, -1);
        Point2D p2 = new Point2D.Double(1, 1);
        Point2D p3 = new Point2D.Double(0.5, 0.5);
        Point2D p4 = new Point2D.Double(1, 0.9999999);

        Set<Point2D> s = new HashSet<>();
        s.add(p1);
        s.add(p2);
        s.add(p3);
        Assert.assertArrayEquals(new Point2D [] {p1, p2}, ConvexHull.findRightmost(s).toArray());

        Set<Point2D> s2 = new HashSet<>();
        s2.add(p1);
        s2.add(p2);
        s2.add(p3);
        s2.add(p4);
        Assert.assertArrayEquals(new Point2D [] {p1, p4, p2}, ConvexHull.findRightmost(s2).toArray());
    }
}
