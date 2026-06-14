package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TelaModo implements Screen {

    private GlobeDelegates jogo;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    private float btnW = 300, btnH = 80;
    private float btnSoloX, btnSoloY;
    private float btnMultiX, btnMultiY;

    public TelaModo(GlobeDelegates jogo) {
        this.jogo = jogo;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(2.5f);
        fundo = new Texture("fundoInicio.png");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        btnSoloX = w/2 - btnW/2;
        btnSoloY = h/2 + 20;
        btnMultiX = w/2 - btnW/2;
        btnMultiY = h/2 - btnH - 20;
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

        // Overlay escuro
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.5f);
        shape.rect(0, 0, w, h);
        shape.end();

        // Titulo
        batch.begin();
        font.setColor(1, 1, 0.3f, 1);
        font.draw(batch, "Como deseja jogar?", w/2 - 240, h * 0.75f);
        batch.end();

        // Botao SOLO
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.5f, 0.1f, 0.9f);
        shape.rect(btnSoloX, btnSoloY, btnW, btnH);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.5f, 1f, 0.5f, 1);
        shape.rect(btnSoloX, btnSoloY, btnW, btnH);
        shape.end();
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "SOLO", btnSoloX + btnW/2 - 55, btnSoloY + btnH - 20);
        batch.end();

        // Botao MULTIPLAYER
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.5f, 0.1f, 0.5f, 0.9f);
        shape.rect(btnMultiX, btnMultiY, btnW, btnH);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(1f, 0.5f, 1f, 1);
        shape.rect(btnMultiX, btnMultiY, btnW, btnH);
        shape.end();
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "MULTIPLAYER", btnMultiX + btnW/2 - 140, btnMultiY + btnH - 20);
        batch.end();

        // Descricao
        batch.begin();
        font.setColor(0.8f, 0.8f, 0.8f, 0.8f);
        font.draw(batch, "2 jogadores, 1 teclado", w/2 - 200, btnMultiY - 30);
        batch.end();

        // Input
        if (Gdx.input.justTouched()) {
            float tx = Gdx.input.getX();
            float ty = h - Gdx.input.getY();

            if (tx >= btnSoloX && tx <= btnSoloX + btnW &&
                ty >= btnSoloY && ty <= btnSoloY + btnH) {
                jogo.setScreen(new TelaEscolha(jogo));
            }
            if (tx >= btnMultiX && tx <= btnMultiX + btnW &&
                ty >= btnMultiY && ty <= btnMultiY + btnH) {
                jogo.setScreen(new TelaCombate(jogo));
            }
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
