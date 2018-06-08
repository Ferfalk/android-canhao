package com.ferfalk.canhao;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.io.IOException;
import java.io.InputStream;

import com.ferfalk.canhao.math.Vector2;

public class IndicadorForca {
    private static final float minScale = 0.25f;
    private static final float maxScale = 1f;

    private Bitmap bitmap;
    private int width;
    private int height;
    public double angulo;
    private float minForca;
    private float maxForca;
    private float scale = 1;
    public Vector2 posicao;
    private Matrix matrix = new Matrix();

    private static final Paint paint = Util.getPaintFilters();

    public IndicadorForca(Vector2 posicao, float angulo, float minForca, float maxForca, Context context) {
        this.posicao = posicao;
        this.angulo = angulo;
        this.minForca = minForca;
        this.maxForca = maxForca;

        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open("indicadorForca.png");
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        width = bitmap.getWidth();
        height = bitmap.getHeight();
    }

    public void update(float deltaTime) {
        scale = Util.scale(GameActivity.forca, minForca, maxForca, minScale, maxScale);
    }

    public void draw(Canvas canvas) {
        matrix.reset();
        matrix.preTranslate((float) (posicao.x - width * scale / 2), (float) (posicao.y - height * scale / 2));
        matrix.preScale(scale, scale);
        matrix.preRotate(90 + (float) (angulo * 180 / Math.PI), width / 2, height / 2);
        canvas.drawBitmap(bitmap, matrix, paint);
    }
}
