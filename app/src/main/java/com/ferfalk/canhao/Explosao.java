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

public class Explosao {

    private static final int fps = 1000 / 24;
    private static final int width = 64;
    private static final int height = 64;
    private static final int widthX = 4;
    private static final int widthY = 4;
    private static final int frames = widthX * widthY;

    private static Bitmap anim[] = null;
    private int frame = 0;
    private float elapsedTime = 0;
    private Vector2 posicao;
    private Matrix matrix = new Matrix();

    private static final Paint paint = Util.getPaintFilters();

    public Explosao(Vector2 posicao, Context context) {
        this.posicao = posicao;

        if (anim == null)
            carregaBitmaps(context);
    }

    private void carregaBitmaps(Context context) {
        Log.d(GameActivity.TAG, "Explosao carregaBitmaps()");
        anim = new Bitmap[16];
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open("explosao.png");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            int i = 0;
            for (int y = 0; y < widthY; y++) {
                for (int x = 0; x < widthX; x++) {
                    anim[i] = Bitmap.createBitmap(bitmap, x * width, y * height, width, height);
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean update(float deltaTime) {
        elapsedTime += deltaTime;
        if (elapsedTime > fps) {
            elapsedTime = 0;
            frame++;
        }
        if (frame >= frames)
            return true;
        else
            return false;
    }

    public void draw(Canvas canvas) {
        matrix.reset();
        matrix.preTranslate((int) posicao.x - width / 2, (int) posicao.y - height / 2);
        canvas.drawBitmap(anim[frame], matrix, paint);
    }
}
