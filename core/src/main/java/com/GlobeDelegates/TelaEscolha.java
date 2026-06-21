package com.GlobeDelegates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TelaEscolha implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture fundo;
    private Texture[] icones;
    private String[] nomes = {"pizza", "controle", "livro", "paleta", "laptop", "bonsai", "emoji", "galinha", "chave"};

    private float iconeSize = 80;
    private float padding = 30;
    private float pergW = 460, pergH = 480; // Tamanho ideal do pergaminho para os 9 ícones
    private float pergX, pergY;
    private float startX, startY;

    public TelaEscolha(GlobeDelegates jogo) {
        this.jogo = jogo;
        this.jogador = new Jogador();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1.8f);
        fundo = new Texture("fundoInicio.png");

        // Carrega as texturas dos ícones dinamicamente
        icones = new Texture[nomes.length];
        for (int i = 0; i < nomes.length; i++) {
            icones[i] = new Texture(nomes[i] + ".png");
        }

        // 1. Centraliza o pergaminho perfeitamente na janela do jogo
        pergX = (Gdx.graphics.getWidth() - pergW) / 2;
        pergY = (Gdx.graphics.getHeight() - pergH) / 2;

        // 2. Calcula a largura total ocupada pela grade (3 colunas de 80px + 2 espaçamentos de 30px)
        float gradeW = (3 * iconeSize) + (2 * padding);

        // 3. Define o ponto inicial X (lado esquerdo) para centralizar a grade dentro do pergaminho
        startX = pergX + (pergW - gradeW) / 2;

        // 4. Define o ponto inicial Y (topo útil do pergaminho, logo abaixo do título)
        // As linhas serão desenhadas subtraindo valores deste ponto, movendo para baixo
        startY = pergY + pergH - 140;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        // 1. Desenha o cenário de fundo da tela inicial
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        
        // 2. Desenha o pergaminho centralizado como base dos ícones
        ImagemUtil.desenharPergaminho(batch, pergX, pergY, pergW, pergH);

        // 3. Desenha o título centralizado no pergaminho (tom marrom escuro combinando com o tema)
        font.setColor(0.3f, 0.2f, 0.1f, 1);
        font.draw(batch, "ESCOLHA UM ICONE", pergX + 65, pergY + pergH - 50);

        // 4. Desenha a grade de ícones (3x3) de cima para baixo
        for (int i = 0; i < icones.length; i++) {
            int col = i % 3; // Colunas: 0, 1, 2
            int row = i / 3; // Linhas: 0, 1, 2

            float x = startX + col * (iconeSize + padding);
            float y = startY - row * (iconeSize + padding); // Subtrai para descer a linha

            batch.draw(icones[i], x, y, iconeSize, iconeSize);
        }
        batch.end();

        // 5. Processamento preciso de Clique / Toque do mouse
        if (Gdx.input.justTouched()) {
            float tx = Gdx.input.getX();
            // Converte o eixo Y invertido do sistema operacional para o do LibGDX
            float ty = h - Gdx.input.getY(); 

            for (int i = 0; i < nomes.length; i++) {
                int col = i % 3;
                int row = i / 3;

                float x = startX + col * (iconeSize + padding);
                float y = startY - row * (iconeSize + padding);

                // Verifica se a coordenada clicada bate exatamente com o quadrado do ícone atual
                if (tx >= x && tx <= x + iconeSize && ty >= y && ty <= y + iconeSize) {
                    Gdx.app.log("EscolhaIcone", "Selecionado com sucesso: " + nomes[i]);
                    
                    jogador.setIcone(nomes[i]);
                    
                    // Transiciona para a tela do mapa principal do jogo
                    jogo.setScreen(new TelaMundo(jogo, jogador));
                    break; // Interrompe o loop por segurança, já que o clique foi processado
                }
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
        font.dispose(); 
        fundo.dispose();
        for (Texture t : icones) {
            if (t != null) t.dispose();
        }
    }
}