package com.GlobeDelegates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Ranking {
    private ArrayList<Jogador> jogadores;

    public Ranking() {
        this.jogadores = new ArrayList<>();
    }

    public void adicionarJogador(Jogador j) {
        jogadores.add(j);
    }

    public void ordenar() {
        Collections.sort(jogadores, new Comparator<Jogador>() {
            public int compare(Jogador a, Jogador b) {
                return b.getPontos() - a.getPontos();
            }
        });
    }

    public void exibir() {
        ordenar();
        for (int i = 0; i < jogadores.size(); i++) {
            System.out.println((i+1) + ". " + jogadores.get(i).getNome() + " - " + jogadores.get(i).getPontos() + " pts");
        }
    }

    public ArrayList<Jogador> getJogadores() { return jogadores; }
}
