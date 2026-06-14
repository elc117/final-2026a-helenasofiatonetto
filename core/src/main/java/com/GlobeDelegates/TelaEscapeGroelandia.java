package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.Collections;

public class TelaEscapeGroelandia implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;

    private Texture fundo;
    private Texture[] objetos;
    private String[] nomesArquivo = {"osso_baleia", "anorak", "barco", "peixe", "foca"};
    private String[] nomesCorretos = {"Osso de Baleia", "Anorak", "Barco", "Peixe Artico", "Foca"};
    private String[] iniciais = {"OB", "A", "B", "P", "F"};

    private int[] ordemEmbaralhada;
    private int objetoAtual = 0;
    private int coletados = 0;
    private boolean mostrándoPergunta = false;
    private boolean mostrándoSenha = false;
    private int opcaoSelecionada = -1;
    private boolean acertou = false;
    private float tempoMensagem = 0;
    private float tempoEspera = 0;

    private String senhaCorreta = "";
    private String senhaDigitada = "";
    private boolean senhaErrada = false;

    private float persX, persY;
    private float persVX = 200;
    private float persSize = 40;

    private float objSize = 100;
    private float btnW = 280, btnH = 50;
    private String[] opcoesMisturadas;
    private float[] objX = {120, 350, 580, 810, 200};
    private float[] objY;

    public TelaEscapeGroelandia(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.6f);
        fundo = new Texture("groelandia/escape.jpg");

        objetos = new Texture[nomesArquivo.length];
        for (int i = 0; i < nomesArquivo.length; i++) {
            objetos[i] = new Texture("groelandia/" + nomesArquivo[i] + ".png");
        }

        float h = Gdx.graphics.getHeight();
        objY = new float[]{h*0.55f, h*0.35f, h*0.6f, h*0.4f, h*0.25f};

        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < nomesArquivo.length; i++) indices.add(i);
        Collections.shuffle(indices);
        ordemEmbaralhada = new int[nomesArquivo.length];
        for (int i = 0; i < nomesArquivo.length; i++) ordemEmbaralhada[i] = indices.get(i);

        persX = 50;
        persY = h * 0.15f;
    }

    private int idxAtual() { return ordemEmbaralhada[objetoAtual]; }

    private void gerarOpcoes() {
        int idx = idxAtual();
        opcoesMisturadas = new String[4];
        opcoesMisturadas[0] = nomesCorretos[idx];
        int[] outros = new int[]{(idx+1)%nomesArquivo.length, (idx+2)%nomesArquivo.length, (idx+3)%nomesArquivo.length};
        for (int i = 0; i < 3; i++) opcoesMisturadas[i+1] = nomesCorretos[outros[i]];
        for (int i = 3; i > 0; i--) {
            int j = (int)(Math.random() * (i+1));
            String tmp = opcoesMisturadas[i]; opcoesMisturadas[i] = opcoesMisturadas[j]; opcoesMisturadas[j] = tmp;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.7f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        batch.draw(fundo, 0, 0, w, h);
        batch.end();

        if (tempoEspera > 0) { tempoEspera -= delta; return; }
        if (coletados >= nomesArquivo.length) mostrándoSenha = true;
        if (mostrándoSenha) { renderSenha(w, h); return; }

        if (!mostrándoPergunta) {
            if (Gdx.input.isKeyPressed(Keys.LEFT))  persX -= persVX * delta;
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) persX += persVX * delta;
            if (Gdx.input.isKeyPressed(Keys.UP))    persY += persVX * delta;
            if (Gdx.input.isKeyPressed(Keys.DOWN))  persY -= persVX * delta;
            persX = Math.max(0, Math.min(persX, w - persSize));
            persY = Math.max(0, Math.min(persY, h - persSize * 2));
        }

        batch.begin();
        for (int i = objetoAtual; i < nomesArquivo.length; i++) {
            batch.draw(objetos[ordemEmbaralhada[i]], objX[i], objY[i], objSize, objSize);
        }
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.2f, 0.4f, 0.9f, 1);
        shape.rect(persX, persY, persSize, persSize * 1.5f);
        shape.setColor(1f, 0.85f, 0.7f, 1);
        shape.circle(persX + persSize/2, persY + persSize * 1.7f, persSize/2);
        shape.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.1f, 0.2f, 0.85f);
        shape.rect(0, h - 55, w, 55);
        shape.end();
        batch.begin();
        font.setColor(1, 1, 0.5f, 1);
        font.draw(batch, "Ande com as setas e toque no objeto! (" + coletados + "/" + nomesArquivo.length + ")", 20, h - 18);
        batch.end();

        if (!mostrándoPergunta) {
            float distX = Math.abs(persX - objX[objetoAtual]);
            float distY = Math.abs(persY - objY[objetoAtual]);
            if (distX < 80 && distY < 80) {
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(0.18f, 0.75f, 0.75f, 1);
                shape.rect(persX - 10, persY + persSize * 2 + 10, 160, 40);
                shape.end();
                batch.begin();
                font.setColor(0, 0, 0, 1);
                font.draw(batch, "Toque: coletar", persX, persY + persSize * 2 + 38);
                batch.end();
            }

            if (Gdx.input.justTouched()) {
                float tx = Gdx.input.getX();
                float ty = h - Gdx.input.getY();
                if (tx >= objX[objetoAtual] && tx <= objX[objetoAtual] + objSize &&
                    ty >= objY[objetoAtual] && ty <= objY[objetoAtual] + objSize) {
                    gerarOpcoes();
                    mostrándoPergunta = true;
                    tempoEspera = 0.3f;
                }
            }
        } else {
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(0.15f, 0.15f, 0.25f, 0.92f);
            shape.rect(w/2 - 320, 60, 640, 460);
            shape.end();

            batch.begin();
            font.setColor(1, 1, 0.5f, 1);
            font.draw(batch, "Qual e o nome deste objeto?", w/2 - 220, h - 80);
            batch.draw(objetos[idxAtual()], w/2 - 50, 390, 100, 100);
            batch.end();

            for (int i = 0; i < 4; i++) {
                float bx = w/2 - btnW/2;
                float by = 350 - i * 75;
                shape.begin(ShapeRenderer.ShapeType.Filled);
                if (opcaoSelecionada == i) {
                    shape.setColor(acertou ? 0.1f : 0.8f, acertou ? 0.8f : 0.1f, 0.1f, 1);
                } else {
                    shape.setColor(0.3f, 0.3f, 0.5f, 1);
                }
                shape.rect(bx, by, btnW, btnH);
                shape.end();
                batch.begin();
                font.setColor(1, 1, 1, 1);
                font.draw(batch, opcoesMisturadas[i], bx + 20, by + btnH - 10);
                batch.end();
            }

            if (opcaoSelecionada == -1 && Gdx.input.justTouched()) {
                float tx = Gdx.input.getX();
                float ty = h - Gdx.input.getY();
                for (int i = 0; i < 4; i++) {
                    float bx = w/2 - btnW/2;
                    float by = 350 - i * 75;
                    if (tx >= bx && tx <= bx + btnW && ty >= by && ty <= by + btnH) {
                        opcaoSelecionada = i;
                        acertou = opcoesMisturadas[i].equals(nomesCorretos[idxAtual()]);
                        tempoMensagem = 1.5f;
                    }
                }
            }

            if (opcaoSelecionada != -1) {
                tempoMensagem -= delta;
                batch.begin();
                font.setColor(acertou ? 0.2f : 1f, acertou ? 1f : 0.2f, 0.2f, 1);
                font.draw(batch, acertou ? "Correto!" : "Errado! Tente novamente!", w/2 - 130, 75);
                batch.end();
                if (tempoMensagem <= 0) {
                    if (acertou) {
                        senhaCorreta += iniciais[idxAtual()];
                        coletados++;
                        objetoAtual++;
                    }
                    opcaoSelecionada = -1;
                    mostrándoPergunta = false;
                    tempoEspera = 0.3f;
                }
            }
        }
    }

    private void renderSenha(float w, float h) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.1f, 0.2f, 0.95f);
        shape.rect(w/2 - 350, h/2 - 200, 700, 400);
        shape.end();

        batch.begin();
        font.setColor(1, 1, 0.5f, 1);
        font.draw(batch, "Todos os objetos coletados!", w/2 - 240, h/2 + 160);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Digite as iniciais na ordem que coletou:", w/2 - 300, h/2 + 110);
        font.draw(batch, "OB=Osso de Baleia  A=Anorak  B=Barco", w/2 - 280, h/2 + 70);
        font.draw(batch, "P=Peixe Artico  F=Foca", w/2 - 160, h/2 + 35);
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.2f, 0.2f, 0.35f, 1);
        shape.rect(w/2 - 200, h/2 - 30, 400, 55);
        shape.end();

        batch.begin();
        font.setColor(0.5f, 1f, 0.5f, 1);
        font.draw(batch, senhaDigitada + "|", w/2 - 185, h/2 + 15);
        if (senhaErrada) {
            font.setColor(1, 0.3f, 0.3f, 1);
            font.draw(batch, "Senha incorreta! Tente novamente.", w/2 - 250, h/2 - 60);
        }
        font.setColor(0.8f, 0.8f, 0.5f, 1);
        font.draw(batch, "ENTER confirmar  |  BACKSPACE apagar", w/2 - 280, h/2 - 110);
        batch.end();

        for (int k = Keys.A; k <= Keys.Z; k++) {
            if (Gdx.input.isKeyJustPressed(k)) {
                senhaDigitada += Keys.toString(k).toUpperCase();
                senhaErrada = false;
            }
        }
        if (Gdx.input.isKeyJustPressed(Keys.BACKSPACE) && senhaDigitada.length() > 0) {
            senhaDigitada = senhaDigitada.substring(0, senhaDigitada.length() - 1);
            senhaErrada = false;
        }
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            if (senhaDigitada.equals(senhaCorreta)) {
                jogo.setScreen(new TelaVilagemGroelandia(jogo, jogador, true, false));
            } else {
                senhaErrada = true;
                senhaDigitada = "";
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
        for (Texture t : objetos) t.dispose();
    }
}
