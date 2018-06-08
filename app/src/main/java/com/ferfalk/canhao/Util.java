package com.ferfalk.canhao;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.Random;

/**
 * Created by Ferahf on 30/09/2015.
 */
public class Util {

    private static final Random random = new Random();
    private static Typeface fonte = null;

    private Util() {
    }

    public static Paint getPaintFilters() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        //paint.setDither(true);
        paint.setFilterBitmap(true);
        return paint;
    }

    public static Paint getPaintBounding() {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }

    public static Paint getPaintBoundingFilters() {
        Paint paint = getPaintFilters();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }

    public static Paint getPaintPadraoTexto() {
        Paint paint = getPaintFilters();
        paint.setTypeface(fonte);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(32);
        return paint;
    }

    public static float scale(float x, float old_min, float old_max, float new_min, float new_max) {
        float old_range = old_max - old_min;
        float new_range = new_max - new_min;
        return new_min + (x - old_min) * new_range / old_range;
    }

    public static void setFonte(Typeface fonte) {
        Util.fonte = fonte;
    }

    public static int randomFromTo(int from, int to) {
        return random.nextInt(to - from + 1) + from;
    }
}
