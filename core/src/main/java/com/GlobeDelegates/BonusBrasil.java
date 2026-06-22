package com.GlobeDelegates;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BonusBrasil implements BonusAtividade {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private boolean concluido = false;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    // Sacola
    private float sacolaX, sacolaY;
    private float sacolaW = 90, sacolaH = 70;
    private float sacolaVX = 350;

    // Itens caindo
    private ArrayList<float[]> itens = new ArrayList<>();
    private float tempoSpawn = 0;
    private float spawnIntervalo = 1.2f;
    private float itemSize = 55;
    private float itemVelocidade = 180;

    // Itens bons: pastel, caldo de cana, acai, tapioca, coxinha
    // Itens ruins: lixo, pedra
    private String[] nomesItens = {"Pastel", "Caldo", "Acai", "Tapioca", "Coxinha", "Lixo", "Pedra"};
    private boolean[] itemBom = {true, true, true, true, true, false, false};
    private float[][] coresItens = {
        {0.9f, 0.7f, 0.1f, 1},  // pastel - amarelo
        {0.5f, 0.8f, 0.2f, 1},  // caldo - verde
        {0.5f, 0.1f, 0.7f, 1},  // acai - roxo
        {1f, 0.95f, 0.8f, 1},   // tapioca - branco
        {0.8f, 0.3f, 0.1f, 1},  // coxinha - laranja
        {0.3f, 0.3f, 0.3f, 1},  // lixo - cinza
        {0.4f, 0.35f, 0.3f, 1}  // pedra - marrom
    };

    private int pontos = 0;
    private int vidas = 3;
    private float tempoJogo = 0;
    private float tempoTotal = 30f;
    private boolean gameOver = false;
    private boolean vitoria = false;
    private int pontosVitoria = 10;

    public BonusBrasil(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.8f);
        fundo = new Texture("brasil/bonus.jpg");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        sacolaX = w/2 - sacolaW/2;
        sacolaY = h * 0.08f;
    }

    private void spawnItem(float w, float h) {
        int tipo = (int)(Math.random() * nomesItens.length);
        float ix = (float)(Math.random() * (w - itemSize));
        itens.add(new float[]{ix, h + itemSize, tipo});
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0.1f, 0.5f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        batch.end();

        if (gameOver) { renderGameOver(w, h); return; }
        if (vitoria) { renderVitoria(w, h); return; }

        tempoJogo += delta;
        if (tempoJogo >= tempoTotal) { gameOver = true; return; }

        // Mover sacola
        if (Gdx.input.isKeyPressed(Keys.LEFT))  sacolaX -= sacolaVX * delta;
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) sacolaX += sacolaVX * delta;
        sacolaX = Math.max(0, Math.min(sacolaX, w - sacolaW));

        // Spawn itens
        tempoSpawn += delta;
        if (tempoSpawn >= spawnIntervalo) {
            tempoSpawn = 0;
            spawnItem(w, h);
            spawnIntervalo = Math.max(0.5f, spawnIntervalo - 0.02f);
        }

        // Mover e verificar itens
        for (int i = itens.size() - 1; i >= 0; i--) {
            float[] item = itens.get(i);
            item[1] -= (itemVelocidade + tempoJogo * 2) * delta;

            // Colisão com sacola
            if (item[1] <= sacolaY + sacolaH && item[1] >= sacolaY &&
                item[0] + itemSize >= sacolaX && item[0] <= sacolaX + sacolaW) {
                int tipo = (int)item[2];
                if (itemBom[tipo]) {
                    pontos++;
                    if (pontos >= pontosVitoria) vitoria = true;
                } else {
                    vidas--;
                    if (vidas <= 0) gameOver = true;
                }
                itens.remove(i);
                continue;
            }

            // Saiu da tela
            if (item[1] < -itemSize) {
                itens.remove(i);
            }
        }

        // Desenhar itens
        for (float[] item : itens) {
            int tipo = (int)item[2];
            float[] cor = coresItens[tipo];
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(cor[0], cor[1], cor[2], cor[3]);
            shape.circle(item[0] + itemSize/2, item[1] + itemSize/2, itemSize/2);
            shape.end();
            batch.begin();
            font.setColor(0, 0, 0, 1);
            font.draw(batch, nomesItens[tipo], item[0] + 2, item[1] + itemSize - 5);
            batch.end();
        }

        // Desenhar sacola
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.6f, 0.4f, 0.1f, 1);
        shape.rect(sacolaX, sacolaY, sacolaW, sacolaH);
        shape.setColor(0.4f, 0.25f, 0.05f, 1);
        shape.rect(sacolaX + sacolaW/2 - 5, sacolaY + sacolaH, 10, 20);
        shape.end();
        batch.begin();
        font.setColor(1, 1, 0.5f, 1);
        font.draw(batch, "[]", sacolaX + 20, sacolaY + sacolaH - 10);
        batch.end();

        // HUD
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.7f);
        shape.rect(0, h - 50, w, 50);
        shape.end();
        batch.begin();
        font.setColor(0.3f, 1f, 0.3f, 1);
        font.draw(batch, "Pontos: " + pontos + "/" + pontosVitoria, 20, h - 15);
        font.draw(batch, "Vidas: " + vidas, w/2 - 60, h - 15);
        font.draw(batch, "Tempo: " + (int)(tempoTotal - tempoJogo) + "s", w - 180, h - 15);
        batch.end();
    }

    private void renderGameOver(float w, float h) {
        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        batch.end();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.5f, 0, 0, 0.85f);
        shape.rect(w/2 - 300, h/2 - 80, 600, 160);
        shape.end();
        batch.begin();
        font.setColor(1, 0.3f, 0.3f, 1);
        font.draw(batch, "Acabou o tempo! Pontos: " + pontos, w/2 - 240, h/2 + 40);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Toque para tentar novamente", w/2 - 230, h/2 - 20);
        batch.end();
        if (Gdx.input.justTouched()) {
            jogo.setScreen(new TelaBonus(jogo, jogador));
        }
    }

    private void renderVitoria(float w, float h) {
        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        batch.end();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0.4f, 0, 0.85f);
        shape.rect(w/2 - 300, h/2 - 80, 600, 160);
        shape.end();
        batch.begin();
        font.setColor(0.3f, 1f, 0.3f, 1);
        font.draw(batch, "Feira completa! " + pontos + " itens!", w/2 - 230, h/2 + 40);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Toque para voltar", w/2 - 140, h/2 - 20);
        batch.end();
        if (Gdx.input.justTouched()) {
            concluido = true;
        }
    }

    public boolean isConcluido() { return concluido; }

    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        font.dispose();
        fundo.dispose();
    }
}
