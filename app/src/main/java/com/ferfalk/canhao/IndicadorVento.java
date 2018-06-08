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

public class IndicadorVento {

    private static final float minScale = 0f;
    private static final float maxScale = 2f;

    private Bitmap bitmap;
    private int width;
    private int height;
    private double angulo = 0;
    private float minVento;
    private float maxVento;
    private Vector2 scale = new Vector2(1f, 0.5f);
    private Vector2 posicao;
    private Matrix matrix = new Matrix();

    private static final Paint paint = Util.getPaintFilters();
    private Paint paintTexto;

    public IndicadorVento(Vector2 posicao, float minVento, float maxVento, Context context) {
        this.posicao = posicao;
        this.minVento = minVento;
        this.maxVento = maxVento;

        paintTexto = Util.getPaintPadraoTexto();
        paintTexto.setTextSize(24);

        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open("indicadorVento.png");
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        width = bitmap.getWidth();
        height = bitmap.getHeight();
    }

    public void update() {
        scale.x = Util.scale((float) GameActivity.vento.getSize(), minVento, maxVento, minScale, maxScale);
        angulo = GameActivity.vento.getAngle();
    }

    public void draw(Canvas canvas) {
        matrix.reset();
        matrix.preTranslate((float) (posicao.x - width * scale.x / 2), (float) (posicao.y - height * scale.y / 2));
        matrix.preScale((float) scale.x, (float) scale.y);
        matrix.preRotate(90 + (float) (angulo * 180 / Math.PI), width / 2, height / 2);
        canvas.drawBitmap(bitmap, matrix, paint);
        canvas.drawText("Vento", (float) posicao.x, (float) (posicao.y - height * scale.y / 2f), paintTexto);
    }
}
