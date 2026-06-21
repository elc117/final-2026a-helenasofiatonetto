package com.GlobeDelegates;

public class Personagem {
    private String aparencia;
    private Chapeu chapeu;
    private EscapeRoom escapeRoom;

    public Personagem(String aparencia) {
        this.aparencia = aparencia;
    }

    public String getAparencia() { return aparencia; }
    public void setAparencia(String aparencia) { this.aparencia = aparencia; }

    public Chapeu getChapeu() { return chapeu; }
    public void setChapeu(Chapeu chapeu) { this.chapeu = chapeu; }

    public EscapeRoom getEscapeRoom() { return escapeRoom; }
    public void setEscapeRoom(EscapeRoom escapeRoom) { this.escapeRoom = escapeRoom; }
}
