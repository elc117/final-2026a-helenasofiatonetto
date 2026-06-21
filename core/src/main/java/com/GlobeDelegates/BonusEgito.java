package com.GlobeDelegates;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.Collections;

public class BonusEgito implements BonusAtividade {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    // Pirâmide: 3 camadas, base=5, meio=3, topo=1
    private int[] camadasTamanho = {5, 3, 1};
    private int camadaAtual = 0;
    private int blocoNaCamada = 0;

    // Blocos caindo
    private float blocoX, blocoY;
    private float blocoVY = 150;
    private float blocoW = 80, blocoH = 60;
    private char[] hieroglifos = {'A','B','C','D','E','F','G','H','I'};
    private char blocoHieroglifo;
    private char blocoCorreto;

    // Blocos na fila (embaralhados)
    private ArrayList<Character> fila = new ArrayList<>();
    private boolean blocoAtivo = false;
    private float tempoEspera = 0;

    // Pirâmide construída
    private ArrayList<float[]> blocosColocados = new ArrayList<>();

    private boolean concluido = false;
    private boolean errou = false;
    private float tempoMensagem = 0;

    private float piramideBaseX, piramideBaseY;
    private float larguraBase;

    public BonusEgito(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(2f);
        fundo = new Texture("egito/bonus.jpg");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        piramideBaseX = w/2 - (camadasTamanho[0] * blocoW)/2f;
        piramideBaseY = h * 0.1f;
        larguraBase = camadasTamanho[0] * blocoW;

        gerarFila();
        lancarBloco(w);
    }

    private void gerarFila() {
        fila.clear();
        int totalBlocos = 0;
        for (int t : camadasTamanho) totalBlocos += t;
        for (int i = 0; i < totalBlocos; i++) {
            fila.add(hieroglifos[i % hieroglifos.length]);
        }
        Collections.shuffle(fila);
    }

    private char getBlocoCorreto() {
        int idx = 0;
        for (int c = 0; c < camadaAtual; c++) idx += camadasTamanho[c];
        idx += blocoNaCamada;
        return fila.get(idx);
    }

    private void lancarBloco(float w) {
        blocoCorreto = getBlocoCorreto();
        // Chance de ser o bloco correto ou errado
        if (Math.random() > 0.4f) {
            blocoHieroglifo = blocoCorreto;
        } else {
            char errado;
            do { errado = hieroglifos[(int)(Math.random() * hieroglifos.length)]; }
            while (errado == blocoCorreto);
            blocoHieroglifo = errado;
        }
        blocoX = (float)(Math.random() * (w - blocoW));
        blocoY = Gdx.graphics.getHeight() + 20;
        blocoAtivo = true;
        errou = false;
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0.15f, 0.1f, 0.02f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
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
        font.setColor(0.9f, 0.75f, 0.1f, 1);
        font.draw(batch, "Construcao da Piramide | Camada " + (camadaAtual+1) + "/3 | Bloco " + (blocoNaCamada+1) + "/" + camadasTamanho[camadaAtual], 15, h - 15);
        batch.end();

        // Desenhar pirâmide construída
        desenharPiramideConstruida(w, h);

        // Mostrar hieróglifo correto
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.15f, 0.1f, 0.02f, 0.9f);
        shape.rect(20, h/2 - 60, 180, 120);
        shape.end();
        batch.begin();
        font.setColor(0.9f, 0.75f, 0.1f, 1);
        font.draw(batch, "Precisa:", 30, h/2 + 45);
        font.setColor(1, 1, 0.3f, 1);
        font.draw(batch, String.valueOf(blocoCorreto), 75, h/2);
        batch.end();

        // Mensagem de erro/acerto
        if (tempoMensagem > 0) {
            tempoMensagem -= delta;
            batch.begin();
            font.setColor(errou ? 1f : 0.2f, errou ? 0.2f : 1f, 0.2f, 1);
            font.draw(batch, errou ? "Errado! Voltou!" : "Correto!", w/2 - 100, h/2 + 50);
            batch.end();
        }

        if (tempoEspera > 0) {
            tempoEspera -= delta;
            if (tempoEspera <= 0) lancarBloco(w);
            return;
        }

        // Mover bloco caindo
        if (blocoAtivo) {
            blocoY -= blocoVY * delta;

            // Desenhar bloco
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(0.7f, 0.55f, 0.2f, 1);
            shape.rect(blocoX, blocoY, blocoW, blocoH);
            shape.end();
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(0.9f, 0.75f, 0.1f, 1);
            shape.rect(blocoX, blocoY, blocoW, blocoH);
            shape.end();
            batch.begin();
            font.setColor(0.1f, 0.05f, 0f, 1);
            font.draw(batch, String.valueOf(blocoHieroglifo), blocoX + blocoW/2 - 12, blocoY + blocoH - 8);
            batch.end();

            // Toque no bloco
            if (Gdx.input.justTouched()) {
                float tx = Gdx.input.getX();
                float ty = h - Gdx.input.getY();
                if (tx >= blocoX && tx <= blocoX + blocoW &&
                    ty >= blocoY && ty <= blocoY + blocoH) {
                    if (blocoHieroglifo == blocoCorreto) {
                        // Colocar na pirâmide
                        float px = getPosicaoBlocoX(w);
                        float py = getPosicaoBlocoY();
                        blocosColocados.add(new float[]{px, py, (float)blocoHieroglifo});
                        blocoNaCamada++;
                        if (blocoNaCamada >= camadasTamanho[camadaAtual]) {
                            camadaAtual++;
                            blocoNaCamada = 0;
                            if (camadaAtual >= camadasTamanho.length) {
                                concluido = true;
                                return;
                            }
                        }
                        errou = false;
                        tempoMensagem = 0.8f;
                        blocoAtivo = false;
                        tempoEspera = 1f;
                    } else {
                        // Bloco errado — volta ao topo
                        errou = true;
                        tempoMensagem = 1f;
                        blocoAtivo = false;
                        tempoEspera = 1f;
                    }
                }
            }

            // Bloco saiu da tela — relança
            if (blocoY < -blocoH) {
                blocoAtivo = false;
                tempoEspera = 0.5f;
            }
        }
    }

    private float getPosicaoBlocoX(float w) {
        int n = camadasTamanho[camadaAtual];
        float camadaW = n * blocoW;
        float startX = w/2 - camadaW/2f;
        return startX + blocoNaCamada * blocoW;
    }

    private float getPosicaoBlocoY() {
        float y = piramideBaseY;
        for (int c = 0; c < camadaAtual; c++) y += blocoH + 5;
        return y;
    }

    private void desenharPiramideConstruida(float w, float h) {
        for (float[] b : blocosColocados) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(0.7f, 0.55f, 0.2f, 1);
            shape.rect(b[0], b[1], blocoW - 2, blocoH - 2);
            shape.end();
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(0.9f, 0.75f, 0.1f, 1);
            shape.rect(b[0], b[1], blocoW - 2, blocoH - 2);
            shape.end();
            batch.begin();
            font.setColor(0.1f, 0.05f, 0f, 1);
            font.draw(batch, String.valueOf((char)(int)b[2]), b[0] + blocoW/2 - 12, b[1] + blocoH - 8);
            batch.end();
        }

        // Grade vazia da pirâmide
        for (int c = 0; c < camadasTamanho.length; c++) {
            int n = camadasTamanho[c];
            float startX = w/2 - (n * blocoW)/2f;
            float y = piramideBaseY + c * (blocoH + 5);
            for (int b2 = 0; b2 < n; b2++) {
                float x = startX + b2 * blocoW;
                shape.begin(ShapeRenderer.ShapeType.Line);
                shape.setColor(0.5f, 0.4f, 0.1f, 0.4f);
                shape.rect(x, y, blocoW - 2, blocoH - 2);
                shape.end();
            }
        }
    }

    private void renderConclusao(float w, float h) {
        desenharPiramideConstruida(w, h);
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.15f, 0.1f, 0.02f, 0.88f);
        shape.rect(w/2 - 300, h/2 - 80, 600, 160);
        shape.end();
        batch.begin();
        font.setColor(0.9f, 0.75f, 0.1f, 1);
        font.draw(batch, "Piramide construida!", w/2 - 200, h/2 + 40);
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
