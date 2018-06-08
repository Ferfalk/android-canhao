/******************************************************************************
 * COPYRIGHT Vinícius G. Mendon�a ALL RIGHTS RESERVED.
 * <p>
 * This software cannot be copied, stored, distributed without
 * Vinícius G.Mendonça prior authorization.
 * <p>
 * This file was made available on http://www.pontov.com.br and it is free
 * to be restributed or used under Creative Commons license 2.5 br:
 * http://creativecommons.org/licenses/by-sa/2.5/br/
 * <p>
 * ******************************************************************************
 * Este software nao pode ser copiado, armazenado, distribuido sem autoriza��o
 * a priori de Vinícius G. Mendonça
 * <p>
 * Este arquivo foi disponibilizado no site http://www.pontov.com.br e esta
 * livre para distribuição seguindo a licença Creative Commons 2.5 br:
 * http://creativecommons.org/licenses/by-sa/2.5/br/
 ******************************************************************************/

package com.ferfalk.canhao.math;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public final class Vector2 implements Cloneable {
    public double x;
    public double y;

    public Vector2() {
        x = y = 0;
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2 createByMagAngle(double mag, double angle) {
        return new Vector2(cos(angle) * mag, sin(angle) * mag);
    }

    public Vector2 set(double newX, double newY) {
        x = newX;
        y = newY;
        return this;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSizeSqr() {
        return x * x + y * y;
    }

    public double getSize() {
        return sqrt(getSizeSqr());
    }

    public Vector2 setSize(double newSize) {
        return (newSize == 0) ? set(0, 0) : normalizeMe().multiplyMe(newSize);
    }

    public Vector2 truncate(double size) {
        return clone().truncateMe(size);
    }

    public Vector2 truncateMe(double size) {
        return (getSizeSqr() > size * size) ? setSize(size) : this;
    }

    public double getAngle() {
        return atan2(y, x);
    }

    public Vector2 rotateMe(double angle) {
        double s = sin(angle);
        double c = cos(angle);

        double newX = x * c - y * s;
        double newY = x * s + y * c;

        x = newX;
        y = newY;

        return this;
    }

    public Vector2 rotate(double angle) {
        return clone().rotateMe(angle);
    }

    public Vector2 normalizeMe() {
        return divideMe(getSize());
    }

    public Vector2 normalize() {
        return clone().normalizeMe();
    }

    public Vector2 addMe(Vector2 other) {
        x += other.x;
        y += other.y;
        return this;
    }

    public Vector2 add(Vector2 other) {
        return clone().addMe(other);
    }

    public Vector2 subtractMe(Vector2 other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    public Vector2 subtract(Vector2 other) {
        return clone().subtractMe(other);
    }

    public Vector2 multiplyMe(double c) {
        x *= c;
        y *= c;
        return this;
    }

    public Vector2 multiply(double c) {
        return clone().multiplyMe(c);
    }

    public Vector2 divideMe(double c) {
        double f = 1.0F / c;
        x *= f;
        y *= f;
        return this;
    }

    public Vector2 divide(double c) {
        return clone().divideMe(c);
    }

    public Vector2 negate() {
        return new Vector2(-x, -y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Vector2)) {
            return false;
        }
        Vector2 other = (Vector2) obj;

        return x == other.x && y == other.y;
    }

    public double dot(Vector2 other) {
        return x * other.x + y * other.y;
    }

    public double angleBetween(Vector2 other) {
        double angle = other.getAngle() - getAngle();

        if (abs(angle) < PI) {
            return angle;
        }
        return (angle + (angle < 0 ? 2 * PI : -2 * PI));
    }

    public double angleSign(Vector2 other) {
        double dp, angPi;

        dp = dot(other); // dot product
        if (dp >= 1.0) {
            dp = 1.0f;
        }
        if (dp <= -1.0) {
            dp = -1.0f;
        }
        angPi = acos(dp);

        // side test
        if (y * other.x > x * other.y) {
            return -angPi;
        }
        return angPi;
    }

    public double distance(Vector2 other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }

    @Override
    public Vector2 clone() {
        try {
            return (Vector2) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public double[] toArray() {
        return new double[]{x, y};
    }

    private int doubleHash(double number) {
        long value = Double.doubleToLongBits(number);
        return (int) (value ^ (value >>> 32));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime + doubleHash(x);
        return prime * result + doubleHash(y);
    }

    @Override
    public String toString() {
        return toString("%.4f");
    }

    public String toString(String numberFormat) {
        return String.format("x: " + numberFormat + " y: " + numberFormat,
                x, y);
    }

}
