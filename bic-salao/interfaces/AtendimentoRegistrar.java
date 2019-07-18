package interfaces;

import dados.ClienteDados;
import dados.DadosException;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.table.JTableHeader;
import entidades.Cliente;
import java.util.List;
import entidades.Servico;
import entidades.Produto;
import java.awt.Color;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import util.DateParse;

/**
 * @author BIC-Jr Guia de Estilo
 */
public class AtendimentoRegistrar extends javax.swing.JDialog {

    /**
     * @param parent
     * @param modal
     */
    public AtendimentoRegistrar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        redimencionarTela();
        alterarIcone();
        preencherListaCliente();
        preencherListaServico();
        preencherListaProduto();
    }

    private void alterarIcone() {
        URL url = this.getClass().getResource("/imagens/calendario.png");
        Image iconeTitulo = Toolkit.getDefaultToolkit().getImage(url);
        this.setIconImage(iconeTitulo);
        this.setLocationRelativeTo(null);
        util.JanelaComum.setAcessibilidade(this);
        painelRolavel.getVerticalScrollBar().setUnitIncrement(16);
    }

    @Override
    public void paint(java.awt.Graphics g) {
        java.awt.Dimension d = getSize();
        java.awt.Dimension m = getMaximumSize();
        java.awt.Dimension i = getMinimumSize();
        boolean resize = d.width > m.width || i.height > d.height;
        d.width = Math.min(m.width, d.width);
        d.height = Math.max(i.height, d.height);
        if (resize) {
            java.awt.Point p = getLocation();
            setVisible(false);
            setSize(d);
            setLocation(p);
            setVisible(true);
        }
        super.paint(g);
    }

    private void redimencionarTela() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        java.awt.Dimension d = tk.getScreenSize();
        if (d.width >= 700 && d.height >= 870) {
            this.setMinimumSize(new java.awt.Dimension(680, 815));
            this.setSize(680, 810);
            this.setResizable(false);
        } else {
            this.setMinimumSize(new java.awt.Dimension(705, d.height - 60));
            this.setSize(705, d.height - 60);
//            this.setResizable(true);
        }
    }

    private void preencherListaCliente() {
        modeloCliente = new DefaultListModel();
        listaCliente.setModel(modeloCliente);
        try {
            clientes = ClienteDados.consultarTodos();
            clientes.forEach((cliente) -> {
                modeloCliente.addElement(cliente.toString());
            });
        } catch (DadosException ex) {
            Problema erroDeConexao = new Problema(new javax.swing.JFrame(), true, ex.getMessage());
            erroDeConexao.setVisible(true);
            painelListaCliente.setBounds(25, 80, 260, 1);
        }
        painelListaCliente.setVisible(false);
    }

    private void pesquisarCliente() {
        try {
            modeloCliente.removeAllElements();
            clientes = ClienteDados.consultarNome(campoNome.getText());
            for (int i = 0; i < clientes.size(); i++) {
                modeloCliente.addElement(clientes.get(i).getNmCliente());
            }
            if (clientes.size() <= 6) {
                painelListaCliente.setBounds(25, 80, 260, clientes.size() * 20 + 1);
            } else {
                painelListaCliente.setBounds(25, 80, 260, 141);
            }
            painelListaCliente.setVisible(true);
        } catch (DadosException e) {
            Problema erroDeConexao = new Problema(new javax.swing.JFrame(), true, e.getMessage());
            erroDeConexao.setVisible(true);
            painelListaCliente.setBounds(25, 80, 260, 1);
        }
    }

    public List<Servico> getServicoFromTable() {
        servicosTabela = new ArrayList<>();
        for (int linha = 0; linha < tabelaServicos.getRowCount(); linha++) {
            TableModel modeloTabela = tabelaServicos.getModel();
            String nmServico = modeloTabela.getValueAt(linha, 0).toString();
            String profissional = modeloTabela.getValueAt(linha, 1).toString();
            double valor = Double.parseDouble(modeloTabela.getValueAt(linha, 2).toString().replace("R$ ", ""));
            servicosTabela.add(new Servico(nmServico, profissional, valor));
        }
        return servicosTabela;
    }

    private boolean validaServico() {
        if (selecaoServico) {
            campoServico.setBorder(new LineBorder(Color.GRAY));
            rotuloServicoObrigatorio.setVisible(false);
            return true;
        } else {
            if (ads) {
                campoServico.setBorder(new LineBorder(Color.RED));
                rotuloServicoObrigatorio.setVisible(true);
            } else {
                campoServico.setBorder(new LineBorder(Color.GRAY));
                rotuloServicoObrigatorio.setVisible(false);
            }
            return false;
        }
//        for (int i = 0; i < servicos.size(); i++) {
//            if (servicos.get(i).equals(campoServico.getText()) && selecaoServico) {
//                campoServico.setBorder(new LineBorder(Color.GRAY));
//                rotuloServicoObrigatorio.setVisible(false);
//                return true;
//            }
//        }
//        campoServico.setBorder(new LineBorder(Color.RED));
//        rotuloServicoObrigatorio.setVisible(true);
//        return false;
    }

    private boolean validaProfissional() {
        if (selecaoProfissional) {
            campoProfissional.setBorder(new LineBorder(Color.GRAY));
            rotuloProfissionalObrigatorio.setVisible(false);
            return true;
        } else {
            if (ads) {
                campoProfissional.setBorder(new LineBorder(Color.RED));
                rotuloProfissionalObrigatorio.setVisible(true);
            } else {
                campoProfissional.setBorder(new LineBorder(Color.GRAY));
                rotuloProfissionalObrigatorio.setVisible(false);
            }
            return false;
        }
//        try {
//            for (int i = 0; i < profissionais.size(); i++) {
//                if (profissionais.get(i).equals(campoProfissional.getText()) && selecaoProfissional) {
//                    campoProfissional.setBorder(new LineBorder(Color.GRAY));
//                    rotuloProfissionalObrigatorio.setVisible(false);
//                    return true;
//                }
//            }
//            campoProfissional.setBorder(new LineBorder(Color.RED));
//            rotuloProfissionalObrigatorio.setVisible(true);
//        } catch (NullPointerException e) {
//        }
//        return false;
    }

    private boolean validaValor() {
        try {
            String valor = campoValor.getText();
            valor = valor.replace(",", ".");
            Double.parseDouble(valor);
            campoValor.setBorder(new LineBorder(Color.GRAY));
            rotuloValorObrigatorio.setVisible(false);
            return true;
        } catch (NumberFormatException e) {
            if (ads) {
                campoValor.setBorder(new LineBorder(Color.RED));
                rotuloValorObrigatorio.setVisible(true);
            } else {
                campoValor.setBorder(new LineBorder(Color.GRAY));
                rotuloValorObrigatorio.setVisible(false);
            }
            return false;
        }
    }

    private void anularServico() {
        campoServico.setText("");
        preencherListaServico();
        campoProfissional.setText("");
        campoValor.setText("");
    }

    private void preencherListaServico() {
        servicos = new ArrayList<>();
        servicos.add(("Corte"));
        servicos.add(("Pintura"));
        servicos.add(("Pedicure"));
        servicos.add(("Manicure"));
        servicos.add(("Maquiagem"));
        modeloServico = new DefaultListModel();
        listaServico.setModel(modeloServico);
        servicos.forEach((servico) -> {
            modeloServico.addElement(servico);
        });
    }

    private void pesquisarServico() {
        campoProfissional.setText("");
        campoValor.setText("");
        modeloServico.removeAllElements();
        int contador = 0;
        for (int i = 0; i < servicos.size(); i++) {
            if (servicos.get(i).toLowerCase().startsWith(campoServico.getText().toLowerCase())) {
                modeloServico.addElement(servicos.get(i));
                contador++;
            }
        }
        if (contador <= 6) {
            painelListaServico.setBounds(25, 175, 160, contador * 20 + 1);
        } else {
            painelListaServico.setBounds(25, 175, 160, 141);
        }
        painelListaServico.setVisible(true);
        selecaoProfissional = false;
    }

    private void preencherListaProfissional() {
        profissionais = new ArrayList<>();
        switch (campoServico.getText()) {
            case "Corte":
                profissionais.add(("Beatriz"));
                profissionais.add(("Bruno"));
                profissionais.add(("Maria"));
                profissionais.add(("Márcio"));
                profissionais.add(("Maurício"));
                break;
            case "Pintura":
                profissionais.add(("Ana"));
                profissionais.add(("Alice"));
                profissionais.add(("Arnaldo"));
                profissionais.add(("Dalila"));
                profissionais.add(("Daniel"));
                break;
            case "Pedicure":
                profissionais.add(("Karen"));
                profissionais.add(("Kiara"));
                profissionais.add(("Nádia"));
                profissionais.add(("Nicole"));
                profissionais.add(("Nilce"));
                break;
            case "Manicure":
                profissionais.add(("Regina"));
                profissionais.add(("Rita"));
                profissionais.add(("Taís"));
                profissionais.add(("Tânia"));
                profissionais.add(("Tereza"));
                break;
            case "Maquiagem":
                profissionais.add(("Carlos"));
                profissionais.add(("Cecília"));
                profissionais.add(("Vilma"));
                profissionais.add(("Vinícius"));
                profissionais.add(("Vitória"));
                break;
            default:
                break;
        }
        modeloProfissional = new DefaultListModel();
        listaProfissional.setModel(modeloProfissional);
        profissionais.forEach((profissional) -> {
            modeloProfissional.addElement(profissional);
        });
    }

    private void pesquisarProfissional() {
        try {
            campoValor.setText("");
            modeloProfissional.removeAllElements();
            int contador = 0;
            for (int i = 0; i < profissionais.size(); i++) {
                if (profissionais.get(i).toLowerCase().startsWith(
                        campoProfissional.getText().toLowerCase()) && selecaoServico) {
                    modeloProfissional.addElement(profissionais.get(i));
                    contador++;
                }
            }
            if (contador <= 6) {
                painelListaProfissional.setBounds(200, 175, 160, contador * 20 + 1);
            } else {
                painelListaProfissional.setBounds(200, 175, 160, 141);
            }
        } catch (NullPointerException e) {
        }
        painelListaProfissional.setVisible(true);
    }

    private void anularProduto() {
        campoProduto.setText("");
        preencherListaProduto();
        campoQuantidade.setText("");
        campoValorUnit.setText("");
    }

    private boolean validaCliente() {
        if (adt) {
            if (!selecaoCliente) {
                campoNome.setBorder(new LineBorder(Color.RED));
                rotuloClienteObrigatorio.setVisible(true);
                return false;
            } else {
                campoNome.setBorder(new LineBorder(Color.GRAY));
                rotuloClienteObrigatorio.setVisible(false);
                return true;
            }
        } else {
            campoNome.setBorder(new LineBorder(Color.GRAY));
            return false;
        }
    }

    private boolean validaData() {
        try {
            DateParse.parseDate(campoData.getText());
            campoData.setBorder(new LineBorder(Color.GRAY));
            rotuloDataObrigatorio.setVisible(false);
            return true;
        } catch (ParseException e) {
            campoData.setBorder(new LineBorder(Color.RED));
            rotuloDataObrigatorio.setVisible(true);
            return false;
        }
    }

    private void focoData() {
        campoData.setBorder(new LineBorder(Color.BLUE));
        if (campoData.getText().equals("dd/mm/aaaa")) {
            campoData.setForeground(Color.BLACK);
            campoData.setText("");
            try {
                MaskFormatter mascaraData = new MaskFormatter("##/##/####");
                mascaraData.setPlaceholderCharacter('_');
                campoData.setFormatterFactory(new DefaultFormatterFactory(mascaraData));
                campoData.setText("");
            } catch (ParseException e) {
            }
        }
    }

    private void preencherTabelaServicos() {
        String[] titulo = new String[]{"Serviço", "Profissional", "Valor", ""};
        String[][] linhas = new String[servicosTabela.size()][4];
        vS = 0;
        for (int i = 0; i < servicosTabela.size(); i++) {
            linhas[i][0] = servicosTabela.get(i).getNmServico();
            linhas[i][1] = servicosTabela.get(i).getValor();
            linhas[i][2] = "R$ " + servicosTabela.get(i).getValores() + "0";
            vS += servicosTabela.get(i).getValores();
        }
        rotuloTotalServicos.setText("Total dos Serviços: R$ " + vS + "0");
        total = vS + vP;
        rotuloTotalGeral.setText("Total Geral: R$ " + total + "0");
        tabelaServicos.setModel(new javax.swing.table.DefaultTableModel(linhas, titulo) {
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return mColIndex == 3;
            }
        });
        JTableHeader cabecalhoS = tabelaProdutos.getTableHeader();
        cabecalhoS.setFont(new java.awt.Font("Arial", 0, 14));
        tabelaServicos.getColumnModel().getColumn(0).setPreferredWidth(220);
        tabelaServicos.getColumnModel().getColumn(1).setPreferredWidth(220);
        tabelaServicos.getColumnModel().getColumn(3).setPreferredWidth(50);
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        direita.setHorizontalAlignment(SwingConstants.RIGHT);
        tabelaServicos.getColumnModel().getColumn(2).setCellRenderer(direita);
        new util.ButtonColumn(tabelaServicos, 3) {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ServicoVisualizar servicoVisualizar = new ServicoVisualizar(
                        new javax.swing.JFrame(), true, servicosTabela.get(
                                tabelaServicos.getSelectedRow()).getDtServico());
                servicoVisualizar.setVisible(true);
                fireEditingStopped();
            }
        };
        botaoRemoverServico.setEnabled(false);
    }

    private void preencherTabelaProdutos() {
        String[] titulo = new String[]{"Produto", "Quantidade", "Valor Unitário", "Subtotal"};
        String[][] linhas = new String[produtosTabela.size()][4];
        vP = 0;
        for (int i = 0; i < produtosTabela.size(); i++) {
            linhas[i][0] = produtosTabela.get(i).getNmProduto();
            linhas[i][1] = produtosTabela.get(i).getQuantidade() + " unidades";
            linhas[i][2] = "R$ " + produtosTabela.get(i).getValor() + "0";
            linhas[i][3] = "R$ " + produtosTabela.get(i).getSubtotal() + "0";
            vP += produtosTabela.get(i).getSubtotal();
        }
        rotuloTotalProdutos.setText("Total dos Produtos: R$ " + vP + "0");
        total = vS + vP;
        rotuloTotalGeral.setText("Total Geral: R$ " + total + "0");
        tabelaProdutos.setModel(new javax.swing.table.DefaultTableModel(linhas, titulo) {
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        });
        JTableHeader cabecalhoP = tabelaProdutos.getTableHeader();
        cabecalhoP.setFont(new java.awt.Font("Arial", 0, 14));
        tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(220);
        botaoRemoverProduto.setEnabled(false);
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        direita.setHorizontalAlignment(SwingConstants.RIGHT);
        tabelaProdutos.getColumnModel().getColumn(2).setCellRenderer(direita);
        tabelaProdutos.getColumnModel().getColumn(3).setCellRenderer(direita);
    }

    private List<Produto> getProdutoFromTable() {
        produtosTabela = new ArrayList<>();
        for (int linha = 0; linha < tabelaProdutos.getRowCount(); linha++) {
            TableModel modeloTabela = tabelaProdutos.getModel();
            String nmProduto = modeloTabela.getValueAt(linha, 0).toString();
            int quantidade = Integer.parseInt(modeloTabela.getValueAt(linha, 1).toString().replace(" unidades", ""));
            double valor = Double.parseDouble(modeloTabela.getValueAt(linha, 2).toString().replace("R$ ", ""));
            double subtotal = Double.parseDouble(modeloTabela.getValueAt(linha, 3).toString().replace("R$ ", ""));
            produtosTabela.add(new Produto(nmProduto, quantidade, valor, subtotal));
        }
        return produtosTabela;
    }

    private void preencherListaProduto() {
        produtos = new ArrayList<>();
        produtos.add(("Tintura"));
        produtos.add(("Kit Cabelo"));
        produtos.add(("Kit Manicure"));
        produtos.add(("Creme de Cachos"));
        produtos.add(("Creme Relaxamento"));
        modeloProduto = new DefaultListModel();
        listaProduto.setModel(modeloProduto);
        produtos.forEach((produto) -> {
            modeloProduto.addElement(produto);
        });
    }

    private void pesquisarProduto() {
        campoQuantidade.setText("");
        campoValorUnit.setText("");
        modeloProduto.removeAllElements();
        int contador = 0;
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).toLowerCase().startsWith(
                    campoProduto.getText().toLowerCase())) {
                modeloProduto.addElement(produtos.get(i));
                contador++;
            }
        }
        if (contador <= 6) {
            painelListaProduto.setBounds(25, 450, 220, contador * 20 + 1);
        } else {
            painelListaProduto.setBounds(25, 450, 220, 141);
        }
        painelListaProduto.setVisible(true);
    }

    private boolean validaProduto() {
        if (selecaoProduto) {
            campoProduto.setBorder(new LineBorder(Color.GRAY));
            rotuloProdutoObrigatorio.setVisible(false);
            return true;
        } else {
            if (adp) {
                campoProduto.setBorder(new LineBorder(Color.RED));
                rotuloProdutoObrigatorio.setVisible(true);
            } else {
                campoProduto.setBorder(new LineBorder(Color.GRAY));
                rotuloProdutoObrigatorio.setVisible(false);
            }
            return false;
        }
//        for (int i = 0; i < produtos.size(); i++) {
//            if (produtos.get(i).equals(campoProduto.getText()) && selecaoProduto) {
//                campoProduto.setBorder(new LineBorder(Color.GRAY));
//                rotuloProdutoObrigatorio.setVisible(false);
//                return true;
//            }
//        }
//        campoProduto.setBorder(new LineBorder(Color.RED));
//        rotuloProdutoObrigatorio.setVisible(true);
//        return false;
    }

    private boolean validaQuantidade() {
        try {
            Integer.parseInt(campoQuantidade.getText());
            campoQuantidade.setBorder(new LineBorder(Color.GRAY));
            rotuloQuantidadeObrigatorio.setVisible(false);
            return true;
        } catch (NumberFormatException e) {
            if (adp) {
                campoQuantidade.setBorder(new LineBorder(Color.RED));
                rotuloQuantidadeObrigatorio.setVisible(true);
            } else {
                campoQuantidade.setBorder(new LineBorder(Color.GRAY));
                rotuloQuantidadeObrigatorio.setVisible(false);
            }
            return false;
        }
    }

    private boolean validaValorUnit() {
        try {
            String valor = campoValorUnit.getText();
            valor = valor.replace(",", ".");
            Double.parseDouble(valor);
            campoValorUnit.setBorder(new LineBorder(Color.GRAY));
            rotuloValorUnitObrigatorio.setVisible(false);
            return true;
        } catch (NumberFormatException e) {
            if (adp) {
                campoValorUnit.setBorder(new LineBorder(Color.RED));
                rotuloValorUnitObrigatorio.setVisible(true);
            } else {
                campoValorUnit.setBorder(new LineBorder(Color.GRAY));
                rotuloValorUnitObrigatorio.setVisible(false);
            }
            return false;
        }
    }

    private boolean validaTabelas() {
        if (tabelaServicos.getRowCount() <= 0 && tabelaProdutos.getRowCount() <= 0) {
            Advertencia atencao = new Advertencia(null, true, "Insira pelo menos um serviço ou um produto.");
            atencao.setVisible(true);
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        painelTopo = new javax.swing.JPanel();
        painelDadosFora = new javax.swing.JPanel();
        painelTitulo = new javax.swing.JPanel();
        rotuloRegistrarAgendamento = new javax.swing.JLabel();
        painelRolavel = new javax.swing.JScrollPane();
        painelFormulario = new javax.swing.JPanel();
        painelMeio = new javax.swing.JLayeredPane();
        painelBotoes = new javax.swing.JPanel();
        botaoCancelar = new javax.swing.JButton();
        botaoSalvar = new javax.swing.JButton();
        rotuloTotalGeral = new javax.swing.JLabel();
        painelListaCliente = new javax.swing.JScrollPane();
        listaCliente = new javax.swing.JList<>();
        rotuloCliente = new javax.swing.JLabel();
        rotuloData = new javax.swing.JLabel();
        campoData = new javax.swing.JFormattedTextField();
        seletorDeData = new com.toedter.calendar.JDateChooser();
        botaoAdicionarCliente = new javax.swing.JButton();
        bordaMer = new javax.swing.JSeparator();
        botaoComboCliente = new javax.swing.JButton();
        campoNome = new javax.swing.JTextField();
        rotuloDataObrigatorio = new javax.swing.JLabel();
        rotuloClienteObrigatorio = new javax.swing.JLabel();
        botaoPesquisar = new javax.swing.JButton();
        bordaSet = new javax.swing.JSeparator();
        bordaOri = new javax.swing.JSeparator();
        rotuloDados = new javax.swing.JLabel();
        bordaOci = new javax.swing.JSeparator();
        bordaCen = new javax.swing.JSeparator();
        botaoComboServico = new javax.swing.JButton();
        campoServico = new javax.swing.JTextField();
        painelListaServico = new javax.swing.JScrollPane();
        listaServico = new javax.swing.JList<>();
        rotuloServicoObrigatorio = new javax.swing.JLabel();
        botaoComboProfissional = new javax.swing.JButton();
        campoProfissional = new javax.swing.JTextField();
        painelListaProfissional = new javax.swing.JScrollPane();
        listaProfissional = new javax.swing.JList<>();
        rotuloProfissionalObrigatorio = new javax.swing.JLabel();
        rotuloValorObrigatorio = new javax.swing.JLabel();
        botaoComboProduto = new javax.swing.JButton();
        campoProduto = new javax.swing.JTextField();
        painelListaProduto = new javax.swing.JScrollPane();
        listaProduto = new javax.swing.JList<>();
        rotuloProdutoObrigatorio = new javax.swing.JLabel();
        rotuloValorUnitObrigatorio = new javax.swing.JLabel();
        painelServicos = new javax.swing.JPanel();
        rotuloServico = new javax.swing.JLabel();
        rotuloValor = new javax.swing.JLabel();
        painelTabelaServicos = new javax.swing.JScrollPane();
        tabelaServicos = new javax.swing.JTable();
        botaoAdicionarServico = new javax.swing.JButton();
        botaoRemoverServico = new javax.swing.JButton();
        rotuloProfissional = new javax.swing.JLabel();
        rotuloTotalServicos = new javax.swing.JLabel();
        campoValor = new javax.swing.JTextField();
        botaoAdicionarDetalhe = new javax.swing.JButton();
        painelProdutos = new javax.swing.JPanel();
        rotuloProduto = new javax.swing.JLabel();
        rotuloValorUnit = new javax.swing.JLabel();
        botaoAdicionarProduto = new javax.swing.JButton();
        botaoRemoverProduto = new javax.swing.JButton();
        campoValorUnit = new javax.swing.JTextField();
        rotuloQuantidade = new javax.swing.JLabel();
        campoQuantidade = new javax.swing.JTextField();
        rotuloTotalProdutos = new javax.swing.JLabel();
        painelTabelaProdutos = new javax.swing.JScrollPane();
        tabelaProdutos = new javax.swing.JTable();
        rotuloQuantidadeObrigatorio = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gerenciar Atendimentos");
        setMaximumSize(new java.awt.Dimension(710, 820));
        setMinimumSize(new java.awt.Dimension(700, 450));
        setPreferredSize(new java.awt.Dimension(700, 550));
        setResizable(false);

        painelTopo.setLayout(new java.awt.BorderLayout());

        painelDadosFora.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        painelTopo.add(painelDadosFora, java.awt.BorderLayout.CENTER);

        painelTitulo.setBackground(java.awt.Color.black);

        rotuloRegistrarAgendamento.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        rotuloRegistrarAgendamento.setForeground(new java.awt.Color(255, 255, 255));
        rotuloRegistrarAgendamento.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloRegistrarAgendamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/barbearia.png"))); // NOI18N
        rotuloRegistrarAgendamento.setText("REGISTRAR ATENDIMENTO");
        painelTitulo.add(rotuloRegistrarAgendamento);

        painelTopo.add(painelTitulo, java.awt.BorderLayout.NORTH);

        getContentPane().add(painelTopo, java.awt.BorderLayout.NORTH);

        painelFormulario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        painelMeio.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        painelBotoes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 5));

        botaoCancelar.setBackground(new java.awt.Color(135, 206, 235));
        botaoCancelar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoCancelar.setMnemonic('c');
        botaoCancelar.setText("Cancelar");
        botaoCancelar.setToolTipText("Cancelar este registro (Alt+C)");
        botaoCancelar.setContentAreaFilled(false);
        botaoCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoCancelar.setOpaque(true);
        botaoCancelar.setPreferredSize(new java.awt.Dimension(130, 35));
        botaoCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoCancelarActionPerformed(evt);
            }
        });
        painelBotoes.add(botaoCancelar);
        botaoCancelar.setOpaque(true);

        botaoSalvar.setBackground(new java.awt.Color(46, 139, 87));
        botaoSalvar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoSalvar.setMnemonic('s');
        botaoSalvar.setText("Salvar");
        botaoSalvar.setToolTipText("Salvar este registro (Alt+S)");
        botaoSalvar.setContentAreaFilled(false);
        botaoSalvar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoSalvar.setMaximumSize(new java.awt.Dimension(89, 25));
        botaoSalvar.setMinimumSize(new java.awt.Dimension(89, 25));
        botaoSalvar.setOpaque(true);
        botaoSalvar.setPreferredSize(new java.awt.Dimension(130, 35));
        botaoSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoSalvarActionPerformed(evt);
            }
        });
        painelBotoes.add(botaoSalvar);
        botaoSalvar.setOpaque(true);

        painelMeio.add(painelBotoes, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 670, 600, 50));

        rotuloTotalGeral.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        rotuloTotalGeral.setText("Total Geral: R$ 0,00");
        rotuloTotalGeral.setToolTipText("");
        painelMeio.add(rotuloTotalGeral, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 655, -1, -1));

        listaCliente.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        listaCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listaClienteMousePressed(evt);
            }
        });
        painelListaCliente.setViewportView(listaCliente);

        painelMeio.setLayer(painelListaCliente, javax.swing.JLayeredPane.POPUP_LAYER);
        painelMeio.add(painelListaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 80, 260, 40));
        painelListaCliente.setVisible(false);

        rotuloCliente.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloCliente.setText("Cliente:*");
        painelMeio.add(rotuloCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 25, 60, 25));

        rotuloData.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloData.setText("Data:*");
        painelMeio.add(rotuloData, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 25, -1, 25));

        campoData.setForeground(java.awt.Color.gray);
        campoData.setText("dd/mm/aaaa");
        campoData.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campoData.setPreferredSize(null);
        campoData.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoDataFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                campoDataFocusLost(evt);
            }
        });
        painelMeio.add(campoData, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 50, 130, 30));

        seletorDeData.setToolTipText("Selecionar data");
        seletorDeData.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        seletorDeData.setMinSelectableDate(new java.util.Date(-62135755095000L));
        seletorDeData.setMinimumSize(new java.awt.Dimension(27, 10));
        seletorDeData.setPreferredSize(new java.awt.Dimension(87, 15));
        seletorDeData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                seletorDeDataPropertyChange(evt);
            }
        });
        painelMeio.add(seletorDeData, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 50, 149, 30));
        seletorDeData.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        botaoAdicionarCliente.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoAdicionarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/add.png"))); // NOI18N
        botaoAdicionarCliente.setMnemonic('n');
        botaoAdicionarCliente.setToolTipText("Criar novo cliente (Alt+N)");
        botaoAdicionarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoAdicionarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoAdicionarClienteActionPerformed(evt);
            }
        });
        painelMeio.add(botaoAdicionarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, 35, 31));

        bordaMer.setForeground(new java.awt.Color(0, 0, 0));
        bordaMer.setPreferredSize(new java.awt.Dimension(40, 1));
        painelMeio.add(bordaMer, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 100, 646, 1));

        botaoComboCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/seta.png"))); // NOI18N
        botaoComboCliente.setContentAreaFilled(false);
        botaoComboCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoComboCliente.setFocusable(false);
        botaoComboCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoComboClienteActionPerformed(evt);
            }
        });
        painelMeio.add(botaoComboCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 50, 35, 31));

        campoNome.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campoNome.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoNomeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                campoNomeFocusLost(evt);
            }
        });
        campoNome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                campoNomeMouseClicked(evt);
            }
        });
        campoNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoNomeKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoNomeKeyTyped(evt);
            }
        });
        painelMeio.add(campoNome, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 50, 260, 30));

        rotuloDataObrigatorio.setForeground(new java.awt.Color(204, 0, 0));
        rotuloDataObrigatorio.setText("Selecione uma data");
        painelMeio.add(rotuloDataObrigatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 80, 150, -1));
        rotuloDataObrigatorio.setVisible(false);

        rotuloClienteObrigatorio.setForeground(new java.awt.Color(204, 0, 0));
        rotuloClienteObrigatorio.setText("Selecione um cliente");
        painelMeio.add(rotuloClienteObrigatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 80, 250, -1));
        rotuloClienteObrigatorio.setVisible(false);

        botaoPesquisar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/zoom.png"))); // NOI18N
        botaoPesquisar.setMnemonic('p');
        botaoPesquisar.setToolTipText("Pesquisar cliente (Alt+P)");
        botaoPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoPesquisarActionPerformed(evt);
            }
        });
        painelMeio.add(botaoPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 50, 35, 31));

        bordaSet.setForeground(new java.awt.Color(0, 0, 0));
        bordaSet.setPreferredSize(new java.awt.Dimension(40, 1));
        painelMeio.add(bordaSet, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 20, 592, 1));

        bordaOri.setForeground(new java.awt.Color(0, 0, 0));
        bordaOri.setOrientation(javax.swing.SwingConstants.VERTICAL);
        painelMeio.add(bordaOri, new org.netbeans.lib.awtextra.AbsoluteConstraints(657, 20, 1, 80));

        rotuloDados.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloDados.setText("Dados");
        painelMeio.add(rotuloDados, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 12, -1, -1));

        bordaOci.setForeground(new java.awt.Color(0, 0, 0));
        bordaOci.setOrientation(javax.swing.SwingConstants.VERTICAL);
        painelMeio.add(bordaOci, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 20, 1, 80));

        bordaCen.setForeground(new java.awt.Color(0, 0, 0));
        bordaCen.setPreferredSize(new java.awt.Dimension(40, 1));
        painelMeio.add(bordaCen, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 20, 5, 1));

        botaoComboServico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/seta.png"))); // NOI18N
        botaoComboServico.setContentAreaFilled(false);
        botaoComboServico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoComboServico.setFocusable(false);
        botaoComboServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoComboServicoActionPerformed(evt);
            }
        });
        painelMeio.add(botaoComboServico, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 145, 35, 31));

        campoServico.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campoServico.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoServicoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                campoServicoFocusLost(evt);
            }
        });
        campoServico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                campoServicoMouseClicked(evt);
            }
        });
        campoServico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoServicoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoServicoKeyTyped(evt);
            }
        });
        painelMeio.add(campoServico, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 145, 160, 30));

        listaServico.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        listaServico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        listaServico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listaServicoMousePressed(evt);
            }
        });
        painelListaServico.setViewportView(listaServico);

        painelMeio.setLayer(painelListaServico, javax.swing.JLayeredPane.POPUP_LAYER);
        painelMeio.add(painelListaServico, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 175, 160, 101));
        painelListaServico.setVisible(false);

        rotuloServicoObrigatorio.setForeground(new java.awt.Color(204, 0, 0));
        rotuloServicoObrigatorio.setText("Selecione um serviço");
        painelMeio.add(rotuloServicoObrigatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 175, 150, -1));
        rotuloServicoObrigatorio.setVisible(false);

        botaoComboProfissional.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/seta.png"))); // NOI18N
        botaoComboProfissional.setContentAreaFilled(false);
        botaoComboProfissional.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoComboProfissional.setFocusable(false);
        botaoComboProfissional.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoComboProfissionalActionPerformed(evt);
            }
        });
        painelMeio.add(botaoComboProfissional, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 145, 35, 31));

        campoProfissional.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campoProfissional.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoProfissionalFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                campoProfissionalFocusLost(evt);
            }
        });
        campoProfissional.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                campoProfissionalMouseClicked(evt);
            }
        });
        campoProfissional.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoProfissionalKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoProfissionalKeyTyped(evt);
            }
        });
        painelMeio.add(campoProfissional, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 145, 160, 30));

        listaProfissional.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        listaProfissional.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        listaProfissional.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listaProfissionalMousePressed(evt);
            }
        });
        painelListaProfissional.setViewportView(listaProfissional);

        painelMeio.setLayer(painelListaProfissional, javax.swing.JLayeredPane.POPUP_LAYER);
        painelMeio.add(painelListaProfissional, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 175, 160, 101));
        painelListaProfissional.setVisible(false);

        rotuloProfissionalObrigatorio.setForeground(new java.awt.Color(204, 0, 0));
        rotuloProfissionalObrigatorio.setText("Selecione um profissional");
        painelMeio.add(rotuloProfissionalObrigatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 175, 150, -1));
        rotuloProfissionalObrigatorio.setVisible(false);

        rotuloValorObrigatorio.setForeground(new java.awt.Color(204, 0, 0));
        rotuloValorObrigatorio.setText("Preencha este campo");
        painelMeio.add(rotuloValorObrigatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(372, 175, 140, -1));
        rotuloValorObrigatorio.setVisible(false);

        botaoComboProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/seta.png"))); // NOI18N
        botaoComboProduto.setContentAreaFilled(false);
        botaoComboProduto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoComboProduto.setFocusable(false);
        botaoComboProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoComboProdutoActionPerformed(evt);
            }
        });
        painelMeio.add(botaoComboProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 420, 35, 31));

        campoProduto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campoProduto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoProdutoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                campoProdutoFocusLost(evt);
            }
        });
        campoProduto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                campoProdutoMouseClicked(evt);
            }
        });
        campoProduto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoProdutoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoProdutoKeyTyped(evt);
            }
        });
        painelMeio.add(campoProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 420, 220, 30));

        listaProduto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        listaProduto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        listaProduto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listaProdutoMousePressed(evt);
            }
        });
        painelListaProduto.setViewportView(listaProduto);

        painelMeio.setLayer(painelListaProduto, javax.swing.JLayeredPane.POPUP_LAYER);
        painelMeio.add(painelListaProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 450, 220, 101));
        painelListaProduto.setVisible(false);

        rotuloProdutoObrigatorio.setForeground(new java.awt.Color(204, 0, 0));
        rotuloProdutoObrigatorio.setText("Selecione um produto");
        painelMeio.add(rotuloProdutoObrigatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 450, 110, -1));
        rotuloProdutoObrigatorio.setVisible(false);

        rotuloValorUnitObrigatorio.setForeground(new java.awt.Color(204, 0, 0));
        rotuloValorUnitObrigatorio.setText("Preencha este campo");
        painelMeio.add(rotuloValorUnitObrigatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(375, 450, 140, -1));
        rotuloValorUnitObrigatorio.setVisible(false);

        painelServicos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Serviços", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N
        painelServicos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        painelServicos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rotuloServico.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloServico.setText("Serviço:");
        painelServicos.add(rotuloServico, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 19, -1, -1));

        rotuloValor.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloValor.setText("Valor:");
        painelServicos.add(rotuloValor, new org.netbeans.lib.awtextra.AbsoluteConstraints(362, 20, -1, -1));

        tabelaServicos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tabelaServicos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serviço", "Profissional", "Valor", ""
            }
        ));
        tabelaServicos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabelaServicos.setRowHeight(30);
        tabelaServicos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabelaServicos.setShowVerticalLines(false);
        tabelaServicos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaServicosMouseClicked(evt);
            }
        });
        painelTabelaServicos.setViewportView(tabelaServicos);
        JTableHeader cabecalhoS = tabelaServicos.getTableHeader();
        cabecalhoS.setFont(new java.awt.Font("Arial", 0, 14));
        tabelaServicos.getColumnModel().getColumn(0).setPreferredWidth(220);
        tabelaServicos.getColumnModel().getColumn(1).setPreferredWidth(220);
        tabelaServicos.getColumnModel().getColumn(3).setPreferredWidth(50);

        painelServicos.add(painelTabelaServicos, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 85, 575, 165));

        botaoAdicionarServico.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoAdicionarServico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/tabelaInserir.png"))); // NOI18N
        botaoAdicionarServico.setMnemonic('a');
        botaoAdicionarServico.setToolTipText("Adicionar serviço à tabela (Alt+A)");
        botaoAdicionarServico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoAdicionarServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoAdicionarServicoActionPerformed(evt);
            }
        });
        painelServicos.add(botaoAdicionarServico, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 35, 35, 35));

        botaoRemoverServico.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoRemoverServico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/tabelaRemover.png"))); // NOI18N
        botaoRemoverServico.setMnemonic('r');
        botaoRemoverServico.setToolTipText("Remover serviço selecionado (Alt+P)");
        botaoRemoverServico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoRemoverServico.setEnabled(false);
        botaoRemoverServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoRemoverServicoActionPerformed(evt);
            }
        });
        painelServicos.add(botaoRemoverServico, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 80, 35, 35));

        rotuloProfissional.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloProfissional.setText("Profissional:");
        painelServicos.add(rotuloProfissional, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, -1));

        rotuloTotalServicos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloTotalServicos.setText("Total dos Serviços: R$ 0,00");
        rotuloTotalServicos.setToolTipText("");
        painelServicos.add(rotuloTotalServicos, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 250, -1, -1));

        campoValor.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campoValor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoValorFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                campoValorFocusLost(evt);
            }
        });
        campoValor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoValorKeyTyped(evt);
            }
        });
        painelServicos.add(campoValor, new org.netbeans.lib.awtextra.AbsoluteConstraints(362, 40, 100, 30));
        //((AbstractDocument) campoValor.getDocument()).setDocumentFilter(new util.IntegerDocument(5));
        //campoValor.setDocument(new util.Numeros());

        botaoAdicionarDetalhe.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoAdicionarDetalhe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/add.png"))); // NOI18N
        botaoAdicionarDetalhe.setMnemonic('d');
        botaoAdicionarDetalhe.setText("Detalhe");
        botaoAdicionarDetalhe.setToolTipText("Adicionar detalhe a este serviço (Alt+D)");
        botaoAdicionarDetalhe.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoAdicionarDetalhe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoAdicionarDetalheActionPerformed(evt);
            }
        });
        painelServicos.add(botaoAdicionarDetalhe, new org.netbeans.lib.awtextra.AbsoluteConstraints(472, 40, 118, 30));

        painelMeio.add(painelServicos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 105, 650, 275));

        painelProdutos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Produtos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N
        painelProdutos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        painelProdutos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rotuloProduto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloProduto.setText("Produto:");
        painelProdutos.add(rotuloProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 19, -1, -1));

        rotuloValorUnit.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloValorUnit.setText("Valor Unitário:");
        painelProdutos.add(rotuloValorUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(365, 20, -1, -1));

        botaoAdicionarProduto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoAdicionarProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/tabelaInserir.png"))); // NOI18N
        botaoAdicionarProduto.setMnemonic('d');
        botaoAdicionarProduto.setToolTipText("Adicionar produto à tabela (Alt+D)");
        botaoAdicionarProduto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoAdicionarProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoAdicionarProdutoActionPerformed(evt);
            }
        });
        painelProdutos.add(botaoAdicionarProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 28, 35, 35));

        botaoRemoverProduto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoRemoverProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/tabelaRemover.png"))); // NOI18N
        botaoRemoverProduto.setMnemonic('e');
        botaoRemoverProduto.setToolTipText("Remover produto selecionado (Alt+E)");
        botaoRemoverProduto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoRemoverProduto.setEnabled(false);
        botaoRemoverProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoRemoverProdutoActionPerformed(evt);
            }
        });
        painelProdutos.add(botaoRemoverProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 80, 35, 35));

        campoValorUnit.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campoValorUnit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoValorUnitFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                campoValorUnitFocusLost(evt);
            }
        });
        campoValorUnit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoValorUnitKeyTyped(evt);
            }
        });
        painelProdutos.add(campoValorUnit, new org.netbeans.lib.awtextra.AbsoluteConstraints(365, 40, 100, 30));
        //((AbstractDocument) campoValorUnit.getDocument()).setDocumentFilter(new util.IntegerDocument(5));

        rotuloQuantidade.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloQuantidade.setText("Quantidade:");
        painelProdutos.add(rotuloQuantidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, -1, -1));

        campoQuantidade.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campoQuantidade.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoQuantidadeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                campoQuantidadeFocusLost(evt);
            }
        });
        campoQuantidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoQuantidadeKeyTyped(evt);
            }
        });
        painelProdutos.add(campoQuantidade, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 40, 100, 30));
        //((AbstractDocument) campoQuantidade.getDocument()).setDocumentFilter(new util.IntegerDocument(5));

        rotuloTotalProdutos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloTotalProdutos.setText("Total dos Produtos: R$ 0,00");
        rotuloTotalProdutos.setToolTipText("");
        painelProdutos.add(rotuloTotalProdutos, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 250, -1, -1));

        tabelaProdutos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tabelaProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serviço", "Quantidade", "Valor Unitário", "Subtotal"
            }
        ));
        tabelaProdutos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabelaProdutos.setRowHeight(30);
        tabelaProdutos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabelaProdutos.setShowVerticalLines(false);
        tabelaProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaProdutosMouseClicked(evt);
            }
        });
        painelTabelaProdutos.setViewportView(tabelaProdutos);
        JTableHeader cabecalhoP = tabelaProdutos.getTableHeader();
        cabecalhoP.setFont(new java.awt.Font("Arial", 0, 14));
        tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(220);

        painelProdutos.add(painelTabelaProdutos, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 85, 575, 165));

        rotuloQuantidadeObrigatorio.setForeground(new java.awt.Color(204, 0, 0));
        rotuloQuantidadeObrigatorio.setText("Preencha este campo");
        painelProdutos.add(rotuloQuantidadeObrigatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 70, 130, -1));
        rotuloQuantidadeObrigatorio.setVisible(false);

        painelMeio.add(painelProdutos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 650, 275));

        painelFormulario.add(painelMeio, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        painelRolavel.setViewportView(painelFormulario);

        getContentPane().add(painelRolavel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoCancelarActionPerformed
        dispose();
    }//GEN-LAST:event_botaoCancelarActionPerformed

    private void tabelaServicosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaServicosMouseClicked
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
            botaoRemoverServico.setEnabled(true);
        }
    }//GEN-LAST:event_tabelaServicosMouseClicked

    private void botaoAdicionarServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoAdicionarServicoActionPerformed
        ads = true;
        boolean s = validaServico();
        boolean p = validaProfissional();
        boolean v = validaValor();
        if (s && p && v) {
            String valor = campoValor.getText();
            valor = valor.replace(",", ".");
            ads = false;
            servicosTabela.add(new Servico(campoServico.getText(),
                    campoProfissional.getText(), Double.parseDouble(
                    valor), texto));
            anularServico();
            preencherTabelaServicos();
            selecaoServico = false;
            selecaoProfissional = false;
            painelListaProfissional.setBounds(200, 175, 160, 1);
        }
    }//GEN-LAST:event_botaoAdicionarServicoActionPerformed

    private void botaoRemoverServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoRemoverServicoActionPerformed
        int linha = tabelaServicos.getSelectedRow();
        DefaultTableModel m = (DefaultTableModel) tabelaServicos.getModel();
        m.removeRow(linha);
        servicosTabela = getServicoFromTable();
        preencherTabelaServicos();
    }//GEN-LAST:event_botaoRemoverServicoActionPerformed

    private void botaoAdicionarProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoAdicionarProdutoActionPerformed
        adp = true;
        boolean p = validaProduto();
        boolean qq = validaQuantidade();
        boolean vv = validaValorUnit();
        if (p && qq && vv) {
            String valor = campoValorUnit.getText();
            valor = valor.replace(",", ".");
            adp = false;
            String produto = campoProduto.getText();
            int q = Integer.parseInt(campoQuantidade.getText());
            double v = Double.parseDouble(valor);
            double s = q * v;
            produtosTabela.add(new Produto(produto, q, v, s));
            preencherTabelaProdutos();
            anularProduto();
            selecaoProduto = false;
        }
    }//GEN-LAST:event_botaoAdicionarProdutoActionPerformed

    private void botaoRemoverProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoRemoverProdutoActionPerformed
        int linha = tabelaProdutos.getSelectedRow();
        DefaultTableModel m = (DefaultTableModel) tabelaProdutos.getModel();
        m.removeRow(linha);
        produtosTabela = getProdutoFromTable();
        preencherTabelaProdutos();
    }//GEN-LAST:event_botaoRemoverProdutoActionPerformed

    private void listaClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaClienteMousePressed
        campoNome.setText(listaCliente.getSelectedValue());
        selecaoCliente = true;
    }//GEN-LAST:event_listaClienteMousePressed

    private void botaoAdicionarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoAdicionarClienteActionPerformed
        ClienteNovo clienteNovo = new ClienteNovo(null, true);
        clienteNovo.setVisible(true);
        Cliente cliente = clienteNovo.getCliente();
        if (cliente != null) {
            campoNome.setText(cliente.getNmCliente());
            selecaoCliente = true;
        }
    }//GEN-LAST:event_botaoAdicionarClienteActionPerformed

    private void campoNomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoNomeFocusLost
        painelListaCliente.setVisible(false);
        if (adt) {
            validaCliente();
        } else {
            campoNome.setBorder(new LineBorder(Color.GRAY));
        }
    }//GEN-LAST:event_campoNomeFocusLost

    private void campoNomeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoNomeFocusGained
        campoNome.setBorder(new LineBorder(Color.BLUE));
//        if (!selecaoCliente) {
//            painelListaCliente.setVisible(true);
//        } else {
//            painelListaCliente.setVisible(false);
//        }
    }//GEN-LAST:event_campoNomeFocusGained

    private void campoNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNomeKeyReleased
        selecaoCliente = false;
        pesquisarCliente();
    }//GEN-LAST:event_campoNomeKeyReleased

    private void botaoPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoPesquisarActionPerformed
        ClientePesquisar clientePesquisar = new ClientePesquisar(null, true);
        clientePesquisar.setVisible(true);
        Cliente cliente = clientePesquisar.getCliente();
        if (cliente != null) {
            campoNome.setText(cliente.getNmCliente());
            selecaoCliente = true;
        }
    }//GEN-LAST:event_botaoPesquisarActionPerformed

    private void campoServicoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoServicoFocusGained
        campoServico.setBorder(new LineBorder(Color.BLUE));
//        if (!selecaoServico) {
//            painelListaServico.setVisible(true);
//        } else {
//            painelListaServico.setVisible(false);
//        }
    }//GEN-LAST:event_campoServicoFocusGained

    private void campoServicoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoServicoFocusLost
        painelListaServico.setVisible(false);
        validaServico();
    }//GEN-LAST:event_campoServicoFocusLost

    private void campoServicoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoServicoKeyReleased
        selecaoServico = false;
        painelListaProfissional.setBounds(200, 175, 160, 1);
        pesquisarServico();
    }//GEN-LAST:event_campoServicoKeyReleased

    private void listaServicoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaServicoMousePressed
        campoServico.setText(listaServico.getSelectedValue());
        selecaoServico = true;
        campoProfissional.setText("");
        painelListaProfissional.setBounds(200, 175, 160, 101);
        preencherListaProfissional();
        campoValor.setText("");
    }//GEN-LAST:event_listaServicoMousePressed

    private void campoProfissionalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoProfissionalFocusGained
        campoProfissional.setBorder(new LineBorder(Color.BLUE));
        if (!selecaoServico) {
            painelListaProfissional.setBounds(200, 175, 220, 1);
        }
//        if (!selecaoProfissional) {
//            painelListaProfissional.setVisible(true);
//        } else {
//            painelListaProfissional.setVisible(false);
//        }
    }//GEN-LAST:event_campoProfissionalFocusGained

    private void campoProfissionalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoProfissionalFocusLost
        painelListaProfissional.setVisible(false);
        validaProfissional();
    }//GEN-LAST:event_campoProfissionalFocusLost

    private void campoProfissionalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoProfissionalKeyReleased
        selecaoProfissional = false;
        pesquisarProfissional();
    }//GEN-LAST:event_campoProfissionalKeyReleased

    private void listaProfissionalMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaProfissionalMousePressed
        campoProfissional.setText(listaProfissional.getSelectedValue());
        selecaoProfissional = true;
        campoValor.setText("");
    }//GEN-LAST:event_listaProfissionalMousePressed

    private void campoServicoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoServicoMouseClicked
        painelListaServico.setVisible(true);
        pesquisarServico();
    }//GEN-LAST:event_campoServicoMouseClicked

    private void campoProfissionalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoProfissionalMouseClicked
        if (selecaoServico) {
            painelListaProfissional.setVisible(true);
        }
        pesquisarProfissional();
    }//GEN-LAST:event_campoProfissionalMouseClicked

    private void listaProdutoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaProdutoMousePressed
        campoProduto.setText(listaProduto.getSelectedValue());
        selecaoProduto = true;
        campoQuantidade.setText("");
        campoValorUnit.setText("");
    }//GEN-LAST:event_listaProdutoMousePressed

    private void campoProdutoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoProdutoFocusGained
        campoProduto.setBorder(new LineBorder(Color.BLUE));
//        if (!selecaoProduto) {
//            painelListaProduto.setVisible(true);
//        } else {
//            painelListaProduto.setVisible(false);
//        }
    }//GEN-LAST:event_campoProdutoFocusGained

    private void campoProdutoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoProdutoFocusLost
        painelListaProduto.setVisible(false);
        validaProduto();
    }//GEN-LAST:event_campoProdutoFocusLost

    private void campoProdutoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoProdutoMouseClicked
        painelListaProduto.setVisible(true);
        pesquisarProduto();
    }//GEN-LAST:event_campoProdutoMouseClicked

    private void campoProdutoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoProdutoKeyReleased
        selecaoProduto = false;
        pesquisarProduto();
    }//GEN-LAST:event_campoProdutoKeyReleased

    private void tabelaProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaProdutosMouseClicked
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
            botaoRemoverProduto.setEnabled(true);
        }
    }//GEN-LAST:event_tabelaProdutosMouseClicked

    private void botaoSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSalvarActionPerformed
        adt = true;
        boolean cliente = validaCliente();
        boolean data = validaData();
        boolean tabelas = validaTabelas();
        if (cliente && data && tabelas) {
            this.dispose();
        }
    }//GEN-LAST:event_botaoSalvarActionPerformed

    private void botaoComboClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoComboClienteActionPerformed
        if (!painelListaCliente.isVisible()) {
            painelListaCliente.setVisible(true);
            campoNome.requestFocusInWindow();
            pesquisarCliente();
        } else {
            painelListaCliente.setVisible(false);
        }
    }//GEN-LAST:event_botaoComboClienteActionPerformed

    private void botaoComboServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoComboServicoActionPerformed
        if (!painelListaServico.isVisible()) {
            painelListaServico.setVisible(true);
            campoServico.requestFocusInWindow();
            pesquisarServico();
        } else {
            painelListaServico.setVisible(false);
        }
    }//GEN-LAST:event_botaoComboServicoActionPerformed

    private void botaoComboProfissionalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoComboProfissionalActionPerformed
        if (!painelListaProfissional.isVisible()) {
            painelListaProfissional.setVisible(true);
            campoProfissional.requestFocusInWindow();
            pesquisarProfissional();
        } else {
            painelListaProfissional.setVisible(false);
        }
    }//GEN-LAST:event_botaoComboProfissionalActionPerformed

    private void botaoComboProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoComboProdutoActionPerformed
        if (!painelListaProduto.isVisible()) {
            painelListaProduto.setVisible(true);
            campoProduto.requestFocusInWindow();
            pesquisarProduto();
        } else {
            painelListaProduto.setVisible(false);
        }
    }//GEN-LAST:event_botaoComboProdutoActionPerformed

    private void campoNomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoNomeMouseClicked
        pesquisarCliente();
        painelListaCliente.setVisible(true);
    }//GEN-LAST:event_campoNomeMouseClicked

    private void campoDataFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoDataFocusGained
        focoData();
    }//GEN-LAST:event_campoDataFocusGained

    private void campoDataFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoDataFocusLost
        String data = campoData.getText();
        data = data.replace("/", "");
        data = data.replace("_", "");
        data = data.replace("d", "");
        data = data.replace("m", "");
        data = data.replace("a", "");
        if (data.equals("")) {
            campoData.setForeground(Color.GRAY);
            campoData.setFormatterFactory(null);
            campoData.setText("dd/mm/aaaa");
        }
        if (adt) {
            validaData();
        } else {
            campoData.setBorder(new LineBorder(Color.GRAY));
        }
    }//GEN-LAST:event_campoDataFocusLost

    private void seletorDeDataPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_seletorDeDataPropertyChange
        try {
            if (!util.DateParse.dateToString(seletorDeData.getDate()).equals("dd/MM/yyyy")) {
                focoData();
                campoData.setText(util.DateParse.dateToString(seletorDeData.getDate()));
                if (adt) {
                    validaData();
                }
            }
        } catch (NullPointerException e) {
        }
    }//GEN-LAST:event_seletorDeDataPropertyChange

    private void campoValorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoValorFocusGained
        campoValor.setBorder(new LineBorder(Color.BLUE));
    }//GEN-LAST:event_campoValorFocusGained

    private void campoValorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoValorFocusLost
        validaValor();
    }//GEN-LAST:event_campoValorFocusLost

    private void campoQuantidadeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoQuantidadeFocusGained
        campoQuantidade.setBorder(new LineBorder(Color.BLUE));
    }//GEN-LAST:event_campoQuantidadeFocusGained

    private void campoQuantidadeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoQuantidadeFocusLost
        validaQuantidade();
    }//GEN-LAST:event_campoQuantidadeFocusLost

    private void campoValorUnitFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoValorUnitFocusGained
        campoValorUnit.setBorder(new LineBorder(Color.BLUE));
    }//GEN-LAST:event_campoValorUnitFocusGained

    private void campoValorUnitFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoValorUnitFocusLost
        validaValorUnit();
    }//GEN-LAST:event_campoValorUnitFocusLost

    private void botaoAdicionarDetalheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoAdicionarDetalheActionPerformed
        ServicoAdicionar servicoAdicionar = new ServicoAdicionar(
                new javax.swing.JFrame(), true);
        servicoAdicionar.setVisible(true);
        texto = servicoAdicionar.getTexto();
    }//GEN-LAST:event_botaoAdicionarDetalheActionPerformed

    private void campoValorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoValorKeyTyped
        String caracteres = "0123456789";
        String caracter = ".,";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            if (campoValor.getText().contains(",")
                    || campoValor.getText().contains(".")
                    || !caracter.contains(evt.getKeyChar() + "")) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_campoValorKeyTyped

    private void campoValorUnitKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoValorUnitKeyTyped
        String caracteres = "0123456789";
        String caracter = ".,";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            if (campoValor.getText().contains(",")
                    || campoValor.getText().contains(".")
                    || !caracter.contains(evt.getKeyChar() + "")) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_campoValorUnitKeyTyped

    private void campoQuantidadeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoQuantidadeKeyTyped
        String caracteres = "0123456789";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();
        }
    }//GEN-LAST:event_campoQuantidadeKeyTyped

    private void campoNomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNomeKeyTyped
        util.JanelaComum.campoTexto(evt);
    }//GEN-LAST:event_campoNomeKeyTyped

    private void campoServicoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoServicoKeyTyped
        util.JanelaComum.campoTexto(evt);
    }//GEN-LAST:event_campoServicoKeyTyped

    private void campoProfissionalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoProfissionalKeyTyped
        util.JanelaComum.campoTexto(evt);
    }//GEN-LAST:event_campoProfissionalKeyTyped

    private void campoProdutoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoProdutoKeyTyped
        util.JanelaComum.campoTexto(evt);
    }//GEN-LAST:event_campoProdutoKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AtendimentoRegistrar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            AtendimentoRegistrar dialog = new AtendimentoRegistrar(
                    new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    private List<Cliente> clientes = null;
    private DefaultListModel modeloCliente;
    private boolean selecaoCliente = false;
    private List<Servico> servicosTabela = new ArrayList<>();
    private List<String> servicos;
    private DefaultListModel modeloServico;
    private boolean selecaoServico = false;
    private List<String> profissionais = new ArrayList<>();
    private DefaultListModel modeloProfissional;
    private boolean selecaoProfissional = false;
    private List<Produto> produtosTabela = new ArrayList<>();
    private List<String> produtos = new ArrayList<>();
    private DefaultListModel modeloProduto;
    private boolean selecaoProduto = false;
    private double vS = 0;
    private double vP = 0;
    private double total = 0;
    private boolean ads = false;
    private boolean adp = false;
    private boolean adt = false;
    private String texto = "";

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator bordaCen;
    private javax.swing.JSeparator bordaMer;
    private javax.swing.JSeparator bordaOci;
    private javax.swing.JSeparator bordaOri;
    private javax.swing.JSeparator bordaSet;
    private javax.swing.JButton botaoAdicionarCliente;
    private javax.swing.JButton botaoAdicionarDetalhe;
    private javax.swing.JButton botaoAdicionarProduto;
    private javax.swing.JButton botaoAdicionarServico;
    private javax.swing.JButton botaoCancelar;
    private javax.swing.JButton botaoComboCliente;
    private javax.swing.JButton botaoComboProduto;
    private javax.swing.JButton botaoComboProfissional;
    private javax.swing.JButton botaoComboServico;
    private javax.swing.JButton botaoPesquisar;
    private javax.swing.JButton botaoRemoverProduto;
    private javax.swing.JButton botaoRemoverServico;
    private javax.swing.JButton botaoSalvar;
    private javax.swing.JFormattedTextField campoData;
    private javax.swing.JTextField campoNome;
    private javax.swing.JTextField campoProduto;
    private javax.swing.JTextField campoProfissional;
    private javax.swing.JTextField campoQuantidade;
    private javax.swing.JTextField campoServico;
    private javax.swing.JTextField campoValor;
    private javax.swing.JTextField campoValorUnit;
    private javax.swing.JList<String> listaCliente;
    private javax.swing.JList<String> listaProduto;
    private javax.swing.JList<String> listaProfissional;
    private javax.swing.JList<String> listaServico;
    private javax.swing.JPanel painelBotoes;
    private javax.swing.JPanel painelDadosFora;
    private javax.swing.JPanel painelFormulario;
    private javax.swing.JScrollPane painelListaCliente;
    private javax.swing.JScrollPane painelListaProduto;
    private javax.swing.JScrollPane painelListaProfissional;
    private javax.swing.JScrollPane painelListaServico;
    private javax.swing.JLayeredPane painelMeio;
    private javax.swing.JPanel painelProdutos;
    private javax.swing.JScrollPane painelRolavel;
    private javax.swing.JPanel painelServicos;
    private javax.swing.JScrollPane painelTabelaProdutos;
    private javax.swing.JScrollPane painelTabelaServicos;
    private javax.swing.JPanel painelTitulo;
    private javax.swing.JPanel painelTopo;
    private javax.swing.JLabel rotuloCliente;
    private javax.swing.JLabel rotuloClienteObrigatorio;
    private javax.swing.JLabel rotuloDados;
    private javax.swing.JLabel rotuloData;
    private javax.swing.JLabel rotuloDataObrigatorio;
    private javax.swing.JLabel rotuloProduto;
    private javax.swing.JLabel rotuloProdutoObrigatorio;
    private javax.swing.JLabel rotuloProfissional;
    private javax.swing.JLabel rotuloProfissionalObrigatorio;
    private javax.swing.JLabel rotuloQuantidade;
    private javax.swing.JLabel rotuloQuantidadeObrigatorio;
    private javax.swing.JLabel rotuloRegistrarAgendamento;
    private javax.swing.JLabel rotuloServico;
    private javax.swing.JLabel rotuloServicoObrigatorio;
    private javax.swing.JLabel rotuloTotalGeral;
    private javax.swing.JLabel rotuloTotalProdutos;
    private javax.swing.JLabel rotuloTotalServicos;
    private javax.swing.JLabel rotuloValor;
    private javax.swing.JLabel rotuloValorObrigatorio;
    private javax.swing.JLabel rotuloValorUnit;
    private javax.swing.JLabel rotuloValorUnitObrigatorio;
    private com.toedter.calendar.JDateChooser seletorDeData;
    private javax.swing.JTable tabelaProdutos;
    private javax.swing.JTable tabelaServicos;
    // End of variables declaration//GEN-END:variables
}
