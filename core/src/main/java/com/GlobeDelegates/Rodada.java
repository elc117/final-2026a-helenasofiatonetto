package com.GlobeDelegates;

import java.util.ArrayList;

public class Rodada {
    private int pontuacao;
    private ArrayList<Pergunta> perguntas;

    public Rodada() {
        this.pontuacao = 0;
        this.perguntas = new ArrayList<>();
    }

    public void iniciar() {
        this.pontuacao = 0;
    }

    public int getPontuacao() { return pontuacao; }

    public void adicionarPontos(int pts) { this.pontuacao += pts; }

    public void adicionarPergunta(Pergunta p) { perguntas.add(p); }

    public ArrayList<Pergunta> getPerguntas() { return perguntas; }
}
