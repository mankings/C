# Projecto de Compiladores 2021/2022

## Tema pDraw, grupo p4g3
### Nota Final: 17
-----
## Index

1. Introdução
2. Constituição dos grupos e participação individual global
3. pdraw ( Visão Global e documentação)
4. Pen (Visão Global e documentação)
5. Compilador, Interpretador e Analise Semântica
6. Exemplos
7. Tratemento de erros semanticos
8. Script de compilaçao

## 1. Introdução

Este documento README.md é o relatório do grupo p4g3 da cadeira de Compiladores 2021/2022.

O seguinte projeto que nos foi atribuido teve como objetivo o desenvolver, criar e implementar uma linguagem de programação, que premitisse a realização de desenhos graficos 2D utilizando o conceito de canetas.

Complementar à primeira linguagem, tambem foi criada uma segunda linguagem, simples e intuitiva, que permite a gestão de canetas.

Para se implementar os objetivos esperados foram usadas várias ferramentas, tais como _ANTLR4_ para construção da árvore sintática e _StringTemplates_ para a geração de código na lingugagem de destino. Vários ambientes de desenvolvimento integrados tambem foram usados como por exemplo, o _Visual Studio Code_ e o _Clion_.

As linguagens de programação usadas para analizar semanticamente e compilar o código da primeira linguagem foram _JAVA_, sendo que a liguagem de destino escolhida foi _C++_.

## 2. Constituição dos grupos e participação individual global

| NMec   | Nome                            | email                        | Participação (%) |
|:------:|:--------------------------------|:-----------------------------|:----------------:|
| 98292  | CATARINA CRUZ OLIVEIRA          | catarinacruzoliveira01@ua.pt | 16.0%            |
| 103696 | CATARINA TEVES MARTINS DA COSTA | catarinateves02@ua.pt        | 16.0%            |
| 103470 | FILIPE MAIA ANTÃO               | fantao@ua.pt                 | 16.0%            |
| 103341 | JOAO MIGUEL ALMEIDA MATOS       | miguelamatos@ua.pt           | 16.0%            |
| 104171 | LUÍS CARLOS CASANOVA AFONSO     | luiscarlos.c.afonso@ua.pt    | 20.0%            |
| 100181 | YANIS MARINA FAQUIR             | yanismarinafaquir@ua.pt      | 16.0%            |

## 3. pdraw ( Visão Global e documentação)
------------------------------------ **Visão Global**--------------------------------------------

A linguagem pdraw tem o proposito de ser uma ferramenta de desenho 2D para o publico em geral.
Para que isto seja possivel, esta usa o conceito de canetas.
Cada instrução da linguagem deve terminar com um ponto e vírgula (';').

**Estrutura da Caneta**

No fundo, uma caneta é definida por um conjunto de propriedades, que podem ser:
  name - nome da caneta;
  color - cor em que a caneta escreve;
  direction - direção em que a caneta aponta;
  position - ponto em que a caneta se encontra;
  thickness - grossura da linha que a caneta deixa para trás.

**Declaração de Variaveis**

Uma variável é definida atravês de uma instrução do tipo:
    [type] [name] -> [value];
  type - tipo da variável (Pen, String, Int, Color, etc.;
  name - nome da variável;
  value - valor a atribuir à variável.

No caso de criação de uma caneta, os valores das propriedades devem ser passados da seguinte forma:
  Pen p1 -> {prop1: value1, prop2: value2, ...}
Assim, é especificada a propriedade onde encaixa tal valor.

----------------------------------------**Documentação**------------------------------------------------
### Declaração

**Tipos de Variveis disponiveis**

Os tipos de variaveis disponiveis são os seguintes:

'String' | 'Double' | 'Int' | 'Boolean' | 'Direction' | 'Color' 

**Criação de uma caneta**

Para criar uma caneta usa-se a seguinte estrutura:

'Pen' ID '->' assignProperty;

Aqui ID é a variavel que representa a caneta, a quem serão atribuidas as propriedades especificadas, tais como a propriedade 'name'.

| property         | Descrição                                                                                          | 
|------------------| ---------------------------------------------------------------------------------------------------|
|'color'           | Expressão hexadecimal que representa a cor da caneta                                               |
|'thickness'       | Numero inteiro ou não que representa a expressura da caneta                                        |
|'direction'       | Direção para onde a caneta vai, pode ser : 'UP','DOWN','LEFT','RIGHT','NORTH','WEST','SOUTH','EAST'|
|'position'        | Posição da caneta na tela, representada por um ponto (2 ints)                                      |
|'name'            | Expressão string que define o nome da caneta                                                       |

Estes são exemplos de instruções válidas:
  Pen verdinha -> {color: #32a852, thickness: 2}
  Pen perdida -> {direction: NORTH, position: (0, 0)}
  verdinha -> {color: #1f5bcc, name: 'azulinha'}

### Métodos

A seguinte tabela contém todas as funções que o pdraw possui:

| Métodos                        | Exemplo                                                                                       |
|--------------------------------|-----------------------------------------------------------------------------------------------|
| 'input' type                   | Introduzir qualquer tipo de valor (I/O)                                                       |
| 'output' type                  | Passar um valor para a consola (I/O)                                                          |

Estes são alguns exemplos de instruções válidas, assumindo que a caneta "Mankings" está já definida:
  output("Hello World");
  input Int;

### Operadores

A seguinte tabela contém todos os operadores que o pdraw possui:

| Operadores                          |  Descrição                                                                          |
|------------------------------------.|-------------------------------------------------------------------------------------|
|'or'|'and'                           | Operadores logicos                                                                  |
|'*' | '/'                            | Operadores da multiplicação e da divisão (aritmeticos)                              |
|'+' | '-'                            | Operadores da soma e da subtração (artimeticos)                                     |
|'==' | '!=' | '<' | '>' | '>=' |'<=' | Operadores de comparação (boolean)                                                  |
| pen 'move' int                      | Move a caneta o número de unidades especificadas na direção da caneta               |
| pen 'move' int 'rotated' int|dir    | Move a caneta o número de unidades especificadas na direção especificada            |
| pen 'moveTo' point                  | Move a caneta para um especifico ponto                                              |
| pen 'rotate' int|dir                | Roda a caneta para estar virada para a direção especificada                         |
| pen 'up'                            | Permite que a caneta escreva/deixe escrito para trás                                |
| pen 'down'                          | Faz com que a caneta pare de escrever                                               |
| pen 'move' int 'rotated' int|double | Move a caneta o número de unidades especificadas com uma rotação em grau específica | 

Estes são exemplos de instruções válidas: 
  1*4;
  1 + 2;
  Int v1 -> 20;
  Int el -> 1;
  v1/el;
  Mankings moveTo (5, 5);
  Mankings move 5;
  Mankings rotate SOUTH;
  Mankings up;
  Mankings down;

Esta gramatica ainda consegue definir tanto números negativos como números positivos (op=('+' | '-') e2=expr), utilizando '-' para transforma um número no seu negativo e '+' no seu inverso.


### Expressões Condicionais

A pdraw também possui algumas expressoes condicionais tais como, 'if', 'else','ifelse'.

Estes são alguns exemplos de instruções válidas:

```

Int inp -> 0;

Pen P1 -> {color : #ffcbdb};

output("Largura do Traço of the Pen");

inp -> input Int;

if (inp > -1) -> {
    P1 -> {thickness : inp};
} else -> {
    output("Por favor escolha um número inteiro Positivo");
} 

```

### Ciclos

Para que a linguagem ficasse completa, criamos 2 ciclos, um sendo o 'repeat' (onde uma instrução é que repetida as vezes que for necessaria até ser parada) e o outro sendo o 'repeatNtimes' (onde uma instrução é repetida N vezes).

Estes são alguns exemplos de instruções válidas:
  
  Pen p1 -> {thickness:1,color : #2ad8fb};
  repeat (5) times -> {p1 move 10;
      p1 rotate 20;
  }

  Boolean b -> false;
  repeat (b) -> {
      p1 rotate 20;
  }

## 4. Pen (Visão Global e documentação)
------------------------------------ **Visão Global**--------------------------------------------

A linguagem secundária Pen tem o proposito de ler assinaturas de canetas.Assim como a gramática principal, cada instrução da linguagem deve terminar com um ponto e vírgula (';').

------------------------------------ **Documentação**-------------------------------------------
### Declaração

**Tipos de Variveis disponiveis**

Os tipos de variaveis disponiveis são os seguintes:

'String' | 'Double' | 'Int' | 'Boolean' | 'Direction' | 'Color'

**Criação de uma caneta**

Para criar uma caneta usa-se a seguinte estrutura:

'Pen' ID '->' assignProperty;

Aqui ID é a variavel que representa a caneta, a quem serão atribuidas as propriedades especificadas, tais como a propriedade 'nome'.

| property         | Descrição                                                                                          | 
|------------------| ---------------------------------------------------------------------------------------------------|
|'color'           | Expressão hexadecimal que representa a cor da caneta                                               |
|'thickness'       | Numero inteiro ou não que representa a expressura da caneta                                        |
|'direction'       | Direção para onde a caneta vai, pode ser : 'UP','DOWN','LEFT','RIGHT','NORTH','WEST','SOUTH','EAST'|
|'position'        | Posição da caneta na tela, representada por um ponto (2 ints)                                      |
|'name'            | Expressão string que define o nome da caneta                                                       |

Estes são exemplos de instruções válidas:
  Pen canetaBanana -> {
    thickness : 2
    color : yellow
    line_format : dashed
}
  Pen canetaOink -> {
    thickness : 4
    color : pink
    line_format : continuous
}
  Pen canetaMorango -> {
    thickness : 1
    color : red
    line_format : dashed
}
  
### Operadores

A seguinte tabela contém todos os operadores que a Pen possui:

| Operadores                         |  Descrição                                            |
|------------------------------------|-------------------------------------------------------|
|'*' | '/'                           | Operadores da multiplicação e da divisão (aritmeticos)|
|'+' | '-'                           | Operadores da soma e da subtração (aritmeticos)       |

Esta gramatica, tal como a priciapl,ainda consegue definir tanto numeros negativos como numeros positivos (op=('+' | '-') e2=expr),utilizando '-' para transforma um número no seu negativo e '+' no seu inverso.

## 5. Compilador, Interpretador e Analise Semântica

Para fazer o compilador desta linguagem foi usado o padrão de software visitor que faz parte integrante da biblioteca ANTLR4. 

Neste padrão cada nó da árvore sintática é visitado de maneira recursiva. Desta forma, temos grande facilidade e flexibilidade no que toca ao tratamento da árvore sintática produzida pelo parser do ANTLR4.

Para a gestão dos templates a serem usados aquando da geração de código, foi utilizada a biblioteca _StringTemplate_, escrita em java.

Esta biblioteca, _StringTemplate_, encarrega-se de carregar o ficheiro de templates, com a extensão _.st_, e de fornecer uma API robusta e amigável para a inserção de outros templates recursivamente.

## 6. Exemplos

Este é um exemplo de um script simples que pode ser criado utilizado esta linguagem. O resultado é uma espiral.
Mais exemplos podem ser encontrados na pasta respetiva.

```
// Criar um quadrado em espiral branco
Pen pSquare -> {thickness: 2, color: #FFFFFF};

pSquare moveTo (0, 0);
pSquare down;

pSquare rotate UP;
pSquare move7;

pSquare rotate RIGHT;
pSquare move 6;

pSquare rotate DOWN;
pSquare move 5;

pSquare rotate LEFT;
pSquare move 4;

pSquare rotate UP;
pSquare move 3;

pSquare rotate RIGHT;
pSquare move 2;

pSquare rotate DOWN;
pSquare move 1;

pSquare up;
// done
```

## 7. Tratamento de erros semânticos

Na análise semântica, fazemos o tratamento de todos os erros que possam acontecer na nossa linguagem. Cada visitor retorna um boolean com o resultado da verificação dos erros. Por exemplos, dois dos erros mais comuns e como são tratados são:
  - operações entre variáveis, por exemplo, soma e subtração de duas variáveis apenas funcionam se as variáveis forem ints ou doubles
  - ao atribuir valor a uma variável, é necessário que o valor atribuido corresponda ao tipo de variável

## 8.  Script de compilação

Para que a compilaçao da linguagem principal e secundaria fosse feita de forma correta, criou-se um sript de compilaçao que ajuda-se nessa tarefa.

## Organização do repositório

- **src** - deve conter todo o código fonte do projeto.

- **doc** -- deve conter toda a documentação adicional a este README.

- **examples** -- deve conter os exemplos ilustrativos das linguagens criadas.

    - Estes exemplos contêm comentários (no formato aceite pelas linguagens), 
      que os tornem auto-explicativos.

## 9. Como Correr

Dentro da pasta src correr script compile.sh, com os seguintes argumentos:

-m nome_exemplo.pd para testar exemplos da gramática Principal;
-a nome_exemplo.pen para testar exemplos da gramática Secundária.

## Contribuições

Aqui estão as contribuições de cada um:
  Catarina Oliveira - linguagem secundaria, gramática principal, um pouco da analise semantica
  Luís Casanova - compilador, biblioteca em C++, interpretador
  Catarina Costa - linguagem secundaria, interpretador, relatório
  Filipe Antão - gramática principal, análise semântica, exemplos
  João Matos - gramática principal, análise semântica, relatório
  Yanis Faquir - exemplos, gramática principal e secundária

Decidimos unanimemente atribuir 20% de participação ao Luís no projeto porque todos sentimos que, além daquilo que lhe foi designado fazer, ele sempre esteve disponível para nos ajudar a todos quando sentímos mais dificuldade.
