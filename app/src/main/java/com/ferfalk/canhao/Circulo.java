package com.ferfalk.canhao;

import com.ferfalk.canhao.math.Vector2;

/**
 * Created by Ferahf on 15/10/2015.
 */
public class Circulo {
    public Vector2 posicao;
    public double raio;

    public Circulo(Vector2 posicao, double raio) {
        this.posicao = posicao;
        this.raio = raio;
    }

    public boolean colide(Circulo outro) {
        double dx = posicao.x - outro.posicao.x;
        double dy = posicao.y - outro.posicao.y;
        double radii = raio + outro.raio;
        return ((dx * dx) + (dy * dy) < radii * radii);
    }

    public boolean colideChao(double y) {
        return (posicao.y + raio) >= y;
    }
}
