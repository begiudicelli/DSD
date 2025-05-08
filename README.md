# Sistemas Distribuídos - FUMEC
## Exercícios Resolvidos de Concorrência e Paralelismo

Este repositório contém implementações de exercícios práticos sobre concorrência, paralelismo e sincronização de threads utilizando semáforos em Java. Os exercícios fazem parte da disciplina de Sistemas Distribuídos da FUMEC.

## Estrutura do Repositório

```
├── src/
│   ├── ex1/        # Impressão coordenada de threads A, B e C
│   ├── ex2/        # Problema do Barbeiro Dorminhoco
│   ├── ex3/        # Contador Limitado com semáforos
│   ├── ex4/        # Problema da Montanha-Russa
│   └── ex5/        # Leitores e Escritores sem inanição
└── README.md
```

## Descrição dos Exercícios

### Exercício 1: Impressão Coordenada de Threads
Implementação de três threads A, B e C que imprimem repetidamente as letras "A", "B" e "C", respectivamente. A solução utiliza semáforos para garantir que o número de letras "C" impressas seja sempre menor ou igual à soma das letras "A" e "B" já impressas.

**Classes principais:**
- `LetraA.java`, `LetraB.java`, `LetraC.java`: Implementações das threads
- `ControleImpressao.java`: Coordenação da impressão usando semáforos
- `Principal.java`: Classe principal para execução

### Exercício 2: Barbeiro Dorminhoco
Implementação do clássico problema de concorrência "Barbeiro Dorminhoco", onde um barbeiro (thread) atende clientes (threads) que chegam à barbearia. Se não há clientes, o barbeiro dorme. Os clientes procuram uma cadeira livre na sala de espera; caso não encontrem, vão embora.

**Classes principais:**
- `Barbeiro.java`: Implementação da thread do barbeiro
- `Cliente.java`: Implementação da thread de cliente
- `Barbearia.java`: Classe de sincronização usando semáforos
- `Principal.java`: Classe principal para execução e configuração

### Exercício 3: Contador Limitado
Implementação de uma classe `ContadorLimitado` com valores máximo e mínimo, que oferece métodos `incrementa()` e `decrementa()`. As operações bloqueiam quando os limites são atingidos e desbloqueiam quando se tornam possíveis novamente.

**Classes principais:**
- `ContadorLimitado.java`: Implementação do contador com semáforos
- `Decrementador.java` e `Incrementador.java`: Threads de teste
- `Principal.java`: Classe para demonstração

### Exercício 4: Montanha-Russa
Solução para o problema da montanha-russa, onde threads de passageiros precisam coordenar com uma thread de carro. O carro só parte quando estiver cheio com `c` passageiros.

**Classes principais:**
- `Passageiro.java`: Implementação das threads de passageiros
- `Carro.java`: Implementação da thread do carro /  Classe de sincronização com semáforos
- `Principal.java`: Configuração e execução

### Exercício 5: Leitores e Escritores sem Inanição
Uma solução para o problema de leitores e escritores que garante que nenhuma thread sofrerá inanição, ou seja, tanto leitores quanto escritores terão sempre a chance de acessar o recurso compartilhado.

**Classes principais:**
- `Leitor.java`: Implementação das threads de leitores
- `Escritor.java`: Implementação das threads de escritores
- `BancoDeDados.java`: Controle de acesso ao recurso compartilhado
- `Principal.java`: Demonstração do funcionamento

## Como Executar

Para executar cada exercício, navegue até o diretório correspondente e execute a classe `Principal`:

```bash
# Compilar
javac exercicioN/*.java

# Executar
java exercicioN.Principal
```

Substitua `N` pelo número do exercício (1 a 5).

## Conceitos Explorados

- **Semáforos**: Mecanismo de sincronização para controlar o acesso a recursos compartilhados
- **Threads**: Unidades de execução concorrente dentro de um processo
- **Concorrência**: Execução de múltiplas tarefas que parecem executar simultaneamente
- **Paralelismo**: Execução real de múltiplas tarefas ao mesmo tempo
- **Problemas Clássicos de Concorrência**: Implementação de soluções para problemas conhecidos da literatura

## Requisitos

- Java Development Kit (JDK) 8 ou superior
- Conhecimento básico de programação concorrente e semáforos
- Familiaridade com os problemas clássicos de concorrência

---

Desenvolvido para a disciplina de Sistemas Distribuídos - FUMEC
