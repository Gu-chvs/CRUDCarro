# 🚗 Documentação Completa do Projeto: CRUDCarro
**Arquitetura MVC, JavaFX com FXML, Princípios SOLID e Fluxo de Execução Passo a Passo**

---

## 📌 1. Visão Geral: Como o Projeto Está Organizado?

O projeto segue à risca o padrão **MVC (Model - View - Controller)** combinado com boas práticas de engenharia de software e princípios **SOLID**. Ele é dividido em pacotes bem definidos:

```
src/
├── java/
│   └── com/template/
│       ├── main/
│       │   └── Main.java                  -> Ponto de partida, configura dependências e fábrica
│       ├── controller/
│       │   └── MainController.java        -> Gerencia apenas anotações e ações @FXML
│       ├── service/
│       │   ├── ICarroService.java         -> Interface de regras de negócio
│       │   └── CarroService.java          -> Lógica de negócio e ponte com o banco
│       ├── validator/
│       │   ├── Validador.java             -> Interface genérica de validação (Validador<T>)
│       │   ├── CamposObrigatoriosValidador.java -> Validação de preenchimento obrigatório (Marca, Modelo, Ano, Placa)
│       │   ├── ModeloValidador.java       -> Validação de formato (só letras no modelo)
│       │   ├── AnoValidador.java          -> Validação de número e ano plausível (1886 até próximo ano)
│       │   ├── ICarroValidador.java       -> Interface de orquestração das validações
│       │   └── CarroValidador.java        -> Reúne a lista de validadores e executa o foreach
│       ├── model/
│       │   ├── Conexao.java               -> Cria e entrega a conexão com o PostgreSQL
│       │   ├── dao/
│       │   │   ├── ICarroDAO.java         -> Interface do DAO
│       │   │   └── CarroDAO.java          -> Executa comandos SQL e registra logs
│       │   └── dto/
│       │       └── CarroDTO.java          -> Objeto que transporta os dados do carro
│       └── util/
│           ├── DialogUtil.java            -> Janelas visuais (Alerts de Sucesso, Erro e Confirmação)
│           └── TableHelper.java           -> Configura colunas, seleção de linhas e pesquisa dinâmica da tabela
└── resources/
    └── com/template/
        ├── main.fxml                      -> Desenho visual da interface (telas, botões, tabela)
        └── style.css                      -> Estilização moderna da aplicação
```

---

## ⚡ 2. O Que Acontece Quando Você Dá o "Play"? (Inicialização)

1. O Java chama o método `main(String[] args)` na classe [`Main.java`](file:///c:/Users/ra2457065/CRUDCarro/src/java/com/template/main/Main.java).
2. O método `start(Stage primaryStage)` entra em ação e prepara o terreno:
   - **Instancia as peças da aplicação:** Cria o `CarroDAO` e o `CarroValidador`, e injeta ambos no `CarroService` (backend de negócio).
   - **Configura a Fábrica de Controladores (`setControllerFactory`):** Nós ensinamos o `FXMLLoader`: *"Quando você for criar o `MainController`, entregue o `ICarroService` pronto para ele!"*. O Controller agora só precisa se preocupar com os elementos visuais da tela.
3. O `FXMLLoader.load()` lê o arquivo [`main.fxml`](file:///c:/Users/ra2457065/CRUDCarro/src/resources/com/template/main.fxml), injeta os botões e campos no `MainController` e chama automaticamente o método `initialize()`:
   - Delega a configuração das colunas para `TableHelper.configurarColunasTabela(...)`;
   - Delega o controle de estado dos botões para `TableHelper.configurarSelecaoDeLinha(...)`;
   - Delega o filtro dinâmico de busca para `TableHelper.configurarPesquisa(...)`;
   - Chama `carregarCarros()`, buscando os registros no banco para popular a tabela.

---

## 🖱️ 3. O Que Acontece Quando Você Clica? (Fluxo Detalhado das Ações)

Aqui está a resposta exata para explicar a qualquer pessoa o caminho que a informação faz na aplicação.

---

### 🟢 Cenário A: Clicar no botão "Adicionar" (Cadastrar Carro)
**Objetivo:** Pegar o que foi digitado, validar tudo no backend, salvar no PostgreSQL e atualizar a tabela.

```
[Tela FXML] 
   └── Botão Adicionar clicado 
         └── MainController.btnAdicionarAction()
               └── carroService.cadastrar(marca, modelo, ano, placa) [BACKEND]
                     ├── 1. validador.validarCampos(...) [CarroValidador]
                     │        └── Percorre a lista com FOREACH:
                     │              ├── CamposObrigatoriosValidador -> passou?
                     │              ├── ModeloValidador -> passou?
                     │              └── AnoValidador -> passou?
                     │              (Se algum falhar -> lança IllegalArgumentException)
                     ├── 2. Monta o objeto CarroDTO com os dados convertidos
                     └── 3. carroDAO.inserirCarro(carro) -> Executa o INSERT no PostgreSQL
         (Se sucesso -> DialogUtil.showInfo, limpa campos e recarrega tabela)
         (Se falha de validação -> DialogUtil.showWarning com a mensagem do backend)
```

* **Passo a passo no código:**
  1. O usuário digita os dados e clica em **Adicionar**. O JavaFX aciona [`btnAdicionarAction(ActionEvent event)`](file:///c:/Users/ra2457065/CRUDCarro/src/java/com/template/controller/MainController.java).
  2. O Controller repassa os textos diretamente para a camada de serviço backend: `carroService.cadastrar(...)`.
  3. No backend, o [`CarroService`](file:///c:/Users/ra2457065/CRUDCarro/src/java/com/template/service/CarroService.java) aciona o [`CarroValidador`](file:///c:/Users/ra2457065/CRUDCarro/src/java/com/template/validator/CarroValidador.java):
     - Percorre a lista polimórfica de validadores com `foreach` (`CamposObrigatoriosValidador`, `ModeloValidador` e `AnoValidador`).
     - Se qualquer validação falhar, o validador lança uma exceção de validação (`IllegalArgumentException`) com a mensagem correspondente. O Controller captura e exibe um aviso amigável via `DialogUtil.showWarning`.
  4. Passando por todas as validações no backend, o serviço cria o `CarroDTO` e delega para o [`CarroDAO`](file:///c:/Users/ra2457065/CRUDCarro/src/java/com/template/model/dao/CarroDAO.java), que executa o `INSERT INTO carros...`.
  5. Concluído com sucesso, o Controller exibe um popup de sucesso via `DialogUtil.showInfo`, limpa os campos e atualiza a tabela.

---

### 🔵 Cenário B: Clicar em uma linha da Tabela de Carros
**Objetivo:** Trazer os dados do carro selecionado de volta para os campos de texto para permitir edição ou exclusão.

1. O usuário clica em qualquer linha da tabela [`tblCarro`](file:///c:/Users/ra2457065/CRUDCarro/src/java/com/template/controller/MainController.java).
2. O método `carregarCampos()` é acionado.
3. Ele pega o objeto `CarroDTO` da linha selecionada.
4. Preenche os campos `txtId`, `txtMarca`, `txtModelo`, `txtAnoFabricacao` e `txtPlaca`.
5. Um `Listener` configurado no `initialize()` percebe que agora existe um item selecionado:
   - **Habilita** os botões **Editar** e **Excluir**;
   - **Desabilita** o botão **Adicionar** (para evitar duplicidade acidental).

---

### 🟡 Cenário C: Clicar no botão "Editar" (Atualizar Veículo)
**Objetivo:** Alterar os dados de um carro existente, validando as informações no backend e confirmando com o usuário na tela.

1. O usuário ajusta os campos desejados e clica em **Editar** ([`btnEditarAction`](file:///c:/Users/ra2457065/CRUDCarro/src/java/com/template/controller/MainController.java)).
2. **Confirmação Visual:** O sistema abre um popup com [`DialogUtil.showConfirmation`](file:///c:/Users/ra2457065/CRUDCarro/src/java/com/template/util/DialogUtil.java): *"Deseja realmente atualizar as informações deste veículo?"*. Se cancelar, a operação é interrompida.
3. Se confirmado, o Controller chama o backend `carroService.atualizar(...)`.
4. O backend valida o ID selecionado e os dados informados via `CarroValidador`. Se houver inconformidade, lança exceção que o Controller exibe como aviso.
5. O `CarroService` aciona o `CarroDAO.atualizarCarro(carro)`, executando o SQL `UPDATE carros SET ... WHERE id = ?`.
6. O Controller exibe o alerta visual de sucesso, limpa os campos e recarrega a tabela.

---

### 🔴 Cenário D: Clicar no botão "Excluir"
**Objetivo:** Remover permanentemente um veículo com total segurança.

1. O usuário seleciona o carro e clica em **Excluir** ([`btnExcluirAction`](file:///c:/Users/ra2457065/CRUDCarro/src/java/com/template/controller/MainController.java)).
2. O sistema abre a confirmação com `DialogUtil.showConfirmation`: *"Tem certeza de que deseja excluir o veículo selecionado?"*.
3. Caso o usuário confirme, chama o backend `carroService.excluir(idStr)`.
4. O backend valida a existência e formato do ID. Se válido, aciona o `CarroDAO.excluirCarro(id)`, executando o SQL `DELETE FROM carros WHERE id = ?`.
5. Se concluído, emite o popup de sucesso, limpa os campos e remove a linha da tabela.

---

### ⚪ Cenário E: Clicar no botão "Limpar Campos"
**Objetivo:** Cancelar a seleção atual e liberar a tela para um novo cadastro.

1. Aciona [`btnLimparAction`](file:///c:/Users/ra2457065/CRUDCarro/src/java/com/template/controller/MainController.java).
2. Limpa todos os textos dos campos `txtId`, `txtMarca`, `txtModelo`, `txtPlaca`, `txtAnoFabricacao` e `txtPesquisa`.
3. Desmarca qualquer linha que estava selecionada na tabela (`clearSelection()`).
4. O listener reabilita o botão **Adicionar** e desabilita **Editar** e **Excluir**.
5. Coloca o cursor de volta no campo da Marca (`txtMarca.requestFocus()`).

---

### 🔍 Cenário F: Digitar algo na Barra de Pesquisa ("Pesquisar Filtro")
**Objetivo:** Filtrar a tabela na hora, sem precisar clicar em nenhum botão e sem fazer novas requisições ao banco.

1. A cada caractere digitado no campo `txtPesquisa`, o evento `textProperty().addListener` é acionado.
2. O `FilteredList` do JavaFX avalia cada carro da lista chamando [`carroService.correspondeATermo(carro, textoDigitado)`](file:///c:/Users/ra2457065/CRUDCarro/src/java/com/template/service/CarroService.java#L52).
3. O serviço verifica se o termo digitado bate com:
   - A **Marca**;
   - O **Modelo**;
   - A **Placa**;
   - Ou o próprio **ID**.
4. A tabela atualiza instantaneamente apenas com os veículos correspondentes.

---

## 🏛️ 4. A Importância do SOLID e Como Ele Foi Aplicado

O **SOLID** é um conjunto de 5 princípios de design de software orientado a objetos criado para tornar o código modular, extensível, fácil de testar e fácil de manter. Veja como cada letra está viva no seu projeto:

---

### 1. `S` - Single Responsibility Principle (Princípio da Responsabilidade Única)
> *"Uma classe deve ter um, e apenas um, motivo para mudar."*

* **Como estava antes (errado):** O `MainController` fazia tudo: cuidava dos botões, validava texto, conectava no banco e executava comandos SQL. Se a regra de imposto mudasse ou o banco de dados mudasse, mexia-se no Controller.
* **Como está agora (correto):**
  - **`MainController`:** Responsável apenas pelo mapeamento e ações `@FXML` e fluxo visual.
  - **`TableHelper`:** Responsável exclusivamente pela configuração das colunas, seleção de linhas e filtro dinâmico de pesquisa da tabela.
  - **`CarroValidador`:** Responsável apenas por coordenar a lista de validações.
  - **`CarroService`:** Responsável pelas regras de negócio e orquestração.
  - **`CarroDAO`:** Responsável apenas por falar com o banco de dados (SQL).
  - **`DialogUtil`:** Responsável apenas por desenhar alertas na tela.

---

### 2. `O` - Open/Closed Principle (Princípio Aberto/Fechado)
> *"Entidades de software devem estar abertas para extensão, mas fechadas para modificação."*

* **No seu código:** A interface [`Validador<T>`](file:///c:/Users/ra2457065/CRUDCarro/src/java/com/template/validator/Validador.java) e o [`CarroValidador`](file:///c:/Users/ra2457065/CRUDCarro/src/java/com/template/validator/CarroValidador.java).
* **Por que isso é incrível:** Se amanhã o Detran exigir uma regra nova para validar **Placa no formato Mercosul**, você **NÃO precisa alterar** o código de `CamposObrigatoriosValidador`, nem de `ModeloValidador`, nem de `AnoValidador`. Você simplesmente cria uma nova classe `PlacaMercosulValidador implements Validador<String>` e adiciona na lista. O sistema aceita a novidade sem risco de quebrar o que já funciona.

---

### 3. `L` - Liskov Substitution Principle (Princípio da Substituição de Liskov)
> *"Subclasses ou implementações devem poder substituir suas interfaces base sem quebrar o sistema."*

* **No seu código:** Qualquer classe que implemente `Validador<String>` (`CamposObrigatoriosValidador`, `ModeloValidador`, `AnoValidador`) pode ser tratada de forma idêntica pela lista `List<Validador<String>>`.
* O loop no `CarroValidador` executa:
  ```java
  for (Validador<String> validador : validadores) {
      if (!validador.validar(validador.getValor())) { ... }
  }
  ```
  Nenhuma classe derivada lança exceções inesperadas ou altera o comportamento esperado da interface base.

---

### 4. `I` - Interface Segregation Principle (Princípio da Segregação de Interfaces)
> *"Uma classe não deve ser forçada a depender de métodos que não utiliza."*

* **No seu código:** Em vez de criarmos uma única interface gigante chamada `ISistema` com métodos de tela, de banco e de validação juntos, criamos interfaces pequenas, coesas e altamente especializadas:
  - `Validador<T>`: possui apenas 3 métodos focados em validação (`validar`, `getMensagemErro`, `getValor`).
  - `ICarroValidador`: focada apenas em validar os campos do carro.
  - `ICarroService`: focada nos serviços de negócio de carro.
  - `ICarroDAO`: focada exclusivamente em operações de persistência do carro.

---

### 5. `D` - Dependency Inversion Principle (Princípio da Inversão de Dependência)
> *"Módulos de alto nível não devem depender de módulos de baixo nível. Ambos devem depender de abstrações."*

* **No seu código:**
  - O `MainController` (módulo de alto nível da interface) **não instancia `CarroService` nem validadores**. Ele depende unicamente da abstração `ICarroService`:
    ```java
    private final ICarroService carroService;

    public MainController(ICarroService carroService) {
        this.carroService = carroService;
    }
    ```
  - Por sua vez, o `CarroService` (camada de negócio) depende das abstrações `ICarroDAO` e `ICarroValidador`:
    ```java
    private final ICarroDAO carroDAO;
    private final ICarroValidador validador;

    public CarroService(ICarroDAO carroDAO, ICarroValidador validador) {
        this.carroDAO = carroDAO;
        this.validador = validador;
    }
    ```
  - Quem cria as instâncias reais e conecta todo o grafo de dependências é o `Main.java`, que entrega o serviço para a interface através da **Fábrica de Controladores** (`loader.setControllerFactory`).
  - **Benefício:** A camada visual (Controller) fica 100% livre de regras de negócio, e o serviço pode ser testado ou substituído facilmente sem que a interface seja impactada.

---

## 💡 5. Dicas de Ouro para a Apresentação Oral

Quando a professora pedir para você explicar o projeto:
1. **Comece mostrando a tela funcionando:** Adicione um carro, mostre que o alerta de sucesso apareceu e a linha foi inserida na tabela.
2. **Mostre a validação no backend funcionando:** Tente cadastrar um carro com campo vazio, ano inválido ou números no modelo (ex: `Civic 2000`). Explique: *"A tela não faz as validações; ela apenas encaminha os dados para o Service no backend, que valida tudo antes de persistir"*.
3. **Abra o `CarroValidador.java`:** Mostre a lista genérica `List<Validador<String>>` e o loop `foreach`. Diga: *"Aqui apliquei o Princípio Aberto/Fechado (OCP). Se eu quiser criar outro validador, basta implementar a interface e adicionar na lista, sem alterar os validadores existentes"*.
4. **Abra o `MainController.java` e o `CarroService.java`:** Destaque o Princípio da Responsabilidade Única (SRP): *"O MainController cuida exclusivamente do FXML e da interação visual. Toda regra de negócio, conversão e chamada de validação foi alocada no CarroService"*.
5. **Abra o `Main.java`:** Mostre o `setControllerFactory` e explique a Inversão de Dependência (DIP): *"As dependências são injetadas de fora para dentro via interfaces"*.
6. **Mostre o `CarroDAO.java`:** Aponte para o `Logger` no bloco `catch` tratando exceções SQL.
