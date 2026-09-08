import os
import sys
from reportlab.lib import colors
from reportlab.lib.pagesizes import letter
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, HRFlowable, KeepTogether
)
from reportlab.pdfgen import canvas

class NumberedCanvas(canvas.Canvas):
    """Canvas de duas passadas para numerar páginas dinamicamente (Página X de Y)."""
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self._saved_page_states = []

    def showPage(self):
        self._saved_page_states.append(dict(self.__dict__))
        self._startPage()

    def save(self):
        num_pages = len(self._saved_page_states)
        for state in self._saved_page_states:
            self.__dict__.update(state)
            self.draw_page_decorations(num_pages)
            super().showPage()
        super().save()

    def draw_page_decorations(self, page_count):
        self.saveState()
        self.setFont("Helvetica", 9)
        self.setFillColor(colors.HexColor("#718096"))

        # Cabeçalho (a partir da página 2)
        if self._pageNumber > 1:
            self.drawString(54, 11 * 72 - 36, "CRUDCarro — Documentação Técnica e Aplicação de SOLID")
            self.setStrokeColor(colors.HexColor("#E2E8F0"))
            self.setLineWidth(0.5)
            self.line(54, 11 * 72 - 42, 8.5 * 72 - 54, 11 * 72 - 42)

        # Rodapé em todas as páginas
        text = f"Página {self._pageNumber} de {page_count}"
        self.drawRightString(8.5 * 72 - 54, 36, text)
        self.drawString(54, 36, "LP VI — Java FX + MVC + SOLID")
        self.setStrokeColor(colors.HexColor("#E2E8F0"))
        self.setLineWidth(0.5)
        self.line(54, 48, 8.5 * 72 - 54, 48)
        self.restoreState()


def gerar_relatorio_pdf(caminho_saida="Documentacao_CRUDCarro.pdf"):
    doc = SimpleDocTemplate(
        caminho_saida,
        pagesize=letter,
        leftMargin=54,
        rightMargin=54,
        topMargin=54,
        bottomMargin=54
    )

    styles = getSampleStyleSheet()

    # Cores do Design
    cor_primaria = colors.HexColor("#1A365D")    # Azul marinho escuro
    cor_secundaria = colors.HexColor("#2B6CB0")  # Azul intermediário
    cor_texto = colors.HexColor("#2D3748")       # Cinza grafite
    cor_fundo_destaque = colors.HexColor("#EDF2F7") # Cinza bem claro
    cor_sucesso = colors.HexColor("#2F855A")     # Verde escuro
    cor_borda = colors.HexColor("#CBD5E0")

    # Estilos customizados
    estilo_titulo = ParagraphStyle(
        'DocTitle',
        parent=styles['Heading1'],
        fontName='Helvetica-Bold',
        fontSize=24,
        leading=28,
        textColor=cor_primaria,
        alignment=0,
        spaceAfter=6
    )

    estilo_subtitulo = ParagraphStyle(
        'DocSubtitle',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=12,
        leading=16,
        textColor=cor_secundaria,
        spaceAfter=14
    )

    estilo_h1 = ParagraphStyle(
        'Heading1Custom',
        parent=styles['Heading2'],
        fontName='Helvetica-Bold',
        fontSize=15,
        leading=19,
        textColor=cor_primaria,
        spaceBefore=14,
        spaceAfter=8,
        keepWithNext=True
    )

    estilo_h2 = ParagraphStyle(
        'Heading2Custom',
        parent=styles['Heading3'],
        fontName='Helvetica-Bold',
        fontSize=12,
        leading=15,
        textColor=cor_secundaria,
        spaceBefore=10,
        spaceAfter=5,
        keepWithNext=True
    )

    estilo_corpo = ParagraphStyle(
        'BodyCustom',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=9.5,
        leading=14,
        textColor=cor_texto,
        spaceAfter=6
    )

    estilo_item = ParagraphStyle(
        'ItemCustom',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=9.5,
        leading=13.5,
        textColor=cor_texto,
        leftIndent=15,
        spaceAfter=4
    )

    estilo_caixa = ParagraphStyle(
        'BoxText',
        parent=styles['Normal'],
        fontName='Courier',
        fontSize=8.5,
        leading=12,
        textColor=colors.HexColor("#1A202C")
    )

    story = []

    # Cabeçalho Principal
    story.append(Paragraph("Sistema de Gestão de Veículos (CRUDCarro)", estilo_titulo))
    story.append(Paragraph("Documentação Técnica: Arquitetura MVC, JavaFX com FXML, Princípios SOLID e Fluxo de Execução", estilo_subtitulo))
    story.append(HRFlowable(width="100%", thickness=2, color=cor_secundaria, spaceBefore=2, spaceAfter=14))

    # 1. Visão Geral e Estrutura
    story.append(Paragraph("1. Visão Geral e Arquitetura do Projeto", estilo_h1))
    story.append(Paragraph(
        "A aplicação é estruturada sobre o padrão <b>MVC (Model-View-Controller)</b> desacoplado, "
        "reforçado pelas boas práticas da Orientação a Objetos e princípios <b>SOLID</b>. O sistema divide suas "
        "responsabilidades em camadas estritas:", estilo_corpo
    ))

    tabela_dados = [
        [Paragraph("<b>Pacote / Camada</b>", estilo_corpo), Paragraph("<b>Classes Principais</b>", estilo_corpo), Paragraph("<b>Responsabilidade no Sistema</b>", estilo_corpo)],
        [Paragraph("<b>main</b>", estilo_corpo), Paragraph("Main.java", estilo_corpo), Paragraph("Ponto de entrada; instancia dependências e configura a Fábrica de Controladores.", estilo_corpo)],
        [Paragraph("<b>controller</b>", estilo_corpo), Paragraph("MainController.java", estilo_corpo), Paragraph("Gerencia exclusivamente os eventos da tela FXML (cliques, inputs, binding, confirmações visuais).", estilo_corpo)],
        [Paragraph("<b>service</b>", estilo_corpo), Paragraph("ICarroService<br/>CarroService.java", estilo_corpo), Paragraph("Regras de negócio, validações no backend (ICarroValidador), montagem de DTO e persistência.", estilo_corpo)],
        [Paragraph("<b>validator</b>", estilo_corpo), Paragraph("Validador&lt;T&gt;, ICarroValidador<br/>CarroValidador, etc.", estilo_corpo), Paragraph("Cadeia polimórfica de validação (campos obrigatórios, ano e modelo). Lança exceções de negócio.", estilo_corpo)],
        [Paragraph("<b>model.dao</b>", estilo_corpo), Paragraph("ICarroDAO<br/>CarroDAO.java", estilo_corpo), Paragraph("Operações SQL diretas no PostgreSQL, com registro de falhas via Logger.", estilo_corpo)],
        [Paragraph("<b>model.dto</b>", estilo_corpo), Paragraph("CarroDTO.java", estilo_corpo), Paragraph("Objeto puro de transferência de dados (POJO com getters e setters).", estilo_corpo)],
        [Paragraph("<b>util</b>", estilo_corpo), Paragraph("DialogUtil.java", estilo_corpo), Paragraph("Padronização de diálogos visuais (Sucesso, Aviso, Erro e Confirmação).", estilo_corpo)]
    ]

    tabela = Table(tabela_dados, colWidths=[80, 140, 284])
    tabela.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, 0), cor_fundo_destaque),
        ('GRID', (0, 0), (-1, -1), 0.5, cor_borda),
        ('VALIGN', (0, 0), (-1, -1), 'TOP'),
        ('TOPPADDING', (0, 0), (-1, -1), 4),
        ('BOTTOMPADDING', (0, 0), (-1, -1), 4),
    ]))
    story.append(tabela)
    story.append(Spacer(1, 12))

    # 2. Ciclo de Inicialização
    story.append(Paragraph("2. Ciclo de Inicialização: O que acontece ao rodar o sistema?", estilo_h1))
    story.append(Paragraph(
        "<b>1. Instanciação Externa:</b> No método <code>start()</code> da classe <code>Main</code>, são instanciados "
        "<code>CarroDAO</code> e <code>CarroValidador</code>, sendo ambos injetados no <code>CarroService</code>.", estilo_item
    ))
    story.append(Paragraph(
        "<b>2. Fábrica de Controladores:</b> É configurado <code>loader.setControllerFactory(...)</code>. Dessa forma, quando o FXMLLoader lê o FXML, "
        "ele solicita à fábrica a instância do <code>MainController</code> injetando apenas o <code>ICarroService</code> via construtor (DIP).", estilo_item
    ))
    story.append(Paragraph(
        "<b>3. Inicialização dos Componentes (initialize):</b> O controller conecta as colunas da <code>TableView</code> às propriedades do <code>CarroDTO</code>, "
        "configura os listeners de seleção e o filtro dinâmico de busca, carregando os registros do banco na tela.", estilo_item
    ))
    story.append(Spacer(1, 10))

    # 3. Fluxo das Ações do Usuário (Clique a Clique)
    story.append(Paragraph("3. Fluxo de Execução Passo a Passo (O que acontece ao Clicar?)", estilo_h1))

    story.append(Paragraph("A. Clicar no botão 'Adicionar' (Cadastrar Carro)", estilo_h2))
    story.append(Paragraph(
        "• <b>Captura e Delegação:</b> O método <code>btnAdicionarAction</code> obtém os textos dos campos e chama <code>carroService.cadastrar(...)</code>.<br/>"
        "• <b>Validação no Backend:</b> O <code>CarroService</code> aciona o <code>CarroValidador</code>, que "
        "percorre com <code>foreach</code> os validadores (<code>CamposObrigatoriosValidador</code>, <code>ModeloValidador</code> e <code>AnoValidador</code>). "
        "Se algum falhar, o backend lança <code>IllegalArgumentException</code> e a tela exibe um aviso com <code>DialogUtil.showWarning</code>.<br/>"
        "• <b>Persistência:</b> Aprovado na validação, o serviço cria o <code>CarroDTO</code> e chama <code>carroDAO.inserirCarro(carro)</code>.<br/>"
        "• <b>Retorno Visual:</b> Exibe diálogo de sucesso (<code>DialogUtil.showInfo</code>), reseta o formulário e atualiza a tabela.",
        estilo_corpo
    ))

    story.append(Paragraph("B. Clicar em uma linha da Tabela", estilo_h2))
    story.append(Paragraph(
        "• O evento <code>carregarCampos()</code> resgata o item selecionado e preenche os campos de texto.<br/>"
        "• Um <code>ChangeListener</code> no modelo de seleção da tabela detecta a linha ativa e <b>habilita os botões Editar e Excluir</b>, desabilitando o botão Adicionar.",
        estilo_corpo
    ))

    story.append(Paragraph("C. Clicar no botão 'Editar' (Atualizar Carro)", estilo_h2))
    story.append(Paragraph(
        "• Abre caixa de diálogo de confirmação visual via <code>DialogUtil.showConfirmation(...)</code>.<br/>"
        "• Se confirmado, chama <code>carroService.atualizar(...)</code>, que valida o ID e os novos dados no backend e executa o SQL <code>UPDATE</code>.<br/>"
        "• Notifica sucesso, limpa os campos e recarrega os dados na tela.",
        estilo_corpo
    ))

    story.append(Paragraph("D. Clicar no botão 'Excluir'", estilo_h2))
    story.append(Paragraph(
        "• Confirma a intenção com o usuário via <code>DialogUtil.showConfirmation(...)</code>.<br/>"
        "• Se confirmado, chama <code>carroService.excluir(idStr)</code>, que valida o ID no backend e executa o comando SQL <code>DELETE</code> no DAO.<br/>"
        "• Notifica com <code>DialogUtil.showInfo</code> e atualiza a lista de veículos.",
        estilo_corpo
    ))

    story.append(Paragraph("E. Digitar na Barra de Pesquisa", estilo_h2))
    story.append(Paragraph(
        "• A cada caractere digitado, o listener do <code>txtPesquisa</code> atualiza o predicado de uma <code>FilteredList</code>.<br/>"
        "• O método <code>carroService.correspondeATermo(carro, termo)</code> testa se o texto bate com Marca, Modelo, Placa ou ID em tempo real, sem novas consultas ao banco.",
        estilo_corpo
    ))
    story.append(Spacer(1, 10))

    # 4. Aplicação e Importância do SOLID
    story.append(Paragraph("4. Aplicação e Importância dos Princípios SOLID", estilo_h1))

    solid_items = [
        ("S — Single Responsibility Principle (Responsabilidade Única)",
         "Cada classe possui apenas uma única razão para mudar. O Controller cuida exclusivamente da tela e eventos FXML de UI; "
         "o Service orquestra as regras de negócio e validações; o DAO executa persistência SQL; os validadores checam conformidade dos dados e o DialogUtil cuida de popups."),

        ("O — Open/Closed Principle (Aberto/Fechado)",
         "A arquitetura está aberta para extensão e fechada para modificação. Ao criar uma nova validação (ex.: formato de Placa Mercosul), "
         "cria-se uma nova classe implementando <code>Validador&lt;String&gt;</code> e a insere na lista, sem alterar nenhuma linha dos validadores já testados."),

        ("L — Liskov Substitution Principle (Substituição de Liskov)",
         "Todas as implementações de <code>Validador&lt;T&gt;</code> podem ser substituídas transparentemente pela abstração base "
         "sem quebrar a execução ou gerar exceções inesperadas dentro da lista genérica percorrida pelo <code>foreach</code>."),

        ("I — Interface Segregation Principle (Segregação de Interfaces)",
         "Interfaces pequenas e especializadas (<code>Validador&lt;T&gt;</code>, <code>ICarroValidador</code>, <code>ICarroService</code>, <code>ICarroDAO</code>) "
         "em vez de interfaces monolíticas. Nenhuma classe é obrigada a implementar métodos irrelevantes para sua função."),

        ("D — Dependency Inversion Principle (Inversão de Dependência)",
         "Módulos de alto nível não dependem de módulos de baixo nível. O Controller depende unicamente de <code>ICarroService</code>; "
         "o <code>CarroService</code> depende de <code>ICarroDAO</code> e <code>ICarroValidador</code>. A injeção ocorre via construtor na Fábrica de Controladores do <code>Main</code>.")
    ]

    for titulo_solid, texto_solid in solid_items:
        story.append(Paragraph(f"<b>{titulo_solid}</b>", estilo_h2))
        story.append(Paragraph(texto_solid, estilo_corpo))

    story.append(Spacer(1, 10))

    # 5. Tratamento de Exceções e Boas Práticas Adicionais
    story.append(Paragraph("5. Tratamento de Erros e Boas Práticas Adicionais", estilo_h1))
    story.append(Paragraph(
        "• <b>Logger no DAO:</b> Conforme exigido nos slides, o <code>CarroDAO</code> utiliza <code>java.util.logging.Logger</code> "
        "para registrar a stack trace técnica com <code>logger.log(Level.SEVERE, ..., e)</code> sem expor detalhes sensíveis ao usuário final.<br/>"
        "• <b>Ausência de Console para Alertas:</b> Toda interação com o operador é feita via <code>DialogUtil</code> (Alerts do JavaFX).<br/>"
        "• <b>Padrões de Nomenclatura:</b> Prefixos canônicos JavaFX (<code>btn</code>, <code>txt</code>, <code>lbl</code>, <code>tbl</code>, <code>col</code>) "
        "e métodos semânticos (<code>btnAdicionarAction</code>, <code>btnEditarAction</code>).",
        estilo_corpo
    ))
    story.append(Spacer(1, 10))

    # 6. Roteiro para Apresentação
    story.append(Paragraph("6. Roteiro para Apresentação e Defesa Oral", estilo_h1))
    story.append(Paragraph(
        "1. <b>Demonstração Prática:</b> Cadastre um veículo válido, mostre o alerta de sucesso e a inserção na tabela.<br/>"
        "2. <b>Demonstração de Validação no Backend:</b> Tente submeter com campos vazios ou modelo com dígitos; mostre que a validação é disparada pelo backend (Service) e o alerta é exibido.<br/>"
        "3. <b>Destaque do OCP:</b> Abra <code>CarroValidador.java</code> e aponte para a lista polimórfica e o loop <code>foreach</code>.<br/>"
        "4. <b>Destaque do SRP e DIP:</b> Abra <code>MainController.java</code> (apenas FXML e UI) e <code>CarroService.java</code> (regras e validações), e mostre <code>Main.java</code> com <code>setControllerFactory</code>.<br/>"
        "5. <b>Destaque do Tratamento de Erros:</b> Abra <code>CarroDAO.java</code> mostrando o <code>Logger</code> em cada bloco <code>catch</code>.",
        estilo_corpo
    ))

    # Construção do documento com o Canvas numerado
    doc.build(story, canvasmaker=NumberedCanvas)
    print(f"PDF gerado com sucesso em: {os.path.abspath(caminho_saida)}")

if __name__ == "__main__":
    caminho = "Documentacao_CRUDCarro.pdf"
    if len(sys.argv) > 1:
        caminho = sys.argv[1]
    gerar_relatorio_pdf(caminho)
