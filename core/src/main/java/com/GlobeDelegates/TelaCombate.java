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

public class TelaCombate implements Screen {

    private GlobeDelegates jogo;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private BitmapFont fontCodigo;
    private Texture fundo;

    // Perguntas: {linguagem, codigo, opcao1, opcao2, opcao3, opcao4, indiceCorreto}
    private String[][] perguntas = {
        // HASKELL
        {"Haskell",
         "dobro x = x * 2\nmain = print (dobro 5)",
         "10", "5", "25", "Erro", "0"},
        {"Haskell",
         "soma [] = 0\nsoma (x:xs) = x + soma xs\nmain = print (soma [1,2,3])",
         "6", "0", "3", "Erro", "0"},
        {"Haskell",
         "f x = x * x\nmain = print (map f [1,2,3])",
         "[1,4,9]", "[1,2,3]", "[2,4,6]", "Erro", "0"},
        {"Haskell",
         "main = print (filter even [1..6])",
         "[2,4,6]", "[1,3,5]", "[1,2,3,4,5,6]", "Erro", "0"},
        {"Haskell",
         "main = print (foldr (+) 0 [1,2,3,4])",
         "10", "0", "4", "Erro", "0"},
        {"Haskell",
         "main = print (take 3 [10,20,30,40,50])",
         "[10,20,30]", "[30,40,50]", "[10,20]", "Erro", "0"},
        {"Haskell",
         "par x = x `mod` 2 == 0\nmain = print (par 4)",
         "True", "False", "4", "Erro", "0"},
        {"Haskell",
         "main = print (zip [1,2,3] ['a','b','c'])",
         "[(1,'a'),(2,'b'),(3,'c')]", "[1,2,3]", "['a','b','c']", "Erro", "0"},
        // PROLOG
        {"Prolog",
         "animal(cao).\nanimal(gato).\n?- animal(cao).",
         "true", "false", "cao", "Erro", "0"},
        {"Prolog",
         "pai(joao, maria).\npai(joao, pedro).\n?- pai(joao, X).",
         "X = maria ; X = pedro", "true", "false", "Erro", "0"},
        {"Prolog",
         "soma([], 0).\nsoma([H|T], S) :- soma(T, S1), S is S1 + H.\n?- soma([1,2,3], X).",
         "X = 6", "X = 3", "X = 0", "Erro", "0"},
        {"Prolog",
         "maior(X, Y) :- X > Y.\n?- maior(5, 3).",
         "true", "false", "5", "Erro", "0"},
        {"Prolog",
         "?- length([a,b,c,d], X).",
         "X = 4", "X = 3", "X = 0", "Erro", "0"},
        {"Prolog",
         "?- append([1,2], [3,4], X).",
         "X = [1,2,3,4]", "X = [3,4,1,2]", "X = [1,2]", "Erro", "0"},
        // JAVA
        {"Java",
         "int x = 5;\nint y = x++;\nSystem.out.println(y);",
         "5", "6", "4", "Erro", "0"},
        {"Java",
         "String s = \"hello\";\nSystem.out.println(s.length());",
         "5", "4", "6", "Erro", "0"},
        {"Java",
         "int[] v = {1,2,3,4};\nSystem.out.println(v.length);",
         "4", "3", "0", "Erro", "0"},
        {"Java",
         "int x = 10;\nif (x > 5) {\n  x = x * 2;\n}\nSystem.out.println(x);",
         "20", "10", "5", "Erro", "0"},
        {"Java",
         "for (int i = 0; i < 3; i++) {\n  System.out.print(i + \" \");\n}",
         "0 1 2", "1 2 3", "0 1 2 3", "Erro", "0"},
        {"Java",
         "int soma = 0;\nfor (int i = 1; i <= 4; i++) soma += i;\nSystem.out.println(soma);",
         "10", "4", "6", "Erro", "0"},
    };

    private String[][] perguntasSelecionadas;
    private int perguntaAtual = 0;
    private int[] opcaoSelecionada = {-1, -1};
    private int[] pontos = {0, 0};
    private int[] vidas = {3, 3};
    private int pontosVitoria = 5;
    private boolean[] respondeu = {false, false};
    private float tempoMensagem = 0;
    private int vencedor = -1; // -1=nenhum, 0=j1, 1=j2
    private String[] opcoesMisturadas;
    private int[] mapeamentoOpcoes; // mapeamento após embaralhamento

    // Controles J1: 1,2,3,4 | J2: 7,8,9,0
    private int[][] teclas = {
        {Keys.NUM_1, Keys.NUM_2, Keys.NUM_3, Keys.NUM_4},
        {Keys.NUM_7, Keys.NUM_8, Keys.NUM_9, Keys.NUM_0}
    };

    public TelaCombate(GlobeDelegates jogo) {
        this.jogo = jogo;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.6f);
        fontCodigo = new BitmapFont();
        fontCodigo.getData().setScale(1.3f);
        fundo = new Texture("fundoInicio.png");

        sortearPerguntas();
        prepararOpcoes();
    }

    private void sortearPerguntas() {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < perguntas.length; i++) indices.add(i);
        Collections.shuffle(indices);
        int qtd = Math.min(15, perguntas.length);
        perguntasSelecionadas = new String[qtd][];
        for (int i = 0; i < qtd; i++) perguntasSelecionadas[i] = perguntas[indices.get(i)];
    }

    private void prepararOpcoes() {
        if (perguntaAtual >= perguntasSelecionadas.length) return;
        String[] p = perguntasSelecionadas[perguntaAtual];
        int correta = Integer.parseInt(p[6]);

        ArrayList<Integer> idx = new ArrayList<>();
        for (int i = 0; i < 4; i++) idx.add(i);
        Collections.shuffle(idx);

        opcoesMisturadas = new String[4];
        mapeamentoOpcoes = new int[4];
        int novaCorreta = 0;
        for (int i = 0; i < 4; i++) {
            opcoesMisturadas[i] = p[idx.get(i) + 2];
            mapeamentoOpcoes[i] = idx.get(i);
            if (idx.get(i) == correta) novaCorreta = i;
        }
        perguntasSelecionadas[perguntaAtual][6] = String.valueOf(novaCorreta);
        opcaoSelecionada[0] = -1;
        opcaoSelecionada[1] = -1;
        respondeu[0] = false;
        respondeu[1] = false;
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
        shape.setColor(0, 0, 0, 0.75f);
        shape.rect(0, 0, w, h);
        shape.end();

        if (vencedor >= 0) { renderVitoria(w, h); return; }

        // HUD jogadores
        // J1 lado esquerdo
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f, 0.3f, 0.8f, 0.9f);
        shape.rect(0, h - 55, w/2 - 5, 55);
        shape.end();
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "J1: " + pontos[0] + " pts | Vidas: " + vidas[0], 15, h - 18);
        font.draw(batch, "Teclas: 1 2 3 4", 15, h - 38);
        batch.end();

        // J2 lado direito
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.8f, 0.1f, 0.1f, 0.9f);
        shape.rect(w/2 + 5, h - 55, w/2 - 5, 55);
        shape.end();
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "J2: " + pontos[1] + " pts | Vidas: " + vidas[1], w/2 + 20, h - 18);
        font.draw(batch, "Teclas: 7 8 9 0", w/2 + 20, h - 38);
        batch.end();

        if (tempoMensagem > 0) {
            tempoMensagem -= delta;
            if (tempoMensagem <= 0) {
                perguntaAtual++;
                if (perguntaAtual >= perguntasSelecionadas.length) perguntaAtual = 0;
                prepararOpcoes();
            }
            return;
        }

        if (perguntaAtual >= perguntasSelecionadas.length) return;
        String[] p = perguntasSelecionadas[perguntaAtual];

        // Linguagem
        shape.begin(ShapeRenderer.ShapeType.Filled);
        float[] cor = p[0].equals("Haskell") ? new float[]{0.5f,0.1f,0.8f} :
                      p[0].equals("Prolog") ? new float[]{0.1f,0.5f,0.2f} :
                      new float[]{0.7f,0.5f,0.1f};
        shape.setColor(cor[0], cor[1], cor[2], 0.9f);
        shape.rect(w/2 - 80, h - 95, 160, 35);
        shape.end();
        batch.begin();
        font.setColor(1, 1, 1, 1);
        font.draw(batch, p[0], w/2 - 50, h - 68);
        batch.end();

        // Codigo
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.08f, 0.08f, 0.12f, 0.95f);
        shape.rect(w/2 - 350, h * 0.52f, 700, 160);
        shape.end();
        batch.begin();
        fontCodigo.setColor(0.3f, 1f, 0.3f, 1);
        String[] linhas = p[1].split("\n");
        for (int i = 0; i < linhas.length; i++) {
            fontCodigo.draw(batch, linhas[i], w/2 - 330, h * 0.52f + 140 - i * 28);
        }
        batch.end();

        // Pergunta
        batch.begin();
        font.setColor(1, 1, 0.5f, 1);
        font.draw(batch, "Qual e a saida deste codigo?", w/2 - 220, h * 0.5f);
        batch.end();

        // Opcoes
        float btnW = 320, btnH = 55;
        String[] letras = {"A", "B", "C", "D"};

        for (int i = 0; i < 4; i++) {
            float bx = (i < 2) ? w/2 - btnW - 10 : w/2 + 10;
            float by = (i % 2 == 0) ? h * 0.35f : h * 0.22f;

            boolean j1Sel = opcaoSelecionada[0] == i;
            boolean j2Sel = opcaoSelecionada[1] == i;
            int corrIdx = Integer.parseInt(p[6]);

            shape.begin(ShapeRenderer.ShapeType.Filled);
            if (j1Sel && j2Sel) shape.setColor(0.8f, 0.8f, 0.1f, 1);
            else if (j1Sel) shape.setColor(0.1f, 0.3f, 0.8f, 1);
            else if (j2Sel) shape.setColor(0.8f, 0.1f, 0.1f, 1);
            else shape.setColor(0.2f, 0.2f, 0.3f, 1);
            shape.rect(bx, by, btnW, btnH);
            shape.end();

            batch.begin();
            font.setColor(1, 1, 1, 1);
            font.draw(batch, letras[i] + ". " + opcoesMisturadas[i], bx + 15, by + btnH - 12);
            batch.end();
        }

        // Legenda opcoes
        batch.begin();
        font.setColor(0.5f, 0.7f, 1f, 0.8f);
        font.draw(batch, "J1: 1=A  2=B  3=C  4=D", 20, h * 0.12f);
        font.setColor(1f, 0.5f, 0.5f, 0.8f);
        font.draw(batch, "J2: 7=A  8=B  9=C  0=D", w - 320, h * 0.12f);
        batch.end();

        // Processar input
        for (int j = 0; j < 2; j++) {
            if (!respondeu[j]) {
                for (int t = 0; t < 4; t++) {
                    if (Gdx.input.isKeyJustPressed(teclas[j][t])) {
                        opcaoSelecionada[j] = t;
                        respondeu[j] = true;

                        int corrIdx = Integer.parseInt(p[6]);
                        if (t == corrIdx) {
                            pontos[j]++;
                            if (pontos[j] >= pontosVitoria) vencedor = j;
                        } else {
                            vidas[j]--;
                            if (vidas[j] <= 0) vencedor = 1 - j;
                        }

                        // Se ambos responderam ou um acertou
                        if (respondeu[0] && respondeu[1] || t == corrIdx) {
                            tempoMensagem = 1.5f;
                        }
                    }
                }
            }
        }
    }

    private void renderVitoria(float w, float h) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(vencedor == 0 ? 0.1f : 0.6f, vencedor == 0 ? 0.3f : 0.1f, vencedor == 0 ? 0.8f : 0.1f, 0.92f);
        shape.rect(w/2 - 350, h/2 - 100, 700, 200);
        shape.end();

        batch.begin();
        font.setColor(1, 1, 0.3f, 1);
        font.draw(batch, "JOGADOR " + (vencedor + 1) + " VENCEU!", w/2 - 220, h/2 + 60);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "J1: " + pontos[0] + " pts | J2: " + pontos[1] + " pts", w/2 - 200, h/2 + 10);
        font.draw(batch, "Toque para jogar novamente", w/2 - 230, h/2 - 40);
        batch.end();

        if (Gdx.input.justTouched()) {
            jogo.setScreen(new TelaModo(jogo));
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
        fontCodigo.dispose();
        fundo.dispose();
    }
}
