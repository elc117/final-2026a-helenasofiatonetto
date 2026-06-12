package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.Collections;

public class TelaDelegacaoCanada implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    private String[][] perguntasHistoria = {
        {"Qual e a capital do Canada?", "Toronto", "Vancouver", "Ottawa", "Montreal", "2"},
        {"Qual e o animal simbolo do Canada?", "Urso polar", "Alce", "Castor", "Aguia", "2"},
        {"Em que ano o Canada se tornou independente?", "1776", "1867", "1901", "1945", "1"},
        {"Qual e a moeda do Canada?", "Dolar americano", "Libra", "Dolar canadense", "Franco", "2"},
        {"Quantas provincias tem o Canada?", "8", "10", "12", "13", "1"},
        {"Qual e o esporte nacional do Canada?", "Baseball", "Futebol", "Hoquei no gelo", "Basquete", "2"},
        {"Qual e a segunda lingua oficial do Canada?", "Espanhol", "Frances", "Italiano", "Alemao", "1"},
        {"Qual e a maior cidade do Canada?", "Ottawa", "Vancouver", "Toronto", "Montreal", "2"},
        {"Como se chama a bandeira do Canada?", "Union Jack", "Maple Leaf", "Fleur de Lis", "Red Ensign", "1"},
        {"Qual povo indigena e originario do Artico canadense?", "Cree", "Inuit", "Mohawk", "Ojibwe", "1"}
    };

    private String[][] perguntasIndigenas = {
        {"O que e um totem?", "Uma danca", "Um escultura sagrada com figuras", "Um ritual", "Uma roupa", "1"},
        {"Como se chama a habitacao tradicional dos Inuit?", "Tenda", "Iglu", "Maloca", "Longhouse", "1"},
        {"O que e o powwow?", "Um prato tipico", "Uma cerimonia de danca e musica", "Uma lingua", "Um artesanato", "1"},
        {"Qual instrumento e tipico dos povos indigenas canadenses?", "Violino", "Tambor", "Flauta europeia", "Acordeon", "1"},
        {"O que significa 'Inuit'?", "Guerreiros", "Povo do gelo", "Povo", "Caçadores", "2"},
        {"Qual e a importancia do castor para os indigenas?", "Apenas um animal", "Simbolo de trabalho e perseveranca", "Animal perigoso", "Fonte de medicina", "1"},
        {"Como se chama a arte de contar historias dos indigenas?", "Escrita", "Pintura rupestre", "Tradicao oral", "Teatro", "2"}
    };

    private String[][] perguntasSelecionadas;
    private int perguntaAtual = 0;
    private int fase = 0;
    private int acertos = 0;
    private int opcaoSelecionada = -1;
    private boolean acertou = false;
    private float tempoMensagem = 0;
    private float tempoEspera = 0;
    private float btnW = 500, btnH = 55;
    private String[] opcoesAtuais;

    public TelaDelegacaoCanada(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.6f);
        fundo = new Texture("canada/delegacao.jpg");
        sortearPerguntas();
    }

    private void sortearPerguntas() {
        String[][] banco = (fase == 0) ? perguntasHistoria : perguntasIndigenas;
        int qtd = (fase == 0) ? 6 : 5;

        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < banco.length; i++) indices.add(i);
        Collections.shuffle(indices);

        perguntasSelecionadas = new String[qtd][];
        for (int i = 0; i < qtd; i++) perguntasSelecionadas[i] = banco[indices.get(i)];
        perguntaAtual = 0;
        acertos = 0;
        opcoesAtuais = null;
    }

    private String[] getOpcoes() {
        String[] p = perguntasSelecionadas[perguntaAtual];
        int correta = Integer.parseInt(p[5]);
        String[] opcoes = {p[1], p[2], p[3], p[4]};

        ArrayList<Integer> idx = new ArrayList<>();
        for (int i = 0; i < 4; i++) idx.add(i);
        Collections.shuffle(idx);

        String[] embaralhadas = new String[4];
        int novaCorreta = 0;
        for (int i = 0; i < 4; i++) {
            embaralhadas[i] = opcoes[idx.get(i)];
            if (idx.get(i) == correta) novaCorreta = i;
        }
        perguntasSelecionadas[perguntaAtual][5] = String.valueOf(novaCorreta);
        return embaralhadas;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.9f, 0.85f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        batch.draw(fundo, 0, 0, w, h);
        batch.end();

        if (tempoEspera > 0) { tempoEspera -= delta; return; }

        if (perguntaAtual >= perguntasSelecionadas.length) {
            if (fase == 0) { fase = 1; sortearPerguntas(); return; }
            else { renderConclusao(w, h); return; }
        }

        if (opcoesAtuais == null) opcoesAtuais = getOpcoes();
        renderPergunta(delta, w, h);
    }

    private void renderPergunta(float delta, float w, float h) {
        String[] p = perguntasSelecionadas[perguntaAtual];
        String[] titulos = {"Historia do Canada", "Povos Indigenas Canadenses"};

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.05f, 0.15f, 0.9f);
        shape.rect(w/2 - 360, 80, 720, 520);
        shape.end();

        batch.begin();
        font.setColor(0.8f, 0.1f, 0.1f, 1);
        font.draw(batch, titulos[fase], w/2 - 240, h - 60);
        font.setColor(0.8f, 0.8f, 0.8f, 1);
        font.draw(batch, "Pergunta " + (perguntaAtual + 1) + "/" + perguntasSelecionadas.length + "  |  Acertos: " + acertos, w/2 - 180, h - 95);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, p[0], w/2 - 320, h - 140, 640, 1, true);
        batch.end();

        for (int i = 0; i < 4; i++) {
            float bx = w/2 - btnW/2;
            float by = 300 - i * 75;
            shape.begin(ShapeRenderer.ShapeType.Filled);
            if (opcaoSelecionada == i) {
                shape.setColor(acertou ? 0.1f : 0.8f, acertou ? 0.8f : 0.1f, 0.1f, 1);
            } else {
                shape.setColor(0.25f, 0.25f, 0.45f, 1);
            }
            shape.rect(bx, by, btnW, btnH);
            shape.end();
            batch.begin();
            font.setColor(1, 1, 1, 1);
            font.draw(batch, (i+1) + ". " + opcoesAtuais[i], bx + 15, by + btnH - 10);
            batch.end();
        }

        if (opcaoSelecionada == -1 && Gdx.input.justTouched()) {
            float tx = Gdx.input.getX();
            float ty = h - Gdx.input.getY();
            for (int i = 0; i < 4; i++) {
                float bx = w/2 - btnW/2;
                float by = 300 - i * 75;
                if (tx >= bx && tx <= bx + btnW && ty >= by && ty <= by + btnH) {
                    opcaoSelecionada = i;
                    acertou = (i == Integer.parseInt(p[5]));
                    if (acertou) acertos++;
                    tempoMensagem = 1.5f;
                }
            }
        }

        if (opcaoSelecionada != -1) {
            tempoMensagem -= delta;
            batch.begin();
            font.setColor(acertou ? 0.2f : 1f, acertou ? 1f : 0.2f, 0.2f, 1);
            font.draw(batch, acertou ? "Correto!" : "Errado!", w/2 - 60, 75);
            batch.end();
            if (tempoMensagem <= 0) {
                perguntaAtual++;
                opcaoSelecionada = -1;
                opcoesAtuais = null;
                tempoEspera = 0.3f;
            }
        }
    }

    private void renderConclusao(float w, float h) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.05f, 0.15f, 0.92f);
        shape.rect(w/2 - 300, h/2 - 100, 600, 200);
        shape.end();

        batch.begin();
        font.setColor(0.8f, 0.1f, 0.1f, 1);
        font.draw(batch, "Delegacao concluida!", w/2 - 180, h/2 + 60);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Toque para voltar a vila", w/2 - 180, h/2 + 10);
        batch.end();

        if (Gdx.input.justTouched()) {
            jogo.setScreen(new TelaVilagemCanada(jogo, jogador, true, true));
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
