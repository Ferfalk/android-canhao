package com.ferfalk.canhao;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Ferahf on 30/09/2015.
 */
public class Chao {
    private Bitmap bitmap;
    private int boundingTop;
    private Rect dst;

    private static final Paint paint = Util.getPaintFilters();
    private static final Paint paintBounding = Util.getPaintBounding();

    public Chao(int boundingTop, Context context) {
        this.boundingTop = boundingTop;

        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open("chao.png");
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int height = bitmap.getHeight();
        dst = new Rect(0, GameActivity.drawHeight - height, GameActivity.drawWidth, GameActivity.drawHeight);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, dst, paint);
    }

    public void drawBoundingBox(Canvas canvas) {
        canvas.drawLine(0, boundingTop, GameActivity.drawWidth, boundingTop, paintBounding);
    }

    public int getBoundingTop() {
        return boundingTop;
    }
}
