package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TelaBonusNZ implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    private int rodadaAtual = 0;
    private int totalRodadas = 5;
    private boolean concluido = false;
    private boolean errou = false;
    private float tempoMensagem = 0;
    private int ilhaSelecionada = -1;

    // Cada rodada: {wakaX%, wakaY%, ilhaCorretaIdx, pista_tipo, pista_direcao}
    // pista_tipo: 0=estrela, 1=vento, 2=corrente
    // pista_direcao: 0=norte, 1=sul, 2=leste, 3=oeste
    private int[][] rodadas = {
        {20, 50, 2, 0, 2},  // waka esquerda, ir para leste, estrela leste
        {50, 80, 0, 1, 0},  // waka baixo, ir para norte, vento norte
        {80, 50, 0, 2, 3},  // waka direita, ir para oeste, corrente oeste
        {50, 20, 3, 0, 1},  // waka cima, ir para sul, estrela sul
        {30, 30, 1, 1, 2},  // waka canto, ir para leste, vento leste
    };

    // 4 ilhas fixas em % da tela
    private float[][] ilhas = {
        {0.5f, 0.85f},  // norte (topo)
        {0.5f, 0.15f},  // sul (baixo)
        {0.85f, 0.5f},  // leste (direita)
        {0.15f, 0.5f},  // oeste (esquerda)
    };
    private String[] nomesIlhas = {"Aotearoa", "Rapa Nui", "Hawaii", "Samoa"};
    private float ilhaSize = 50;

    public TelaBonusNZ(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.5f);
        fundo = new Texture("nz/bonus.jpg");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.1f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Fundo oceano
        batch.begin();
        batch.draw(fundo, 0, 0, w, h);
        batch.end();

        // Overlay azul escuro semi-transparente
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.02f, 0.05f, 0.2f, 0.7f);
        shape.rect(0, 0, w, h);
        shape.end();

        if (concluido) { renderConclusao(w, h); return; }

        int[] r = rodadas[rodadaAtual];
        float wakaX = w * r[0] / 100f;
        float wakaY = h * r[1] / 100f;

        desenharGrade(w, h);
        desenharIlhas(w, h, r[2]);
        desenharWaka(wakaX, wakaY);
        desenharPista(w, h, r[3], r[4]);
        desenharHUD(w, h);

        // Input
        if (Gdx.input.justTouched() && tempoMensagem <= 0) {
            float tx = Gdx.input.getX();
            float ty = h - Gdx.input.getY();
            for (int i = 0; i < ilhas.length; i++) {
                float ix = w * ilhas[i][0];
                float iy = h * ilhas[i][1];
                if (tx >= ix - ilhaSize && tx <= ix + ilhaSize &&
                    ty >= iy - ilhaSize && ty <= iy + ilhaSize) {
                    ilhaSelecionada = i;
                    if (i == r[2]) {
                        errou = false;
                        tempoMensagem = 1.5f;
                    } else {
                        errou = true;
                        tempoMensagem = 1.5f;
                    }
                }
            }
        }

        if (tempoMensagem > 0) {
            tempoMensagem -= delta;
            batch.begin();
            if (!errou) {
                font.setColor(0.2f, 1f, 0.2f, 1);
                font.draw(batch, "Correto! Ka pai!", w/2 - 120, h/2);
            } else {
                font.setColor(1f, 0.2f, 0.2f, 1);
                font.draw(batch, "Errado! Observe as pistas!", w/2 - 200, h/2);
            }
            batch.end();

            if (tempoMensagem <= 0 && !errou) {
                rodadaAtual++;
                ilhaSelecionada = -1;
                if (rodadaAtual >= totalRodadas) concluido = true;
            }
        }
    }

    private void desenharGrade(float w, float h) {
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.2f, 0.4f, 0.6f, 0.3f);
        for (int x = 0; x < w; x += 80) shape.line(x, 0, x, h);
        for (int y = 0; y < h; y += 80) shape.line(0, y, w, y);
        shape.end();

        // Estrelas decorativas
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(1f, 1f, 0.8f, 0.6f);
        float[][] estrelas = {{0.15f,0.9f},{0.5f,0.95f},{0.85f,0.9f},{0.1f,0.7f},
                              {0.9f,0.7f},{0.3f,0.92f},{0.7f,0.93f},{0.2f,0.8f},{0.8f,0.8f}};
        for (float[] e : estrelas) shape.circle(w * e[0], h * e[1], 3);
        shape.end();
    }

    private void desenharIlhas(float w, float h, int correta) {
        for (int i = 0; i < ilhas.length; i++) {
            float ix = w * ilhas[i][0];
            float iy = h * ilhas[i][1];

            shape.begin(ShapeRenderer.ShapeType.Filled);
            if (ilhaSelecionada == i && errou) {
                shape.setColor(0.8f, 0.1f, 0.1f, 0.8f);
            } else if (ilhaSelecionada == i && !errou) {
                shape.setColor(0.1f, 0.8f, 0.1f, 0.8f);
            } else {
                shape.setColor(0.2f, 0.5f, 0.15f, 0.9f);
            }
            shape.ellipse(ix - ilhaSize/2, iy - ilhaSize/3, ilhaSize, ilhaSize * 0.6f);
            shape.setColor(0.8f, 0.7f, 0.4f, 0.7f);
            shape.circle(ix, iy + 5, 8);
            shape.end();

            batch.begin();
            font.setColor(1, 1, 0.8f, 1);
            font.draw(batch, nomesIlhas[i], ix - 40, iy - ilhaSize/2 - 5);
            batch.end();
        }
    }

    private void desenharWaka(float x, float y) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        // Casco da canoa
        shape.setColor(0.5f, 0.3f, 0.1f, 1);
        shape.ellipse(x - 30, y - 12, 60, 24);
        // Vela
        shape.setColor(0.9f, 0.85f, 0.7f, 0.9f);
        shape.triangle(x, y + 8, x - 15, y + 45, x + 15, y + 45);
        // Mastro
        shape.setColor(0.4f, 0.25f, 0.1f, 1);
        shape.rectLine(x, y + 8, x, y + 45, 2);
        shape.end();

        batch.begin();
        font.setColor(0.9f, 0.7f, 0.2f, 1);
        font.draw(batch, "WAKA", x - 25, y - 18);
        batch.end();
    }

    private void desenharPista(float w, float h, int tipo, int direcao) {
        // Painel de pista
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.75f);
        shape.rect(w/2 - 200, 60, 400, 100);
        shape.end();

        String[] tipoNome = {"Estrela", "Vento", "Corrente"};
        String[] dirNome = {"Norte", "Sul", "Leste", "Oeste"};
        float[][] setas = {
            {w/2, 130, w/2, 160},   // norte — seta para cima
            {w/2, 160, w/2, 130},   // sul — seta para baixo
            {w/2 - 20, 145, w/2 + 20, 145},  // leste — seta para direita
            {w/2 + 20, 145, w/2 - 20, 145},  // oeste — seta para esquerda
        };

        shape.begin(ShapeRenderer.ShapeType.Filled);
        if (tipo == 0) shape.setColor(1f, 1f, 0.3f, 1);
        else if (tipo == 1) shape.setColor(0.5f, 0.8f, 1f, 1);
        else shape.setColor(0.2f, 0.5f, 1f, 1);

        // Desenhar seta de direcao
        float[] s = setas[direcao];
        shape.rectLine(s[0], s[1], s[2], s[3], 4);
        if (direcao == 0) { // norte
            shape.triangle(s[2]-8, s[3], s[2]+8, s[3], s[2], s[3]+15);
        } else if (direcao == 1) { // sul
            shape.triangle(s[2]-8, s[3], s[2]+8, s[3], s[2], s[3]-15);
        } else if (direcao == 2) { // leste
            shape.triangle(s[2], s[3]-8, s[2], s[3]+8, s[2]+15, s[3]);
        } else { // oeste
            shape.triangle(s[2], s[3]-8, s[2], s[3]+8, s[2]-15, s[3]);
        }
        shape.end();

        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, tipoNome[tipo] + " aponta para o " + dirNome[direcao], w/2 - 180, 155);
        font.draw(batch, "Para onde deve ir a waka?", w/2 - 160, 120);
        batch.end();
    }

    private void desenharHUD(float w, float h) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.7f);
        shape.rect(0, h - 50, w, 50);
        shape.end();
        batch.begin();
        font.setColor(0.3f, 1f, 0.8f, 1);
        font.draw(batch, "Waka - Rodada " + (rodadaAtual + 1) + "/" + totalRodadas + " | Clique na ilha correta!", 20, h - 15);
        batch.end();
    }

    private void renderConclusao(float w, float h) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.05f, 0.2f, 0.05f, 0.92f);
        shape.rect(w/2 - 300, h/2 - 80, 600, 160);
        shape.end();
        batch.begin();
        font.setColor(0.3f, 1f, 0.3f, 1);
        font.draw(batch, "Navegacao completa! Toitu he whenua!", w/2 - 290, h/2 + 40);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Toque para voltar", w/2 - 140, h/2 - 20);
        batch.end();
        if (Gdx.input.justTouched()) {
            jogo.setScreen(new TelaVilagemNZ(jogo, jogador, true, true));
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
