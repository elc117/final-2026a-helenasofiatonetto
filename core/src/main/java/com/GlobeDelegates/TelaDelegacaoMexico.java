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

public class TelaDelegacaoMexico implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    private String[][] perguntasHistoria = {
        {"Qual e a capital do Mexico?", "Cidade do Mexico", "Guadalajara", "Monterrey", "Cancun", "0"},
        {"Qual civilizacao construiu as piramides de Teotihuacan?", "Maias", "Incas", "Astecas", "Olmecas", "2"},
        {"Quem foi o lider da independencia do Mexico?", "Hernan Cortes", "Miguel Hidalgo", "Moctezuma", "Benito Juarez", "1"},
        {"Em que ano o Mexico se tornou independente?", "1776", "1810", "1821", "1848", "2"},
        {"Qual e a moeda do Mexico?", "Real", "Dolar", "Peso", "Sol", "2"},
        {"Como se chama a serpente emplumada da mitologia asteca?", "Tlaloc", "Huitzilopochtli", "Quetzalcoatl", "Tezcatlipoca", "2"},
        {"Qual e o prato nacional do Mexico?", "Tacos", "Burrito", "Mole", "Enchilada", "2"},
        {"Quantos estados tem o Mexico?", "24", "28", "31", "34", "2"},
        {"Qual pintora mexicana e famosa por seus auto-retratos?", "Diego Rivera", "Frida Kahlo", "Rufino Tamayo", "Jose Orozco", "1"},
        {"O que e o Dia dos Mortos?", "Um feriado triste", "Uma celebracao para honrar os mortos", "Um carnaval", "Uma festa religiosa crista", "1"}
    };

    private String[][] perguntasEspanhol = {
        {"O que significa 'Buenos dias'?", "Bom dia", "Boa tarde", "Boa noite", "Ola", "0"},
        {"O que significa '¿Como estas?'?", "Como voce esta?", "Onde voce mora?", "O que voce quer?", "Ate logo", "0"},
        {"O que significa 'Me llamo'?", "Meu nome e", "Eu moro", "Eu quero", "Eu tenho", "0"},
        {"O que significa '¡Andale!'?", "Vamos/Rapido", "Pare", "Cuidado", "Obrigado", "0"},
        {"O que significa 'Chido'?", "Legal/Maneiro", "Feio", "Pequeno", "Velho", "0"},
        {"O que significa 'Orale'?", "Ok/Ta bom", "Nunca", "Talvez", "Por que?", "0"},
        {"O que significa '¿Que onda?'?", "Qual e a novidade?", "Que horas sao?", "Onde fica?", "Quanto custa?", "0"},
        {"O que significa 'Ahorita'?", "Agora/Em breve", "Nunca", "Ontem", "Amanha", "0"}
    };

    private String[][] perguntasCalendario = {
        {"O simbolo 'Tonatiuh' representa:", "O Sol", "A Lua", "A chuva", "O vento", "0"},
        {"O simbolo 'Tlaloc' representa:", "O deus da chuva", "O deus do sol", "O deus da guerra", "O deus do fogo", "0"},
        {"Quantos dias tinha o calendario asteca sagrado?", "180", "240", "260", "365", "2"},
        {"O simbolo 'Ollin' representa:", "Movimento/Terremoto", "Agua", "Fogo", "Arvore", "0"},
        {"Quantos meses tinha o calendario solar asteca?", "12", "16", "18", "20", "2"},
        {"Como se chama o calendario solar asteca de 365 dias?", "Tonalpohualli", "Xiuhpohualli", "Tzolkin", "Haab", "1"},
        {"O simbolo 'Ehecatl' representa:", "O deus do vento", "O deus do fogo", "O deus da agua", "O deus da terra", "0"},
        {"Quantos dias tinha cada mes do calendario solar asteca?", "15", "18", "20", "25", "2"},
        {"O simbolo 'Cipactli' representa:", "Um crocodilo mitico", "Uma serpente", "Um jaguar", "Uma aguia", "0"},
        {"O que significa 'Nahui Ollin'?", "Quatro ventos", "Quatro movimentos", "Quatro sois", "Quatro deuses", "1"}
    };

    private String[][] perguntasSelecionadas;
    private int perguntaAtual = 0;
    private int fase = 0; // 0=historia, 1=espanhol, 2=calendario
    private int acertos = 0;
    private int opcaoSelecionada = -1;
    private boolean acertou = false;
    private float tempoMensagem = 0;
    private float tempoEspera = 0;

    private float btnW = 500, btnH = 55;
    private String[] opcoesAtuais;

    public TelaDelegacaoMexico(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.6f);
        fundo = new Texture("mexico/delegacao.jpg");
        sortearPerguntas();
    }

    private void sortearPerguntas() {
        String[][] banco;
        int qtd;
        if (fase == 0) { banco = perguntasHistoria; qtd = 6; }
        else if (fase == 1) { banco = perguntasEspanhol; qtd = 5; }
        else { banco = perguntasCalendario; qtd = 5; }

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
            if (fase < 2) {
                fase++;
                sortearPerguntas();
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
        String[] titulos = {"Historia e Cultura do Mexico", "Espanhol Mexicano", "Calendario Asteca"};

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.05f, 0.15f, 0.9f);
        shape.rect(w/2 - 360, 80, 720, 520);
        shape.end();

        batch.begin();
        font.setColor(1, 0.6f, 0.1f, 1);
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
        font.setColor(1, 0.6f, 0.1f, 1);
        font.draw(batch, "Delegacao concluida!", w/2 - 180, h/2 + 60);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Toque para voltar a vila", w/2 - 180, h/2 + 10);
        batch.end();

        if (Gdx.input.justTouched()) {
            jogo.setScreen(new TelaVilagemMexico(jogo, jogador, true, true));
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
