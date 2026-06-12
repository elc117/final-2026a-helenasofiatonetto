package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TelaEscolha implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;
    private Texture[] icones;
    private String[] nomes = {"pizza", "controle", "livro", "paleta", "laptop", "bonsai", "emoji", "galinha", "chave"};

    private float iconeSize = 80;
    private float padding = 30;
    private float pergW, pergH, pergX, pergY;
    private float startX, startY;

    public TelaEscolha(GlobeDelegates jogo) {
        this.jogo = jogo;
        this.jogador = new Jogador();
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(2f);
        fundo = new Texture("fundoInicio.png");

        icones = new Texture[nomes.length];
        for (int i = 0; i < nomes.length; i++) {
            icones[i] = new Texture(nomes[i] + ".png");
        }

        pergW = 3 * iconeSize + 2 * padding + 100;
        pergH = 3 * iconeSize + 2 * padding + 120;
        pergX = (Gdx.graphics.getWidth() - pergW) / 2;
        pergY = (Gdx.graphics.getHeight() - pergH) / 2;
        startX = pergX + 50;
        startY = pergY + 40;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        batch.draw(fundo, 0, 0, w, h);
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.76f, 0.60f, 0.35f, 1);
        shape.rect(pergX, pergY, pergW, pergH);
        shape.setColor(0.65f, 0.48f, 0.25f, 1);
        shape.rect(pergX, pergY, pergW, 20);
        shape.rect(pergX, pergY + pergH - 20, pergW, 20);
        shape.end();

        batch.begin();
        font.setColor(0.1f, 0.5f, 0.1f, 1);
        font.draw(batch, "ESCOLHA UM ICONE", pergX + 50, pergY + pergH - 30);

        for (int i = 0; i < icones.length; i++) {
            int col = i % 3;
            int row = 2 - (i / 3);
            float x = startX + col * (iconeSize + padding);
            float y = startY + row * (iconeSize + padding);
            batch.draw(icones[i], x, y, iconeSize, iconeSize);
        }
        batch.end();

        if (Gdx.input.justTouched()) {
            float tx = Gdx.input.getX();
            float ty = h - Gdx.input.getY();
            for (int i = 0; i < nomes.length; i++) {
                int col = i % 3;
                int row = 2 - (i / 3);
                float x = startX + col * (iconeSize + padding);
                float y = startY + row * (iconeSize + padding);
                if (tx >= x && tx <= x + iconeSize && ty >= y && ty <= y + iconeSize) {
                    jogador.setIcone(nomes[i]);
                    jogo.setScreen(new TelaMundo(jogo, jogador));
                }
            }
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
        for (Texture t : icones) t.dispose();
    }
}
