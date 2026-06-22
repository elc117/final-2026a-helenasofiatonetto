package com.GlobeDelegates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TelaVilagem implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    private PersonagemAnimado personagem;

    private float[] entradaX;
    private float[] entradaY;
    private float entradaSize = 70;
    private boolean[] desbloqueado;
    private boolean[] faseConcluida;

    public TelaVilagem(GlobeDelegates jogo, Jogador jogador, boolean escapeCompleto, boolean delegacaoCompleta) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.5f);
        String caminhoBase = getPasta(jogador.getPais()) + "/telaEscolha";
        try {
            fundo = new Texture(caminhoBase + ".jpg");
        } catch (Exception e) {
            fundo = new Texture(caminhoBase + ".png");
        }

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        configurarEntradas(jogador.getPais(), w, h);
        desbloqueado = new boolean[]{true, escapeCompleto, escapeCompleto && delegacaoCompleta};
        faseConcluida = new boolean[]{escapeCompleto, delegacaoCompleta, false};
        
        personagem = new PersonagemAnimado(50, h * 0.3f);
    }

    private void configurarEntradas(String pais, float w, float h) {
        switch (pais) {
            case "Japao":
                entradaX = new float[]{w*0.15f, w*0.50f, w*0.80f};
                entradaY = new float[]{h*0.35f, h*0.40f, h*0.35f};
                break;
            case "Mexico":
                entradaX = new float[]{w*0.15f, w*0.50f, w*0.80f};
                entradaY = new float[]{h*0.35f, h*0.40f, h*0.35f};
                break;
            case "Canada":
                entradaX = new float[]{w*0.15f, w*0.50f, w*0.80f};
                entradaY = new float[]{h*0.35f, h*0.40f, h*0.35f};
                break;
            case "Nova Zelandia":
                entradaX = new float[]{w*0.15f, w*0.50f, w*0.80f};
                entradaY = new float[]{h*0.35f, h*0.40f, h*0.35f};
                break;
            case "Groelandia":
                entradaX = new float[]{w*0.15f, w*0.50f, w*0.80f};
                entradaY = new float[]{h*0.35f, h*0.40f, h*0.35f};
                break;
            case "Austria":
                entradaX = new float[]{w*0.15f, w*0.50f, w*0.80f};
                entradaY = new float[]{h*0.35f, h*0.40f, h*0.35f};
                break;
            case "Egito":
                entradaX = new float[]{w*0.15f, w*0.50f, w*0.80f};
                entradaY = new float[]{h*0.35f, h*0.40f, h*0.35f};
                break;
            case "Brasil":
                entradaX = new float[]{w*0.15f, w*0.50f, w*0.80f};
                entradaY = new float[]{h*0.35f, h*0.40f, h*0.35f};
                break;
            default:
                entradaX = new float[]{w*0.15f, w*0.50f, w*0.80f};
                entradaY = new float[]{h*0.35f, h*0.40f, h*0.35f};
        }
    }

    private String getPasta(String pais) {
        switch (pais) {
            case "Japao": return "japao";
            case "Mexico": return "mexico";
            case "Canada": return "canada";
            case "Nova Zelandia": return "nz";
            case "Groelandia": return "groelandia";
            case "Austria": return "austria";
            case "Egito": return "egito";
            case "Brasil": return "brasil";
            case "Bussola": return "bussola";
            default: return "japao";
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        personagem.draw(batch);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            jogo.setScreen(new TelaEscolha(jogo));
            return;
        }

        personagem.update(delta);

        
        // Entradas — só destaque visual quando perto
        for (int i = 0; i < 3; i++) {
            float dist = Math.abs(personagem.getX() - entradaX[i]);
            if (dist < 80) {
                shape.begin(ShapeRenderer.ShapeType.Line);
                if (!desbloqueado[i]) {
                    shape.setColor(1f, 0.3f, 0.3f, 0.8f);
                } else {
                    shape.setColor(1f, 1f, 0.3f, 0.9f);
                }
                shape.rect(entradaX[i] - entradaSize/2, entradaY[i] - entradaSize/2, entradaSize, entradaSize);
                shape.rect(entradaX[i] - entradaSize/2 + 3, entradaY[i] - entradaSize/2 + 3, entradaSize - 6, entradaSize - 6);
                shape.end();

                if (desbloqueado[i] && Gdx.input.isKeyJustPressed(Keys.SPACE)) {
                    if (i == 0) jogo.setScreen(new TelaEscape(jogo, jogador));
                    else if (i == 1) jogo.setScreen(new TelaDelegacao(jogo, jogador));
                    else jogo.setScreen(new TelaBonus(jogo, jogador));
                }
            }
        }
    }

    @Override public void show() {}
    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        batch.dispose(); shape.dispose(); font.dispose(); fundo.dispose();
    }
}
