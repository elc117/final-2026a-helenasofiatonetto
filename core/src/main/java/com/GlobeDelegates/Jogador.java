package com.GlobeDelegates;

public class Jogador {

    private String icone;
    private String pais;
    private String cor;

    public Jogador() {}

    public void setIcone(String icone) {
        this.icone = icone;
        definirPais();
    }

    private void definirPais() {
        switch (icone) {
            case "emoji":    pais = "Brasil";        cor = "roxo";        break;
            case "galinha":  pais = "Mexico";        cor = "vermelho";    break;
            case "chave":    pais = "Canada";        cor = "azul";        break;
            case "paleta":   pais = "Nova Zelandia"; cor = "amarelo";     break;
            case "controle": pais = "Japao";         cor = "rosa";        break;
            case "laptop":   pais = "Groelandia";    cor = "laranja";     break;
            case "livro":    pais = "Austria";       cor = "azul petrol"; break;
            case "pizza":    pais = "Egito";         cor = "verde";       break;
            case "bonsai":   pais = "Bussola";       cor = "bussola";     break;
            default:         pais = "???";           cor = "???";         break;
        }
    }

    public String getIcone() { return icone; }
    public String getPais()  { return pais; }
    public String getCor()   { return cor; }
}
