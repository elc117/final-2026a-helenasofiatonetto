package com.GlobeDelegates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        btnW = 120; btnH = 40;
        btnX = 20; btnY = h - 60;

        switch (jogador.getPais()) {
            case "Japao":
                casinha = new Texture("japao/japao_casinha2.png");
                casinhaX = w * 0.78f; casinhaY = h * 0.72f - casinhaSize;
                break;
            case "Canada":
                casinha = new Texture("canada/casinha.png");
                casinhaX = w * 0.22f; casinhaY = h * 0.72f - casinhaSize;
                break;
            case "Mexico":
                casinha = new Texture("mexico/casa_mexico.png");
                casinhaX = w * 0.22f; casinhaY = h * 0.55f - casinhaSize;
                break;
            case "Nova Zelandia":
                casinha = new Texture("nz/casinha.png");
                casinhaX = w * 0.82f; casinhaY = h * 0.42f - casinhaSize;
                break;
            case "Groelandia":
                casinha = new Texture("groelandia/casinha.png");
                casinhaX = w * 0.35f; casinhaY = h * 0.88f - casinhaSize;
                break;
            case "Austria":
                casinha = new Texture("austria/casinha.png");
                casinhaX = w * 0.50f; casinhaY = h * 0.72f - casinhaSize;
                break;
            case "Egito":
                casinha = new Texture("egito/casinha.png");
                casinhaX = w * 0.50f; casinhaY = h * 0.60f - casinhaSize;
                break;
            case "Brasil":
                casinha = new Texture("brasil/casinha.jpg");
                casinhaX = w * 0.30f; casinhaY = h * 0.55f - casinhaSize;
                break;
            case "Bussola":
                casinha = new Texture("bussola/casinha.png");
                casinhaX = w * 0.15f; casinhaY = h * 0.65f - casinhaSize;
                break;
            default:
                casinha = new Texture("japao/japao_casinha2.png");
                casinhaX = w * 0.5f; casinhaY = h * 0.5f;
                break;
        }

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

        if (Gdx.input.isKeyPressed(Keys.LEFT))  persX -= persVX * delta;
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) persX += persVX * delta;
        if (Gdx.input.isKeyPressed(Keys.UP))    persY += persVX * delta;
        if (Gdx.input.isKeyPressed(Keys.DOWN))  persY -= persVX * delta;
        persX = Math.max(0, Math.min(persX, w - persSize));
        persY = Math.max(0, Math.min(persY, h - persSize * 2));

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.2f, 0.4f, 0.9f, 1);
        shape.rect(persX, persY, persSize, persSize * 1.5f);
        shape.setColor(1f, 0.85f, 0.7f, 1);
        shape.circle(persX + persSize/2, persY + persSize * 1.7f, persSize/2);
        shape.end();

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
                switch (jogador.getPais()) {
                    case "Japao":
                        jogo.setScreen(new TelaVilagemJapao(jogo, jogador, false, false));
                        break;
                    case "Canada":
                        jogo.setScreen(new TelaVilagemCanada(jogo, jogador, false, false));
                        break;
                    case "Mexico":
                        jogo.setScreen(new TelaVilagemMexico(jogo, jogador, false, false));
                        break;
                    case "Nova Zelandia":
                        jogo.setScreen(new TelaVilagemNZ(jogo, jogador, false, false));
                        break;
                    case "Austria":
                        jogo.setScreen(new TelaVilagemAustria(jogo, jogador, false, false));
                        break;
                    case "Groelandia":
                        jogo.setScreen(new TelaVilagemGroelandia(jogo, jogador, false, false));
                        break;
                    case "Egito":
                        jogo.setScreen(new TelaVilagemEgito(jogo, jogador, false, false));
                        break;
                    case "Brasil":
                        jogo.setScreen(new TelaVilagemBrasil(jogo, jogador, false, false));
                        break;
                    case "Bussola":
                        jogo.setScreen(new TelaVilagemBussola(jogo, jogador, false, false, false));
                        break;
                }
            }
        }

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
