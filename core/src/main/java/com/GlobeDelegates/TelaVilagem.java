package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TelaVilagem implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    private float persX, persY;
    private float persSize = 40;
    private float persVX = 250;

    private float[] entradaX = {150, 450, 750};
    private float[] entradaY;
    private float entradaSize = 80;
    private String[] labels = {"Escape Room", "Delegacao", "Bonus"};
    private boolean[] desbloqueado;

    public TelaVilagem(GlobeDelegates jogo, Jogador jogador, boolean escapeCompleto, boolean delegacaoCompleta) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.5f);

        String pasta = getPasta(jogador.getPais());
        fundo = new Texture(pasta + "/telaEscolha.jpg");

        float h = Gdx.graphics.getHeight();
        entradaY = new float[]{h * 0.4f, h * 0.4f, h * 0.4f};
        desbloqueado = new boolean[]{true, escapeCompleto, escapeCompleto && delegacaoCompleta};

        persX = 50;
        persY = h * 0.3f;
    }

    private String getPasta(String pais) {
        switch (pais) {
            case "Japao": return "japao";
            case "Mexico": return "mexico";
            case "Canada": return "canada";
            case "Nova Zelandia": return "nz";
            case "Groelandia": return "groelandia";
            case "Austria": return "austria";
            case "Egito": return "egito";
            case "Brasil": return "brasil";
            case "Bussola": return "bussola";
            default: return "japao";
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            jogo.setScreen(new TelaEscolha(jogo));
            return;
        }

        // WASD mover
        if (Gdx.input.isKeyPressed(Keys.LEFT)) persX -= persVX * delta;
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) persX += persVX * delta;
        persX = Math.max(0, Math.min(persX, w - persSize));

        for (int i = 0; i < 3; i++) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(desbloqueado[i] ? 0.2f : 0.5f, desbloqueado[i] ? 0.7f : 0.5f, 0.2f, 0.7f);
            shape.rect(entradaX[i] - 10, entradaY[i] - 10, entradaSize + 20, entradaSize + 20);
            shape.end();
            batch.begin();
            font.setColor(1, 1, 1, 1);
            font.draw(batch, labels[i], entradaX[i] - 10, entradaY[i] + entradaSize + 25);
            if (!desbloqueado[i]) {
                font.setColor(1, 0.5f, 0.5f, 1);
                font.draw(batch, "(bloqueado)", entradaX[i], entradaY[i] + entradaSize + 5);
            }
            batch.end();
        }

        // Personagem
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.2f, 0.4f, 0.9f, 1);
        shape.rect(persX, persY, persSize, persSize * 1.5f);
        shape.setColor(1f, 0.85f, 0.7f, 1);
        shape.circle(persX + persSize/2, persY + persSize * 1.7f, persSize/2);
        shape.end();

        for (int i = 0; i < 3; i++) {
            float dist = Math.abs(persX - entradaX[i]);
            if (dist < 80 && desbloqueado[i]) {
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(0.18f, 0.75f, 0.75f, 1);
                shape.rect(persX - 10, persY + persSize * 2 + 10, 180, 40);
                shape.end();
                batch.begin();
                font.setColor(0, 0, 0, 1);
                font.draw(batch, "SPACE: entrar", persX, persY + persSize * 2 + 38);
                batch.end();

                if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
                    if (i == 0) jogo.setScreen(new TelaEscape(jogo, jogador));
                    else if (i == 1) jogo.setScreen(new TelaDelegacao(jogo, jogador));
                    else jogo.setScreen(new TelaBonus(jogo, jogador));
                }
            }
        }

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.1f, 0.2f, 0.85f);
        shape.rect(0, h - 50, w, 50);
        shape.end();
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Setas=mover | SPACE=entrar | ESC=sair", 20, h - 18);
        batch.end();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
        batch.dispose(); shape.dispose(); font.dispose(); fundo.dispose();
    }
}
