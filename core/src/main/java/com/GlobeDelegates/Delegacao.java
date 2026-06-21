package com.GlobeDelegates;

import java.util.ArrayList;

public class Delegacao {
    private int pontuacaoTotal;
    private ArrayList<Jogador> jogadores;
    private Ranking ranking;

    public Delegacao() {
        this.pontuacaoTotal = 0;
        this.jogadores = new ArrayList<>();
        this.ranking = new Ranking();
    }

    public int getPontuacaoTotal() { return pontuacaoTotal; }

    public void adicionarJogador(Jogador j) {
        jogadores.add(j);
        ranking.adicionarJogador(j);
        pontuacaoTotal += j.getPontos();
    }

    public ArrayList<Jogador> getJogadores() { return jogadores; }
    public Ranking getRanking() { return ranking; }
}
