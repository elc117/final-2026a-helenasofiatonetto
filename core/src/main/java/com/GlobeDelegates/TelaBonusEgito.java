package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TelaBonusEgito implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    // Modo: 0 = decifrar mensagem, 1 = espelho
    private int modo = 0;
    private int rodada = 0;
    private int totalRodadas = 6;
    private boolean concluido = false;
    private float tempoMensagem = 0;
    private boolean acertou = false;

    // Modo 0 — Decifrar mensagem
    private String[][] palavras = {
        {"SOL", "Ra"},
        {"VIDA", "Ankh"},
        {"REI", "Farao"},
        {"GATO", "Miau"},
        {"AGUA", "Nilo"},
        {"MORTE", "Osiris"}
    };
    private String palavraDigitada = "";
    private String[] letrasDisponiveis = {"A","B","C","D","E","F","G","H","I","J","K","L","M",
                                           "N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private float letraSize = 45;

    // Modo 1 — Espelho (grade 5x5)
    private boolean[][] modelo;
    private boolean[][] jogadorGrade;
    private int gradeSize = 5;
    private float celulaSize = 60;

    public TelaBonusEgito(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.6f);
        fundo = new Texture("egito/bonus.jpg");
        iniciarRodada();
    }

    private void iniciarRodada() {
        palavraDigitada = "";
        modo = rodada % 2;
        if (modo == 1) {
            gerarModelo();
        }
    }

    private void gerarModelo() {
        modelo = new boolean[gradeSize][gradeSize];
        jogadorGrade = new boolean[gradeSize][gradeSize];
        // Gerar padrão aleatório simétrico
        for (int r = 0; r < gradeSize; r++) {
            for (int c = 0; c < gradeSize/2 + 1; c++) {
                boolean val = Math.random() > 0.5f;
                modelo[r][c] = val;
                modelo[r][gradeSize - 1 - c] = val;
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.1f, 0.02f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        batch.draw(fundo, 0, 0, w, h);
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.6f);
        shape.rect(0, 0, w, h);
        shape.end();

        if (concluido) { renderConclusao(w, h); return; }

        // HUD
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.8f);
        shape.rect(0, h - 50, w, 50);
        shape.end();
        batch.begin();
        font.setColor(0.9f, 0.75f, 0.1f, 1);
        font.draw(batch, "Decifrando Hieroglifos | Rodada " + (rodada+1) + "/" + totalRodadas, 20, h - 15);
        batch.end();

        if (tempoMensagem > 0) {
            tempoMensagem -= delta;
            batch.begin();
            font.setColor(acertou ? 0.2f : 1f, acertou ? 1f : 0.2f, 0.2f, 1);
            font.draw(batch, acertou ? "Correto!" : "Errado! Tente novamente!", w/2 - 160, h/2);
            batch.end();
            if (tempoMensagem <= 0 && acertou) {
                rodada++;
                if (rodada >= totalRodadas) concluido = true;
                else iniciarRodada();
            }
            return;
        }

        if (modo == 0) renderDecifrar(w, h);
        else renderEspelho(w, h);
    }

    private void renderDecifrar(float w, float h) {
        int idx = rodada / 2;
        if (idx >= palavras.length) idx = palavras.length - 1;
        String palavra = palavras[idx][0];
        String dica = palavras[idx][1];

        // Painel
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.15f, 0.1f, 0.02f, 0.9f);
        shape.rect(w/2 - 350, 80, 700, 500);
        shape.end();

        // Hieróglifos desenhados como símbolos geométricos
        batch.begin();
        font.setColor(0.9f, 0.75f, 0.1f, 1);
        font.draw(batch, "Decifre o hieroglifo! Dica: " + dica, w/2 - 240, h - 70);
        batch.end();

        // Desenhar símbolos hieroglíficos para cada letra
        float sx = w/2 - (palavra.length() * 70)/2f;
        for (int i = 0; i < palavra.length(); i++) {
            desenharHieroglifo(palavra.charAt(i), sx + i * 70, h/2 + 80);
        }

        // Campo digitado
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.2f, 0.15f, 0.05f, 1);
        shape.rect(w/2 - 150, h/2 + 10, 300, 50);
        shape.end();
        batch.begin();
        font.setColor(0.5f, 1f, 0.5f, 1);
        font.draw(batch, palavraDigitada + "|", w/2 - 135, h/2 + 48);
        batch.end();

        // Teclado
        int cols = 13;
        float kw = 45, kh = 40;
        float kStartX = w/2 - (cols * (kw + 5))/2f;
        for (int i = 0; i < letrasDisponiveis.length; i++) {
            int row = i / cols;
            int col = i % cols;
            float kx = kStartX + col * (kw + 5);
            float ky = h/2 - 60 - row * (kh + 8);

            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(0.3f, 0.2f, 0.05f, 1);
            shape.rect(kx, ky, kw, kh);
            shape.end();
            batch.begin();
            font.setColor(1, 0.9f, 0.5f, 1);
            font.draw(batch, letrasDisponiveis[i], kx + 12, ky + kh - 8);
            batch.end();

            if (Gdx.input.justTouched()) {
                float tx = Gdx.input.getX();
                float ty = Gdx.graphics.getHeight() - Gdx.input.getY();
                if (tx >= kx && tx <= kx + kw && ty >= ky && ty <= ky + kh) {
                    palavraDigitada += letrasDisponiveis[i];
                }
            }
        }

        // Botoes confirmar e apagar
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.5f, 0.1f, 1);
        shape.rect(w/2 - 200, 100, 160, 45);
        shape.setColor(0.5f, 0.1f, 0.1f, 1);
        shape.rect(w/2 + 40, 100, 160, 45);
        shape.end();
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "CONFIRMAR", w/2 - 185, 133);
        font.draw(batch, "APAGAR", w/2 + 65, 133);
        batch.end();

        if (Gdx.input.justTouched()) {
            float tx = Gdx.input.getX();
            float ty = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (tx >= w/2 - 200 && tx <= w/2 - 40 && ty >= 100 && ty <= 145) {
                acertou = palavraDigitada.equals(palavra);
                tempoMensagem = 1.5f;
                if (!acertou) palavraDigitada = "";
            }
            if (tx >= w/2 + 40 && tx <= w/2 + 200 && ty >= 100 && ty <= 145) {
                if (palavraDigitada.length() > 0)
                    palavraDigitada = palavraDigitada.substring(0, palavraDigitada.length() - 1);
            }
        }
    }

    private void desenharHieroglifo(char letra, float x, float y) {
        float s = 55;
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.9f, 0.75f, 0.1f, 0.3f);
        shape.rect(x, y, s, s);
        shape.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.9f, 0.75f, 0.1f, 1);

        float cx = x + s/2, cy = y + s/2, r = s * 0.3f;
        int l = letra - 'A';
        if (l % 5 == 0) { shape.circle(cx, cy, r); }
        else if (l % 5 == 1) { shape.triangle(cx, cy+r, cx-r, cy-r, cx+r, cy-r); }
        else if (l % 5 == 2) { shape.rect(cx-r, cy-r, r*2, r*2); }
        else if (l % 5 == 3) { shape.line(cx-r, cy, cx+r, cy); shape.line(cx, cy-r, cx, cy+r); }
        else { shape.line(cx-r, cy-r, cx+r, cy+r); shape.line(cx-r, cy+r, cx+r, cy-r); }
        shape.end();

        batch.begin();
        font.setColor(1, 0.9f, 0.5f, 0.5f);
        font.draw(batch, String.valueOf(letra), cx - 8, y - 5);
        batch.end();
    }

    private void renderEspelho(float w, float h) {
        float totalW = gradeSize * celulaSize;
        float modeloX = w/2 - totalW - 40;
        float jogadorX = w/2 + 40;
        float gradeY = h/2 - totalW/2;

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.15f, 0.1f, 0.02f, 0.9f);
        shape.rect(w/2 - 420, gradeY - 60, 840, totalW + 120);
        shape.end();

        batch.begin();
        font.setColor(0.9f, 0.75f, 0.1f, 1);
        font.draw(batch, "Reproduza o hieroglifo na grade da direita!", w/2 - 280, h - 70);
        font.setColor(0.7f, 0.7f, 0.7f, 1);
        font.draw(batch, "MODELO", modeloX + totalW/2 - 50, gradeY + totalW + 30);
        font.draw(batch, "SUA GRADE", jogadorX + totalW/2 - 60, gradeY + totalW + 30);
        batch.end();

        // Grade modelo
        for (int r = 0; r < gradeSize; r++) {
            for (int c = 0; c < gradeSize; c++) {
                float cx = modeloX + c * celulaSize;
                float cy = gradeY + (gradeSize - 1 - r) * celulaSize;
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(modelo[r][c] ? 0.9f : 0.2f, modelo[r][c] ? 0.75f : 0.15f, modelo[r][c] ? 0.1f : 0.05f, 1);
                shape.rect(cx + 2, cy + 2, celulaSize - 4, celulaSize - 4);
                shape.end();
            }
        }

        // Grade jogador
        for (int r = 0; r < gradeSize; r++) {
            for (int c = 0; c < gradeSize; c++) {
                float cx = jogadorX + c * celulaSize;
                float cy = gradeY + (gradeSize - 1 - r) * celulaSize;
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(jogadorGrade[r][c] ? 0.1f : 0.2f, jogadorGrade[r][c] ? 0.7f : 0.15f, jogadorGrade[r][c] ? 0.1f : 0.05f, 1);
                shape.rect(cx + 2, cy + 2, celulaSize - 4, celulaSize - 4);
                shape.end();
                shape.begin(ShapeRenderer.ShapeType.Line);
                shape.setColor(0.5f, 0.4f, 0.1f, 0.5f);
                shape.rect(cx + 2, cy + 2, celulaSize - 4, celulaSize - 4);
                shape.end();
            }
        }

        // Toque na grade do jogador
        if (Gdx.input.justTouched()) {
            float tx = Gdx.input.getX();
            float ty = h - Gdx.input.getY();
            for (int r = 0; r < gradeSize; r++) {
                for (int c = 0; c < gradeSize; c++) {
                    float cx = jogadorX + c * celulaSize;
                    float cy = gradeY + (gradeSize - 1 - r) * celulaSize;
                    if (tx >= cx && tx <= cx + celulaSize && ty >= cy && ty <= cy + celulaSize) {
                        jogadorGrade[r][c] = !jogadorGrade[r][c];
                    }
                }
            }
        }

        // Botao confirmar
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.5f, 0.1f, 1);
        shape.rect(w/2 - 80, 100, 160, 45);
        shape.end();
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "CONFIRMAR", w/2 - 65, 133);
        batch.end();

        if (Gdx.input.justTouched()) {
            float tx = Gdx.input.getX();
            float ty = h - Gdx.input.getY();
            if (tx >= w/2 - 80 && tx <= w/2 + 80 && ty >= 100 && ty <= 145) {
                // Verificar se grade do jogador == modelo
                boolean igual = true;
                for (int r = 0; r < gradeSize; r++) {
                    for (int c = 0; c < gradeSize; c++) {
                        if (jogadorGrade[r][c] != modelo[r][c]) { igual = false; break; }
                    }
                }
                acertou = igual;
                tempoMensagem = 1.5f;
                if (!acertou) {
                    for (int r = 0; r < gradeSize; r++)
                        for (int c = 0; c < gradeSize; c++)
                            jogadorGrade[r][c] = false;
                }
            }
        }
    }

    private void renderConclusao(float w, float h) {
        batch.begin();
        batch.draw(fundo, 0, 0, w, h);
        batch.end();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.15f, 0.1f, 0.02f, 0.88f);
        shape.rect(w/2 - 300, h/2 - 80, 600, 160);
        shape.end();
        batch.begin();
        font.setColor(0.9f, 0.75f, 0.1f, 1);
        font.draw(batch, "Hieroglifos decifrados!", w/2 - 210, h/2 + 40);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Toque para voltar", w/2 - 140, h/2 - 20);
        batch.end();
        if (Gdx.input.justTouched()) {
            jogo.setScreen(new TelaVilagemEgito(jogo, jogador, true, true));
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
