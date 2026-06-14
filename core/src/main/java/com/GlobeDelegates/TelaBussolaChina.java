package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;

public class TelaBussolaChina implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    // Lanternas — sequência para acender
    private int totalLanternas = 8;
    private float[] lanternaX, lanternaY;
    private boolean[] acesa;
    private int[] sequenciaCorreta;
    private ArrayList<Integer> sequenciaJogador = new ArrayList<>();
    private boolean mostrando = true;
    private int indiceExibicao = 0;
    private float tempoExibicao = 0;
    private float tempoEspera = 1.5f;
    private boolean concluido = false;
    private boolean errou = false;
    private float tempoMensagem = 0;

    public TelaBussolaChina(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.8f);
        fundo = new Texture("bussola/china.jpg");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        lanternaX = new float[totalLanternas];
        lanternaY = new float[totalLanternas];
        acesa = new boolean[totalLanternas];
        sequenciaCorreta = new int[totalLanternas];

        // Posicionar lanternas em arco
        for (int i = 0; i < totalLanternas; i++) {
            float angle = (float)(i * Math.PI / (totalLanternas - 1));
            lanternaX[i] = w/2 + (w * 0.35f) * (float)Math.cos(angle);
            lanternaY[i] = h * 0.5f + (h * 0.2f) * (float)Math.sin(angle);
        }

        // Gerar sequência aleatória
        ArrayList<Integer> idx = new ArrayList<>();
        for (int i = 0; i < totalLanternas; i++) idx.add(i);
        java.util.Collections.shuffle(idx);
        for (int i = 0; i < totalLanternas; i++) sequenciaCorreta[i] = idx.get(i);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        batch.draw(fundo, 0, 0, w, h);
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.5f);
        shape.rect(0, 0, w, h);
        shape.end();

        if (concluido) { renderConclusao(w, h); return; }

        // HUD
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.8f);
        shape.rect(0, h - 55, w, 55);
        shape.end();
        batch.begin();
        font.setColor(0.9f, 0.2f, 0.2f, 1);
        font.draw(batch, "China - Muralha das Lanternas", w/2 - 250, h - 15);
        batch.end();

        if (tempoEspera > 0) {
            tempoEspera -= delta;
            batch.begin();
            font.setColor(1, 0.8f, 0.2f, 1);
            font.draw(batch, mostrando ? "Memorize a sequencia!" : "Agora repita!", w/2 - 200, h/2);
            batch.end();
            desenharLanternas(w, h, -1);
            return;
        }

        if (mostrando) {
            tempoExibicao += delta;
            int lanternaAtual = sequenciaCorreta[indiceExibicao];

            if (tempoExibicao < 0.7f) {
                desenharLanternas(w, h, lanternaAtual);
            } else if (tempoExibicao < 1.0f) {
                desenharLanternas(w, h, -1);
            } else {
                tempoExibicao = 0;
                indiceExibicao++;
                if (indiceExibicao >= totalLanternas) {
                    mostrando = false;
                    tempoEspera = 1f;
                }
            }
        } else {
            desenharLanternas(w, h, -1);

            if (tempoMensagem > 0) {
                tempoMensagem -= delta;
                batch.begin();
                font.setColor(errou ? 1f : 0.2f, errou ? 0.2f : 1f, 0.2f, 1);
                font.draw(batch, errou ? "Errado! Tente novamente!" : "Correto!", w/2 - 160, h * 0.2f);
                batch.end();
                if (tempoMensagem <= 0 && errou) {
                    sequenciaJogador.clear();
                    for (boolean b : acesa) b = false;
                    java.util.Arrays.fill(acesa, false);
                }
                return;
            }

            batch.begin();
            font.setColor(1, 0.8f, 0.2f, 1);
            font.draw(batch, "Clique nas lanternas na ordem correta! (" + sequenciaJogador.size() + "/" + totalLanternas + ")", w/2 - 340, h * 0.15f);
            batch.end();

            if (Gdx.input.justTouched()) {
                float tx = Gdx.input.getX();
                float ty = h - Gdx.input.getY();
                for (int i = 0; i < totalLanternas; i++) {
                    if (!acesa[i] && Math.abs(tx - lanternaX[i]) < 35 && Math.abs(ty - lanternaY[i]) < 35) {
                        int pos = sequenciaJogador.size();
                        if (sequenciaCorreta[pos] == i) {
                            acesa[i] = true;
                            sequenciaJogador.add(i);
                            if (sequenciaJogador.size() == totalLanternas) concluido = true;
                        } else {
                            errou = true;
                            tempoMensagem = 1.5f;
                            java.util.Arrays.fill(acesa, false);
                            sequenciaJogador.clear();
                        }
                        break;
                    }
                }
            }
        }
    }

    private void desenharLanternas(float w, float h, int iluminada) {
        for (int i = 0; i < totalLanternas; i++) {
            boolean brilhando = acesa[i] || i == iluminada;
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(brilhando ? 0.9f : 0.3f, brilhando ? 0.1f : 0.1f, 0.1f, 1);
            shape.rect(lanternaX[i] - 20, lanternaY[i] - 30, 40, 60);
            if (brilhando) {
                shape.setColor(1f, 0.8f, 0.1f, 0.6f);
                shape.circle(lanternaX[i], lanternaY[i], 50);
            }
            shape.end();
            batch.begin();
            font.setColor(1, 1, 1, 0.8f);
            font.draw(batch, String.valueOf(i+1), lanternaX[i] - 8, lanternaY[i] + 10);
            batch.end();
        }
    }

    private void renderConclusao(float w, float h) {
        desenharLanternas(w, h, -1);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.5f, 0f, 0f, 0.88f);
        shape.rect(w/2 - 300, h/2 - 80, 600, 160);
        shape.end();
        batch.begin();
        font.setColor(1f, 0.8f, 0.1f, 1);
        font.draw(batch, "Muralha iluminada!", w/2 - 180, h/2 + 40);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Toque para continuar", w/2 - 170, h/2 - 20);
        batch.end();
        if (Gdx.input.justTouched()) {
            jogo.setScreen(new TelaVilagemBussola(jogo, jogador, true, false, false));
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
