package com.ferfalk.canhao;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.io.IOException;
import java.io.InputStream;

import com.ferfalk.canhao.math.Vector2;

public class Canhao {
    private static final int width = 96;
    private static final int height = 16;

    private static final int maxVida = 100;

    private Bitmap bitmap;
    private double angulo;
    private Vector2 posicao;
    private Circulo bounding;
    private Matrix matrix = new Matrix();
    private Context context;
    private int vida;
    private String nome;
    private int cor;
    private int quantBala1 = 3;
    private int quantBala2 = 5;

    private Suporte suporte;

    private static final Paint paint = Util.getPaintFilters();
    private static final Paint paintBounding = Util.getPaintBoundingFilters();

    public Canhao(int tipo, Vector2 posicao, Context context) {
        this.posicao = posicao;
        this.context = context;
        this.vida = maxVida;

        String sprite;
        if (tipo == 0) {
            sprite = "canhaoAzul.png";
            nome = "Azul";
            cor = Color.BLUE;
        } else {
            sprite = "canhaoVermelho.png";
            nome = "Vermelho";
            cor = Color.RED;
        }
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(sprite);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        suporte = new Suporte(tipo, new Vector2(posicao.x, posicao.y + 8), context);
        bounding = new Circulo(new Vector2(posicao.x, posicao.y + 16), 30);
    }

    public Vector2 getPosicao() {
        return posicao;
    }

    public double getAngulo() {
        return angulo;
    }

    public void setAngulo(double angulo) {
        this.angulo = angulo;
    }

    public int getVida() {
        return vida;
    }

    public int getMaxVida() {
        return maxVida;
    }

    public void recebeDano(int dano) {
        vida -= dano;
        vida = Math.max(0, vida);
    }

    public void update(float deltaTime) {
        suporte.update(deltaTime);
    }

    public void draw(Canvas canvas) {
        matrix.reset();
        matrix.preTranslate((float) (posicao.x - width / 2), (float) (posicao.y - height / 2));
        matrix.preRotate((float) (angulo * 180 / Math.PI), width / 2, height / 2);
        canvas.drawBitmap(bitmap, matrix, paint);
        suporte.draw(canvas);
    }

    public void drawBounding(Canvas canvas) {
        canvas.drawCircle((int) bounding.posicao.x, (int) bounding.posicao.y, (int) bounding.raio, paintBounding);
    }

    public Bala fire(Bala.Tipo tipoBala, float forca) {
        switch (tipoBala) {
            case Bala1:
                quantBala1--;
                break;
            case Bala2:
                quantBala2--;
                break;
        }

        Vector2 posBala = new Vector2(posicao.x, posicao.y);
        Vector2 offset = Vector2.createByMagAngle(50, this.angulo);
        posBala.addMe(offset);
        return new Bala(tipoBala, posBala, (float) angulo, forca, context);
    }

    public boolean isHit(Bala bala) {
        return bounding.colide(bala.getBounding());
    }

    public int getQuantBala1() {
        return quantBala1;
    }

    public int getQuantBala2() {
        return quantBala2;
    }

    public String getNome() {
        return nome;
    }

    public int getCor() {
        return cor;
    }
}
