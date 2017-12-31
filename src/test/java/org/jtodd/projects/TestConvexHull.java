package org.jtodd.projects;

import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class TestConvexHull {

    private static final AffineTransform IDENTITY_TRANSFORM = new AffineTransform();

    @Test
    public void testHullOfEmptySet() {
        Path2D hull = ConvexHull.calculate(new HashSet<>());
        Assert.assertTrue(comparePaths(new Path2D.Double(), hull));
    }

    @Test
    public void testHullOfTwoPoints() {
        Point2D p1 = new Point2D.Double(0, 0);
        Point2D p2 = new Point2D.Double(1, 1);
        Set<Point2D> s = new HashSet<>();
        s.add(p1);
        s.add(p2);

        Path2D expectedHull = new Path2D.Double();
        expectedHull.moveTo(p2.getX(), p2.getY());
        expectedHull.lineTo(p1.getX(), p1.getY());
        expectedHull.lineTo(p2.getX(), p2.getY());

        Path2D foundHull = ConvexHull.calculate(s);
        boolean passed = comparePaths(expectedHull, foundHull);
        Assert.assertTrue(passed);
    }

    @Test
    public void testCalculateHull() {
        Set<Point2D> s = new HashSet<>();
        s.add(new Point2D.Double(1, -1));
        s.add(new Point2D.Double(1, 1));
        s.add(new Point2D.Double(0.5, 0.5));
        s.add(new Point2D.Double(0, 0));
        s.add(new Point2D.Double(0.5, 0));

        Path2D expectedHull = new Path2D.Double();
        expectedHull.moveTo(1, -1);
        expectedHull.lineTo(1, 1);
        expectedHull.lineTo(0.5, 0.5);
        expectedHull.lineTo(0, 0);
        expectedHull.lineTo(1, -1);

        Path2D foundHull = ConvexHull.calculate(s);
        boolean passed = comparePaths(expectedHull, foundHull);
        Assert.assertTrue(passed);
    }

    public static boolean comparePaths(Path2D expectedHull, Path2D foundHull) {
        PathIterator pe = expectedHull.getPathIterator(IDENTITY_TRANSFORM);
        pe.next();
        PathIterator pf = foundHull.getPathIterator(IDENTITY_TRANSFORM);
        pf.next();
        boolean passed = true;
        double [] expectedCoords = new double[6];
        double [] foundCoords = new double[6];
        int expectedType;
        int foundType;
        for (; (!pe.isDone() || !pf.isDone()) && passed; pe.next(), pf.next()) {
            expectedType = pe.currentSegment(expectedCoords);
            foundType = pf.currentSegment(foundCoords);
            if ((pe.isDone() && !pf.isDone()) || (!pe.isDone() && pf.isDone())) {
                System.out.printf("Paths have different lengths");
                passed = false;
            } else if (expectedType != foundType) {
                System.err.println("Paths have sides of different types");
                passed = false;
            } else if (expectedCoords[0] != foundCoords[0]) {
                System.err.printf("Paths have a vertex with different coordinates: (%f,%f) != (%f,%f)\n", expectedCoords[0], expectedCoords[1], foundCoords[0], foundCoords[1]);
                passed = false;
            } else if (expectedCoords[1] != foundCoords[1]) {
                System.err.printf("Paths have a vertex with different coordinates: (%f,%f) != (%f,%f)\n", expectedCoords[0], expectedCoords[1], foundCoords[0], foundCoords[1]);
                passed = false;
            }
        }
        return passed;
    }

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

    @Test
    public void testFindNextEdgeWithOneRightmostPoint() {
        HashSet<Point2D> s = new HashSet<>();
        ArrayList<Point2D> partialHull = new ArrayList<>();
        Point2D p1 = new Point2D.Double(1, -1);
        Point2D p2 = new Point2D.Double(1, 1);
        Point2D p3 = new Point2D.Double(0, 0);
        s.add(p1);
        s.add(p2);
        s.add(p3);
        partialHull.add(p1);
        partialHull.add(p2);
        Line2D prevEdge = new Line2D.Double(p1, p2);

        List<Point2D> nextVertexList = ConvexHull.findNextEdge(s, partialHull, 0, prevEdge, p1);
        Assert.assertArrayEquals(new Point2D [] {p3}, nextVertexList.toArray());
    }

    @Test
    public void testFindNextEdgeWithTwoRightmostPoints() {
        HashSet<Point2D> s = new HashSet<>();
        ArrayList<Point2D> partialHull = new ArrayList<>();
        Point2D p1 = new Point2D.Double(1, -1);
        Point2D p2 = new Point2D.Double(1, 1);
        Point2D p3 = new Point2D.Double(0, 0);
        Point2D p4 = new Point2D.Double(0.5, 0.5);
        Point2D p5 = new Point2D.Double(0.5, 0);
        s.add(p1);
        s.add(p2);
        s.add(p3);
        s.add(p4);
        s.add(p5);
        partialHull.add(p1);
        partialHull.add(p2);
        Line2D prevEdge = new Line2D.Double(p1, p2);

        List<Point2D> nextVertexList = ConvexHull.findNextEdge(s, partialHull, 0, prevEdge, p1);
        Assert.assertArrayEquals(new Point2D [] {p4, p3}, nextVertexList.toArray());
    }
}
