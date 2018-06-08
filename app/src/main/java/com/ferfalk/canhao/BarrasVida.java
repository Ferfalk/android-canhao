package com.ferfalk.canhao;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.List;

import com.ferfalk.canhao.math.Vector2;

public class BarrasVida {

    private int width;
    private static final int height = 20;
    private static final int offset = 10;

    private List<Canhao> canhoes;
    private Vector2 posicaoVidaAzul;
    private Vector2 posicaoVidaVermelho;
    private static final Paint paintBarAzul = new Paint();
    private static final Paint paintBarVermelho = new Paint();
    private Rect dstAzul;
    private Rect dstAzulStroke;
    private Rect dstVermelho;
    private Rect dstVermelhoStroke;
    private static final Paint paintStroke = new Paint();

    public BarrasVida(List<Canhao> canhoes) {
        this.canhoes = canhoes;

        width = canhoes.get(0).getMaxVida();

        dstAzul = new Rect();
        dstVermelho = new Rect();

        paintStroke.setColor(Color.BLACK);
        paintStroke.setStrokeWidth(1);
        paintStroke.setStyle(Paint.Style.STROKE);

        posicaoVidaAzul = new Vector2(offset, offset);
        posicaoVidaVermelho = new Vector2(GameActivity.drawWidth - width - offset, offset);

        dstAzulStroke = new Rect((int) posicaoVidaAzul.x, (int) posicaoVidaAzul.y, (int) posicaoVidaAzul.x + width, (int) posicaoVidaAzul.y + height);
        dstVermelhoStroke = new Rect((int) posicaoVidaVermelho.x, (int) posicaoVidaVermelho.y, (int) posicaoVidaVermelho.x + width, (int) posicaoVidaVermelho.y + height);
    }

    public void update(float deltaTime) {
        int vidaAzul = canhoes.get(0).getVida();
        dstAzul.set((int) posicaoVidaAzul.x, (int) posicaoVidaAzul.y, (int) posicaoVidaAzul.x + vidaAzul, (int) posicaoVidaAzul.y + height);
        if (vidaAzul > 50)
            paintBarAzul.setColor(Color.GREEN);
        else if (vidaAzul <= 25)
            paintBarAzul.setColor(Color.RED);
        else
            paintBarAzul.setColor(Color.YELLOW);

        int vidaVermelho = canhoes.get(1).getVida();
        dstVermelho.set((int) posicaoVidaVermelho.x + width - vidaVermelho, (int) posicaoVidaVermelho.y, GameActivity.drawWidth - offset, (int) posicaoVidaVermelho.y + height);
        if (vidaVermelho > 50)
            paintBarVermelho.setColor(Color.GREEN);
        else if (vidaVermelho <= 25)
            paintBarVermelho.setColor(Color.RED);
        else
            paintBarVermelho.setColor(Color.YELLOW);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(dstAzul, paintBarAzul);
        canvas.drawRect(dstAzulStroke, paintStroke);

        canvas.drawRect(dstVermelho, paintBarVermelho);
        canvas.drawRect(dstVermelhoStroke, paintStroke);
    }
}
