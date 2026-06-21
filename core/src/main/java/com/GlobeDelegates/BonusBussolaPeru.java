package com.GlobeDelegates;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BonusBussolaPeru implements BonusAtividade {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    private static final int COLS = 8;
    private static final int ROWS = 6;
    private boolean[][] escavado = new boolean[ROWS][COLS];
    private int[][] artefato = new int[ROWS][COLS]; // 0=vazio, 1-5=artefato
    private boolean[] artefatoEncontrado = new boolean[6];

    private float celulaW, celulaH;
    private float gradeX, gradeY;
    private float gradeW, gradeH;

    private String[] nomesArtefatos = {"", "Quipu", "Mascara", "Cantaro", "Flauta", "Pedra Solar"};
    private float[][] coresArtefatos = {
        {0,0,0,0},
        {0.8f,0.4f,0.1f,1},
        {0.6f,0.2f,0.8f,1},
        {0.2f,0.5f,0.8f,1},
        {0.5f,0.7f,0.2f,1},
        {0.9f,0.7f,0.1f,1}
    };

    private int artefatosEncontrados = 0;
    private boolean concluido = false;
    private float tempoMensagem = 0;
    private String mensagem = "";

    public BonusBussolaPeru(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.6f);
        fundo = new Texture("bussola/peru.jpg");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        gradeW = w * 0.7f;
        gradeH = h * 0.65f;
        gradeX = (w - gradeW) / 2;
        gradeY = h * 0.12f;
        celulaW = gradeW / COLS;
        celulaH = gradeH / ROWS;

        posicionarArtefatos();
    }

    private void posicionarArtefatos() {
        // Artefatos ocupam 2-3 células
        int[][] tamanhos = {{2,1},{2,2},{1,2},{3,1},{2,2}};
        for (int a = 1; a <= 5; a++) {
            int tw = tamanhos[a-1][0];
            int th = tamanhos[a-1][1];
            boolean colocado = false;
            int tentativas = 0;
            while (!colocado && tentativas < 100) {
                tentativas++;
                int r = (int)(Math.random() * (ROWS - th));
                int c = (int)(Math.random() * (COLS - tw));
                boolean livre = true;
                for (int dr = 0; dr < th && livre; dr++)
                    for (int dc = 0; dc < tw && livre; dc++)
                        if (artefato[r+dr][c+dc] != 0) livre = false;
                if (livre) {
                    for (int dr = 0; dr < th; dr++)
                        for (int dc = 0; dc < tw; dc++)
                            artefato[r+dr][c+dc] = a;
                    colocado = true;
                }
            }
        }
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0.4f, 0.3f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.4f);
        shape.rect(0, 0, w, h);
        shape.end();

        if (concluido) { renderConclusao(w, h); return; }

        // HUD
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.8f);
        shape.rect(0, h - 55, w, 55);
        shape.end();
        batch.begin();
        font.setColor(0.9f, 0.7f, 0.2f, 1);
        font.draw(batch, "Peru - Escavacao Inca | Artefatos: " + artefatosEncontrados + "/5", w/2 - 250, h - 15);
        batch.end();

        // Grade
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                float cx = gradeX + c * celulaW;
                float cy = gradeY + (ROWS - 1 - r) * celulaH;

                shape.begin(ShapeRenderer.ShapeType.Filled);
                if (escavado[r][c]) {
                    int a = artefato[r][c];
                    float[] cor = coresArtefatos[a];
                    shape.setColor(a == 0 ? 0.6f : cor[0], a == 0 ? 0.45f : cor[1], a == 0 ? 0.2f : cor[2], 1);
                } else {
                    shape.setColor(0.35f, 0.25f, 0.1f, 1);
                }
                shape.rect(cx + 2, cy + 2, celulaW - 4, celulaH - 4);
                shape.end();

                shape.begin(ShapeRenderer.ShapeType.Line);
                shape.setColor(0.5f, 0.4f, 0.2f, 0.6f);
                shape.rect(cx + 2, cy + 2, celulaW - 4, celulaH - 4);
                shape.end();

                if (escavado[r][c] && artefato[r][c] != 0) {
                    batch.begin();
                    font.setColor(1, 1, 1, 0.9f);
                    String nome = nomesArtefatos[artefato[r][c]];
                    font.draw(batch, nome.substring(0, Math.min(3, nome.length())), cx + 5, cy + celulaH - 5);
                    batch.end();
                }
            }
        }

        // Artefatos encontrados
        batch.begin();
        font.setColor(0.9f, 0.7f, 0.2f, 1);
        font.draw(batch, "Encontrados:", w * 0.78f, h * 0.85f);
        for (int a = 1; a <= 5; a++) {
            float[] cor = coresArtefatos[a];
            font.setColor(artefatoEncontrado[a] ? cor[0] : 0.4f, artefatoEncontrado[a] ? cor[1] : 0.4f, artefatoEncontrado[a] ? cor[2] : 0.4f, 1);
            font.draw(batch, (artefatoEncontrado[a] ? "✓ " : "? ") + nomesArtefatos[a], w * 0.78f, h * 0.78f - (a-1) * 35);
        }
        batch.end();

        // Mensagem
        if (tempoMensagem > 0) {
            tempoMensagem -= delta;
            batch.begin();
            font.setColor(0.9f, 0.7f, 0.1f, 1);
            font.draw(batch, mensagem, w/2 - 150, h * 0.1f);
            batch.end();
        }

        // Input
        if (Gdx.input.justTouched()) {
            float tx = Gdx.input.getX();
            float ty = h - Gdx.input.getY();
            int c = (int)((tx - gradeX) / celulaW);
            int r = ROWS - 1 - (int)((ty - gradeY) / celulaH);
            if (r >= 0 && r < ROWS && c >= 0 && c < COLS && !escavado[r][c]) {
                escavado[r][c] = true;
                int a = artefato[r][c];
                if (a != 0 && !artefatoEncontrado[a]) {
                    // Verificar se todo artefato foi revelado
                    boolean completo = true;
                    for (int dr = 0; dr < ROWS; dr++)
                        for (int dc = 0; dc < COLS; dc++)
                            if (artefato[dr][dc] == a && !escavado[dr][dc]) completo = false;
                    if (completo) {
                        artefatoEncontrado[a] = true;
                        artefatosEncontrados++;
                        mensagem = nomesArtefatos[a] + " encontrado!";
                        tempoMensagem = 1.5f;
                        if (artefatosEncontrados >= 5) concluido = true;
                    }
                }
            }
        }
    }

    private void renderConclusao(float w, float h) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.3f, 0.2f, 0.05f, 0.88f);
        shape.rect(w/2 - 300, h/2 - 80, 600, 160);
        shape.end();
        batch.begin();
        font.setColor(0.9f, 0.7f, 0.2f, 1);
        font.draw(batch, "Todos os artefatos encontrados!", w/2 - 270, h/2 + 40);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Toque para continuar", w/2 - 170, h/2 - 20);
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
