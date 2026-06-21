package com.GlobeDelegates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.Collections;

public class TelaDelegacao implements Screen {

    private GlobeDelegates jogo;
    private Jogador jogador;
    private SpriteBatch batch;
    private ShapeRenderer shape;
    private BitmapFont font;
    private Texture fundo;

    private String[][] todasPerguntas;
    private String[][] perguntasSelecionadas;
    private int perguntaAtual = 0;
    private int acertos = 0;
    private int minimoAcertos = 3;
    private int opcaoSelecionada = -1;
    private boolean acertou = false;
    private float tempoMensagem = 0;
    private float tempoEspera = 0;
    private float btnW = 520, btnH = 55;
    private String[] opcoesAtuais;
    private int opcaoHighlight = 0;
    private boolean falhou = false;

    public TelaDelegacao(GlobeDelegates jogo, Jogador jogador) {
        this.jogo = jogo;
        this.jogador = jogador;
        batch = new SpriteBatch();
        shape = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(1.6f);
        fundo = new Texture(getPasta(jogador.getPais()) + "/delegacao.jpg");
        configurarPerguntas(jogador.getPais());
        sortearPerguntas();
    }

    private String getPasta(String pais) {
        switch (pais) {
            case "Japao": return "japao";
            case "Mexico": return "mexico";
            case "Canada": return "canada";
            case "Nova Zelandia": return "nz";
            case "Groelandia": return "groelandia";
            case "Austria": return "austria";
            case "Egito": return "egito";
            case "Brasil": return "brasil";
            default: return "japao";
        }
    }

    private void configurarPerguntas(String pais) {
        switch (pais) {
            case "Japao":
                todasPerguntas = new String[][]{
                    {"Qual e a capital do Japao?","Tokio","Osaka","Kyoto","Hiroshima","0"},
                    {"Quais sao as 4 ilhas principais do Japao?","Honshu, Hokkaido, Kyushu, Shikoku","Okinawa, Tokyo, Osaka, Kyoto","Honshu, Fuji, Sakura, Edo","Kyushu, Nagasaki, Hiroshima, Nara","0"},
                    {"Como se chama o teatro tradicional japones?","Kabuki","Noh","Bunraku","Taiko","0"},
                    {"O que e o Bushido?","Uma danca","Um prato tipico","O codigo de honra dos samurais","Um festival","2"},
                    {"Em que ano o Japao sediou as primeiras Olimpiadas?","1956","1964","1972","1980","1"},
                    {"Qual religiao e nativa do Japao?","Budismo","Hinduismo","Xintoismo","Taoismo","2"},
                    {"Como se chama a escrita japonesa de origem chinesa?","Hiragana","Katakana","Kanji","Romaji","2"},
                    {"O que significa 'Arigatou'?","Obrigado","Com licenca","Oi","Tchau","0"},
                    {"O que significa 'Sakura'?","Flor de cerejeira","Montanha","Rio","Samurai","0"},
                    {"O que significa 'Kawaii'?","Fofo/bonito","Perigoso","Grande","Velho","0"},
                    {"O que significa 'Sensei'?","Professor","Guerreiro","Rei","Medico","0"},
                    {"O que significa 'Manga'?","Historia em quadrinhos","Fruta","Musica","Danca","0"}
                };
                break;
            case "Mexico":
                todasPerguntas = new String[][]{
                    {"Qual e a capital do Mexico?","Cidade do Mexico","Guadalajara","Monterrey","Cancun","0"},
                    {"Qual civilizacao construiu as piramides de Teotihuacan?","Maias","Incas","Astecas","Olmecas","2"},
                    {"Quem foi o lider da independencia do Mexico?","Hernan Cortes","Miguel Hidalgo","Moctezuma","Benito Juarez","1"},
                    {"Qual e a moeda do Mexico?","Real","Dolar","Peso","Sol","2"},
                    {"Como se chama a serpente emplumada da mitologia asteca?","Tlaloc","Huitzilopochtli","Quetzalcoatl","Tezcatlipoca","2"},
                    {"O simbolo 'Tonatiuh' representa:","O Sol","A Lua","A chuva","O vento","0"},
                    {"Quantos dias tinha o calendario asteca sagrado?","180","240","260","365","2"},
                    {"O que significa 'Buenos dias'?","Bom dia","Boa tarde","Boa noite","Ola","0"},
                    {"O que significa 'Andale'?","Vamos/Rapido","Pare","Cuidado","Obrigado","0"},
                    {"O que significa 'Orale'?","Ok/Ta bom","Nunca","Talvez","Por que?","0"}
                };
                break;
            case "Canada":
                todasPerguntas = new String[][]{
                    {"Qual e a capital do Canada?","Toronto","Vancouver","Ottawa","Montreal","2"},
                    {"Qual e o animal simbolo do Canada?","Urso polar","Alce","Castor","Aguia","2"},
                    {"Em que ano o Canada se tornou independente?","1776","1867","1901","1945","1"},
                    {"Qual e o esporte nacional do Canada?","Baseball","Futebol","Hoquei no gelo","Basquete","2"},
                    {"Qual e a segunda lingua oficial do Canada?","Espanhol","Frances","Italiano","Alemao","1"},
                    {"O que e um totem?","Uma danca","Uma escultura sagrada com figuras","Um ritual","Uma roupa","1"},
                    {"Como se chama a habitacao tradicional dos Inuit?","Tenda","Iglu","Maloca","Longhouse","1"},
                    {"O que significa 'Inuit'?","Guerreiros","Povo do gelo","Povo","Cacadores","2"},
                    {"Qual e a maior cidade do Canada?","Ottawa","Vancouver","Toronto","Montreal","2"},
                    {"Como se chama a arte de contar historias dos indigenas?","Escrita","Pintura rupestre","Tradicao oral","Teatro","2"}
                };
                break;
            case "Nova Zelandia":
                todasPerguntas = new String[][]{
                    {"Qual e a capital da Nova Zelandia?","Auckland","Wellington","Christchurch","Dunedin","1"},
                    {"Qual e o povo indigena da Nova Zelandia?","Aborigene","Inuit","Maori","Zulu","2"},
                    {"Qual e o esporte nacional da Nova Zelandia?","Cricket","Futebol","Rugby","Natacao","2"},
                    {"Como se chama a danca de guerra maori?","Samba","Haka","Capoeira","Polka","1"},
                    {"Qual ave e simbolo da Nova Zelandia?","Aguia","Kiwi","Papagaio","Flamingo","1"},
                    {"O que significa 'Kia ora' em maori?","Ola/Obrigado","Tchau","Com licenca","Desculpe","0"},
                    {"O que significa 'Aroha' em maori?","Guerra","Amor/Compaixao","Coragem","Sabedoria","1"},
                    {"O que significa 'Whanau' em maori?","Terra","Mar","Familia","Ceu","2"},
                    {"O que significa 'Mana' em maori?","Prestigio/Poder espiritual","Fraqueza","Tristeza","Alegria","0"},
                    {"Como se chama o time de rugby da Nova Zelandia?","Wallabies","Springboks","All Blacks","Lions","2"}
                };
                break;
            case "Groelandia":
                todasPerguntas = new String[][]{
                    {"Qual pais administra a Groelandia?","Canada","Noruega","Dinamarca","Islandia","2"},
                    {"Qual e o povo indigena da Groelandia?","Inuit","Maori","Aborigene","Sami","0"},
                    {"Qual e a capital da Groelandia?","Nuuk","Reykjavik","Tromsoe","Iqaluit","0"},
                    {"Quanto da Groelandia e coberto por gelo?","50%","65%","80%","85%","3"},
                    {"O que significa Groelandia em dinamarques?","Terra do gelo","Terra verde","Terra fria","Terra branca","1"},
                    {"O que e um iceberg?","Uma montanha de neve","Um bloco de gelo que flutua no mar","Um lago congelado","Uma tempestade de neve","1"},
                    {"O que e permafrost?","Chuva congelada","Solo permanentemente congelado","Gelo flutuante","Neve compactada","1"},
                    {"Como os Inuit se locomovem no gelo?","Esquis","Treno puxado por cachorros","Snowmobile","Todas as opcoes","3"},
                    {"O que e a Aurora Boreal?","Uma tempestade artica","Luz solar refletida no gelo","Fenomeno luminoso causado pelo campo magnetico","Estrelas proximas","2"},
                    {"Qual e o maior animal terrestre da Groelandia?","Urso polar","Boi almiscarado","Rena","Lobo artico","1"}
                };
                break;
            case "Austria":
                todasPerguntas = new String[][]{
                    {"Qual e a capital da Austria?","Salzburgo","Innsbruck","Viena","Graz","2"},
                    {"Qual compositor nasceu em Salzburgo?","Beethoven","Bach","Mozart","Chopin","2"},
                    {"Qual e a moeda da Austria?","Coroa","Franco","Euro","Schilling","2"},
                    {"Qual e o rio mais famoso que passa por Viena?","Reno","Danubio","Elba","Sena","1"},
                    {"Qual danca classica e tipica da Austria?","Tango","Valsa","Samba","Flamenco","1"},
                    {"Qual e o prato tipico austriaco?","Schnitzel","Bratwurst","Strudel","Sauerkraut","0"},
                    {"Qual instrumento Mozart tocava desde crianca?","Violino e piano","Flauta","Harpa","Orgao","0"},
                    {"Como se chama a sala de concertos mais famosa de Viena?","Carnegie Hall","Royal Albert Hall","Musikverein","Scala de Milao","2"},
                    {"Quantos anos tinha Mozart quando compôs sua primeira sinfonia?","5","8","10","12","1"},
                    {"Como se chama o palacio imperial mais famoso de Viena?","Versalhes","Buckingham","Schonbrunn","Blenheim","2"}
                };
                break;
            case "Egito":
                todasPerguntas = new String[][]{
                    {"Qual e o rio mais importante do Egito?","Nilo","Amazonas","Tigre","Eufrates","0"},
                    {"Qual farao mandou construir as piramides de Gize?","Tutankamon","Cleopatra","Queops","Ramses II","2"},
                    {"O que e uma mumia?","Um fantasma egipcio","Um corpo preservado quimicamente","Uma divindade","Um sacerdote","1"},
                    {"Qual e a escrita dos antigos egipcios?","Cuneiforme","Alfabeto latino","Hieroglifos","Ideogramas","2"},
                    {"Qual animal era sagrado no Egito antigo?","Cachorro","Cavalo","Gato","Lobo","2"},
                    {"O que significa o Ankh?","Guerra","Morte","Vida eterna","Prosperidade","2"},
                    {"O que e o Olho de Horus?","Um simbolo de mau-olhado","Um simbolo de protecao e cura","Uma constelacao","Um hieroglifo comum","1"},
                    {"Qual deus egipcio tinha cabeca de falcao?","Anubis","Osiris","Horus","Thoth","2"},
                    {"Qual deus guiava os mortos no alem?","Ra","Isis","Anubis","Seth","2"},
                    {"Qual e a capital atual do Egito?","Alexandria","Luxor","Cairo","Assua","2"}
                };
                break;
            case "Brasil":
                todasPerguntas = new String[][]{
                    {"Qual e a capital do Brasil?","Sao Paulo","Rio de Janeiro","Brasilia","Salvador","2"},
                    {"Em que ano o Brasil se tornou independente?","1800","1822","1889","1910","1"},
                    {"Qual e o maior rio do Brasil?","Sao Francisco","Parana","Amazonas","Tocantins","2"},
                    {"Qual e a moeda do Brasil?","Cruzeiro","Real","Cruzado","Mil-Reis","1"},
                    {"Quem proclamou a Republica no Brasil?","Dom Pedro I","Dom Pedro II","Deodoro da Fonseca","Tiradentes","2"},
                    {"Qual e o maior estado do Brasil?","Minas Gerais","Para","Mato Grosso","Amazonas","3"},
                    {"Qual regiao do Brasil tem o maior numero de estados?","Norte","Sul","Nordeste","Centro-Oeste","2"},
                    {"Qual e a culinaria tipica do Nordeste?","Feijoada","Churrasco","Baiao de Dois","Moqueca","2"},
                    {"Qual e o prato nacional do Brasil?","Coxinha","Pao de queijo","Feijoada","Acaraje","2"},
                    {"Qual instrumento e mais usado no samba?","Guitarra","Pandeiro","Violao","Flauta","1"}
                };
                break;
            default:
                todasPerguntas = new String[][]{{"Pergunta?","A","B","C","D","0"}};
        }
    }

    private void sortearPerguntas() {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < todasPerguntas.length; i++) indices.add(i);
        Collections.shuffle(indices);
        int qtd = Math.min(5, todasPerguntas.length);
        perguntasSelecionadas = new String[qtd][];
        for (int i = 0; i < qtd; i++) perguntasSelecionadas[i] = todasPerguntas[indices.get(i)].clone();
        perguntaAtual = 0;
        acertos = 0;
        opcoesAtuais = null;
        falhou = false;
    }

    private String[] getOpcoes() {
        String[] p = perguntasSelecionadas[perguntaAtual];
        int correta = Integer.parseInt(p[5]);
        String[] opcoes = {p[1], p[2], p[3], p[4]};
        ArrayList<Integer> idx = new ArrayList<>();
        for (int i = 0; i < 4; i++) idx.add(i);
        Collections.shuffle(idx);
        String[] embaralhadas = new String[4];
        int novaCorreta = 0;
        for (int i = 0; i < 4; i++) {
            embaralhadas[i] = opcoes[idx.get(i)];
            if (idx.get(i) == correta) novaCorreta = i;
        }
        perguntasSelecionadas[perguntaAtual][5] = String.valueOf(novaCorreta);
        opcaoHighlight = 0;
        return embaralhadas;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        batch.begin();
        ImagemUtil.desenharFundo(batch, fundo, w, h);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            jogo.setScreen(new TelaEscolha(jogo));
            return;
        }

        if (tempoEspera > 0) { tempoEspera -= delta; return; }

        // Falhou — mostrar tela de falha
        if (falhou) {
            renderFalha(w, h);
            return;
        }

        // Concluiu as 5 perguntas
        if (perguntaAtual >= perguntasSelecionadas.length) {
            if (acertos >= minimoAcertos) {
                renderConclusao(w, h);
            } else {
                falhou = true;
            }
            return;
        }

        if (opcoesAtuais == null) opcoesAtuais = getOpcoes();
        renderPergunta(delta, w, h);
    }

    private void renderPergunta(float delta, float w, float h) {
        String[] p = perguntasSelecionadas[perguntaAtual];

        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.08f, 0.08f, 0.15f, 0.92f);
        shape.rect(w/2 - 380, 70, 760, 560);
        shape.end();

        batch.begin();
        font.setColor(1, 0.8f, 0.2f, 1);
        font.draw(batch, jogador.getPais() + " - Delegacao", w/2 - 180, h - 55);
        font.setColor(0.8f, 0.8f, 0.8f, 1);
        font.draw(batch, "Pergunta " + (perguntaAtual+1) + "/5  |  Acertos: " + acertos + "  |  Minimo: " + minimoAcertos, w/2 - 240, h - 85);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, p[0], w/2 - 340, h - 130, 680, 1, true);
        batch.end();

        // Navegar opcoes com W/S
        if (Gdx.input.isKeyJustPressed(Keys.UP) || Gdx.input.isKeyJustPressed(Keys.UP))
            opcaoHighlight = (opcaoHighlight - 1 + 4) % 4;
        if (Gdx.input.isKeyJustPressed(Keys.DOWN) || Gdx.input.isKeyJustPressed(Keys.DOWN))
            opcaoHighlight = (opcaoHighlight + 1) % 4;

        for (int i = 0; i < 4; i++) {
            float bx = w/2 - btnW/2;
            float by = 310 - i * 75;
            shape.begin(ShapeRenderer.ShapeType.Filled);
            if (opcaoSelecionada == i) {
                shape.setColor(acertou ? 0.1f : 0.8f, acertou ? 0.8f : 0.1f, 0.1f, 1);
            } else if (opcaoHighlight == i) {
                shape.setColor(0.35f, 0.35f, 0.6f, 1);
            } else {
                shape.setColor(0.2f, 0.2f, 0.38f, 1);
            }
            shape.rect(bx, by, btnW, btnH);
            shape.end();
            batch.begin();
            font.setColor(1, 1, 1, 1);
            String[] letras = {"A","B","C","D"};
            font.draw(batch, letras[i] + ". " + opcoesAtuais[i], bx + 15, by + btnH - 10);
            batch.end();
        }

        batch.begin();
        font.setColor(0.6f, 0.6f, 0.8f, 0.8f);
        font.draw(batch, "Setas=navegar | ENTER=confirmar | ESC=sair", w/2 - 260, 55);
        batch.end();

        if (opcaoSelecionada == -1) {
            if (Gdx.input.isKeyJustPressed(Keys.ENTER)) confirmarOpcao(opcaoHighlight, p);
            if (Gdx.input.justTouched()) {
                float tx = Gdx.input.getX();
                float ty = h - Gdx.input.getY();
                for (int i = 0; i < 4; i++) {
                    float bx = w/2 - btnW/2;
                    float by = 310 - i * 75;
                    if (tx >= bx && tx <= bx + btnW && ty >= by && ty <= by + btnH)
                        confirmarOpcao(i, p);
                }
            }
        }

        if (opcaoSelecionada != -1) {
            tempoMensagem -= delta;
            batch.begin();
            font.setColor(acertou ? 0.2f : 1f, acertou ? 1f : 0.2f, 0.2f, 1);
            font.draw(batch, acertou ? "Correto!" : "Errado!", w/2 - 70, 75);
            batch.end();
            if (tempoMensagem <= 0) {
                perguntaAtual++;
                opcaoSelecionada = -1;
                opcoesAtuais = null;
                tempoEspera = 0.3f;
            }
        }
    }

    private void confirmarOpcao(int i, String[] p) {
        opcaoSelecionada = i;
        acertou = (i == Integer.parseInt(p[5]));
        if (acertou) acertos++;
        tempoMensagem = 1.5f;
    }

    private void renderFalha(float w, float h) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.5f, 0f, 0f, 0.92f);
        shape.rect(w/2 - 320, h/2 - 100, 640, 200);
        shape.end();
        batch.begin();
        font.setColor(1, 0.3f, 0.3f, 1);
        font.draw(batch, "Poucos acertos! " + acertos + "/" + minimoAcertos + " necessarios", w/2 - 290, h/2 + 55);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "ENTER=tentar novamente | ESC=sair", w/2 - 260, h/2);
        batch.end();
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) sortearPerguntas();
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) jogo.setScreen(new TelaEscolha(jogo));
    }

    private void renderConclusao(float w, float h) {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.05f, 0.2f, 0.05f, 0.92f);
        shape.rect(w/2 - 300, h/2 - 100, 600, 200);
        shape.end();
        batch.begin();
        font.setColor(0.3f, 1f, 0.3f, 1);
        font.draw(batch, "Delegacao concluida! " + acertos + "/5 acertos!", w/2 - 270, h/2 + 55);
        font.setColor(1, 1, 1, 1);
        font.draw(batch, "ENTER=continuar | ESC=sair", w/2 - 200, h/2);
        batch.end();
        if (Gdx.input.isKeyJustPressed(Keys.ENTER) || Gdx.input.justTouched())
            jogo.setScreen(new TelaVilagem(jogo, jogador, true, true));
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) jogo.setScreen(new TelaEscolha(jogo));
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
        batch.dispose(); shape.dispose(); font.dispose(); fundo.dispose();
    }
}
