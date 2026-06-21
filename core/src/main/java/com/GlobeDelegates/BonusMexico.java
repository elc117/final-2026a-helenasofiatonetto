package com.GlobeDelegates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;

public class BonusMexico implements BonusAtividade {
    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;
    private boolean concluido = false;

    private float[][] cores = {
        {0.9f, 0.4f, 0.0f, 1}, {0.6f, 0.1f, 0.7f, 1},
        {0.9f, 0.1f, 0.4f, 1}, {0.9f, 0.8f, 0.0f, 1}
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

    public BonusMexico(GlobeDelegates jogo, Jogador jogador) {
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
    public void update(float delta) {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        if (tempoEspera > 0) { tempoEspera -= delta; return; }

        if (estado == Estado.MOSTRANDO) {
            tempoExibicao += delta;
            if (tempoExibicao >= 0.9f) {
                tempoExibicao = 0;
                indiceExibicao++;
                if (indiceExibicao >= sequencia.size()) estado = Estado.AGUARDANDO;
            }
        } else if (estado == Estado.AGUARDANDO) {
            if (Gdx.input.justTouched()) {
                float tx = Gdx.input.getX();
                float ty = h - Gdx.input.getY();
                for (int i = 0; i < 4; i++) {
                    if (tx >= btnPos[i][0] && tx <= btnPos[i][0] + btnSize &&
                        ty >= btnPos[i][1] && ty <= btnPos[i][1] + btnSize) {
                        btnIluminado = i;
                        inputJogador.add(i);
                        int pos = inputJogador.size() - 1;
                        if (inputJogador.get(pos) != sequencia.get(pos)) {
                            estado = Estado.ERROU;
                            tempoEspera = 2f;
                        } else if (inputJogador.size() == sequencia.size()) {
                            estado = rodada >= maxRodadas ? Estado.VITORIA : Estado.ACERTOU;
                            tempoEspera = 1f;
                        }
                    }
                }
            }
        } else if (estado == Estado.ACERTOU) {
            tempoEspera -= delta;
            if (tempoEspera <= 0) { rodada++; adicionarSequencia(); }
        } else if (estado == Estado.ERROU) {
            tempoEspera -= delta;
            if (tempoEspera <= 0 && Gdx.input.justTouched()) {
                sequencia.clear(); rodada = 1; adicionarSequencia();
            }
        } else if (estado == Estado.VITORIA) {
            if (Gdx.input.justTouched()) concluido = true;
        }
    }

    @Override
    public void render() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.5f);
        shape.rect(0, 0, w, h);
        shape.end();

        batch.begin();
        font.setColor(1, 0.6f, 0.1f, 1);
        font.draw(batch, "Simon Says - Dia dos Mortos | Rodada " + rodada + "/" + maxRodadas, w/2 - 340, h - 40);
        batch.end();

        if (tempoEspera > 0 && estado != Estado.ACERTOU && estado != Estado.ERROU && estado != Estado.VITORIA) {
            renderBotoes(-1); return;
        }

        if (estado == Estado.MOSTRANDO) {
            int il = tempoExibicao < 0.6f ? sequencia.get(indiceExibicao) : -1;
            renderBotoes(il);
        } else if (estado == Estado.AGUARDANDO) {
            renderBotoes(btnIluminado);
            batch.begin();
            font.setColor(0.5f, 1f, 0.5f, 1);
            font.draw(batch, "Repita a sequencia!", w/2 - 160, 80);
            batch.end();
        } else if (estado == Estado.ACERTOU) {
            renderBotoes(-1);
            batch.begin();
            font.setColor(0.2f, 1f, 0.2f, 1);
            font.draw(batch, "Correto!", w/2 - 80, h/2);
            batch.end();
        } else if (estado == Estado.ERROU) {
            renderBotoes(-1);
            batch.begin();
            font.setColor(1f, 0.2f, 0.2f, 1);
            font.draw(batch, "Errou! Toque para recomecar", w/2 - 240, h/2);
            batch.end();
        } else if (estado == Estado.VITORIA) {
            renderBotoes(-1);
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(0.1f, 0.4f, 0.1f, 0.9f);
            shape.rect(w/2 - 300, h/2 - 80, 600, 160);
            shape.end();
            batch.begin();
            font.setColor(1f, 0.8f, 0.1f, 1);
            font.draw(batch, "Parabens! Bonus completo!", w/2 - 240, h/2 + 40);
            font.setColor(1, 1, 1, 1);
            font.draw(batch, "Toque para continuar", w/2 - 180, h/2 - 20);
            batch.end();
        }
    }

    private void renderBotoes(int iluminado) {
        for (int i = 0; i < 4; i++) {
            float[] cor = cores[i];
            float brilho = (iluminado == i) ? 1.0f : 0.4f;
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(cor[0]*brilho, cor[1]*brilho, cor[2]*brilho, 1);
            shape.rect(btnPos[i][0], btnPos[i][1], btnSize, btnSize);
            shape.end();
            batch.begin();
            font.setColor(1, 1, 1, 0.8f);
            font.draw(batch, nomesCores[i], btnPos[i][0] + 10, btnPos[i][1] + btnSize/2 + 10);
            batch.end();
        }
    }

    @Override public boolean isConcluido() { return concluido; }
    @Override public void dispose() { batch.dispose(); shape.dispose(); font.dispose(); fundo.dispose(); }
}
