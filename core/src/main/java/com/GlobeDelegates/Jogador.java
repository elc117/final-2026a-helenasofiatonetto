package com.GlobeDelegates;

public class Jogador {

    private String icone;
    private String pais;
    private String cor;
    private String nome;
    private int pontos;

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
}
