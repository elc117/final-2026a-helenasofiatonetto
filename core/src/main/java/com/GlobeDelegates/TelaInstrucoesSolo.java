package com.GlobeDelegates;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TelaInstrucoesSolo implements Screen {

    private GlobeDelegates jogo;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shape;

    private float btnX, btnY;
    private final float btnW = 250;
    private final float btnH = 70;

    public TelaInstrucoesSolo(GlobeDelegates jogo) {
        this.jogo = jogo;

        batch = new SpriteBatch();
        font = new BitmapFont();
        shape = new ShapeRenderer();

        font.getData().setScale(2f);

        btnX = Gdx.graphics.getWidth()/2 - btnW/2;
        btnY = 80;
    }

    @Override
    public void render(float delta) {

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        font.draw(batch, "INSTRUÇÕES SOLO", w/2 - 180, h - 100);

        font.draw(batch, "Se aventure em alguns dos lugares que participam da delegação da ONU!", 100, h - 200);
        font.draw(batch, "1. Escolha um dos ícones para visitar uma Delegação", 100, h - 260);
        font.draw(batch, "2. ScapeRoom:Colete os objetos e digite a senha para avançar de fase", 100, h - 320);
        font.draw(batch, "3. Quizz: acerte pelo menos 3 perguntas sobre o país", 100, h - 380);
        font.draw(batch, "4. Bônus: uma fase diferente em cada Delegação! Complete-a e concluirá a Delegação", 100, h - 440);

        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.GREEN);
        shape.rect(btnX, btnY, btnW, btnH);
        shape.end();

        batch.begin();
        font.draw(batch, "CONTINUAR", btnX + 30, btnY + 50);
        batch.end();

        if (Gdx.input.justTouched()) {

            float tx = Gdx.input.getX();
            float ty = h - Gdx.input.getY();

            if (tx >= btnX && tx <= btnX + btnW &&
                ty >= btnY && ty <= btnY + btnH) {

                jogo.setScreen(new TelaEscolha(jogo));
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
        font.dispose();
        shape.dispose();
    }
}
