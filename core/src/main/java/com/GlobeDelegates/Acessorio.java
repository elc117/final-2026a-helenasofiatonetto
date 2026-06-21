package com.GlobeDelegates;

public class Acessorio {
    private String nome;
    private boolean ehBonus;

    public Acessorio(String nome, boolean ehBonus) {
        this.nome = nome;
        this.ehBonus = ehBonus;
    }

    public boolean isBonus() { return ehBonus; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEhBonus(boolean ehBonus) { this.ehBonus = ehBonus; }
}
