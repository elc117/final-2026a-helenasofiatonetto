package com.GlobeDelegates;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TelaEscape implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;
    private Texture[] objetos;

    private String[] nomesArquivo;
    private String[] nomesCorretos;
    private String[] iniciais;
    private String[] extensoes;
    private String pastaAssets;

    private int[] ordemEmbaralhada;
    private int objetoAtual = 0;
    private int coletados = 0;
    private boolean mostraPergunta = false;
    private boolean mostraSenha = false;
    private int opcaoSelecionada = -1;
    private boolean acertou = false;
    private float tempoMensagem = 0;
    private float tempoEspera = 0;

    private String senhaCorreta = "";
    private String senhaDigitada = "";
    private boolean senhaErrada = false;

    private float persX, persY;
    private float persVX = 250;
    private float persSize = 40;
    private float objSize = 100;
    private float btnW = 320, btnH = 55;
    private String[] opcoesMisturadas;
    private float[] objX, objY;

    // Selecao de opcao com teclado
    private int opcaoHighlight = 0;

    public TelaEscape(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.6f);

        configurarPais(jogador.getPais());

        fundo = new Texture(pastaAssets + "/escape.jpg");
        objetos = new Texture[nomesArquivo.length];
        for (int i = 0; i < nomesArquivo.length; i++) {
            objetos[i] = new Texture(pastaAssets + "/" + nomesArquivo[i] + extensoes[i]);
        }

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        int n = nomesArquivo.length;
        objX = new float[n];
        objY = new float[n];
        float[] posX = {100, 280, 460, 640, 820, 180, 580, 380};
        float[] posYr = {0.55f, 0.35f, 0.6f, 0.4f, 0.25f, 0.5f, 0.3f, 0.45f};
        for (int i = 0; i < n; i++) {
            objX[i] = posX[i % posX.length];
            objY[i] = h * posYr[i % posYr.length];
        }

        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < n; i++) indices.add(i);
        Collections.shuffle(indices);
        ordemEmbaralhada = new int[n];
        for (int i = 0; i < n; i++) ordemEmbaralhada[i] = indices.get(i);

        persX = 50;
        persY = h * 0.15f;
    }

    private void configurarPais(String pais) {
        switch (pais) {
            case "Japao":
                pastaAssets = "japao";
                nomesArquivo = new String[]{"koi","dango","onigiri","leque","torii","moeda","cerejeira"};
                nomesCorretos = new String[]{"Carpa Koi","Dango","Onigiri","Leque","Torii","Moeda da Sorte","Cerejeira"};
                iniciais = new String[]{"CK","D","O","L","T","MS","C"};
                extensoes = new String[]{".png",".png",".png",".png",".png",".png",".png"};
                break;
            case "Mexico":
                pastaAssets = "mexico";
                nomesArquivo = new String[]{"pinata","agave","maracas","piramide","caveira","sombrero","flor"};
                nomesCorretos = new String[]{"Pinata","Agave","Maracas","Piramide","Caveira","Sombrero","Flor de Cempasuchil"};
                iniciais = new String[]{"P","A","M","PI","C","S","FC"};
                extensoes = new String[]{".png",".png",".png",".png",".png",".png",".png"};
                break;
            case "Canada":
                pastaAssets = "canada";
                nomesArquivo = new String[]{"folha","totem","castor","iglu","xarope","hoquei"};
                nomesCorretos = new String[]{"Folha de Bordo","Totem","Castor","Iglu","Xarope de Bordo","Hoquei"};
                iniciais = new String[]{"FB","T","C","I","XB","H"};
                extensoes = new String[]{".png",".png",".png",".png",".png",".png"};
                break;
            case "Nova Zelandia":
                pastaAssets = "nz";
                nomesArquivo = new String[]{"kiwi","maori","ovelha","rugby","tuatara","feto"};
                nomesCorretos = new String[]{"Kiwi","Guerreiro Maori","Ovelha","Rugby","Tuatara","Feto Prateado"};
                iniciais = new String[]{"K","M","O","R","T","F"};
                extensoes = new String[]{".png",".png",".png",".png",".png",".png"};
                break;
            case "Groelandia":
                pastaAssets = "groelandia";
                nomesArquivo = new String[]{"osso_baleia","anorak","barco","peixe","foca"};
                nomesCorretos = new String[]{"Osso de Baleia","Anorak","Barco","Peixe Artico","Foca"};
                iniciais = new String[]{"OB","A","B","P","F"};
                extensoes = new String[]{".png",".png",".png",".png",".png"};
                break;
            case "Austria":
                pastaAssets = "austria";
                nomesArquivo = new String[]{"calca","chapeu","flor","pretzel","ski","violino"};
                nomesCorretos = new String[]{"Lederhosen","Chapeu Tiroles","Edelweiss","Pretzel","Esqui","Violino"};
                iniciais = new String[]{"L","CT","E","P","S","V"};
                extensoes = new String[]{".png",".png",".png",".png",".png",".png"};
                break;
            case "Egito":
                pastaAssets = "egito";
                nomesArquivo = new String[]{"Escaravelho","cruz","esfinge","gato","mumia","olho","papiro","piramide"};
                nomesCorretos = new String[]{"Escaravelho","Ankh","Esfinge","Gato Sagrado","Mumia","Olho de Horus","Papiro","Piramide"};
                iniciais = new String[]{"E","A","ES","GS","M","OH","P","PI"};
                extensoes = new String[]{".png",".png",".png",".png",".png",".png",".png",".png"};
                break;
            case "Brasil":
                pastaAssets = "brasil";
                nomesArquivo = new String[]{"arara","caramelo","cristo","filtroBarro","orelhão","zegotinha","feira"};
                nomesCorretos = new String[]{"Arara","Cachorro Caramelo","Cristo Redentor","Filtro de Barro","Orelhao","Ze Gotinha","Pastel e Caldo de Cana"};
                iniciais = new String[]{"A","CC","CR","FB","O","ZG","PC"};
                extensoes = new String[]{".jpg",".jpg",".jpg",".jpg",".jpg",".jpg",".jpg"};
                break;
            default:
                pastaAssets = "japao";
                nomesArquivo = new String[]{"koi"};
                nomesCorretos = new String[]{"Carpa Koi"};
                iniciais = new String[]{"CK"};
                extensoes = new String[]{".png"};
        }
    }

    private int idxAtual() { return ordemEmbaralhada[objetoAtual]; }

    private void gerarOpcoes() {
        int idx = idxAtual();
        opcoesMisturadas = new String[4];
        opcoesMisturadas[0] = nomesCorretos[idx];
        int n = nomesArquivo.length;
        int[] outros = new int[]{(idx+1)%n, (idx+2)%n, (idx+3)%n};
        for (int i = 0; i < 3; i++) opcoesMisturadas[i+1] = nomesCorretos[outros[i]];
        for (int i = 3; i > 0; i--) {
            int j = (int)(Math.random() * (i+1));
            String tmp = opcoesMisturadas[i]; opcoesMisturadas[i] = opcoesMisturadas[j]; opcoesMisturadas[j] = tmp;
        }
        opcaoHighlight = 0;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        batch.end();

        if (tempoEspera > 0) { tempoEspera -= delta; return; }
        if (coletados >= nomesArquivo.length) mostraSenha = true;
        if (mostraSenha) { renderSenha(w, h); return; }

        // ESC volta ao menu
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            jogo.setScreen(new TelaEscolha(jogo));
            return;
        }

        if (!mostraPergunta) {
            // WASD para mover
            if (Gdx.input.isKeyPressed(Keys.LEFT)) persX -= persVX * delta;
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) persX += persVX * delta;
            if (Gdx.input.isKeyPressed(Keys.UP)) persY += persVX * delta;
            if (Gdx.input.isKeyPressed(Keys.DOWN)) persY -= persVX * delta;
            persX = Math.max(0, Math.min(persX, w - persSize));
            persY = Math.max(0, Math.min(persY, h - persSize * 2));
        }

        // Objetos
        batch.begin();
        for (int i = objetoAtual; i < nomesArquivo.length; i++) {
            batch.draw(objetos[ordemEmbaralhada[i]], objX[i], objY[i], objSize, objSize);
        }
        batch.end();

        // Personagem
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.2f, 0.4f, 0.9f, 1);
        shape.rect(persX, persY, persSize, persSize * 1.5f);
        shape.setColor(1f, 0.85f, 0.7f, 1);
        shape.circle(persX + persSize/2, persY + persSize * 1.7f, persSize/2);
        shape.end();

        if (!mostraPergunta) {
            // Proximidade com objeto
            if (objetoAtual < nomesArquivo.length) {
                float distX = Math.abs(persX - objX[objetoAtual]);
                float distY = Math.abs(persY - objY[objetoAtual]);
                if (distX < 80 && distY < 80) {
                    shape.begin(ShapeRenderer.ShapeType.Filled);
                    shape.setColor(0.18f, 0.75f, 0.75f, 1);
                    shape.rect(persX - 10, persY + persSize * 2 + 10, 180, 40);
                    shape.end();
                    batch.begin();
                    font.setColor(0, 0, 0, 1);
                    batch.end();

                    if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
                        gerarOpcoes();
                        mostraPergunta = true;
                        tempoEspera = 0.2f;
                    }
                }
            }
        } else {
            // Painel pergunta
            batch.begin();
            font.setColor(1, 1, 0.5f, 1);
            font.draw(batch, "Qual e o nome deste objeto?", w/2 - 220, h - 80);
            batch.draw(objetos[idxAtual()], w/2 - 55, 400, 110, 110);
            batch.end();

            // Navegacao com setas/WASD entre opcoes
            if (Gdx.input.isKeyJustPressed(Keys.UP) || Gdx.input.isKeyJustPressed(Keys.UP))
                opcaoHighlight = (opcaoHighlight - 1 + 4) % 4;
            if (Gdx.input.isKeyJustPressed(Keys.DOWN) || Gdx.input.isKeyJustPressed(Keys.DOWN))
                opcaoHighlight = (opcaoHighlight + 1) % 4;

            for (int i = 0; i < 4; i++) {
                float bx = w/2 - btnW/2;
                float by = 360 - i * 75;
                shape.begin(ShapeRenderer.ShapeType.Filled);
                if (opcaoSelecionada == i) {
                    shape.setColor(acertou ? 0.1f : 0.8f, acertou ? 0.8f : 0.1f, 0.1f, 1);
                } else if (opcaoHighlight == i) {
                    shape.setColor(0.4f, 0.4f, 0.7f, 1);
                } else {
                    shape.setColor(0.25f, 0.25f, 0.45f, 1);
                }
                shape.rect(bx, by, btnW, btnH);
                shape.end();
                batch.begin();
                font.setColor(1, 1, 1, 1);
                String letra = new String[]{"A","B","C","D"}[i];
                font.draw(batch, letra + ". " + opcoesMisturadas[i], bx + 15, by + btnH - 10);
                batch.end();
            }

            // Confirmar com ENTER ou toque
            if (opcaoSelecionada == -1) {
                if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
                    selecionarOpcao(opcaoHighlight);
                }
                if (Gdx.input.justTouched()) {
                    float tx = Gdx.input.getX();
                    float ty = h - Gdx.input.getY();
                    for (int i = 0; i < 4; i++) {
                        float bx = w/2 - btnW/2;
                        float by = 360 - i * 75;
                        if (tx >= bx && tx <= bx + btnW && ty >= by && ty <= by + btnH) {
                            selecionarOpcao(i);
                        }
                    }
                }
            }

            if (opcaoSelecionada != -1) {
                tempoMensagem -= delta;
                batch.begin();
                font.setColor(acertou ? 0.2f : 1f, acertou ? 1f : 0.2f, 0.2f, 1);
                font.draw(batch, acertou ? "Correto!" : "Errado! Tente novamente!", w/2 - 160, 75);
                batch.end();
                if (tempoMensagem <= 0) {
                    if (acertou) {
                        senhaCorreta += iniciais[idxAtual()];
                        coletados++;
                        objetoAtual++;
                    }
                    opcaoSelecionada = -1;
                    mostraPergunta = false;
                    tempoEspera = 0.3f;
                }
            }
        }
    }

    private void selecionarOpcao(int i) {
        opcaoSelecionada = i;
        acertou = opcoesMisturadas[i].equals(nomesCorretos[idxAtual()]);
        tempoMensagem = 1.5f;
    }

    private void renderSenha(float w, float h) {
        // ESC volta
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            jogo.setScreen(new TelaEscolha(jogo));
            return;
        }

        batch.begin();
        ImagemUtil.desenharPergaminho(batch, w/2 - 400, h/2 - 220, 800, 440);
        batch.end();

        batch.begin();
        font.setColor(1, 1, 0.5f, 1);
        font.draw(batch, "Todos os objetos coletados!", w/2 - 240, h/2 + 180);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "Digite as iniciais na ordem que coletou:", w/2 - 300, h/2 + 130);

        // Dicas de iniciais
        StringBuilder dica = new StringBuilder();
        for (int i = 0; i < iniciais.length; i++) {
            dica.append(iniciais[i]).append("=").append(nomesCorretos[i]);
            if (i < iniciais.length - 1) dica.append("  ");
            if (i == 3) {
                batch.end();
                batch.begin();
                font.draw(batch, dica.toString(), w/2 - 370, h/2 + 90);
                dica = new StringBuilder();
            }
        }
        if (dica.length() > 0) font.draw(batch, dica.toString(), w/2 - 370, h/2 + 55);
        batch.end();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.2f, 0.2f, 0.35f, 1);
        shape.rect(w/2 - 220, h/2 - 10, 440, 55);
        shape.end();

        batch.begin();
        font.setColor(0.5f, 1f, 0.5f, 1);
        font.draw(batch, senhaDigitada + "|", w/2 - 200, h/2 + 35);
        if (senhaErrada) {
            font.setColor(1, 0.3f, 0.3f, 1);
            font.draw(batch, "Senha incorreta! Tente novamente.", w/2 - 250, h/2 - 55);
        }
        font.setColor(0.8f, 0.8f, 0.5f, 1);
        batch.end();

        // Input teclado
        for (int k = Keys.LEFT; k <= Keys.Z; k++) {
            if (Gdx.input.isKeyJustPressed(k)) {
                senhaDigitada += Keys.toString(k).toUpperCase();
                senhaErrada = false;
            }
        }
        // BACKSPACE funcionando
        if (Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) {
            if (senhaDigitada.length() > 0) {
                senhaDigitada = senhaDigitada.substring(0, senhaDigitada.length() - 1);
                senhaErrada = false;
            }
        }
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            if (senhaDigitada.equals(senhaCorreta)) {
                jogo.setScreen(new TelaVilagem(jogo, jogador, true, false));
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
        batch.dispose(); shape.dispose(); font.dispose(); fundo.dispose();
        for (Texture t : objetos) if (t != null) t.dispose();
    }
}
