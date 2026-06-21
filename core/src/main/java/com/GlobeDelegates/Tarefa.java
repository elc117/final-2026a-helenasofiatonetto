package com.GlobeDelegates;

public class Tarefa {
    private String tipo;
    private String descricao;

    public Tarefa(String tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public void executar() {
        System.out.println("Executando tarefa: " + descricao);
    }

    public String getTipo() { return tipo; }
    public String getDescricao() { return descricao; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
