package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TelaBonus implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private BonusAtividade atividade;

    public TelaBonus(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        atividade = criarAtividade(jogador.getPais());
    }

    private BonusAtividade criarAtividade(String pais) {
        switch (pais) {
            case "Japao":         return new BonusJapao(jogo, jogador);
            case "Mexico":        return new BonusMexico(jogo, jogador);
            case "Canada":        return new BonusCanada(jogo, jogador);
            case "Nova Zelandia": return new BonusNZ(jogo, jogador);
            case "Groelandia":    return new BonusGroelandia(jogo, jogador);
            case "Austria":       return new BonusAustria(jogo, jogador);
            case "Egito":         return new BonusEgito(jogo, jogador);
            case "Brasil":        return new BonusBrasil(jogo, jogador);
            case "Bussola-China": return new BonusBussolaChina(jogo, jogador);
            case "Bussola-Peru":  return new BonusBussolaPeru(jogo, jogador);
            case "Bussola-Grecia":return new BonusBussolaGrecia(jogo, jogador);
            default:              return new BonusJapao(jogo, jogador);
        }
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            jogo.setScreen(new TelaEscolha(jogo));
            return;
        }
        atividade.update(delta);
        atividade.render();
        if (atividade.isConcluido()) {
            jogo.setScreen(new TelaVilagem(jogo, jogador, true, true));
        }
    }

    @Override public void show() {}
    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { atividade.dispose(); }
}
