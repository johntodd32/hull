package org.jtodd.projects;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Geom {

    public static double vectorAngle(Line2D v1, Line2D v2) {
        Point2D tv1 = translateVector(v1);
        Point2D tv2 = translateVector(v2);
        return Math.atan2(crossProduct(tv1, tv2), dotProduct(tv1, tv2));
    }

    public static Point2D translateVector(Line2D l) {
        return new Point2D.Double(l.getX2() - l.getX1(), l.getY2() - l.getY1());
    }

    public static double dotProduct(Point2D tv1, Point2D tv2) {
        return tv1.getX() * tv2.getX() + tv1.getY() * tv2.getY();
    }

    public static double crossProduct(Point2D tv1, Point2D tv2) {
        return tv1.getX() * tv2.getY() - tv1.getY() * tv2.getX();
    }
}
