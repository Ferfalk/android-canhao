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

import com.ferfalk.canhao.math.Vector2;

/**
 * Created by Ferahf on 30/09/2015.
 */
public class Botao {

    private Vector2 posicao;
    int insetBounding;
    private Bitmap bitmap;
    private Rect dst;

    private static final Paint paint = new Paint();
    private static final Paint paintBounding = Util.getPaintBounding();

    public Botao(Vector2 posicao, String sprite, int insetBounding, Context context) {
        this.posicao = posicao;
        this.insetBounding = insetBounding;

        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(sprite);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dst = new Rect((int) posicao.x - bitmap.getWidth() / 2, (int) posicao.y - bitmap.getHeight() / 2, (int) posicao.x + bitmap.getWidth() / 2, (int) posicao.y + bitmap.getHeight() / 2);
    }

    public Vector2 getPosicao() {
        return posicao;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, dst, paint);
    }

    public void drawBoundingBox(Canvas canvas) {

        canvas.drawRect(getBoundingBox(), paintBounding);
    }

    public Rect getBoundingBox() {
        Rect rect = new Rect(dst);
        rect.inset(insetBounding, insetBounding);
        return rect;
    }

    public boolean isHit(Vector2 pos) {
        return getBoundingBox().contains((int) pos.x, (int) pos.y);
    }
}
