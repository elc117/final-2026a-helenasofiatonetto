package com.GlobeDelegates;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BonusAustria implements BonusAtividade {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture pintura;
    private TextureRegion[] fragmentos;

    private String[] obras = {"austria/thekiss.jpg", "austria/sissi.jpg", "austria/BRUEGELS.jpg", "austria/hallstatt.jpeg"};
    private String[] titulosObras = {"O Beijo - Klimt", "Retrato de Sissi", "Bruegel - Paisagem", "Hallstatt"};
    private String obraAtual;

    private float pinturaX, pinturaY, pinturaW, pinturaH;

    private float[][] lacunas = {
        {0.1f, 0.6f, 0.2f, 0.2f},
        {0.35f, 0.4f, 0.2f, 0.25f},
        {0.6f, 0.55f, 0.2f, 0.2f},
        {0.15f, 0.15f, 0.2f, 0.2f},
        {0.55f, 0.15f, 0.2f, 0.2f},
        {0.35f, 0.72f, 0.2f, 0.2f}
    };

    private float[] fragX, fragY;
    private float[] fragOrigX, fragOrigY;
    private float fragSize = 90;
    private boolean[] fragColocado;
    private int[] fragCorreto;

    private int dragging = -1;
    private float dragOffX, dragOffY;
    private boolean tocandoAntes = false;
    private boolean concluido = false;
    private float[] lacunaErrada = null;
    private float tempoErro = 0;
    private int obraIdx;

    public BonusAustria(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.4f);

        obraIdx = (int)(Math.random() * obras.length);
        obraAtual = obras[obraIdx];
        pintura = new Texture(obraAtual);

        inicializar();
    }

    private void inicializar() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        pinturaH = h * 0.75f;
        pinturaW = pinturaH * ((float)pintura.getWidth() / pintura.getHeight());
        if (pinturaW > w * 0.6f) {
            pinturaW = w * 0.6f;
            pinturaH = pinturaW * ((float)pintura.getHeight() / pintura.getWidth());
        }
        pinturaX = (w - pinturaW) / 2;
        pinturaY = (h - pinturaH) / 2;

        int n = lacunas.length;
        fragX = new float[n];
        fragY = new float[n];
        fragOrigX = new float[n];
        fragOrigY = new float[n];
        fragColocado = new boolean[n];
        fragCorreto = new int[n];
        fragmentos = new TextureRegion[n];

        for (int i = 0; i < n; i++) {
            fragCorreto[i] = i;
            int rx = (int)(lacunas[i][0] * pintura.getWidth());
            int ry = (int)((1f - lacunas[i][1] - lacunas[i][3]) * pintura.getHeight());
            int rw = (int)(lacunas[i][2] * pintura.getWidth());
            int rh = (int)(lacunas[i][3] * pintura.getHeight());
            fragmentos[i] = new TextureRegion(pintura, rx, ry, rw, rh);

            float lx = i < 3 ? 20 : w - fragSize - 20;
            float ly = h * 0.15f + (i % 3) * (fragSize + 20);
            fragX[i] = lx;
            fragY[i] = ly;
            fragOrigX[i] = lx;
            fragOrigY[i] = ly;
        }
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClearColor(0.05f, 0.03f, 0.08f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        if (tempoErro > 0) tempoErro -= delta;
        if (concluido) { renderConclusao(w, h); return; }

        batch.begin();
        batch.draw(pintura, pinturaX, pinturaY, pinturaW, pinturaH);
        batch.end();

        for (int i = 0; i < lacunas.length; i++) {
            float lx = pinturaX + lacunas[i][0] * pinturaW;
            float ly = pinturaY + lacunas[i][1] * pinturaH;
            float lw = lacunas[i][2] * pinturaW;
            float lh = lacunas[i][3] * pinturaH;

            if (!fragColocado[i]) {
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(0, 0, 0, 0.85f);
                shape.rect(lx, ly, lw, lh);
                shape.end();
                shape.begin(ShapeRenderer.ShapeType.Line);
                shape.setColor(0.9f, 0.7f, 0.1f, 0.9f);
                shape.rect(lx, ly, lw, lh);
                shape.end();
            } else {
                batch.begin();
                batch.draw(fragmentos[i], lx, ly, lw, lh);
                batch.end();
            }
        }

        if (tempoErro > 0 && lacunaErrada != null) {
            shape.begin(ShapeRenderer.ShapeType.Line);
            shape.setColor(1, 0.1f, 0.1f, tempoErro);
            shape.rect(lacunaErrada[0], lacunaErrada[1], lacunaErrada[2], lacunaErrada[3]);
            shape.end();
        }

        for (int i = 0; i < lacunas.length; i++) {
            if (fragColocado[i] || dragging == i) continue;
            desenharFragmento(i, fragX[i], fragY[i]);
        }

        if (dragging >= 0) {
            desenharFragmento(dragging, fragX[dragging], fragY[dragging]);
        }

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0, 0, 0, 0.75f);
        shape.rect(0, h - 50, w, 50);
        shape.end();
        batch.begin();
        font.setColor(0.9f, 0.7f, 0.1f, 1);
        int restantes = 0;
        for (boolean f : fragColocado) if (!f) restantes++;
        font.draw(batch, titulosObras[obraIdx] + " | Arraste os fragmentos! Restam: " + restantes, 10, h - 15);
        batch.end();

        handleInput(w, h);

        boolean todos = true;
        for (boolean f : fragColocado) if (!f) { todos = false; break; }
        if (todos) concluido = true;
    }

    private void desenharFragmento(int idx, float x, float y) {
        batch.begin();
        batch.draw(fragmentos[idx], x, y, fragSize, fragSize);
        batch.end();
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(0.9f, 0.7f, 0.1f, 1);
        shape.rect(x, y, fragSize, fragSize);
        shape.end();
    }

    private void handleInput(float w, float h) {
        boolean tocandoAgora = Gdx.input.isTouched();
        float tx = Gdx.input.getX();
        float ty = h - Gdx.input.getY();

        if (tocandoAgora && !tocandoAntes && dragging < 0) {
            for (int i = 0; i < lacunas.length; i++) {
                if (fragColocado[i]) continue;
                if (tx >= fragX[i] && tx <= fragX[i] + fragSize &&
                    ty >= fragY[i] && ty <= fragY[i] + fragSize) {
                    dragging = i;
                    dragOffX = tx - fragX[i];
                    dragOffY = ty - fragY[i];
                    break;
                }
            }
        }

        if (tocandoAgora && dragging >= 0) {
            fragX[dragging] = tx - dragOffX;
            fragY[dragging] = ty - dragOffY;
        }

        if (!tocandoAgora && tocandoAntes && dragging >= 0) {
            boolean solto = false;
            for (int z = 0; z < lacunas.length; z++) {
                if (fragColocado[z]) continue;
                float lx = pinturaX + lacunas[z][0] * pinturaW;
                float ly = pinturaY + lacunas[z][1] * pinturaH;
                float lw = lacunas[z][2] * pinturaW;
                float lh = lacunas[z][3] * pinturaH;

                float cx = fragX[dragging] + fragSize/2;
                float cy = fragY[dragging] + fragSize/2;

                if (cx >= lx && cx <= lx + lw && cy >= ly && cy <= ly + lh) {
                    if (fragCorreto[dragging] == z) {
                        fragColocado[z] = true;
                    } else {
                        lacunaErrada = new float[]{lx, ly, lw, lh};
                        tempoErro = 0.8f;
                        fragX[dragging] = fragOrigX[dragging];
                        fragY[dragging] = fragOrigY[dragging];
                    }
                    solto = true;
                    break;
                }
            }
            if (!solto) {
                fragX[dragging] = fragOrigX[dragging];
                fragY[dragging] = fragOrigY[dragging];
            }
            dragging = -1;
        }

        tocandoAntes = tocandoAgora;
    }

    private void renderConclusao(float w, float h) {
        batch.begin();
        batch.draw(pintura, pinturaX, pinturaY, pinturaW, pinturaH);
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.05f, 0.03f, 0.08f, 0.88f);
        shape.rect(w/2 - 320, h/2 - 80, 640, 160);
        shape.end();

        batch.begin();
        font.setColor(0.9f, 0.7f, 0.1f, 1);
        font.draw(batch, "Obra restaurada! Wunderbar!", w/2 - 220, h/2 + 40);
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
        pintura.dispose();
    }
}
