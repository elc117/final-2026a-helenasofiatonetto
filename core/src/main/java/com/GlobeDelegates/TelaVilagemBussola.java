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
    private float persVX = 250;

    private float[] entradaX = {200, 550, 900};
    private float[] entradaY;
    private float entradaSize = 70;
    private String[] labels = {"China", "Peru", "Grecia"};
    private boolean[] concluido;

    public TelaVilagemBussola(GlobeDelegates jogo, Jogador jogador, boolean chinaConcluida, boolean peruConcluido, boolean greciaConclui) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.8f);
        fundo = new Texture("bussola/telaEscolha.jpg");

        float h = Gdx.graphics.getHeight();
        entradaY = new float[]{h * 0.4f, h * 0.4f, h * 0.4f};
        concluido = new boolean[]{chinaConcluida, peruConcluido, greciaConclui};
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
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            jogo.setScreen(new TelaEscolha(jogo));
            return;
        }

        if (Gdx.input.isKeyPressed(Keys.LEFT))  persX -= persVX * delta;
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) persX += persVX * delta;
        persX = Math.max(0, Math.min(persX, w - persSize));

        // Personagem
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.2f, 0.4f, 0.9f, 1);
        shape.rect(persX, persY, persSize, persSize * 1.5f);
        shape.setColor(1f, 0.85f, 0.7f, 1);
        shape.circle(persX + persSize/2, persY + persSize * 1.7f, persSize/2);
        shape.end();

        // Entradas
        for (int i = 0; i < 3; i++) {
            float dist = Math.abs(persX - entradaX[i]);
            if (dist < 80 && !concluido[i]) {
                shape.begin(ShapeRenderer.ShapeType.Line);
                shape.setColor(1f, 1f, 0.3f, 1);
                shape.rect(entradaX[i] - entradaSize/2, entradaY[i] - entradaSize/2, entradaSize, entradaSize);
                shape.rect(entradaX[i] - entradaSize/2 + 3, entradaY[i] - entradaSize/2 + 3, entradaSize - 6, entradaSize - 6);
                shape.end();

                if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
                    if (i == 0) jogo.setScreen(new TelaBonusBussola(jogo, jogador, "China"));
                    else if (i == 1) jogo.setScreen(new TelaBonusBussola(jogo, jogador, "Peru"));
                    else jogo.setScreen(new TelaBonusBussola(jogo, jogador, "Grecia"));
                }
            }
        }

        // Verificar se todas concluidas
        if (concluido[0] && concluido[1] && concluido[2]) {
            jogo.setScreen(new TelaFimPais(jogo, jogador));
        }
    }

    @Override public void show() {}
    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        batch.dispose(); shape.dispose(); font.dispose(); fundo.dispose();
    }
}
