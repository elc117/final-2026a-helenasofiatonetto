package com.GlobeDelegates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;

public class BonusCanada implements BonusAtividade {
    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;
    private boolean concluido = false;

    private float barrilX, barrilY;
    private float barrilW = 50, barrilH = 60;
    private float barrilVX = 300;

    private ArrayList<float[]> pedras = new ArrayList<>();
    private float tempoSpawn = 0;
    private float pedraW = 45, pedraH = 45;

    private boolean gameOver = false;
    private float tempoJogo = 0;
    private float tempoTotal = 30f;
    private int vidas = 3;

    public BonusCanada(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(2f);
        fundo = new Texture("canada/bonus.jpg");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        barrilX = w / 2 - barrilW / 2;
        barrilY = h * 0.15f;
    }

    @Override
    public void update(float delta) {
        if (gameOver || concluido) return;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        tempoJogo += delta;
        if (tempoJogo >= tempoTotal) { concluido = true; return; }

        if (Gdx.input.isKeyPressed(Keys.LEFT))  barrilX -= barrilVX * delta;
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) barrilX += barrilVX * delta;
        barrilX = Math.max(0, Math.min(barrilX, w - barrilW));

        tempoSpawn += delta;
        float intervalo = Math.max(0.4f, 1.2f - tempoJogo * 0.02f);
        if (tempoSpawn >= intervalo) {
            tempoSpawn = 0;
            // Pedras caem de cima (vertical)
            float px = (float)(Math.random() * (w - pedraW));
            pedras.add(new float[]{px, h + pedraH, (float)(Math.random() * 150 + 150)});
        }

        for (int i = pedras.size() - 1; i >= 0; i--) {
            float[] p = pedras.get(i);
            p[1] -= p[2] * delta; // cai para baixo
            if (p[1] < -pedraH) { pedras.remove(i); continue; }
            // Colisão
            if (barrilX < p[0] + pedraW && barrilX + barrilW > p[0] &&
                barrilY < p[1] + pedraH && barrilY + barrilH > p[1]) {
                pedras.remove(i);
                vidas--;
                if (vidas <= 0) gameOver = true;
            }
        }
    }

    @Override
    public void render() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        batch.end();

        if (gameOver) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(0.5f, 0, 0, 0.85f);
            shape.rect(w/2 - 300, h/2 - 80, 600, 160);
            shape.end();
            batch.begin();
            font.setColor(1, 0.3f, 0.3f, 1);
            font.draw(batch, "Game Over!", w/2 - 120, h/2 + 40);
            font.setColor(1, 1, 1, 1);
            font.draw(batch, "ESC para sair", w/2 - 120, h/2 - 20);
            batch.end();
            return;
        }

        // Pedras
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.4f, 0.3f, 0.2f, 1);
        for (float[] p : pedras) shape.rect(p[0], p[1], pedraW, pedraH);
        shape.end();

        // Barril
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.5f, 0.3f, 0.1f, 1);
        shape.rect(barrilX, barrilY, barrilW, barrilH);
        // Aros do barril
        shape.setColor(0.3f, 0.2f, 0.05f, 1);
        shape.rect(barrilX, barrilY + barrilH * 0.3f, barrilW, 5);
        shape.rect(barrilX, barrilY + barrilH * 0.65f, barrilW, 5);
        shape.end();

        // HUD
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.7f);
        shape.rect(0, h - 50, w, 50);
        shape.end();
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Vidas: " + vidas + " | Tempo: " + (int)(tempoTotal - tempoJogo) + "s | Setas=mover", 20, h - 15);
        batch.end();
    }

    @Override public boolean isConcluido() { return concluido; }
    @Override public void dispose() { batch.dispose(); shape.dispose(); font.dispose(); fundo.dispose(); }
}
