package com.GlobeDelegates;

public abstract class Jogo {
    protected String fase;

    public Jogo(String fase) {
        this.fase = fase;
    }

    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }
}
