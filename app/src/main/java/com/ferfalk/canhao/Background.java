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
public class Background {
    private Bitmap bitmap;
    private Rect dst;

    private static final Paint paint = Util.getPaintFilters();

    public Background(Context context) {
        dst = new Rect(0, 0, GameActivity.drawWidth, GameActivity.drawHeight);

        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open("background.png");
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, dst, paint);
    }
}
