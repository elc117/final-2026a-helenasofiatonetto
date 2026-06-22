<h1>GlobeDelegates</h1>

## Identificação:
Sofia Tonetto e Helena Kellermann - Sistemas de Informação

Link para o jogo no itch.io: [GlobeDelegates](https://hlk0000.itch.io/globedelegates)

## Proposta:
Globe Delegates é voltado para a temática de viagens e [simulações da ONU](https://www.crimsoneducation.org/br/blog/simulacao-da-onu), também conhecidas como 'Model United Nations'. o MUN é um evento acadêmico no qual estudantes representam diplomatas de diferentes países para debater questões globais.<br>
A ideia do jogo é explorar curiosidades e a história (de alguns) dos países envolvidos na ONU, ligando esse tema com java e grupos.
Desenvolvemos o jogo utilizando o framework de desenvolvimento de jogos [libGDX](https://libgdx.com/) em conjunto com a plataforma digital [itch.io](https://itch.io/) para hospedar na web.

Primeira Parte - Simulação:
* Personagem: jogador escolhe um acessório que determinará o país que representará, ou seja, de qual delegação (grupo) ele fará parte;
* Delegação: composta pelo jogador com seu personagem escolhido;
* Escape Room: sala com tarefas que deverão ser cumpridas como quizzes, coletar acessórios, etc.
* Bônus: quem completar todo o escape room, ganhará uma menção honrosa ou outro acessório (como ocorre nas simulações), ou ainda, ganhará uma dica para a parte do Combate de Delegações;
* Países: 8 países representados por 8 vestimentas diferentes;
* Perguntas: baseadas nas curiosidades e história dos países representados.

Segunda Parte - Combate de Delegações:
* Multiplayer: parte pensada para jogar-se em conjunto, onde os alunos escolhem os personagens;
* Perguntas: organizadas em códigos em Haskell, Prolog e Java, em que os jogadores deverão ler e identificar a saída correta do código ou o que está faltando nele.


## Processo de desenvolvimento:
## Decisões durante o desenvolvimento
No EscapeRoom pensamos que somente coletar os itens ficaria muito fácil, por isso implementamos uma senha que é a inicial dos obejtos e quando digitadas  na ordem que os objetos foram coletados, tmabém colocamos a ordem de coleta aleatória para que não fique sempre com a mesma ordem, é liberada a fase de Delegação com um quiz sobre cada paiz. Nessa parte a maior dificuldade foi deixar um limite de 5 perguntas com um minímo de 3 acertos, pois na pré entrega tínhamos as 5 perguntas, mas depois vinham mais 6 e não estavamos conseguindo deixar as 5 só em modo aleatório. Na proposta pensamos que o Bônus seria uma dica, mas como estavamos tendo muitas ideias de atividades, resolvemos alterar para que o bônus fosse uma atividade diferente para cada país: 

<b>Japão</b> - montar haiku clicando nas palavras
<b>México</b> - jogo de decorar a ordem das cores e repetir
<b>Canadá</b> - desviar o barco dos objetos que estão vindo
<b>Nova Zelândia</b> - Navegação por coordenada 
<b>Groelândia</b> - puzzle deslizante para formar imagem ao colocar números na ordem
<b>Áustria</b> - monstar obra de arte ao arrastar partes da pintura que estão faltando
<b>Egito</b> - pirâmide que deve ser montada com os blocos que estão caindo
<b>Brasil</b> - sacola para pegar os itens na feira
<b>Bússola</b> - atividades do Peru ("escavação para descobrir objetos incas"), China ("colocar sequência da lanterna correta) e Grécia (tocha olímpica)

Nessa parte surgiram muitas ideias e foi usado o Claude Sonnet 4.6 com esforço baixo versão Pro para ajudar nas movimentações de cada jogo. Então descrevi o que cada parte do jogo teria e o que eu não sabia para que ele me desse o código que eu entendesse e que funcionasse. 

No multiplayer pensamos em ter perguntas sobre as 3 linguagens que estudamos onde 2 pessoas se enfrentam.

Na prévia tinha um arquivo para cada país para cada jogo, então tinha o TelaBonusJapao, o TelaEscapeJapao e o TelaDelegacaoJapao, o que ficou muito ruimm por isso alteramos, usando o conceito de polimorfismo para que tivesse um arquivo TelaEscape e um TelaDelegacao com todos os países, mas não consegui fazer isso para o bônus, pois cada país tem uma atividade diferente. Como não sabíamos como resolver, recorremos ao Claude e solicitamos como solucionar, assim fomos seguindo as intruções e pedindo ajuda com o código para ele. 

Tambem decidimos não implementar a parte de ranking, pois não sabiamos e não teríamos tempo de implementar essa funcionalidade e como não era fundamental para o jogo, decidimos não fazer, já que não altera nada no jogo.

Para ficar mais fácil, organizamos os assets com uma pasta para cada país. Contamos errado, então precisavámos de 8 países, mas tinhamos 9 ícones e já tínhamos 8 cores separadas, então colocamos uma Bússola que representaria o nono país. No multiplayer foi complicado a parte de lidar com duas pessoas e um teclado sem que desse conflito. Seguindo o Claude, usamos o ImagemUtil para uma classe de estéticaa para ajustar as distorções e repetição de código nas telas.


### Entrega Parcial (14/06): O que foi feito até agora

* etapas básicas: adicionado pasta oculta .devcontainer/ e formulários enviados;
* exemplos: foi rodado os exemplos sugeridos para termos uma base de como funcionaria o projeto;
* readme: adicionado a proposta do projeto, links usados como referẽncia, diagrama de classes, planejamento do trabalho e agora, uma breve revisão do que foi feito até a data de entrega parcial.
* projeto: criado o projeto através do gdx-liftoff. Tendo ele rodado sem problemas, iniciou-se a criação das classes e implementação do trabalho em si. Pasta assets recebeu a inclusão de diversas imagens que foram selecionadas para se ter uma ideia da parte visual do trabalho. Aos poucos, conforme o projeto recebia incrementações, era realizado cada vez o teste para ver se estava rodando, desde as telas/cenários, quizzes, ações do jogador, inclusão total de um país, etc. Foi testado também os comandos para rodar o index.html do jogo, que funcionaram tranquilamente, sem problemas. Após a conclusão básica da primeira etapa do jogo, iniciou-se a parte do multiplayer.
* itch.io: como grande parte dos requisitos para se entregar parcialmente o projeto foram atendidos, criou-se as contas no itch.io para realizar o upload do jogo para que o mesmo ficasse disponível para o público.
* próximos passos: revisar mais uma vez o projeto, anotar as melhorias e implementa-las. A seguir, algumas anotações e revisão do trabalho.

BUGS GERAIS
* backspace não funciona na senha, tem que dar enter e escrever de novo. se esquece a senha/nao sabe, fica preso na tela
* após completar um país, não encerrra o jogo, fica no cenário do país
* mesmo errando todas as respostas, a delegação é concluida.

PAÍSES
* Egito: ok, tem bastante objetos a coletar
* Japao: haiku do rio que corre, não some o 'que' da lista. não é problema pq se clicar nele ele some junto com o outro 'que'
* Austria: ok
* Groelandia: trocar 'todos os anteriores' por 'todas opcoes' no meio de locomoção dos inuits.
* Canada: ok

SUGESTOES DE MUDANÇAS GERAIS
* trocar setas por wasd, clicar para coletar por space;
* se possivel, trocar clicar na opção por percorrer as opções e enter para escolher;
* ter a opção de voltar no esc(mas jogador terá de recomeçar)
* esc para desistir/voltar ao menu inicial
* colocar um minimo de acertos nos quizzes

No multiplayer:
* trocar numero 1,2,3,4 das opções por A,B,C,D
* fazer uma tela de explicação do jogo:<br>
"digitou opção certa, ganha 1 pt"<br>
"digitou oção errada, -1 vida"<br>
"se um errar, o outro pode escolher uma opção"<br>
"ganha quando adversário perder as 3 vidas"<br>

Nos países:
* Canada: no bonus, colocar os obstaculos vindo da vertical e o bonequinho ser um barril
* Egito: colocar simbolos no lugar das letras, aumentar velocidade

REFINAMENTO:
* largura das imagens
* placeholders
* telas dos quizzes
* imagens de fundo
* jogador
* localização dos objetos/entradas

Esses pontos foram levantados quando as duas testaram e anotaram o que deveria ser resolvido futuramente até a entrega final.

### Entrega Final (21/06):

* Focamos em corrigir bugs e aprimorar o visual. Correções desde a entrega parcial:

BUGS GERAIS
- ✅ backspace não funciona na senha, tem que dar enter e escrever de novo. se esquece a senha/nao sabe, fica preso na tela
- ✅ após completar um país, não encerrra o jogo, fica no cenário do país
- ✅ mesmo errando todas as respostas, a delegação é concluida.

PAÍSES
* ✅ Egito: ok, tem bastante objetos a coletar
* Japao: haiku do rio que corre, não some o 'que' da lista. não é problema pq se clicar nele ele some junto com o outro 'que'
* Austria: ok
- ✅ Groelandia: trocar 'todos os anteriores' por 'todas opcoes' no meio de locomoção dos inuits.
* Canada: ok

SUGESTOES DE MUDANÇAS GERAIS
* trocar setas por wasd, clicar para coletar por space;
* se possivel, trocar clicar na opção por percorrer as opções e enter para escolher;
* ter a opção de voltar no esc(mas jogador terá de recomeçar)
* esc para desistir/voltar ao menu inicial
* colocar um minimo de acertos nos quizzes

No multiplayer:
- ✅ Trocar numero 1,2,3,4 das opções por A,B,C,D
- ✅ Removido a descrição do multiplayer "2 jogadores 1 teclado" no menu inicial
* fazer uma tela de explicação do jogo:<br>
"digitou opção certa, ganha 1 pt"<br>
"digitou oção errada, -1 vida"<br>
"se um errar, o outro pode escolher uma opção"<br>
"ganha quando adversário perder as 3 vidas"<br>

Nos países:
- ✅ Canada: no bonus, colocar os obstaculos vindo da vertical e o bonequinho ser um barril
* Egito: colocar simbolos no lugar das letras, aumentar velocidade

REFINAMENTO:
- ✅ Alterado arquivos: aplicação de polimorfismo e herança para redução da quantidade de arquivos
- ✅ largura das imagens
* placeholders
* telas dos quizzes
* imagens de fundo
* jogador
* localização dos objetos/entradas

## Diagrama de classes:
Diagrama final de classes do jogo: <br><img width="1839" height="1591" alt="classes final" src="https://github.com/user-attachments/assets/fcc5d96a-fab4-4b9e-9283-02c21adfb64d" />
O diagrama final ficou diferente do primeiro que fizemos. Porque como optamos em escolher 9 ícones em que cada um seria um país, não precisamos mais do Acessorio nem Chapeu. A classe BonusAtividade foi adicionada seguingo o  conselho para tratar a parte dos bônus igualmente. O Persongaem ficou mais simples porque não tem mais a parte do Acessorio nem do Chapeu.
Começamos fazendo um diagrama somente com o nome das classes, sem pensar em atributos nem métodos. Depois, construimos o diagrama completo, com todos os atributos e métodos.<br>
# Diagramas de classes
Diagrama com somente as classes: <br>
<img width="600" height="694" alt="5A9D7ECF-2B63-410E-BA5A-3D39D0A78500" src="https://github.com/user-attachments/assets/cafc4967-ca60-46f6-9fd9-13409b96b09c" /><br>
Diagrama com métodos, classes e atributos:<br>
<img width="800" height="1128" alt="0F6948E7-D574-4DB3-B0E4-E1F0F8544C9E" src="https://github.com/user-attachments/assets/6685f4b0-4b4d-4009-877f-32317688a9ab" />

Utilizamos o site [PlantUML](https://plantuml.com), onde colocarmos o código em java para que ele fizesse o diagrama.

## Orientações para execução: instalação de dependências, etc.
Você pode jogar o jogo pelo itch.io : [GlobeDelegates](https://hlk0000.itch.io/globedelegates)
Para rodar o código: ./gradlew lwjgl3:run (usamos a versão Java 21)
Para o build web: ./gradlew html:dist

## Resultado final: demonstrar execução em GIF animado ou vídeo curto

## Referências e créditos (incluindo alguns prompts, quando aplicável)
Todas as perguntas da parte de delegação foram retiradas de sites de quizzes como BuzzFeed e Quizur.
Todas as sprites foram retiradas do Pinterest.
Prompt para resolver problema de muitos arquivos Bonus: 
"No meu jogo libGDX, cada país tem um bônus diferente. Como posso usar conceitos de POO para que uma classe TelaBonus funcione para qualquer país sem saber qual é? Me diz o conceito e como aplicar." A partir disso, fomos construindo o código. 
"Tenho uma classe por país na atividade tipo TelaEscapeJapao, TelaEscapeMexico como  mudar usando POO para ter menos arquivos?" - perguntei para alterar os muitos artigos que tinha
"Como fazer um puzzle deslizante 4x4 em libGDX? Como fazer drag and drop de imagens em libGDX? Como detectar colisão entre dois retângulos em libGDX? Como funciona o padrão Screen do libGDX? Como trocar de tela dentro do jogo?" - usadas para os jogos do bônus.


## Plano de trabalho
### Semana 1 — Ambientação com libGDX
- ✅ Executar os exemplos simples fornecidos no material de aula
- ✅ Configurar o projeto com build para web/html desde o início
- ✅ Testar o build web e garantir que funciona
- ✅ Cada integrante cria pequenos exemplos individuais com libGDX
- ✅ Pensar nos diagramas de classes

### Semana 2 — Mecânica básica
- ✅ Definir e combinar a forma de colaboração da dupla (síncrona ou assíncrona) e registrar no README
- ✅ Implementar a tela inicial com sprites placeholder
- ✅ Fazer o personagem aparecer na tela e responder a ações do usuário
- ✅ Implementar a escolha do chapéu e a lógica de alocação por país
- ✅ Commit a cada avanço, por pequeno que seja

### Semana 3 — Fase 1: Escape Room
- ✅ Implementar a estrutura básica do Escape Room com placeholders
- ✅ Implementar a lógica de tarefas e quiz
- ♦️ Implementar o sistema de bônus
- ✅ Registrar dúvidas e decisões de design no README

### Semana 4 — Fase 2: Combate de delegações
- ✅ Implementar a lógica de delegações e pontuação
- ✅/falta Implementar as perguntas de código (Java, Haskell, Prolog)
- ✖️ Implementar o ranking final
- [ ] Testar o build web novamente

### Semana 5 — Polimento e entrega
- ✅ Substituir placeholders pelos assets definitivos (com créditos no README)
- ✅ Refatorar o código conforme necessidade e registrar no README
- ✅ Revisar organização das classes e boas práticas de OOP
- ✅ Garantir que ambas têm commits e entendem o projeto como um todo
- [ ] Finalizar o README com diário de desenvolvimento, créditos e instruções<br>

<b>Esse plano de trabalho foi gerado pelo claude ia na versão Sonnet 4.6 PRO com esforço LOW<b>

## Referências
<br>Como começar com LibGDX - https://libgdx.com/dev/
<br>Istruções de Setup - https://libgdx.com/wiki/start/setup
<br>Exemplo "A Simple Game" - https://libgdx.com/wiki/start/a-simple-game
<br>Estendendo "A Simple Game" - https://libgdx.com/wiki/start/simple-game-extended
<br>Tutorial para Iniciante - https://colourtann.github.io/HelloLibgdx/
<br>Wiki da LibGDX - https://libgdx.com/wiki/
<br>Ferramentas para desenvolvimento - https://libgdx.com/dev/tools/
<br>Itchi.io - https://itch.io/
<br>[https://www.alura.com.br/artigos/poo-programacao-orientada-a-objetos?utm_term=&utm_campaign=&utm_source=google&utm_medium=cpc&campaign_id=23805973578__&utm_id=23805973578__&hsa_acc=7964138385&hsa_cam=&hsa_grp=&hsa_ad=&hsa_src=x&hsa_tgt=&hsa_kw=&hsa_mt=&hsa_net=google&hsa_ver=3&gad_source=1&gad_campaignid=23815806613&gbraid=0AAAAADpqZICzPPsBWNgiNa6jJxcJGt5Bn&gclid=CjwKCAjwl97RBhBWEiwAa9rbXR8-b8jDVQPVTY9bG9T7NRf6UmlqAxzA59hh_Po_--0c4QXy7aO8cRoCbfMQAvD_BwE](POO)
<br>https://github.com/AndreaInfUFSM/elc117-2026a 
<br> https://github.com/BenJeau/JavaFX-SimonSays - jogo do méxico para começar a pensar na lógica
