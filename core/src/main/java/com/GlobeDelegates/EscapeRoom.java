package com.GlobeDelegates;

import java.util.ArrayList;

public class EscapeRoom {
    private boolean concluido;
    private ArrayList<Tarefa> tarefas;
    private Bonus bonus;

    public EscapeRoom() {
        this.concluido = false;
        this.tarefas = new ArrayList<>();
    }

    public void executar() {
        System.out.println("Executando Escape Room...");
        concluido = false;
    }

    public boolean isConcluido() { return concluido; }
    public void setConcluido(boolean concluido) { this.concluido = concluido; }

    public void adicionarTarefa(Tarefa t) { tarefas.add(t); }
    public ArrayList<Tarefa> getTarefas() { return tarefas; }

    public Bonus getBonus() { return bonus; }
    public void setBonus(Bonus bonus) { this.bonus = bonus; }
}
