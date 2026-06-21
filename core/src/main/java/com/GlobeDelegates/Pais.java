package com.GlobeDelegates;

public class Pais {
    private String nome;
    private String bandeira;

    public Pais(String nome, String bandeira) {
        this.nome = nome;
        this.bandeira = bandeira;
    }

    public String getNome() { return nome; }
    public String getBandeira() { return bandeira; }
    public void setNome(String nome) { this.nome = nome; }
    public void setBandeira(String bandeira) { this.bandeira = bandeira; }
}
