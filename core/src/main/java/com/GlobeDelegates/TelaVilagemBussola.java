package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TelaVilagemBussola implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    private float persX, persY;
    private float persSize = 40;
    private float persVX = 200;

    private float[] faseX = {200, 500, 800};
    private float[] faseY;
    private float faseSize = 80;
    private String[] labels = {"China - Lanternas", "Peru - Escavacao", "Grecia - Tocha"};
    private boolean[] desbloqueado;

    public TelaVilagemBussola(GlobeDelegates jogo, Jogador jogador, boolean fase1, boolean fase2, boolean fase3) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.5f);
        fundo = new Texture("bussola/telaEscolha.jpg");

        float h = Gdx.graphics.getHeight();
        faseY = new float[]{h * 0.4f, h * 0.4f, h * 0.4f};
        desbloqueado = new boolean[]{true, fase1, fase2};

        persX = 50;
        persY = h * 0.3f;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        batch.draw(fundo, 0, 0, w, h);
        batch.end();

        if (Gdx.input.isKeyPressed(Keys.LEFT))  persX -= persVX * delta;
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) persX += persVX * delta;
        persX = Math.max(0, Math.min(persX, w - persSize));

        for (int i = 0; i < 3; i++) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(desbloqueado[i] ? 0.8f : 0.4f, desbloqueado[i] ? 0.2f : 0.4f, desbloqueado[i] ? 0.1f : 0.4f, 0.8f);
            shape.rect(faseX[i] - 10, faseY[i] - 10, faseSize + 20, faseSize + 20);
            shape.end();
            batch.begin();
            font.setColor(1, 1, 1, 1);
            font.draw(batch, labels[i], faseX[i] - 20, faseY[i] + faseSize + 25);
            if (!desbloqueado[i]) {
                font.setColor(1, 0.5f, 0.5f, 1);
                font.draw(batch, "(bloqueado)", faseX[i], faseY[i] + faseSize + 5);
            }
            batch.end();
        }

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.2f, 0.4f, 0.9f, 1);
        shape.rect(persX, persY, persSize, persSize * 1.5f);
        shape.setColor(1f, 0.85f, 0.7f, 1);
        shape.circle(persX + persSize/2, persY + persSize * 1.7f, persSize/2);
        shape.end();

        for (int i = 0; i < 3; i++) {
            float dist = Math.abs(persX - faseX[i]);
            if (dist < 80 && desbloqueado[i]) {
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(0.18f, 0.75f, 0.75f, 1);
                shape.rect(persX - 10, persY + persSize * 2 + 10, 160, 40);
                shape.end();
                batch.begin();
                font.setColor(0, 0, 0, 1);
                font.draw(batch, "ESPACO: entrar", persX, persY + persSize * 2 + 38);
                batch.end();

                if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
                    if (i == 0) jogo.setScreen(new TelaBussolaChina(jogo, jogador));
                    if (i == 1) jogo.setScreen(new TelaBussolaPeru(jogo, jogador));
                    if (i == 2) jogo.setScreen(new TelaBussolaGrecia(jogo, jogador));
                }
            }
        }

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.1f, 0.2f, 0.85f);
        shape.rect(0, h - 50, w, 50);
        shape.end();
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Use as setas para andar. ESPACO para entrar.", 20, h - 18);
        batch.end();
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
