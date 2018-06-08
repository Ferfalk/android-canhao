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

public class Suporte {

    private static final int width = 64;
    private static final int height = 48;

    private Bitmap bitmap;
    private double angle;
    private Vector2 posicao;
    private Matrix matrix = new Matrix();

    private static final Paint paint = Util.getPaintFilters();

    public Suporte(int tipo, Vector2 posicao, Context context) {
        this.posicao = posicao;

        String spriteSuporte;
        if (tipo == 0)
            spriteSuporte = "suporteAzul.png";
        else
            spriteSuporte = "suporteVermelho.png";
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(spriteSuporte);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(float deltaTime) {
    }

    public void draw(Canvas canvas) {
        matrix.reset();
        matrix.preTranslate((int) posicao.x - width / 2, (int) posicao.y - height / 2);
        matrix.preRotate((float) (angle * 180 / Math.PI), width / 2, height / 2);
        canvas.drawBitmap(bitmap, matrix, paint);
    }
}
