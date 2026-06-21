package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TelaFimPais implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture fundo;
    private float tempo = 0;

    public TelaFimPais(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);
        fundo = new Texture("mundiJogo.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        batch.end();

        batch.begin();
        ImagemUtil.desenharPergaminho(batch, w/2 - 350, h/2 - 150, 700, 300);
        batch.end();

        batch.begin();
        font.setColor(0.3f, 0.15f, 0.0f, 1);
        font.draw(batch, "Parabens!", w/2 - 100, h/2 + 90);
        font.draw(batch, jogador.getPais() + " concluido!", w/2 - 180, h/2 + 40);
        font.setColor(0.4f, 0.2f, 0.0f, 1);
        font.draw(batch, "ENTER para voltar ao mapa", w/2 - 230, h/2 - 30);
        batch.end();

        tempo += delta;
        if (Gdx.input.isKeyJustPressed(Keys.ENTER) || (Gdx.input.justTouched() && tempo > 1f)) {
            jogo.setScreen(new TelaMundo(jogo, jogador));
        }
    }

    @Override public void show() {}
    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        batch.dispose(); font.dispose(); fundo.dispose();
    }
}
