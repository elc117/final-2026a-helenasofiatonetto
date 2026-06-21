package com.GlobeDelegates;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BonusGroelandia implements BonusAtividade {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture imagem;

    private static final int GRID = 4;
    private int[] board = new int[GRID * GRID];
    private int espacoVazio;

    private float tileSize;
    private float offsetX, offsetY;

    private boolean concluido = false;
    private int movimentos = 0;

    public BonusGroelandia(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.8f);
        imagem = new Texture("groelandia/bonus.jpg");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        tileSize = Math.min(w, h) * 0.18f;
        offsetX = (w - GRID * tileSize) / 2;
        offsetY = (h - GRID * tileSize) / 2;

        iniciarPuzzle();
    }

    private void iniciarPuzzle() {
        for (int i = 0; i < GRID * GRID; i++) board[i] = i;
        espacoVazio = GRID * GRID - 1;
        // Embaralhar com movimentos aleatorios validos
        for (int i = 0; i < 200; i++) {
            int[] vizinhos = getVizinhos(espacoVazio);
            int rand = vizinhos[(int)(Math.random() * vizinhos.length)];
            trocar(espacoVazio, rand);
            espacoVazio = rand;
        }
    }

    private int[] getVizinhos(int pos) {
        int row = pos / GRID;
        int col = pos % GRID;
        java.util.ArrayList<Integer> viz = new java.util.ArrayList<>();
        if (row > 0) viz.add(pos - GRID);
        if (row < GRID-1) viz.add(pos + GRID);
        if (col > 0) viz.add(pos - 1);
        if (col < GRID-1) viz.add(pos + 1);
        return viz.stream().mapToInt(Integer::intValue).toArray();
    }

    private void trocar(int a, int b) {
        int tmp = board[a]; board[a] = board[b]; board[b] = tmp;
    }

    private boolean verificarVitoria() {
        for (int i = 0; i < GRID * GRID - 1; i++) {
            if (board[i] != i) return false;
        }
        return true;
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0.05f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        if (concluido) {
            renderConclusao(w, h);
            return;
        }

        // Desenhar tiles
        for (int i = 0; i < GRID * GRID; i++) {
            int row = i / GRID;
            int col = i % GRID;
            float x = offsetX + col * tileSize;
            float y = offsetY + (GRID - 1 - row) * tileSize;

            if (board[i] == GRID * GRID - 1) {
                // Espaco vazio
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(0.1f, 0.2f, 0.4f, 1);
                shape.rect(x + 2, y + 2, tileSize - 4, tileSize - 4);
                shape.end();
            } else {
                int trow = board[i] / GRID;
                int tcol = board[i] % GRID;
                float u = (float)tcol / GRID;
                float v = 1f - (float)(trow + 1) / GRID;
                float uw = 1f / GRID;
                float vh = 1f / GRID;

                batch.begin();
                batch.draw(imagem,
                    x + 2, y + 2, tileSize - 4, tileSize - 4,
                    (int)(u * imagem.getWidth()),
                    (int)(v * imagem.getHeight()),
                    (int)(uw * imagem.getWidth()),
                    (int)(vh * imagem.getHeight()),
                    false, false);
                batch.end();

                shape.begin(ShapeRenderer.ShapeType.Line);
                shape.setColor(0.5f, 0.8f, 1f, 0.5f);
                shape.rect(x + 2, y + 2, tileSize - 4, tileSize - 4);
                shape.end();

                // Numero da peca
                batch.begin();
                font.setColor(1, 1, 1, 0.5f);
                font.draw(batch, String.valueOf(board[i] + 1), x + 5, y + tileSize - 5);
                batch.end();
            }
        }

        // HUD
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.7f);
        shape.rect(0, h - 50, w, 50);
        shape.end();
        batch.begin();
        font.setColor(0.5f, 0.8f, 1f, 1);
        font.draw(batch, "Quebra-Gelo | Movimentos: " + movimentos + " | Clique numa peca ao lado do espaco vazio!", 20, h - 15);
        batch.end();

        // Input
        if (Gdx.input.justTouched()) {
            float tx = Gdx.input.getX();
            float ty = h - Gdx.input.getY();

            int col = (int)((tx - offsetX) / tileSize);
            int row = GRID - 1 - (int)((ty - offsetY) / tileSize);

            if (col >= 0 && col < GRID && row >= 0 && row < GRID) {
                int pos = row * GRID + col;
                int[] vizinhos = getVizinhos(espacoVazio);
                for (int v : vizinhos) {
                    if (v == pos) {
                        trocar(espacoVazio, pos);
                        espacoVazio = pos;
                        movimentos++;
                        if (verificarVitoria()) concluido = true;
                        break;
                    }
                }
            }
        }
    }

    private void renderConclusao(float w, float h) {
        batch.begin();
        batch.draw(imagem, offsetX, offsetY, GRID * tileSize, GRID * tileSize);
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0.1f, 0.2f, 0.85f);
        shape.rect(w/2 - 300, h/2 - 80, 600, 160);
        shape.end();

        batch.begin();
        font.setColor(0.5f, 1f, 1f, 1);
        font.draw(batch, "Puzzle completo!", w/2 - 160, h/2 + 40);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Movimentos: " + movimentos + " | Toque para voltar", w/2 - 250, h/2 - 20);
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
        imagem.dispose();
    }
}
