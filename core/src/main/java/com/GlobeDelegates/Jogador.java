package com.GlobeDelegates;

public class Jogador {

    private String icone;
    private String pais;
    private String cor;
    private String nome;
    private int pontos;
    private int faseBussola = 0; // 0=China, 1=Peru, 2=Grecia

    public Jogador() {
        this.pontos = 0;
        this.nome = "Jogador";
    }

    public void setIcone(String icone) {
        this.icone = icone;
        definirPais();
    }

    private void definirPais() {
        switch (icone) {
            case "emoji":    pais = "Brasil";           cor = "roxo";          break;
            case "galinha":  pais = "Mexico";           cor = "vermelho";      break;
            case "chave":    pais = "Canada";           cor = "azul";          break;
            case "paleta":   pais = "Nova Zelandia";    cor = "amarelo";       break;
            case "controle": pais = "Japao";            cor = "rosa";          break;
            case "laptop":   pais = "Groelandia";       cor = "laranja";       break;
            case "livro":    pais = "Austria";          cor = "azul petroleo"; break;
            case "pizza":    pais = "Egito";            cor = "verde";         break;
            case "bonsai":   pais = "Bussola";          cor = "bussola";       break;
            default:         pais = "???";              cor = "???";           break;
        }
    }

    public void adicionarPontos(int pts) { this.pontos += pts; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getPontos() { return pontos; }
    public String getIcone() { return icone; }
    public String getPais()  { return pais; }
    public String getCor()   { return cor; }
    public int getFaseBussola() { return faseBussola; }
    public void avancarFaseBussola() { faseBussola++; }
    public String getPaisBussola() {
        switch (faseBussola) {
            case 0: return "Bussola-China";
            case 1: return "Bussola-Peru";
            case 2: return "Bussola-Grecia";
            default: return "Bussola-Grecia";
        }
    }
}
