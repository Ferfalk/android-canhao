package com.ferfalk.canhao;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import com.ferfalk.canhao.math.Vector2;

public class Bala {

    public enum Tipo {
        Bala1(0), Bala2(1), Bala3(2);

        private int id;

        Tipo(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public static final int width = 14;
    public static final int height = 35;

    private static Bitmap[] bitmaps = null;
    private Tipo tipo;
    public Vector2 posicao;
    private Vector2 momento;
    private float massa;
    private int dano;
    private Matrix matrix = new Matrix();

    private static final Paint paint = Util.getPaintFilters();
    private static final Paint paintBounding = Util.getPaintBoundingFilters();

    public Bala(Bala.Tipo tipo, Vector2 posicao, float angulo, float forca, Context context) {
        this.tipo = tipo;
        this.posicao = posicao;

        if (bitmaps == null)
            carregaBitmaps(context);

        switch (tipo) {
            case Bala1:
                massa = 1.5f;
                dano = 20;
                break;
            case Bala2:
                massa = 1.25f;
                dano = 10;
                break;
            case Bala3:
            default:
                massa = 1f;
                dano = 5;
                break;
        }
        momento = Vector2.createByMagAngle(forca, angulo).divide(massa);
    }

    private void carregaBitmaps(Context context) {
        Log.d(GameActivity.TAG, "Bala carregaBitmaps()");
        bitmaps = new Bitmap[3];
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open("bala1.png");
            bitmaps[Tipo.Bala1.getId()] = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            inputStream = assetManager.open("bala2.png");
            bitmaps[Tipo.Bala2.getId()] = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            inputStream = assetManager.open("bala3.png");
            bitmaps[Tipo.Bala3.getId()] = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getDano() {
        return dano;
    }

    public void update(float deltaTime) {
        float secs = deltaTime / 1000.0f;
        Vector2 gravidade = GameActivity.gravidade.clone();
        Vector2 vento = GameActivity.vento.clone();

        Vector2 accelSecs = vento.divide(massa).add(gravidade).multiply(secs);
        posicao.addMe(momento.add(accelSecs.divide(2)).multiply(secs));
        momento.addMe(accelSecs);

    }

    public void draw(Canvas canvas) {
        matrix.reset();
        matrix.preTranslate((float) (posicao.x - width / 2), (float) (posicao.y - height / 2));
        matrix.preRotate(90 + (float) (momento.getAngle() * 180 / Math.PI), width / 2, height / 2);
        canvas.drawBitmap(bitmaps[tipo.getId()], matrix, paint);
    }

    public void drawBounding(Canvas canvas) {
        Circulo circulo = getBounding();
        canvas.drawCircle((int) circulo.posicao.x, (int) circulo.posicao.y, (int) circulo.raio, paintBounding);
    }

    public Circulo getBounding() {
        Vector2 offset = Vector2.createByMagAngle(8, this.momento.getAngle());
        return new Circulo(posicao.add(offset), 6);
    }
}
