package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TelaVilagemJapao implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    private float persX, persY;
    private float persSize = 40;
    private float persVX = 200;

    private float[] casinhaX = {150, 450, 750};
    private float[] casinhaY;
    private float casinhaSize = 80;
    private String[] labels = {"Escape Room", "Delegacao", "Bonus"};
    private boolean[] desbloqueado;

    public TelaVilagemJapao(GlobeDelegates jogo, Jogador jogador, boolean escapeCompleto, boolean delegacaoCompleta) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.5f);
        fundo = new Texture("japao/japao_casinha.png");

        float h = Gdx.graphics.getHeight();
        casinhaY = new float[]{h * 0.4f, h * 0.4f, h * 0.4f};

        desbloqueado = new boolean[]{true, escapeCompleto, escapeCompleto && delegacaoCompleta};

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
            shape.setColor(desbloqueado[i] ? 0.2f : 0.5f, desbloqueado[i] ? 0.7f : 0.5f, 0.2f, 0.7f);
            shape.rect(casinhaX[i] - 10, casinhaY[i] - 10, casinhaSize + 20, casinhaSize + 20);
            shape.end();
            batch.begin();
            font.setColor(1, 1, 1, 1);
            font.draw(batch, labels[i], casinhaX[i], casinhaY[i] + casinhaSize + 25);
            if (!desbloqueado[i]) {
                font.setColor(1, 0.5f, 0.5f, 1);
                font.draw(batch, "(bloqueado)", casinhaX[i], casinhaY[i] + casinhaSize + 5);
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
            float dist = Math.abs(persX - casinhaX[i]);
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
                    if (i == 0) jogo.setScreen(new TelaEscapeJapao(jogo, jogador));
                    if (i == 1) jogo.setScreen(new TelaDelegacaoJapao(jogo, jogador));
                    if (i == 2) jogo.setScreen(new TelaBonusJapao(jogo, jogador));
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
