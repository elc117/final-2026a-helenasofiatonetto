package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;

public class TelaBonusBussola implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private BonusAtividade atividade;
    private String fase;

    public TelaBonusBussola(GlobeDelegates jogo, Jogador jogador, String fase) {
        this.jogo = jogo;
        this.jogador = jogador;
        this.fase = fase;
        switch (fase) {
            case "China":  atividade = new BonusBussolaChina(jogo, jogador); break;
            case "Peru":   atividade = new BonusBussolaPeru(jogo, jogador); break;
            case "Grecia": atividade = new BonusBussolaGrecia(jogo, jogador); break;
            default:       atividade = new BonusBussolaChina(jogo, jogador);
        }
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            jogo.setScreen(new TelaVilagemBussola(jogo, jogador, false, false, false));
            return;
        }
        atividade.update(delta);
        atividade.render();
        if (atividade.isConcluido()) {
            boolean china = fase.equals("China");
            boolean peru = fase.equals("Peru");
            boolean grecia = fase.equals("Grecia");
            jogo.setScreen(new TelaVilagemBussola(jogo, jogador, china, peru, grecia));
        }
    }

    @Override public void show() {}
    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { atividade.dispose(); }
}
