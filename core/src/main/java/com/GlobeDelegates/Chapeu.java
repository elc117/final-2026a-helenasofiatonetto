package com.GlobeDelegates;

public class Chapeu {
    private String tipo;
    private Pais pais;

    public Chapeu(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Pais getPais() { return pais; }
    public void setPais(Pais pais) { this.pais = pais; }
}
