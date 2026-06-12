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

public class TelaBonusJapao implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    // cada haiku: {verso1, verso2, verso3}
    // palavras separadas por espaco
    private String[][] haikus = {
        {"Velha lagoa", "Uma ra mergulha", "Som da agua"},
        {"Flores de cereja", "Petalas caem devagar", "Vento de primavera"},
        {"Lua de outono", "Ilumina o caminho", "Silencio profundo"},
        {"Neve no jardim", "O pinheiro se curva", "Frio da madrugada"},
        {"Rio que corre", "Pedras guardam o segredo", "Tempo que passa"}
    };

    private String[] haikusSelecionado;
    private String[] palavrasEmbaralhadas;
    private String[] palavrasCorretas;
    private ArrayList<String> palavrasClicadas = new ArrayList<>();
    private boolean errou = false;
    private boolean concluido = false;
    private float tempoErro = 0;

    private float btnW = 160, btnH = 50;

    public TelaBonusJapao(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.8f);
        fundo = new Texture("japao/haiku.jpg");

        sortearHaiku();
    }

    private void sortearHaiku() {
        int idx = (int)(Math.random() * haikus.length);
        haikusSelecionado = haikus[idx];

        // juntar todas as palavras dos 3 versos
        ArrayList<String> palavras = new ArrayList<>();
        for (String verso : haikusSelecionado) {
            for (String p : verso.split(" ")) {
                palavras.add(p);
            }
        }

        palavrasCorretas = palavras.toArray(new String[0]);

        // embaralhar
        Collections.shuffle(palavras);
        palavrasEmbaralhadas = palavras.toArray(new String[0]);
        palavrasClicadas.clear();
        errou = false;
        concluido = false;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.9f, 1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        batch.draw(fundo, 0, 0, w, h);
        batch.end();

        if (tempoErro > 0) {
            tempoErro -= delta;
            if (tempoErro <= 0) {
                palavrasClicadas.clear();
                errou = false;
            }
        }

        if (concluido) {
            renderConclusao(w, h);
            return;
        }

        // painel principal
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.05f, 0.05f, 0.15f, 0.88f);
        shape.rect(w/2 - 380, 60, 760, 560);
        shape.end();

        // titulo
        batch.begin();
        font.setColor(1, 0.8f, 0.2f, 1);
        font.draw(batch, "Monte Fuji - Desafio do Haiku", w/2 - 280, h - 50);
        font.setColor(0.8f, 0.8f, 1f, 1);
        font.draw(batch, "Monte as palavras na ordem correta!", w/2 - 280, h - 85);
        batch.end();

        // haiku sendo montado
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.15f, 0.15f, 0.3f, 1);
        shape.rect(w/2 - 340, 380, 680, 180);
        shape.end();

        batch.begin();
        font.setColor(1, 1, 0.7f, 1);
        String montado = String.join(" ", palavrasClicadas);
        // mostrar versos formados
        int palavraIdx = 0;
        for (int v = 0; v < haikusSelecionado.length; v++) {
            String[] wordsVerso = haikusSelecionado[v].split(" ");
            StringBuilder versoMontado = new StringBuilder();
            boolean versoCompleto = true;
            for (int wi = 0; wi < wordsVerso.length; wi++) {
                if (palavraIdx < palavrasClicadas.size()) {
                    versoMontado.append(palavrasClicadas.get(palavraIdx)).append(" ");
                    palavraIdx++;
                } else {
                    versoCompleto = false;
                    versoMontado.append("_____ ");
                }
            }
            font.setColor(versoCompleto ? 0.5f : 1f, versoCompleto ? 1f : 1f, versoCompleto ? 0.5f : 0.7f, 1);
            font.draw(batch, versoMontado.toString().trim(), w/2 - 310, 540 - v * 50);
        }
        batch.end();

        // palavras embaralhadas disponíveis
        batch.begin();
        font.setColor(0.8f, 0.8f, 0.8f, 1);
        font.draw(batch, "Palavras disponíveis:", w/2 - 340, 360);
        batch.end();

        int cols = 4;
        for (int i = 0; i < palavrasEmbaralhadas.length; i++) {
            if (palavrasClicadas.contains(palavrasEmbaralhadas[i] + "_" + i)) continue;

            // verificar se já foi usada
            boolean usada = false;
            for (String c : palavrasClicadas) {
                if (c.equals(palavrasEmbaralhadas[i])) { usada = true; break; }
            }

            // contar quantas vezes essa palavra aparece nas clicadas vs nas embaralhadas
            long countEmbaralhadas = 0;
            long countClicadas = 0;
            for (String p : palavrasEmbaralhadas) if (p.equals(palavrasEmbaralhadas[i])) countEmbaralhadas++;
            for (String p : palavrasClicadas) if (p.equals(palavrasEmbaralhadas[i])) countClicadas++;

            if (countClicadas >= countEmbaralhadas) continue;

            int col = i % cols;
            int row = i / cols;
            float bx = w/2 - 340 + col * (btnW + 15);
            float by = 280 - row * (btnH + 10);

            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(errou ? 0.6f : 0.2f, errou ? 0.1f : 0.4f, errou ? 0.1f : 0.7f, 1);
            shape.rect(bx, by, btnW, btnH);
            shape.end();

            batch.begin();
            font.setColor(1, 1, 1, 1);
            font.draw(batch, palavrasEmbaralhadas[i], bx + 10, by + btnH - 10);
            batch.end();

            if (!errou && Gdx.input.justTouched()) {
                float tx = Gdx.input.getX();
                float ty = h - Gdx.input.getY();
                if (tx >= bx && tx <= bx + btnW && ty >= by && ty <= by + btnH) {
                    palavrasClicadas.add(palavrasEmbaralhadas[i]);

                    // verificar se palavra está certa até agora
                    int pos = palavrasClicadas.size() - 1;
                    if (!palavrasClicadas.get(pos).equals(palavrasCorretas[pos])) {
                        errou = true;
                        tempoErro = 1.5f;
                    } else if (palavrasClicadas.size() == palavrasCorretas.length) {
                        concluido = true;
                    }
                }
            }
        }

        if (errou) {
            batch.begin();
            font.setColor(1, 0.3f, 0.3f, 1);
            font.draw(batch, "Ordem errada! Tentando novamente...", w/2 - 260, 100);
            batch.end();
        }
    }

    private void renderConclusao(float w, float h) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.05f, 0.2f, 0.05f, 0.92f);
        shape.rect(w/2 - 300, h/2 - 100, 600, 200);
        shape.end();

        batch.begin();
        font.setColor(0.5f, 1f, 0.5f, 1);
        font.draw(batch, "Haiku completo!", w/2 - 150, h/2 + 60);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Toque para voltar a vila", w/2 - 200, h/2 + 10);
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
