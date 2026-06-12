package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;

public class TelaBonusMexico implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    // 4 cores do Dia dos Mortos
    private float[][] cores = {
        {0.9f, 0.4f, 0.0f, 1},  // laranja
        {0.6f, 0.1f, 0.7f, 1},  // roxo
        {0.9f, 0.1f, 0.4f, 1},  // rosa
        {0.9f, 0.8f, 0.0f, 1}   // amarelo
    };
    private String[] nomesCores = {"Laranja", "Roxo", "Rosa", "Amarelo"};

    private ArrayList<Integer> sequencia = new ArrayList<>();
    private ArrayList<Integer> inputJogador = new ArrayList<>();

    private enum Estado { MOSTRANDO, AGUARDANDO, ACERTOU, ERROU, VITORIA }
    private Estado estado = Estado.MOSTRANDO;

    private int rodada = 1;
    private int maxRodadas = 6;
    private int indiceExibicao = 0;
    private float tempoExibicao = 0;
    private float tempoEspera = 0;
    private int btnIluminado = -1;

    private float btnSize = 150;
    private float[][] btnPos;

    public TelaBonusMexico(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(2f);
        fundo = new Texture("mexico/bonus.jpg");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        btnPos = new float[][]{
            {w/2 - btnSize - 20, h/2 + 20},
            {w/2 + 20, h/2 + 20},
            {w/2 - btnSize - 20, h/2 - btnSize - 20},
            {w/2 + 20, h/2 - btnSize - 20}
        };

        adicionarSequencia();
    }

    private void adicionarSequencia() {
        sequencia.add((int)(Math.random() * 4));
        indiceExibicao = 0;
        tempoExibicao = 0;
        tempoEspera = 1.0f;
        estado = Estado.MOSTRANDO;
        inputJogador.clear();
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

        // painel escuro
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.5f);
        shape.rect(0, 0, w, h);
        shape.end();

        // titulo
        batch.begin();
        font.setColor(1, 0.6f, 0.1f, 1);
        font.draw(batch, "Simon Says - Dia dos Mortos", w/2 - 280, h - 40);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Rodada " + rodada + "/" + maxRodadas, w/2 - 80, h - 75);
        batch.end();

        // espera antes de mostrar
        if (tempoEspera > 0) {
            tempoEspera -= delta;
            renderBotoes(-1);
            return;
        }

        if (estado == Estado.MOSTRANDO) {
            tempoExibicao += delta;
            if (tempoExibicao < 0.6f) {
                renderBotoes(sequencia.get(indiceExibicao));
            } else if (tempoExibicao < 0.9f) {
                renderBotoes(-1);
            } else {
                tempoExibicao = 0;
                indiceExibicao++;
                if (indiceExibicao >= sequencia.size()) {
                    estado = Estado.AGUARDANDO;
                    btnIluminado = -1;
                }
            }
        } else if (estado == Estado.AGUARDANDO) {
            renderBotoes(btnIluminado);

            batch.begin();
            font.setColor(0.5f, 1f, 0.5f, 1);
            font.draw(batch, "Repita a sequencia!", w/2 - 160, 80);
            batch.end();

            if (Gdx.input.justTouched()) {
                float tx = Gdx.input.getX();
                float ty = h - Gdx.input.getY();
                for (int i = 0; i < 4; i++) {
                    if (tx >= btnPos[i][0] && tx <= btnPos[i][0] + btnSize &&
                        ty >= btnPos[i][1] && ty <= btnPos[i][1] + btnSize) {
                        btnIluminado = i;
                        tempoEspera = 0.3f;
                        inputJogador.add(i);

                        int pos = inputJogador.size() - 1;
                        if (inputJogador.get(pos) != sequencia.get(pos)) {
                            estado = Estado.ERROU;
                            tempoEspera = 2f;
                        } else if (inputJogador.size() == sequencia.size()) {
                            if (rodada >= maxRodadas) {
                                estado = Estado.VITORIA;
                            } else {
                                estado = Estado.ACERTOU;
                                tempoEspera = 1f;
                            }
                        }
                    }
                }
            }
        } else if (estado == Estado.ACERTOU) {
            renderBotoes(-1);
            batch.begin();
            font.setColor(0.2f, 1f, 0.2f, 1);
            font.draw(batch, "Correto! Proxima rodada...", w/2 - 220, h/2);
            batch.end();
            tempoEspera -= delta;
            if (tempoEspera <= 0) {
                rodada++;
                adicionarSequencia();
            }
        } else if (estado == Estado.ERROU) {
            renderBotoes(-1);
            batch.begin();
            font.setColor(1f, 0.2f, 0.2f, 1);
            font.draw(batch, "Errou! Tente novamente!", w/2 - 200, h/2 + 30);
            font.setColor(1, 1, 1, 1);
            font.draw(batch, "Toque para recomecar", w/2 - 180, h/2 - 30);
            batch.end();
            if (Gdx.input.justTouched()) {
                sequencia.clear();
                rodada = 1;
                adicionarSequencia();
            }
        } else if (estado == Estado.VITORIA) {
            renderBotoes(-1);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(0.1f, 0.4f, 0.1f, 0.9f);
            shape.rect(w/2 - 300, h/2 - 80, 600, 160);
            shape.end();
            batch.begin();
            font.setColor(1f, 0.8f, 0.1f, 1);
            font.draw(batch, "Parabens! Bonus completo!", w/2 - 250, h/2 + 40);
            font.setColor(1, 1, 1, 1);
            font.draw(batch, "Toque para voltar", w/2 - 140, h/2 - 20);
            batch.end();
            if (Gdx.input.justTouched()) {
                jogo.setScreen(new TelaVilagemMexico(jogo, jogador, true, true));
            }
        }
    }

    private void renderBotoes(int iluminado) {
        for (int i = 0; i < 4; i++) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            float[] cor = cores[i];
            float brilho = (iluminado == i) ? 1.0f : 0.4f;
            shape.setColor(cor[0] * brilho, cor[1] * brilho, cor[2] * brilho, 1);
            shape.rect(btnPos[i][0], btnPos[i][1], btnSize, btnSize);
            shape.end();

            batch.begin();
            font.setColor(1, 1, 1, 0.8f);
            font.draw(batch, nomesCores[i], btnPos[i][0] + 10, btnPos[i][1] + btnSize/2 + 10);
            batch.end();
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
