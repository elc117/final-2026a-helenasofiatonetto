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

public class TelaDelegacaoJapao implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    private String[][] perguntasHistoria = {
        {"Qual e a capital do Japao?", "Tokio", "Osaka", "Kyoto", "Hiroshima", "0"},
        {"Quais sao as 4 ilhas principais do Japao?", "Honshu, Hokkaido, Kyushu, Shikoku", "Okinawa, Tokyo, Osaka, Kyoto", "Honshu, Fuji, Sakura, Edo", "Kyushu, Nagasaki, Hiroshima, Nara", "0"},
        {"Como se chama o teatro tradicional japones?", "Kabuki", "Noh", "Bunraku", "Taiko", "0"},
        {"Qual era o titulo do imperador medieval japones?", "Shogun", "Tenno", "Samurai", "Daimyo", "1"},
        {"O que e o Bushido?", "Uma danca", "Um prato tipico", "O codigo de honra dos samurais", "Um festival", "2"},
        {"Em que ano o Japao sediou as primeiras Olimpiadas?", "1956", "1964", "1972", "1980", "1"},
        {"Qual religiao e nativa do Japao?", "Budismo", "Hinduismo", "Xintoismo", "Taoismo", "2"},
        {"Como se chama a escrita japonesa de origem chinesa?", "Hiragana", "Katakana", "Kanji", "Romaji", "2"},
        {"Qual periodo historico japones durou de 1603 a 1868?", "Meiji", "Heian", "Edo", "Nara", "2"},
        {"O que e o Monte Fuji?", "Um templo", "Uma ilha", "Um vulcao", "Um castelo", "2"}
    };

    private String[][] perguntasEscrita = {
        {"O que significa 'Arigatou'?", "Obrigado", "Com licenca", "Oi", "Tchau", "0"},
        {"O que significa 'Sakura'?", "Flor de cerejeira", "Montanha", "Rio", "Samurai", "0"},
        {"O que significa 'Kawaii'?", "Fofo/bonito", "Perigoso", "Grande", "Velho", "0"},
        {"O que significa 'Sensei'?", "Professor", "Guerreiro", "Rei", "Medico", "0"},
        {"O que significa 'Manga'?", "Historia em quadrinhos", "Fruta", "Musica", "Danca", "0"},
        {"O que significa 'Neko'?", "Cachorro", "Gato", "Peixe", "Passaro", "1"},
        {"O que significa 'Hana'?", "Ceu", "Mar", "Flor", "Pedra", "2"},
        {"O que significa 'Samurai'?", "Pescador", "Monge", "Guerreiro", "Comerciante", "2"},
        {"O que significa 'Umi'?", "Montanha", "Mar", "Rio", "Floresta", "1"},
        {"O que significa 'Torii'?", "Castelo", "Jardim", "Portal de santuario", "Ponte", "2"}
    };

    private String[][] perguntasSelecionadas;
    private int perguntaAtual = 0;
    private int fase = 0; // 0 = historia, 1 = escrita
    private int acertos = 0;
    private int opcaoSelecionada = -1;
    private boolean acertou = false;
    private float tempoMensagem = 0;
    private float tempoEspera = 0;

    private float btnW = 500, btnH = 55;

    public TelaDelegacaoJapao(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.6f);
        fundo = new Texture("japao/delegacao.jpg");

        sortearPerguntas();
    }

    private void sortearPerguntas() {
        String[][] banco = (fase == 0) ? perguntasHistoria : perguntasEscrita;
        int qtd = (fase == 0) ? 6 : 5;

        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < banco.length; i++) indices.add(i);
        Collections.shuffle(indices);

        perguntasSelecionadas = new String[qtd][];
        for (int i = 0; i < qtd; i++) {
            perguntasSelecionadas[i] = banco[indices.get(i)];
        }
        perguntaAtual = 0;
        acertos = 0;
    }

    private String[] getOpcoes() {
        String[] p = perguntasSelecionadas[perguntaAtual];
        int correta = Integer.parseInt(p[5]);
        String[] opcoes = {p[1], p[2], p[3], p[4]};

        // embaralhar mantendo track da correta
        ArrayList<Integer> idx = new ArrayList<>();
        for (int i = 0; i < 4; i++) idx.add(i);
        Collections.shuffle(idx);

        String[] embaralhadas = new String[4];
        int novaCorreta = 0;
        for (int i = 0; i < 4; i++) {
            embaralhadas[i] = opcoes[idx.get(i)];
            if (idx.get(i) == correta) novaCorreta = i;
        }
        // guarda a correta embaralhada no índice 0 de um array temporário
        perguntasSelecionadas[perguntaAtual][5] = String.valueOf(novaCorreta);
        return embaralhadas;
    }

    private String[] opcoesAtuais;

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

        // Fase concluida
        if (perguntaAtual >= perguntasSelecionadas.length) {
            if (fase == 0) {
                fase = 1;
                sortearPerguntas();
                opcoesAtuais = null;
                return;
            } else {
                renderConclusao(w, h);
                return;
            }
        }

        if (opcoesAtuais == null) opcoesAtuais = getOpcoes();

        renderPergunta(delta, w, h);
    }

    private void renderPergunta(float delta, float w, float h) {
        String[] p = perguntasSelecionadas[perguntaAtual];
        String titulo = (fase == 0) ? "Historia e Cultura do Japao" : "Desafio de Escrita Japonesa";
        int totalPerguntas = perguntasSelecionadas.length;

        // painel
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.05f, 0.15f, 0.9f);
        shape.rect(w/2 - 360, 80, 720, 520);
        shape.end();

        batch.begin();
        font.setColor(1, 0.8f, 0.2f, 1);
        font.draw(batch, titulo, w/2 - 240, h - 60);
        font.setColor(0.8f, 0.8f, 0.8f, 1);
        font.draw(batch, "Pergunta " + (perguntaAtual + 1) + "/" + totalPerguntas + "  |  Acertos: " + acertos, w/2 - 180, h - 95);
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
        font.setColor(1, 1, 0.5f, 1);
        font.draw(batch, "Delegacao concluida!", w/2 - 180, h/2 + 60);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Toque para voltar a vila", w/2 - 180, h/2 + 10);
        batch.end();

        if (Gdx.input.justTouched()) {
            jogo.setScreen(new TelaVilagemJapao(jogo, jogador, true, true));
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
