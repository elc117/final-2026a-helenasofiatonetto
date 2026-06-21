package com.GlobeDelegates;

import java.util.ArrayList;
import java.util.Date;

public class SimulacaoJogo extends Jogo {
    private Date dataInicio;
    private boolean emAndamento;
    private ArrayList<Delegacao> delegacoes;
    private ArrayList<Rodada> rodadas;

    public SimulacaoJogo() {
        super("inicio");
        this.dataInicio = new Date();
        this.emAndamento = false;
        this.delegacoes = new ArrayList<>();
        this.rodadas = new ArrayList<>();
    }

    public void iniciar() {
        this.emAndamento = true;
        this.dataInicio = new Date();
        this.fase = "em andamento";
    }

    public void encerrar() {
        this.emAndamento = false;
        this.fase = "encerrado";
    }

    public void adicionarDelegacao(Delegacao d) { delegacoes.add(d); }
    public void adicionarRodada(Rodada r) { rodadas.add(r); }
    public ArrayList<Delegacao> getDelegacoes() { return delegacoes; }
    public ArrayList<Rodada> getRodadas() { return rodadas; }
    public boolean isEmAndamento() { return emAndamento; }
    public Date getDataInicio() { return dataInicio; }
}
