package com.GlobeDelegates;

public class Pergunta {
    private String enunciado;
    private String paradigma;
    private String respostaCorreta;
    private String[] opcoes;

    public Pergunta(String enunciado, String paradigma, String respostaCorreta, String[] opcoes) {
        this.enunciado = enunciado;
        this.paradigma = paradigma;
        this.respostaCorreta = respostaCorreta;
        this.opcoes = opcoes;
    }

    public boolean verificarResposta(String resposta) {
        return respostaCorreta.equals(resposta);
    }

    public String getEnunciado() { return enunciado; }
    public String getParadigma() { return paradigma; }
    public String getRespostaCorreta() { return respostaCorreta; }
    public String[] getOpcoes() { return opcoes; }
}
