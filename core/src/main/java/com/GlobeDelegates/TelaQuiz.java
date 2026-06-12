package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TelaQuiz implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private BitmapFont font;
    private boolean redirecionado = false;

    public TelaQuiz(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!redirecionado) {
            redirecionado = true;
            if (jogador.getPais().equals("Japao") || jogador.getPais().equals("Mexico")) {
                jogo.setScreen(new TelaEscapeJapao(jogo, jogador));
                return;
            }
        }

        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Pais nao implementado ainda", 100, Gdx.graphics.getHeight()/2);
        batch.end();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() { batch.dispose(); font.dispose(); }
}
