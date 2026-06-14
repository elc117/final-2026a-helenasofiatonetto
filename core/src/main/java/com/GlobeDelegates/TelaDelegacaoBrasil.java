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

public class TelaDelegacaoBrasil implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    private String[][] perguntasHistoria = {
        {"Qual e a capital do Brasil?", "Sao Paulo", "Rio de Janeiro", "Brasilia", "Salvador", "2"},
        {"Em que ano o Brasil se tornou independente?", "1800", "1822", "1889", "1910", "1"},
        {"Qual e o maior rio do Brasil?", "Sao Francisco", "Parana", "Amazonas", "Tocantins", "2"},
        {"Qual e o bioma que cobre a maior parte do Brasil?", "Cerrado", "Caatinga", "Mata Atlantica", "Amazonia", "3"},
        {"Qual e a moeda do Brasil?", "Cruzeiro", "Real", "Cruzado", "Mil-Reis", "1"},
        {"Quem proclamou a Republica no Brasil?", "Dom Pedro I", "Dom Pedro II", "Deodoro da Fonseca", "Tiradentes", "2"},
        {"Qual e o maior estado do Brasil?", "Minas Gerais", "Para", "Mato Grosso", "Amazonas", "3"},
        {"Qual esporte e mais popular no Brasil?", "Volei", "Basquete", "Futebol", "Natacao", "2"},
        {"Qual e o pais mais populoso da America do Sul?", "Argentina", "Colombia", "Brasil", "Peru", "2"},
        {"Qual cidade brasileira e conhecida como a Cidade Maravilhosa?", "Sao Paulo", "Salvador", "Rio de Janeiro", "Fortaleza", "2"}
    };

    private String[][] perguntasRegioes = {
        {"Qual regiao do Brasil tem o maior numero de estados?", "Norte", "Sul", "Nordeste", "Centro-Oeste", "2"},
        {"Qual e a culinaria tipica do Nordeste?", "Feijoada", "Churrasco", "Baiao de Dois", "Moqueca", "2"},
        {"Qual danca e tipica do Nordeste brasileiro?", "Samba", "Forro", "Frevo", "Carimbo", "1"},
        {"Qual festival e famoso em Parintins, Amazonas?", "Carnaval", "Festa Junina", "Festival do Boi-Bumba", "Oktoberfest", "2"},
        {"Qual e o prato nacional do Brasil?", "Coxinha", "Pao de queijo", "Feijoada", "Acaraje", "2"},
        {"Qual cidade e famosa pelo seu carnaval com blocos de rua?", "Sao Paulo", "Recife e Olinda", "Brasilia", "Belo Horizonte", "1"},
        {"De qual estado e tipico o pao de queijo?", "Sao Paulo", "Rio de Janeiro", "Minas Gerais", "Goias", "2"},
        {"Qual instrumento e mais usado no samba?", "Guitarra", "Pandeiro", "Violao", "Flauta", "1"}
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

    public TelaDelegacaoBrasil(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.6f);
        fundo = new Texture("brasil/delegacao.jpg");
        sortearPerguntas();
    }

    private void sortearPerguntas() {
        String[][] banco = (fase == 0) ? perguntasHistoria : perguntasRegioes;
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
        Gdx.gl.glClearColor(0.1f, 0.5f, 0.1f, 1);
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
        String[] titulos = {"Historia do Brasil", "Regioes e Cultura Brasileira"};

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.05f, 0.2f, 0.05f, 0.9f);
        shape.rect(w/2 - 360, 80, 720, 520);
        shape.end();

        batch.begin();
        font.setColor(0.3f, 1f, 0.3f, 1);
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
                shape.setColor(0.1f, 0.35f, 0.1f, 1);
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
        shape.setColor(0.05f, 0.2f, 0.05f, 0.92f);
        shape.rect(w/2 - 300, h/2 - 100, 600, 200);
        shape.end();

        batch.begin();
        font.setColor(0.3f, 1f, 0.3f, 1);
        font.draw(batch, "Delegacao concluida!", w/2 - 180, h/2 + 60);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Toque para voltar a vila", w/2 - 180, h/2 + 10);
        batch.end();

        if (Gdx.input.justTouched()) {
            jogo.setScreen(new TelaVilagemBrasil(jogo, jogador, true, true));
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
