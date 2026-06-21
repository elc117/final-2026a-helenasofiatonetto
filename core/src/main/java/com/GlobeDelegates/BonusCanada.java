package com.GlobeDelegates;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BonusCanada implements BonusAtividade {
    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;
    private boolean concluido = false;

    // Barril (Seu "barco")
    private float barrilX, barrilY;
    private float barrilW = 50, barrilH = 60;
    private float barrilVX = 450; // AUMENTADO: Velocidade do barril (era 300)

    // Classe simples para representar os obstáculos (formas)
    private static class Objeto {
        float x, y;
        float vx, vy; // Velocidades X e Y separadas
        float tamanho = 45;
    }

    private ArrayList<Objeto> obstaculos = new ArrayList<>();
    private float tempoSpawn = 0;

    private boolean gameOver = false;
    private float tempoJogo = 0;
    private float tempoTotal = 30f;
    private int vidas = 3;

    public BonusCanada(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(2f);
        fundo = new Texture("canada/bonus.jpg");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        barrilX = w / 2 - barrilW / 2;
        barrilY = h * 0.15f;
    }

    @Override
    public void update(float delta) {
        if (gameOver || concluido) return;
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        tempoJogo += delta;
        if (tempoJogo >= tempoTotal) { concluido = true; return; }

        // Movimentação do Barril
        if (Gdx.input.isKeyPressed(Keys.LEFT))  barrilX -= barrilVX * delta;
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) barrilX += barrilVX * delta;
        if (Gdx.input.isKeyPressed(Keys.UP))    barrilY += barrilVX * delta;
        if (Gdx.input.isKeyPressed(Keys.DOWN))  barrilY -= barrilVX * delta;
        barrilX = Math.max(0, Math.min(barrilX, w - barrilW));
        barrilY = Math.max(0, Math.min(barrilY, h - barrilH));

        // Mecânica de Nascimento (Spawn) dos Objetos
        tempoSpawn += delta;
        float intervalo = Math.max(0.3f, 1.0f - tempoJogo * 0.03f); // Spawna mais rápido com o tempo
        if (tempoSpawn >= intervalo) {
            tempoSpawn = 0;
            
            Objeto novo = new Objeto();
            // Escolhe aleatoriamente se o objeto vem de CIMA(0), da ESQUERDA(1) ou da DIREITA(2)
            int tipoSpawn = (int)(Math.random() * 3); 

            if (tipoSpawn == 0) {
                // Vem de CIMA (Vertical)
                novo.x = (float)(Math.random() * (w - novo.tamanho));
                novo.y = h + novo.tamanho;
                novo.vx = 0;
                novo.vy = -(float)(Math.random() * 200 + 250); // AUMENTADO (Velocidade de queda)
            } else if (tipoSpawn == 1) {
                // Vem da ESQUERDA (Horizontal)
                novo.x = -novo.tamanho;
                novo.y = (float)(Math.random() * (h * 0.5f) + h * 0.15f); // Altura próxima ao barril
                novo.vx = (float)(Math.random() * 200 + 250); // AUMENTADO (Velocidade para a direita)
                novo.vy = 0;
            } else {
                // Vem da DIREITA (Horizontal)
                novo.x = w + novo.tamanho;
                novo.y = (float)(Math.random() * (h * 0.5f) + h * 0.15f);
                novo.vx = -(float)(Math.random() * 200 + 250); // AUMENTADO (Velocidade para a esquerda)
                novo.vy = 0;
            }
            obstaculos.add(novo);
        }

        // Atualização dos Objetos e Colisões
        for (int i = obstaculos.size() - 1; i >= 0; i--) {
            Objeto o = obstaculos.get(i);
            
            // Move o objeto baseado em suas velocidades individuais
            o.x += o.vx * delta;
            o.y += o.vy * delta;

            // Remove se sair da tela (por baixo ou pelas laterais)
            if (o.y < -o.tamanho || o.x < -o.tamanho * 2 || o.x > w + o.tamanho * 2) {
                obstaculos.remove(i);
                continue;
            }

            // Teste de Colisão (AABB)
            if (barrilX < o.x + o.tamanho && barrilX + barrilW > o.x &&
                barrilY < o.y + o.tamanho && barrilY + barrilH > o.y) {
                obstaculos.remove(i);
                vidas--;
                if (vidas <= 0) gameOver = true;
            }
        }
    }

    @Override
    public void render() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Fundo
        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        batch.end();

        // Tela de Game Over
        if (gameOver) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(0.5f, 0, 0, 0.85f);
            shape.rect(w/2 - 300, h/2 - 80, 600, 160);
            shape.end();
            batch.begin();
            font.setColor(1, 0.3f, 0.3f, 1);
            font.draw(batch, "Game Over!", w/2 - 120, h/2 + 40);
            font.setColor(1, 1, 1, 1);
            font.draw(batch, "ESC para sair", w/2 - 120, h/2 - 20);
            batch.end();
            return;
        }

        // Desenhar Formas (Obstáculos)
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.4f, 0.3f, 0.2f, 1); // Cor marrom/cinza para as formas
        for (Objeto o : obstaculos) {
            shape.rect(o.x, o.y, o.tamanho, o.tamanho);
        }
        shape.end();

        // Desenhar Barril
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.5f, 0.3f, 0.1f, 1);
        shape.rect(barrilX, barrilY, barrilW, barrilH);
        // Detalhes/Aros do barril
        shape.setColor(0.3f, 0.2f, 0.05f, 1);
        shape.rect(barrilX, barrilY + barrilH * 0.3f, barrilW, 5);
        shape.rect(barrilX, barrilY + barrilH * 0.65f, barrilW, 5);
        shape.end();

        // HUD (Placar)
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.7f);
        shape.rect(0, h - 50, w, 50);
        shape.end();
        
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Vidas: " + vidas + " | Tempo: " + (int)Math.max(0, tempoTotal - tempoJogo) + "s", 20, h - 15);
        batch.end();
    }

    @Override public boolean isConcluido() { return concluido; }
    @Override public void dispose() { batch.dispose(); shape.dispose(); font.dispose(); fundo.dispose(); }
}