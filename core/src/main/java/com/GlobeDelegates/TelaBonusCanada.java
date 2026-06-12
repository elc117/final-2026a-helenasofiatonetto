package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;

public class TelaBonusCanada implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    private float barcoX, barcoY;
    private float barcoW = 60, barcoH = 40;
    private float barcoVX = 300;

    private ArrayList<float[]> pedras = new ArrayList<>();
    private float tempoSpawnPedra = 0;
    private float pedraW = 40, pedraH = 40;
    private float velocidadePedras = 200;

    private boolean gameOver = false;
    private boolean vitoria = false;
    private float tempoJogo = 0;
    private float tempoTotal = 30f;
    private int vidas = 3;

    public TelaBonusCanada(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(2f);
        fundo = new Texture("canada/bonus.jpg");

        float h = Gdx.graphics.getHeight();
        barcoX = 100;
        barcoY = h / 2;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.5f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        batch.draw(fundo, 0, 0, w, h);
        batch.end();

        if (gameOver) { renderGameOver(w, h); return; }
        if (vitoria) { renderVitoria(w, h); return; }

        tempoJogo += delta;
        if (tempoJogo >= tempoTotal) { vitoria = true; return; }

        // Mover barco
        if (Gdx.input.isKeyPressed(Keys.UP))   barcoY += barcoVX * delta;
        if (Gdx.input.isKeyPressed(Keys.DOWN)) barcoY -= barcoVX * delta;
        barcoY = Math.max(0, Math.min(barcoY, h - barcoH));

        // Spawnar pedras
        tempoSpawnPedra += delta;
        float intervalo = Math.max(0.5f, 1.5f - tempoJogo * 0.02f);
        if (tempoSpawnPedra >= intervalo) {
            tempoSpawnPedra = 0;
            float py = (float)(Math.random() * (h - pedraH));
            pedras.add(new float[]{w, py});
        }

        // Mover pedras
        for (int i = pedras.size() - 1; i >= 0; i--) {
            pedras.get(i)[0] -= (velocidadePedras + tempoJogo * 3) * delta;
            if (pedras.get(i)[0] < -pedraW) {
                pedras.remove(i);
            }
        }

        // Colisão
        for (int i = pedras.size() - 1; i >= 0; i--) {
            float[] p = pedras.get(i);
            if (barcoX < p[0] + pedraW && barcoX + barcoW > p[0] &&
                barcoY < p[1] + pedraH && barcoY + barcoH > p[1]) {
                pedras.remove(i);
                vidas--;
                if (vidas <= 0) gameOver = true;
            }
        }

        // Desenhar pedras
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.4f, 0.3f, 0.2f, 1);
        for (float[] p : pedras) {
            shape.rect(p[0], p[1], pedraW, pedraH);
        }
        shape.end();

        // Desenhar barco
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.6f, 0.3f, 0.1f, 1);
        shape.rect(barcoX, barcoY, barcoW, barcoH);
        shape.setColor(1f, 0.9f, 0.1f, 1);
        shape.triangle(barcoX + barcoW, barcoY + barcoH/2,
                       barcoX + barcoW + 30, barcoY + barcoH,
                       barcoX + barcoW + 30, barcoY);
        shape.end();

        // HUD
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.6f);
        shape.rect(0, h - 50, w, 50);
        shape.end();
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Vidas: " + vidas, 20, h - 15);
        font.draw(batch, "Tempo: " + (int)(tempoTotal - tempoJogo) + "s", w/2 - 60, h - 15);
        font.draw(batch, "Use UP/DOWN para desviar!", w - 400, h - 15);
        batch.end();
    }

    private void renderGameOver(float w, float h) {
        batch.begin();
        batch.draw(fundo, 0, 0, w, h);
        batch.end();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.5f, 0, 0, 0.85f);
        shape.rect(w/2 - 300, h/2 - 80, 600, 160);
        shape.end();
        batch.begin();
        font.setColor(1, 0.3f, 0.3f, 1);
        font.draw(batch, "Game Over!", w/2 - 120, h/2 + 40);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Toque para tentar novamente", w/2 - 240, h/2 - 20);
        batch.end();
        if (Gdx.input.justTouched()) {
            jogo.setScreen(new TelaBonusCanada(jogo, jogador));
        }
    }

    private void renderVitoria(float w, float h) {
        batch.begin();
        batch.draw(fundo, 0, 0, w, h);
        batch.end();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0.4f, 0, 0.85f);
        shape.rect(w/2 - 300, h/2 - 80, 600, 160);
        shape.end();
        batch.begin();
        font.setColor(0.3f, 1f, 0.3f, 1);
        font.draw(batch, "Parabens! Voce chegou!", w/2 - 220, h/2 + 40);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Toque para voltar", w/2 - 150, h/2 - 20);
        batch.end();
        if (Gdx.input.justTouched()) {
            jogo.setScreen(new TelaVilagemCanada(jogo, jogador, true, true));
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        font.dispose();
        fundo.dispose();
    }
}
