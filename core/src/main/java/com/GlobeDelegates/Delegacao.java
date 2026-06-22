package com.GlobeDelegates;

import java.util.ArrayList;

public class Delegacao {
    private int pontuacaoTotal;
    private ArrayList<Jogador> jogadores;

    public Delegacao() {
        this.pontuacaoTotal = 0;
        this.jogadores = new ArrayList<>();
    }

    public int getPontuacaoTotal() { return pontuacaoTotal; }

    public void adicionarJogador(Jogador j) {
        jogadores.add(j);
        pontuacaoTotal += j.getPontos();
    }

    public ArrayList<Jogador> getJogadores() { return jogadores; }
}
