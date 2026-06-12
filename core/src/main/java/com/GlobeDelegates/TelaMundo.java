package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TelaMundo implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;
    private Texture icone;
    private Texture casinha;

    private float persX, persY;
    private float persSize = 40;
    private float persVX = 200;

    private float casinhaX, casinhaY;
    private float casinhaSize = 100;

    private float btnX, btnY, btnW, btnH;

    public TelaMundo(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.5f);
        fundo = new Texture("mundiJogo.png");
        icone = new Texture(jogador.getIcone() + ".png");
        casinha = new Texture("japao/japao_casinha2.png");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        btnW = 120; btnH = 40;
        btnX = 20; btnY = h - 60;

        // Posição da casinha baseada no país
        switch (jogador.getPais()) {
            case "Japao": casinhaX = w * 0.78f; casinhaY = h * 0.72f - casinhaSize; break;
            default:      casinhaX = w * 0.5f;  casinhaY = h * 0.5f; break;
        }

        // Personagem começa do lado esquerdo
        persX = 50;
        persY = h * 0.4f;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        batch.draw(fundo, 0, 0, w, h);
        batch.draw(casinha, casinhaX, casinhaY, casinhaSize, casinhaSize);
        batch.end();

        // Mover personagem
        if (Gdx.input.isKeyPressed(Keys.LEFT))  persX -= persVX * delta;
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) persX += persVX * delta;
        if (Gdx.input.isKeyPressed(Keys.UP))    persY += persVX * delta;
        if (Gdx.input.isKeyPressed(Keys.DOWN))  persY -= persVX * delta;
        persX = Math.max(0, Math.min(persX, w - persSize));
        persY = Math.max(0, Math.min(persY, h - persSize * 2));

        // Personagem placeholder
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.2f, 0.4f, 0.9f, 1);
        shape.rect(persX, persY, persSize, persSize * 1.5f);
        shape.setColor(1f, 0.85f, 0.7f, 1);
        shape.circle(persX + persSize/2, persY + persSize * 1.7f, persSize/2);
        shape.end();

        // Verificar proximidade com casinha
        float dist = (float) Math.sqrt(Math.pow(persX - casinhaX, 2) + Math.pow(persY - casinhaY, 2));
        if (dist < 100) {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(0.18f, 0.75f, 0.75f, 1);
            shape.rect(persX - 10, persY + persSize * 2 + 10, 180, 40);
            shape.end();
            batch.begin();
            font.setColor(0, 0, 0, 1);
            font.draw(batch, "ESPACO: entrar", persX, persY + persSize * 2 + 38);
            batch.end();

            if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
                if (jogador.getPais().equals("Japao")) {
                    jogo.setScreen(new TelaVilagemJapao(jogo, jogador, false, false));
                }
            }
        }

        // Botão voltar
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.8f, 0.2f, 0.2f, 1);
        shape.rect(btnX, btnY, btnW, btnH);
        shape.end();
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "< VOLTAR", btnX + 10, btnY + btnH - 8);
        batch.end();

        if (Gdx.input.justTouched()) {
            float tx = Gdx.input.getX();
            float ty = h - Gdx.input.getY();
            if (tx >= btnX && tx <= btnX + btnW && ty >= btnY && ty <= btnY + btnH) {
                jogo.setScreen(new TelaEscolha(jogo));
            }
        }

        // HUD
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.1f, 0.2f, 0.8f);
        shape.rect(0, h - 50, w, 50);
        shape.end();
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Use as setas para andar. Chegue na casinha!", 20, h - 18);
        batch.end();
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
        icone.dispose();
        casinha.dispose();
    }
}
