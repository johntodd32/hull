package org.jtodd.projects;

import org.junit.Assert;
import org.junit.Test;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class TestGeom {

    public static final double DELTA = 1e-6;

    @Test
    public void testVectorAngles() {
        Line2D v1 = new Line2D.Double(0, 0, 1, 0);
        Line2D v2 = new Line2D.Double(0, 0, 0, 1);
        Line2D v3 = new Line2D.Double(0, 0, -1, 0);
        Line2D v4 = new Line2D.Double(1, 1, 1, 2);
        Line2D point = new Line2D.Double(0, 0, 0, 0);
        Assert.assertEquals( Math.PI / 2, Geom.vectorAngle(v1, v2), DELTA);
        Assert.assertEquals(-Math.PI / 2, Geom.vectorAngle(v2, v1), DELTA);
        Assert.assertEquals( Math.PI / 2, Geom.vectorAngle(v1, v4), DELTA);
        Assert.assertEquals(Math.PI, Geom.vectorAngle(v1, v3), DELTA);
        Assert.assertEquals("Expected an angle of 0.0 between a line and itself", 0, Geom.vectorAngle(v1, v1), DELTA);
        Assert.assertEquals("Expected an angle of 0.0 between a line and a point", 0, Geom.vectorAngle(v1, point), DELTA);
        Assert.assertEquals("Expected an angle of 0.0 between a point and a line", 0, Geom.vectorAngle(point, v1), DELTA);
        Assert.assertEquals("Expected an angle of 0.0 between two points", 0.0, Geom.vectorAngle(point, point), DELTA);

        Line2D v5 = new Line2D.Double(1, -1, 1, 1);
        Line2D v6 = new Line2D.Double(1, 1, 0, 0);
        Line2D v7 = new Line2D.Double(1, 1, 0.5, 0);
        Assert.assertEquals(3 * Math.PI / 4, Geom.vectorAngle(v5, v6), DELTA);
        Assert.assertEquals(Math.PI - Math.acos(2.0 / Math.sqrt(5)), Geom.vectorAngle(v5, v7), DELTA);
        Assert.assertTrue(Geom.vectorAngle(v5, v6) < Geom.vectorAngle(v5, v7));
    }

    @Test
    public void testTranslateVector() {
        Line2D l = new Line2D.Double(1, 1, 2, 2);
        Assert.assertEquals(new Point2D.Double(1, 1), Geom.translateVector(l));
    }

    @Test
    public void testDotProduct() {
        Point2D p1 = new Point2D.Double(2, 2);
        Point2D p2 = new Point2D.Double(0, 3);
        Assert.assertEquals(6, Geom.dotProduct(p1, p2), DELTA);
    }

    @Test
    public void testCrossProduct() {
        Point2D p1 = new Point2D.Double(2, 2);
        Point2D p2 = new Point2D.Double(0, 3);
        Assert.assertEquals(6, Geom.crossProduct(p1, p2), DELTA);
    }
}
