package com.ferfalk.canhao;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ferfalk.canhao.math.Vector2;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener {
    public static final String TAG = "CanhaoDebug";
    public static final int drawWidth = 800, drawHeight = 480;
    private static final boolean drawBoundings = false;

    private Tela tela;
    private SoundManager soundManager;
    private int efeitoTiro, efeitoExplosao;

    private Bitmap drawBitmap;
    private Canvas drawCanvas;
    Rect dstCanvas = new Rect();

    private Background background;
    private Chao chao;

    private BarrasVida barrasVida;
    private IndicadorForca indicadorForca;
    private IndicadorVento indicadorVento;
    private List<Canhao> canhoes = new ArrayList<>();
    private List<Bala> balas = new ArrayList<>();
    private List<Explosao> explosoes = new ArrayList<>();

    public static final Vector2 gravidade = new Vector2(0, 980);
    private final int maxVento = 300;
    public static Vector2 vento = new Vector2();

    private boolean pressionando = false;
    private float startPressTempo = 0f;
    private final float maxTempo = 2.5f;
    private final float minForca = 250f;
    private final float maxForca = 1500f;
    public static float forca = 0f;

    public Estado estado;
    private int vez = 0;
    private Bala.Tipo tipoSelecionado = Bala.Tipo.Bala3;

    private Botao btPronto, btBala1, btBala2, btBala3;
    private boolean tocandoBtPronto = false;

    private Paint paintFilters = Util.getPaintFilters();
    private Paint paintTexto = Util.getPaintPadraoTexto();
    private Paint paintTextoNome = Util.getPaintPadraoTexto();
    private Paint paintTextoNomeVencedor = Util.getPaintPadraoTexto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        estado = Estado.ESCOLHER_BALA;


        Util.setFonte(Typeface.createFromAsset(getAssets(), "Roboto-Bold.ttf"));
        paintTexto = Util.getPaintPadraoTexto();
        paintTextoNome = Util.getPaintPadraoTexto();
        paintTextoNome.setTextSize(40);
        paintTextoNomeVencedor = Util.getPaintPadraoTexto();
        paintTextoNomeVencedor.setTextSize(60);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dstCanvas.set(0, 0, size.x, size.y);

        soundManager = new SoundManager(this);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        try {
            efeitoTiro = soundManager.load("efeitoTiro.ogg");
            efeitoExplosao = soundManager.load("efeitoExplosao.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }


        drawBitmap = Bitmap.createBitmap(drawWidth, drawHeight, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(drawBitmap);

        background = new Background(this);

        Canhao canhaoAzul = new Canhao(0, new Vector2(45, drawHeight - 45), this);
        canhaoAzul.setAngulo(Math.toRadians(-45));
        canhoes.add(canhaoAzul);
        Canhao canhaoVermelho = new Canhao(1, new Vector2(drawWidth - 45, drawHeight - 45), this);
        canhaoVermelho.setAngulo(Math.toRadians(225));
        canhoes.add(canhaoVermelho);

        chao = new Chao((int) canhaoAzul.getPosicao().y + 32, this);

        btPronto = new Botao(new Vector2(drawWidth / 2, drawHeight - 40), "btPronto.png", 0, this);
        int offSet = 200;
        int insetBounding = -5;
        btBala1 = new Botao(new Vector2(drawWidth / 2 - offSet, drawHeight / 2), "btBala1.png", insetBounding, this);
        btBala2 = new Botao(new Vector2(drawWidth / 2, drawHeight / 2), "btBala2.png", insetBounding, this);
        btBala3 = new Botao(new Vector2(drawWidth / 2 + offSet, drawHeight / 2), "btBala3.png", insetBounding, this);

        barrasVida = new BarrasVida(canhoes);
        indicadorForca = new IndicadorForca(new Vector2(), 0, minForca, maxForca, this);
        indicadorVento = new IndicadorVento(new Vector2(drawWidth / 2, 50), 0, maxVento, this);
        trocaVento();

        tela = new Tela(this);
        tela.setOnTouchListener(this);
        setContentView(tela);
    }

    private void update(float deltaTime) {
        switch (estado) {
            case ESCOLHER_BALA:
                break;
            case ESCOLHER_ANGULO:
                break;
            case ATIRAR:
                if (pressionando) {
                    float tempo = (System.nanoTime() - startPressTempo) / 1000000000.0f;
                    tempo = Math.min(tempo, maxTempo);
                    forca = Util.scale(tempo, 0, maxTempo, minForca, maxForca);
                    indicadorForca.update(deltaTime);
                }
                break;
            case FISICA:
                for (Canhao canhao : canhoes) {
                    canhao.update(deltaTime);
                }

                if (balas.size() > 0) {
                    for (int i = balas.size() - 1; i >= 0; i--) {
                        Bala bala = balas.get(i);
                        // Executa o loop dividido em várias vezes pra melhorar colisão em alta velocidade
                        int vezes = 6;
                        for (int y = 0; y < vezes; y++) {
                            bala.update(deltaTime / vezes);
                            if (canhoes.get(1 - vez).isHit(bala)) {
                                balas.remove(i);
                                canhoes.get(1 - vez).recebeDano(bala.getDano());
                                explosoes.add(new Explosao(bala.getBounding().posicao, this));
                                soundManager.play(efeitoExplosao);
                                break;
                            } else if (canhoes.get(vez).isHit(bala)) {
                                balas.remove(i);
                                canhoes.get(vez).recebeDano(bala.getDano());
                                explosoes.add(new Explosao(bala.getBounding().posicao, this));
                                soundManager.play(efeitoExplosao);
                                break;
                            } else if (bala.getBounding().colideChao(chao.getBoundingTop())) {
                                balas.remove(i);
                                explosoes.add(new Explosao(bala.getBounding().posicao, this));
                                soundManager.play(efeitoExplosao);
                                break;
                            }
                        }
                    }
                }

                if (explosoes.size() > 0) {
                    for (int i = explosoes.size() - 1; i >= 0; i--) {
                        Explosao explosao = explosoes.get(i);
                        if (explosao.update(deltaTime))
                            explosoes.remove(i);
                    }
                }

                if (balas.size() == 0 && explosoes.size() == 0) {
                    vez = 1 - vez;
                    trocaVento();
                    estado = Estado.ESCOLHER_BALA;
                }

                if (canhoes.get(0).getVida() <= 0 || canhoes.get(1).getVida() <= 0) {
                    estado = Estado.FIM;
                }
                break;
        }
        barrasVida.update(deltaTime);
    }

    private void drawGame(Canvas canvas) {
        canvas.drawRGB(255, 255, 255);
        background.draw(canvas);
        barrasVida.draw(canvas);
        indicadorVento.draw(canvas);

        paintTextoNome.setColor(canhoes.get(vez).getCor());
        int posNome = 110;
        int posDica = posNome + 50;

        switch (estado) {
            case ESCOLHER_BALA:
                for (Canhao canhao : canhoes) {
                    canhao.draw(canvas);
                }
                chao.draw(canvas);
                canvas.drawText(canhoes.get(vez).getNome(), drawWidth / 2, posNome, paintTextoNome);
                int offsetY = 88;
                canvas.drawText("Escolha a bala", drawWidth / 2, posDica, paintTexto);
                if (canhoes.get(vez).getQuantBala1() > 0) {
                    btBala1.draw(canvas);
                    canvas.drawText("" + canhoes.get(vez).getQuantBala1(), (int) btBala1.getPosicao().x, (int) btBala1.getPosicao().y + offsetY, paintTexto);
                }
                if (canhoes.get(vez).getQuantBala2() > 0) {
                    btBala2.draw(canvas);
                    canvas.drawText("" + canhoes.get(vez).getQuantBala2(), (int) btBala2.getPosicao().x, (int) btBala2.getPosicao().y + offsetY, paintTexto);
                }
                btBala3.draw(canvas);
                canvas.drawText("∞", (int) btBala3.getPosicao().x, (int) btBala3.getPosicao().y - 3 + offsetY, paintTexto);
                break;
            case ESCOLHER_ANGULO:
                for (Canhao canhao : canhoes) {
                    canhao.draw(canvas);
                }
                chao.draw(canvas);
                canvas.drawText(canhoes.get(vez).getNome(), drawWidth / 2, posNome, paintTextoNome);
                canvas.drawText("Ajuste o canhão", drawWidth / 2, posDica, paintTexto);
                btPronto.draw(canvas);
                break;
            case ATIRAR:
                for (Canhao canhao : canhoes) {
                    canhao.draw(canvas);
                }
                chao.draw(canvas);
                canvas.drawText(canhoes.get(vez).getNome(), drawWidth / 2, posNome, paintTextoNome);
                canvas.drawText("Toque, segure, e solte para atirar!", drawWidth / 2, posDica, paintTexto);
                if (pressionando) {
                    indicadorForca.draw(canvas);
                }
                break;
            case FISICA:
                if (balas.size() > 0) {
                    for (int i = balas.size() - 1; i >= 0; i--) {
                        Bala bala = balas.get(i);
                        bala.draw(canvas);
                    }
                }
                for (Canhao canhao : canhoes) {
                    canhao.draw(canvas);
                }
                chao.draw(canvas);
                if (explosoes.size() > 0) {
                    for (int i = explosoes.size() - 1; i >= 0; i--) {
                        Explosao explosao = explosoes.get(i);
                        explosao.draw(canvas);
                    }
                }
                break;
            case FIM:
                for (Canhao canhao : canhoes) {
                    canhao.draw(canvas);
                }
                chao.draw(canvas);
                Canhao vencedor;
                if (canhoes.get(0).getVida() > 0)
                    vencedor = canhoes.get(0);
                else
                    vencedor = canhoes.get(1);
                paintTextoNomeVencedor.setColor(vencedor.getCor());
                canvas.drawText(vencedor.getNome() + " ganhou!", drawWidth / 2, drawHeight / 2, paintTextoNomeVencedor);
                break;
        }
    }

    public void drawBoundingBoxes(Canvas canvas) {
        for (Canhao canhao : canhoes) {
            canhao.drawBounding(canvas);
        }
        chao.drawBoundingBox(canvas);

        switch (estado) {
            case ESCOLHER_BALA:
                if (canhoes.get(vez).getQuantBala1() > 0)
                    btBala1.drawBoundingBox(canvas);
                if (canhoes.get(vez).getQuantBala2() > 0)
                    btBala2.drawBoundingBox(canvas);
                btBala3.drawBoundingBox(canvas);
                break;
            case ESCOLHER_ANGULO:
                btPronto.drawBoundingBox(canvas);
                break;
            case ATIRAR:
                break;
            case FISICA:
                if (balas.size() > 0) {
                    for (int i = balas.size() - 1; i >= 0; i--) {
                        Bala bala = balas.get(i);
                        bala.drawBounding(canvas);
                    }
                }
                break;
            case FIM:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (estado) {
            case ESCOLHER_BALA:
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (btBala1.isHit(converterToque(new Vector2(event.getX(), event.getY()))) && canhoes.get(vez).getQuantBala1() > 0) {
                        tipoSelecionado = Bala.Tipo.Bala1;
                        estado = Estado.ESCOLHER_ANGULO;
                    } else if (btBala2.isHit(converterToque(new Vector2(event.getX(), event.getY()))) && canhoes.get(vez).getQuantBala2() > 0) {
                        tipoSelecionado = Bala.Tipo.Bala2;
                        estado = Estado.ESCOLHER_ANGULO;
                    } else if (btBala3.isHit(converterToque(new Vector2(event.getX(), event.getY())))) {
                        tipoSelecionado = Bala.Tipo.Bala3;
                        estado = Estado.ESCOLHER_ANGULO;
                    }
                }
                break;
            case ESCOLHER_ANGULO:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (btPronto.isHit(converterToque(new Vector2(event.getX(), event.getY())))) {
                            tocandoBtPronto = true;
                            break;
                        }
                    case MotionEvent.ACTION_MOVE:
                        if (!tocandoBtPronto) {
                            Vector2 pos = converterToque(new Vector2(event.getX(), event.getY()));
                            if (vez == 0) {
                                double angulo = Math.toDegrees(Math.atan2(pos.getY() - canhoes.get(0).getPosicao().y, pos.getX() - canhoes.get(0).getPosicao().x));
                                angulo = Math.max(-90, angulo);
                                angulo = Math.min(-30, angulo);
                                canhoes.get(0).setAngulo(Math.toRadians(angulo));
                            } else {
                                double angulo = Math.toDegrees(Math.atan2(canhoes.get(1).getPosicao().y - pos.getY(), canhoes.get(1).getPosicao().x - pos.getX()));
                                angulo = Math.max(30, angulo);
                                angulo = Math.min(90, angulo);
                                canhoes.get(1).setAngulo(Math.toRadians(angulo + 180));
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (tocandoBtPronto && btPronto.isHit(converterToque(new Vector2(event.getX(), event.getY())))) {
                            tocandoBtPronto = false;
                            estado = Estado.ATIRAR;
                        }
                        break;
                }
                break;
            case ATIRAR:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startPressTempo = System.nanoTime();
                        pressionando = true;
                        indicadorForca.posicao = converterToque(new Vector2(event.getX(), event.getY()));
                        indicadorForca.angulo = Math.toRadians(Util.randomFromTo(0, 360));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        indicadorForca.posicao = converterToque(new Vector2(event.getX(), event.getY()));
                        break;
                    case MotionEvent.ACTION_UP:
                        pressionando = false;
                        Log.d(TAG, "Forca: " + forca);
                        balas.add(canhoes.get(vez).fire(tipoSelecionado, forca));
                        soundManager.play(efeitoTiro);
                        estado = Estado.FISICA;
                        break;
                }
                break;
            case FISICA:
                break;
            case FIM:
                if (event.getAction() == MotionEvent.ACTION_UP)
                    finish();
                break;
        }
        return true;
    }


    public Vector2 converterToque(Vector2 pos) {
        double x = pos.getX() * drawWidth / dstCanvas.width();
        double y = pos.getY() * drawHeight / dstCanvas.height();
        return new Vector2(x, y);
    }

    public void trocaVento() {
        vento.set(Util.randomFromTo(-maxVento, maxVento), 0);
        //vento.set(0, 0);
        indicadorVento.update();
        Log.d(TAG, "Vento: " + vento);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundManager.unloadAll();
    }


    public enum Estado {
        ESCOLHER_BALA, ESCOLHER_ANGULO, ATIRAR, FISICA, FIM
    }


    public class Tela extends View {
        float startTime;

        public Tela(Context context) {
            super(context);
            startTime = System.nanoTime();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float deltaTime = (System.nanoTime() - startTime) / 1000000.0f;
            startTime = System.nanoTime();

            update(deltaTime);
            drawGame(drawCanvas);
            if (drawBoundings)
                drawBoundingBoxes(drawCanvas);

            canvas.drawBitmap(drawBitmap, null, dstCanvas, paintFilters);
            invalidate();
        }

    }
}