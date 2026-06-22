package com.GlobeDelegates;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;

public class BonusBussolaGrecia implements BonusAtividade {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;
    private Texture tochaImg;

    // Tocha
    private float tochaX, tochaY;
    private float tochaW = 30, tochaH = 60;
    private float tochaVX = 300;
    private boolean tochaAcesa = true;

    // Percurso — checkpoints
    private float[][] checkpoints;
    private int checkpointAtual = 0;
    private boolean chegou = false;

    // Ventos — obstáculos
    private ArrayList<float[]> ventos = new ArrayList<>();
    private float tempoSpawnVento = 0;
    private float ventW = 80, ventH = 40;

    private boolean concluido = false;
    private boolean gameOver = false;
    private float tempoJogo = 0;
    private float tempoTotal = 45f;
    private int vidas = 3;

    public BonusBussolaGrecia(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.8f);
        fundo = new Texture("bussola/grecia.jpg");
        tochaImg = new Texture("bussola/tocha.png");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        tochaX = 80;
        tochaY = h * 0.5f;

        checkpoints = new float[][]{
            {w * 0.25f, h * 0.7f},
            {w * 0.5f, h * 0.3f},
            {w * 0.75f, h * 0.6f},
            {w * 0.9f, h * 0.5f}
        };
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0.7f, 0.8f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        batch.end();

        if (concluido) { renderConclusao(w, h); return; }
        if (gameOver) { renderGameOver(w, h); return; }

        tempoJogo += delta;
        if (tempoJogo >= tempoTotal || vidas <= 0) { gameOver = true; return; }

        // Mover tocha
        if (Gdx.input.isKeyPressed(Keys.LEFT))  tochaX -= tochaVX * delta;
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) tochaX += tochaVX * delta;
        if (Gdx.input.isKeyPressed(Keys.UP))    tochaY += tochaVX * delta;
        if (Gdx.input.isKeyPressed(Keys.DOWN))  tochaY -= tochaVX * delta;
        tochaX = Math.max(0, Math.min(tochaX, w - tochaW));
        tochaY = Math.max(0, Math.min(tochaY, h - tochaH));

        // Spawn ventos
        tempoSpawnVento += delta;
        if (tempoSpawnVento >= 1.5f) {
            tempoSpawnVento = 0;
            float vy = (float)(Math.random() * (h - ventH));
            ventos.add(new float[]{w, vy, (float)(Math.random() * 150 + 100)});
        }

        // Mover ventos
        for (int i = ventos.size() - 1; i >= 0; i--) {
            float[] v = ventos.get(i);
            v[0] -= v[2] * delta;
            if (v[0] < -ventW) { ventos.remove(i); continue; }

            // Colisão com tocha
            if (tochaX < v[0] + ventW && tochaX + tochaW > v[0] &&
                tochaY < v[1] + ventH && tochaY + tochaH > v[1]) {
                vidas--;
                ventos.remove(i);
            }
        }

        // Verificar checkpoint
        if (checkpointAtual < checkpoints.length) {
            float[] cp = checkpoints[checkpointAtual];
            float dist = (float)Math.sqrt(Math.pow(tochaX - cp[0], 2) + Math.pow(tochaY - cp[1], 2));
            if (dist < 50) {
                checkpointAtual++;
                if (checkpointAtual >= checkpoints.length) concluido = true;
            }
        }

        // Desenhar percurso
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.8f, 0.6f, 0.1f, 0.5f);
        for (int i = 0; i < checkpoints.length - 1; i++) {
            shape.line(checkpoints[i][0], checkpoints[i][1], checkpoints[i+1][0], checkpoints[i+1][1]);
        }
        shape.end();

        // Desenhar checkpoints
        for (int i = 0; i < checkpoints.length; i++) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(i < checkpointAtual ? 0.1f : 0.8f, i < checkpointAtual ? 0.8f : 0.6f, 0.1f, 1);
            shape.circle(checkpoints[i][0], checkpoints[i][1], 25);
            shape.end();
            batch.begin();
            font.setColor(1, 1, 1, 1);
            font.draw(batch, String.valueOf(i+1), checkpoints[i][0] - 8, checkpoints[i][1] + 10);
            batch.end();
        }

        // Desenhar ventos
        for (float[] v : ventos) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(0.5f, 0.7f, 0.9f, 0.7f);
            shape.ellipse(v[0], v[1], ventW, ventH);
            shape.end();
            batch.begin();
            font.setColor(0.2f, 0.3f, 0.8f, 1);
            font.draw(batch, "~vento~", v[0] + 5, v[1] + ventH - 5);
            batch.end();
        }

        // Desenhar tocha
        batch.begin();
        batch.draw(tochaImg, tochaX, tochaY, tochaW, tochaH);
        batch.end();

        // Chama da tocha
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(1f, 0.5f, 0.1f, 0.8f);
        shape.circle(tochaX + tochaW/2, tochaY + tochaH, 15);
        shape.setColor(1f, 0.9f, 0.1f, 0.6f);
        shape.circle(tochaX + tochaW/2, tochaY + tochaH + 5, 8);
        shape.end();

        // HUD
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.7f);
        shape.rect(0, h - 50, w, 50);
        shape.end();
        batch.begin();
        font.setColor(0.9f, 0.7f, 0.1f, 1);
        font.draw(batch, "Grecia - Tocha Olimpica | Vidas: " + vidas + " | Checkpoint: " + checkpointAtual + "/4 | Tempo: " + (int)(tempoTotal - tempoJogo) + "s", 10, h - 15);
        batch.end();
    }

    private void renderConclusao(float w, float h) {
        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        batch.end();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.1f, 0.4f, 0.88f);
        shape.rect(w/2 - 300, h/2 - 80, 600, 160);
        shape.end();
        batch.begin();
        font.setColor(0.9f, 0.7f, 0.1f, 1);
        font.draw(batch, "Tocha entregue! Parabens!", w/2 - 240, h/2 + 40);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Toque para voltar", w/2 - 140, h/2 - 20);
        batch.end();
        if (Gdx.input.justTouched()) {
            concluido = true;
        }
    }

    private void renderGameOver(float w, float h) {
        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        batch.end();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.5f, 0f, 0f, 0.85f);
        shape.rect(w/2 - 300, h/2 - 80, 600, 160);
        shape.end();
        batch.begin();
        font.setColor(1, 0.3f, 0.3f, 1);
        font.draw(batch, "A tocha apagou!", w/2 - 160, h/2 + 40);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Toque para tentar novamente", w/2 - 230, h/2 - 20);
        batch.end();
        if (Gdx.input.justTouched()) {
            jogo.setScreen(new TelaBonusBussola(jogo, jogador, "Grecia"));
        }
    }

    public boolean isConcluido() { return concluido; }

    @Override
    public void dispose() {
        batch.dispose();
        shape.dispose();
        font.dispose();
        fundo.dispose();
        tochaImg.dispose();
    }
}
