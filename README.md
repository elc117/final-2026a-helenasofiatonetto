[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/8MfjtJ-y)

<h1>GlobeDelegates</h1>

## Identificação:
Sofia Tonetto e Helena Kellermann - Sistemas de Informação

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


## Processo de desenvolvimento: comentários sobre etapas do desenvolvimento, incluindo detalhes técnicos sobre os recursos de orientação a objetos utilizados, sobre erros/dificuldades/soluções e sobre as contribuições de cada integrante (⚠️ não usar IA para gerar ou revisar esses comentários!)

## Diagrama de classes: 
Começamos fazendo um diagrama somente com o nome das classes, sem pensar em atributos nem métodos
<img width="300" alt="Somente classes do sistema" src="https://github.com/user-attachments/assets/e4f6edce-3f01-4824-bfd4-7f351605891a" />

Depois, construimos o diagrama completo, com todos os atributos e métodos.<br>
<img width="300" alt="Com os atributos e métodos" src="https://github.com/user-attachments/assets/06437183-ff0f-4a19-9b8d-0ceededc8b4e" />
# Diagramas de classes
Diagrama com somente as classes: <br>
<img width="600" height="694" alt="5A9D7ECF-2B63-410E-BA5A-3D39D0A78500" src="https://github.com/user-attachments/assets/cafc4967-ca60-46f6-9fd9-13409b96b09c" /><br>
Diagrama com métodos, classes e atributos:<br>
<img width="800" height="1128" alt="0F6948E7-D574-4DB3-B0E4-E1F0F8544C9E" src="https://github.com/user-attachments/assets/6685f4b0-4b4d-4009-877f-32317688a9ab" />

Utilizamos o site [PlantUML](https://plantuml.com), onde colocarmos o código em java para que ele fizesse o diagrama. 

## Orientações para execução: instalação de dependências, etc.

## Resultado final: demonstrar execução em GIF animado ou vídeo curto

## Referências e créditos (incluindo alguns prompts, quando aplicável)

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
- [ ] Fazer o personagem aparecer na tela e responder a ações do usuário
- [ ] Implementar a escolha do chapéu e a lógica de alocação por país
- [ ] Commit a cada avanço, por pequeno que seja

### Semana 3 — Fase 1: Escape Room
- [ ] Implementar a estrutura básica do Escape Room com placeholders
- [ ] Implementar a lógica de tarefas e quiz
- [ ] Implementar o inventário de acessórios e o sistema de bônus
- [ ] Registrar dúvidas e decisões de design no README

### Semana 4 — Fase 2: Combate de delegações
- [ ] Implementar a lógica de delegações e pontuação
- [ ] Implementar as perguntas de código (Java, Haskell, Prolog)
- [ ] Implementar o ranking final
- [ ] Testar o build web novamente

### Semana 5 — Polimento e entrega
- [ ] Substituir placeholders pelos assets definitivos (com créditos no README)
- [ ] Refatorar o código conforme necessidade e registrar no README
- [ ] Revisar organização das classes e boas práticas de OOP
- [ ] Garantir que ambas têm commits e entendem o projeto como um todo
- [ ] Finalizar o README com diário de desenvolvimento, créditos e instruções<br>

<b>Esse plano de trabalho foi gerado pelo claude ia na versão Sonnet 4.6 PRO com esforço LOW<b>




## Referências

LINKS LIBGDX:

<br>Como começar com LibGDX - https://libgdx.com/dev/
<br>Istruções de Setup - https://libgdx.com/wiki/start/setup
<br>Exemplo "A Simple Game" - https://libgdx.com/wiki/start/a-simple-game
<br>Estendendo "A Simple Game" - https://libgdx.com/wiki/start/simple-game-extended
<br>Tutorial para Iniciante - https://colourtann.github.io/HelloLibgdx/
<br>Wiki da LibGDX - https://libgdx.com/wiki/
<br>Ferramentas para desenvolvimento - https://libgdx.com/dev/tools/

<br>Itchi.io - https://itch.io/
