package com.GlobeDelegates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TelaInicio implements Screen {

    private GlobeDelegates jogo;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    private float btnX, btnY, btnW, btnH;

    public TelaInicio(GlobeDelegates jogo) {
        this.jogo = jogo;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(3f);
        fundo = new Texture("fundoInicio.png");

        btnW = 220;
        btnH = 70;
        btnX = (Gdx.graphics.getWidth() - btnW) / 2;
        btnY = (Gdx.graphics.getHeight() - btnH) / 2;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(fundo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.18f, 0.75f, 0.75f, 1);
        shape.rect(btnX, btnY, btnW, btnH);
        shape.setColor(0.1f, 0.5f, 0.5f, 1);
        shape.rect(btnX, btnY, btnW, 6);
        shape.rect(btnX, btnY + btnH - 6, btnW, 6);
        shape.rect(btnX, btnY, 6, btnH);
        shape.rect(btnX + btnW - 6, btnY, 6, btnH);
        shape.end();

        batch.begin();
        font.setColor(0.05f, 0.05f, 0.15f, 1);
        font.draw(batch, "START", btnX + 38, btnY + btnH - 18);
        batch.end();

        if (Gdx.input.justTouched()) {
            float tx = Gdx.input.getX();
            float ty = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (tx >= btnX && tx <= btnX + btnW && ty >= btnY && ty <= btnY + btnH) {
                jogo.setScreen(new TelaModo(jogo));
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
