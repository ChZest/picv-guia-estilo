package interfaces;

import java.awt.Color;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Toolkit;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.jfree.data.category.DefaultCategoryDataset;
import java.util.Date;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import util.DateParse;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;

/* @author BIC-Jr Guia de Estilo */
public class HistoricoReceita extends javax.swing.JDialog {

    /**
     * @param parent
     * @param modal
     */
    public HistoricoReceita(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        alterarIcone();
    }

    private void alterarIcone() {
        URL url = this.getClass().getResource("/imagens/arquivo.png");
        Image iconeTitulo = Toolkit.getDefaultToolkit().getImage(url);
        this.setIconImage(iconeTitulo);
        this.setLocationRelativeTo(null);
        new util.ButtonColumn(tabelaServicos, 3);
        util.JanelaComum.setAcessibilidade(this);
    }

    private void preencherTabela() {
        boolean i = validaDataInicial();
        boolean f = validaDataFinal();
        if (!i && !f) {
            campoDataInicial.setBorder(new LineBorder(Color.RED));
            campoDataFinal.setBorder(new LineBorder(Color.RED));
            rotuloDataObrigatorio.setVisible(true);
        } else {
            rotuloDataObrigatorio.setVisible(false);
        }
        List<entidades.Servico> servicos = preencherServicos();
        java.util.List<entidades.Servico> s = new java.util.ArrayList<>();
        for (int j = 0; j < servicos.size(); j++) {
            entidades.Servico servico = servicos.get(j);
            java.util.Date data = servico.getDtServicos();
            java.util.Date inicial = null;
            java.util.Date finale = null;
            try {
                inicial = util.DateParse.parseDate(campoDataInicial.getText());
            } catch (ParseException e) {
            }
            try {
                finale = util.DateParse.parseDate(campoDataFinal.getText());
            } catch (ParseException e) {
            }
            if (i && !f) {
                if (util.DateParse.equalsDate(data, inicial)
                        || util.DateParse.biggerDate(inicial, data)) {
                    s.add(servico);
                }
            } else if (f && !i) {
                if (util.DateParse.equalsDate(data, finale)
                        || util.DateParse.biggerDate(data, finale)) {
                    s.add(servico);
                }
            } else if (i && f) {
                if (util.DateParse.equalsDate(data, inicial)
                        || util.DateParse.equalsDate(data, finale)
                        || (util.DateParse.biggerDate(inicial, data) && util.DateParse.biggerDate(data, finale))) {
                    s.add(servico);
                }
            }
        }
        preencher(s);
    }

    private List<entidades.Servico> preencherServicos() {
        List<entidades.Servico> servicos = null;
        try {
            servicos = dados.ClienteDados.consultarServicos();
        } catch (dados.DadosException e) {
        }
        return servicos;
    }

    private void focoDataFinal() {
        campoDataFinal.setBorder(new LineBorder(Color.BLUE));
        if (campoDataFinal.getText().equals("dd/mm/aaaa")) {
            campoDataFinal.setForeground(Color.BLACK);
            campoDataFinal.setText("");
            try {
                MaskFormatter mascaraData = new MaskFormatter("##/##/####");
                mascaraData.setPlaceholderCharacter('_');
                campoDataFinal.setFormatterFactory(new DefaultFormatterFactory(mascaraData));
                campoDataFinal.setText("");
            } catch (ParseException e) {
            }
        }
    }

    private boolean validaDataFinal() {
        try {
            DateParse.parseDate(campoDataFinal.getText());
            campoDataFinal.setBorder(new LineBorder(Color.GRAY));
            return true;
        } catch (ParseException e) {
            campoDataFinal.setBorder(new LineBorder(Color.GRAY));
            return false;
        }
    }

    private void focoDataInicial() {
        campoDataInicial.setBorder(new LineBorder(Color.BLUE));
        if (campoDataInicial.getText().equals("dd/mm/aaaa")) {
            campoDataInicial.setForeground(Color.BLACK);
            campoDataInicial.setText("");
            try {
                MaskFormatter mascaraData = new MaskFormatter("##/##/####");
                mascaraData.setPlaceholderCharacter('_');
                campoDataInicial.setFormatterFactory(new DefaultFormatterFactory(mascaraData));
                campoDataInicial.setText("");
            } catch (ParseException e) {
            }
        }
    }

    private boolean validaDataInicial() {
        try {
            DateParse.parseDate(campoDataInicial.getText());
            campoDataInicial.setBorder(new LineBorder(Color.GRAY));
            return true;
        } catch (ParseException e) {
            campoDataInicial.setBorder(new LineBorder(Color.GRAY));
            return false;
        }
    }

    private void preencher(java.util.List<entidades.Servico> servicos) {
        String[] titulo = new String[]{"Data", "Horário", "Serviço", "Valor", ""};
        String[][] linhas = new String[servicos.size()][4];
        for (int posicao = 0; posicao < servicos.size(); posicao++) {
            entidades.Servico servico = servicos.get(posicao);
            linhas[posicao][0] = util.DateParse.dateToString(servico.getDtServicos());
            linhas[posicao][1] = servico.getNmServico();
            linhas[posicao][2] = servico.getValores() + "";
            linhas[posicao][3] = null;
        }
        tabelaServicos.setModel(new javax.swing.table.DefaultTableModel(linhas, titulo) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });
        tabelaServicos.getColumnModel().getColumn(0).setPreferredWidth(120);
        tabelaServicos.getColumnModel().getColumn(1).setPreferredWidth(240);
        tabelaServicos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabelaServicos.getColumnModel().getColumn(3).setPreferredWidth(50);
        new util.ButtonColumn(tabelaServicos, 3);
    }

    private void ativarExibir() {
        boolean i = validaDataInicial();
        boolean f = validaDataFinal();

        if (i || f) {
            botaoExibir.setEnabled(true);
        } else {
            botaoExibir.setEnabled(false);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Imprimir gráfico">
    private void criarGrafico() {
        List<Date> datas = new ArrayList<>();
        TableModel modeloTabela = tabelaServicos.getModel();
        for (int linha = 0; linha < tabelaServicos.getRowCount(); linha++) {
            try {
                datas.add(DateParse.parseDate(modeloTabela.getValueAt(linha, 0).toString()));
            } catch (ParseException ex) {
                Problema erroDeConexao = new Problema(new javax.swing.JFrame(), true, ex.getMessage());
                erroDeConexao.setVisible(true);
            }
        }
        List<Double> diaAno = new ArrayList<>();
        diaAno.add(0.0);
        diaAno.add(0.0);
        diaAno.add(0.0);
        diaAno.add(0.0);
        diaAno.add(0.0);
        diaAno.add(0.0);
        diaAno.add(0.0);
        diaAno.add(0.0);
        diaAno.add(0.0);
        diaAno.add(0.0);
        diaAno.add(0.0);
        diaAno.add(0.0);
        for (int i = 0; i < datas.size(); i++) {
            int j = datas.get(i).getMonth();
            diaAno.set(j, diaAno.get(j) + Double.parseDouble(modeloTabela.getValueAt(i, 2).toString().replace(",", ".")));
        }
        DefaultCategoryDataset barra = new DefaultCategoryDataset();
        if (diaAno.get(0) != 0) {
            barra.setValue(diaAno.get(0), "1", "Janeiro/2018");
        }
        if (diaAno.get(1) != 0) {
            barra.setValue(diaAno.get(1), "2", "Fevereiro/2018");
        }
        if (diaAno.get(2) != 0) {
            barra.setValue(diaAno.get(2), "3", "Março/2018");
        }
        if (diaAno.get(3) != 0) {
            barra.setValue(diaAno.get(3), "4", "Abril/2018");
        }
        if (diaAno.get(4) != 0) {
            barra.setValue(diaAno.get(4), "5", "Maio/2018");
        }
        if (diaAno.get(5) != 0) {
            barra.setValue(diaAno.get(5), "6", "Junho/2018");
        }
        if (diaAno.get(6) != 0) {
            barra.setValue(diaAno.get(6), "7", "Julho/2018");
        }
        if (diaAno.get(7) != 0) {
            barra.setValue(diaAno.get(7), "8", "Agosto/2018");
        }
        if (diaAno.get(8) != 0) {
            barra.setValue(diaAno.get(8), "9", "Setembro/2018");
        }
        if (diaAno.get(9) != 0) {
            barra.setValue(diaAno.get(9), "10", "Outubro/2018");
        }
        if (diaAno.get(10) != 0) {
            barra.setValue(diaAno.get(10), "11", "Novembro/2018");
        }
        if (diaAno.get(11) != 0) {
            barra.setValue(diaAno.get(11), "12", "Dezembro/2018");
        }
        org.jfree.data.category.CategoryDataset dataSet = barra;
        org.jfree.chart.JFreeChart grafico = ChartFactory.createBarChart("De " + campoDataInicial.getText() + " a " + campoDataFinal.getText(), "Mês", "Receita (R$)", dataSet, PlotOrientation.HORIZONTAL, false, true, true);
        org.jfree.chart.ChartPanel painelGrafico = new org.jfree.chart.ChartPanel(grafico);
        CategoryPlot plot = grafico.getCategoryPlot();
        plot.setBackgroundPaint(new Color(240, 240, 240));
        plot.setDomainGridlinePaint(new Color(240, 240, 240));
        plot.setRangeGridlinePaint(new Color(240, 240, 240));
        plot.setOutlineVisible(false);
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        renderer.setGradientPaintTransformer(null);
        renderer.setBarPainter(new StandardBarPainter());
        int barras = 0;
        for(int i = 0; i < diaAno.size(); i++) {
            if(diaAno.get(i) != 0) {
                barras++; 
            }
        }
        if(barras >=8) {
            renderer.setItemMargin(-5);
        } else if (barras < 8 && barras > 4) {
            renderer.setItemMargin(-2);
        }
        Paint[] colors = {
            new Color(139, 71, 137),
            new Color(139, 71, 137),
            new Color(139, 71, 137),
            new Color(139, 71, 137),
            new Color(139, 71, 137),
            new Color(139, 71, 137),
            new Color(139, 71, 137),
            new Color(139, 71, 137),
            new Color(139, 71, 137),
            new Color(139, 71, 137),
            new Color(139, 71, 137),
            new Color(139, 71, 137)
        };
        for (int i = 0; i < 13; i++) {
            renderer.setSeriesPaint(i, colors[i % colors.length]);
        }

        this.painelBarras.setLayout(new java.awt.BorderLayout());
        this.painelBarras.add(painelGrafico);
        pack();
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Imprimir gráfico v0.1">
    /*
    public void criarGrafico() {
        List<Date> datas = new ArrayList<>();
        TableModel modeloTabela = tabelaServicos.getModel();
        for (int linha = 0; linha < tabelaServicos.getRowCount(); linha++) {
            try {
                datas.add(DateParse.parseDate(modeloTabela.getValueAt(linha, 0).toString()));
            } catch (ParseException ex) {
                Problema erroDeConexao = new Problema(new javax.swing.JFrame(), true, ex.getMessage());
                erroDeConexao.setVisible(true);
            }
        }
        List<Integer> diaAno = new ArrayList<>();
        diaAno.add(0);
        diaAno.add(0);
        diaAno.add(0);
        diaAno.add(0);
        diaAno.add(0);
        diaAno.add(0);
        diaAno.add(0);
        diaAno.add(0);
        diaAno.add(0);
        for (int i = 0; i < datas.size(); i++) {
            switch (datas.get(i).getMonth()) {
                case 1:
                    diaAno.set(0, diaAno.get(0) + 1);
                    break;
                case 2:
                    diaAno.set(1, diaAno.get(1) + 1);
                    break;
                case 3:
                    diaAno.set(2, diaAno.get(2) + 1);
                    break;
                case 4:
                    diaAno.set(3, diaAno.get(3) + 1);
                    break;
                case 5:
                    diaAno.set(4, diaAno.get(4) + 1);
                    break;
                case 6:
                    diaAno.set(5, diaAno.get(5) + 1);
                    break;
                case 7:
                    diaAno.set(6, diaAno.get(6) + 1);
                    break;
                case 8:
                    diaAno.set(7, diaAno.get(7) + 1);
                    break;
                case 9:
                    diaAno.set(8, diaAno.get(8) + 1);
                    break;
            }
        }
        DefaultCategoryDataset barra = new DefaultCategoryDataset();
        if (diaAno.get(0) != 0) {
            barra.setValue(diaAno.get(0), "Janeiro", "Janeiro");
        }
        if (diaAno.get(1) != 0) {
            barra.setValue(diaAno.get(1), "Fevereiro", "Fevereiro");
        }
        if (diaAno.get(2) != 0) {
            barra.setValue(diaAno.get(2), "Março", "Março");
        }
        if (diaAno.get(3) != 0) {
            barra.setValue(diaAno.get(3), "Abril", "Abril");
        }
        if (diaAno.get(4) != 0) {
            barra.setValue(diaAno.get(4), "Maio", "Maio");
        }
        if (diaAno.get(5) != 0) {
            barra.setValue(diaAno.get(5), "Junho", "Junho");
        }
        if (diaAno.get(6) != 0) {
            barra.setValue(diaAno.get(6), "Julho", "Julho");
        }
        if (diaAno.get(7) != 0) {
            barra.setValue(diaAno.get(7), "Agosto", "Agosto");
        }
        if (diaAno.get(8) != 0) {
            barra.setValue(diaAno.get(8), "Setembro", "Setembro");
        }
        org.jfree.data.category.CategoryDataset dataSet = barra;
        org.jfree.chart.JFreeChart grafico = ChartFactory.createBarChart3D("Relatório de Rendimentos", "Mês", "Nº Atendimentos", dataSet, PlotOrientation.VERTICAL, false, false, true);
        org.jfree.chart.ChartPanel painelGrafico = new org.jfree.chart.ChartPanel(grafico);
        painelGrafico.setPreferredSize(new java.awt.Dimension(900, 600));
        Grafico graficos = new Grafico(null, true, painelGrafico);
        graficos.setVisible(true);
    }*/// </editor-fold>
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        painelTitulo = new javax.swing.JPanel();
        rotuloTitulo = new javax.swing.JLabel();
        painelBotoes = new javax.swing.JPanel();
        botaoImprimir = new javax.swing.JButton();
        botaoFechar = new javax.swing.JButton();
        painelMeio = new javax.swing.JLayeredPane();
        campoDataInicial = new javax.swing.JFormattedTextField();
        campoDataFinal = new javax.swing.JFormattedTextField();
        rotuloDataInicial = new javax.swing.JLabel();
        rotuloDataFinal = new javax.swing.JLabel();
        seletorDeDataInicial = new com.toedter.calendar.JDateChooser();
        seletorDeDataFinal = new com.toedter.calendar.JDateChooser();
        botaoExibir = new javax.swing.JButton();
        rotuloDataObrigatorio = new javax.swing.JLabel();
        painelBarras = new javax.swing.JPanel();
        painelTabelaServicos = new javax.swing.JScrollPane();
        tabelaServicos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Relatórios");
        setMaximumSize(new java.awt.Dimension(525, 1080));
        setMinimumSize(new java.awt.Dimension(525, 225));
        setPreferredSize(new java.awt.Dimension(730, 635));
        setResizable(false);

        painelTitulo.setBackground(java.awt.Color.black);

        rotuloTitulo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        rotuloTitulo.setForeground(new java.awt.Color(255, 255, 255));
        rotuloTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/relatorioGrafico.png"))); // NOI18N
        rotuloTitulo.setText("RENDIMENTOS POR PERÍODO");
        painelTitulo.add(rotuloTitulo);

        getContentPane().add(painelTitulo, java.awt.BorderLayout.NORTH);

        painelBotoes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

        botaoImprimir.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/imprimir.png"))); // NOI18N
        botaoImprimir.setMnemonic('i');
        botaoImprimir.setText("Imprimir");
        botaoImprimir.setToolTipText("Preparar gráfico para impressão (Alt+I)");
        botaoImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoImprimir.setEnabled(false);
        botaoImprimir.setPreferredSize(new java.awt.Dimension(130, 35));
        botaoImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoImprimirActionPerformed(evt);
            }
        });
        painelBotoes.add(botaoImprimir);

        botaoFechar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/fechar.png"))); // NOI18N
        botaoFechar.setMnemonic('f');
        botaoFechar.setText("Fechar");
        botaoFechar.setToolTipText("Fechar esta janela (Alt+F)");
        botaoFechar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoFechar.setPreferredSize(new java.awt.Dimension(130, 35));
        botaoFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoFecharActionPerformed(evt);
            }
        });
        painelBotoes.add(botaoFechar);
        botaoFechar.setOpaque(true);

        getContentPane().add(painelBotoes, java.awt.BorderLayout.SOUTH);

        campoDataInicial.setForeground(java.awt.Color.gray);
        campoDataInicial.setText("dd/mm/aaaa");
        campoDataInicial.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campoDataInicial.setPreferredSize(null);
        campoDataInicial.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoDataInicialFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                campoDataInicialFocusLost(evt);
            }
        });
        campoDataInicial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoDataInicialKeyReleased(evt);
            }
        });
        painelMeio.add(campoDataInicial);
        campoDataInicial.setBounds(10, 20, 130, 30);

        campoDataFinal.setForeground(java.awt.Color.gray);
        campoDataFinal.setText("dd/mm/aaaa");
        campoDataFinal.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campoDataFinal.setPreferredSize(null);
        campoDataFinal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoDataFinalFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                campoDataFinalFocusLost(evt);
            }
        });
        campoDataFinal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoDataFinalKeyReleased(evt);
            }
        });
        painelMeio.add(campoDataFinal);
        campoDataFinal.setBounds(170, 20, 130, 30);

        rotuloDataInicial.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloDataInicial.setText("Data Inicial:");
        painelMeio.add(rotuloDataInicial);
        rotuloDataInicial.setBounds(10, 0, 73, 25);

        rotuloDataFinal.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloDataFinal.setText("Data Final:");
        painelMeio.add(rotuloDataFinal);
        rotuloDataFinal.setBounds(170, 0, 69, 25);

        seletorDeDataInicial.setToolTipText("Selecionar data");
        seletorDeDataInicial.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        seletorDeDataInicial.setMinSelectableDate(new java.util.Date(-62135755095000L));
        seletorDeDataInicial.setMinimumSize(new java.awt.Dimension(27, 10));
        seletorDeDataInicial.setPreferredSize(new java.awt.Dimension(87, 15));
        seletorDeDataInicial.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                seletorDeDataInicialPropertyChange(evt);
            }
        });
        painelMeio.add(seletorDeDataInicial);
        seletorDeDataInicial.setBounds(10, 20, 149, 30);
        seletorDeDataInicial.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        seletorDeDataFinal.setToolTipText("Selecionar data");
        seletorDeDataFinal.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        seletorDeDataFinal.setMinSelectableDate(new java.util.Date(-62135755095000L));
        seletorDeDataFinal.setMinimumSize(new java.awt.Dimension(27, 10));
        seletorDeDataFinal.setPreferredSize(new java.awt.Dimension(87, 15));
        seletorDeDataFinal.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                seletorDeDataFinalPropertyChange(evt);
            }
        });
        seletorDeDataFinal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                seletorDeDataFinalKeyReleased(evt);
            }
        });
        painelMeio.add(seletorDeDataFinal);
        seletorDeDataFinal.setBounds(170, 20, 149, 30);
        seletorDeDataFinal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        botaoExibir.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoExibir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/filtro.png"))); // NOI18N
        botaoExibir.setMnemonic('e');
        botaoExibir.setText("Exibir");
        botaoExibir.setToolTipText("Gerar gráfico de acordo com o período escolhido (Alt+E)");
        botaoExibir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoExibir.setEnabled(false);
        botaoExibir.setMaximumSize(new java.awt.Dimension(89, 25));
        botaoExibir.setMinimumSize(new java.awt.Dimension(89, 25));
        botaoExibir.setPreferredSize(new java.awt.Dimension(130, 35));
        botaoExibir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoExibirActionPerformed(evt);
            }
        });
        painelMeio.add(botaoExibir);
        botaoExibir.setBounds(330, 15, 110, 35);
        botaoExibir.setOpaque(true);

        rotuloDataObrigatorio.setForeground(new java.awt.Color(204, 0, 0));
        rotuloDataObrigatorio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloDataObrigatorio.setText("Preencha pelo menos uma data válida");
        painelMeio.add(rotuloDataObrigatorio);
        rotuloDataObrigatorio.setBounds(10, 50, 310, 15);
        rotuloDataObrigatorio.setVisible(false);

        painelBarras.setMinimumSize(new java.awt.Dimension(700, 400));
        painelBarras.setPreferredSize(new java.awt.Dimension(700, 400));
        painelBarras.setLayout(new java.awt.BorderLayout());
        painelMeio.add(painelBarras);
        painelBarras.setBounds(10, 70, 700, 420);

        tabelaServicos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tabelaServicos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"30/01/2018", "Corte", "99,00", null},
                {"31/01/2018", "Maquiagem", "89,00", null},
                {"14/02/2018", "Manicure", "79,00", null},
                {"14/02/2018", "Pedicure", "99,00", null},
                {"26/03/2018", "Matização", "89,00", null},
                {"28/03/2018", "Corte", "79,00", null},
                {"30/03/2018", "Maquiagem", "99,00", null},
                {"31/03/2018", "Manicure", "89,00", null},
                {"11/04/2018", "Pedicure", "79,00", null},
                {"05/05/2018", "Matização", "99,00", null},
                {"07/05/2018", "Corte", "89,00", null},
                {"16/05/2018", "Maquigem", "79,00", null},
                {"25/05/2018", "Manicure", "99,00", null},
                {"29/05/2018", "Pedicure", "89,00", null},
                {"30/05/2018", "Matização", "79,00", null},
                {"07/06/2018", "Corte", "99,00", null},
                {"08/06/2018", "Maquiagem", "89,00", null},
                {"09/06/2018", "Manicure", "79,00", null},
                {"10/06/2018", "Pedicure", "99,00", null},
                {"11/06/2018", "Matização", "89,00", null},
                {"12/07/2018", "Corte", "79,00", null},
                {"18/08/2018", "Manicure", "99,00", null},
                {"24/08/2018", "Pedicure", "89,00", null},
                {"09/09/2018", "Maquigem", "79,00", null},
                {"17/09/2018", "Matização", "99,00", null}
            },
            new String [] {
                "Data", "Serviço", "Valor (R$)", ""
            }
        ));
        tabelaServicos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabelaServicos.setRowHeight(30);
        tabelaServicos.setShowVerticalLines(false);
        painelTabelaServicos.setViewportView(tabelaServicos);
        tabelaServicos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"30/01/2018", "Corte", "99,00", null},
                {"31/01/2018", "Maquiagem", "89,00", null},
                {"14/02/2018", "Manicure", "79,00", null},
                {"14/02/2018", "Pedicure", "99,00", null},
                {"26/03/2018", "Matização", "89,00", null},
                {"28/03/2018", "Corte", "79,00", null},
                {"30/03/2018", "Maquiagem", "99,00", null},
                {"31/03/2018", "Manicure", "89,00", null},
                {"11/04/2018", "Pedicure", "79,00", null},
                {"05/05/2018", "Matização", "99,00", null},
                {"07/05/2018", "Corte", "89,00", null},
                {"16/05/2018", "Maquigem", "79,00", null},
                {"25/05/2018", "Manicure", "99,00", null},
                {"29/05/2018", "Pedicure", "89,00", null},
                {"30/05/2018", "Matização", "79,00", null},
                {"07/06/2018", "Corte", "99,00", null},
                {"08/06/2018", "Maquiagem", "89,00", null},
                {"09/06/2018", "Manicure", "79,00", null},
                {"10/06/2018", "Pedicure", "99,00", null},
                {"11/06/2018", "Matização", "89,00", null},
                {"12/07/2018", "Corte", "79,00", null},
                {"18/08/2018", "Manicure", "99,00", null},
                {"24/08/2018", "Pedicure", "89,00", null},
                {"09/09/2018", "Maquigem", "79,00", null},
                {"17/09/2018", "Matização", "99,00", null}
            },
            new String [] {
                "Data", "Serviço", "Valor (R$)", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        });

        JTableHeader cabecalhoS = tabelaServicos.getTableHeader();
        cabecalhoS.setFont(new java.awt.Font("Arial", 0, 14));
        tabelaServicos.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabelaServicos.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelaServicos.getColumnModel().getColumn(3).setPreferredWidth(50);
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        direita.setHorizontalAlignment(SwingConstants.RIGHT);
        tabelaServicos.getColumnModel().getColumn(2).setCellRenderer(direita);

        painelMeio.add(painelTabelaServicos);
        painelTabelaServicos.setBounds(0, 580, 500, 20);
        painelTabelaServicos.setVisible(false);

        getContentPane().add(painelMeio, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoFecharActionPerformed
        dispose();
    }//GEN-LAST:event_botaoFecharActionPerformed

    private void botaoExibirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoExibirActionPerformed
        boolean i = validaDataInicial();
        boolean f = validaDataFinal();
        if (i || f) {
            rotuloDataObrigatorio.setVisible(false);
            campoDataInicial.setBorder(new LineBorder(Color.GRAY));
            campoDataFinal.setBorder(new LineBorder(Color.GRAY));
            preencherTabela();
            criarGrafico();
            botaoImprimir.setEnabled(true);
        } else {
            campoDataInicial.setBorder(new LineBorder(Color.RED));
            campoDataFinal.setBorder(new LineBorder(Color.RED));
            rotuloDataObrigatorio.setVisible(true);
        }
    }//GEN-LAST:event_botaoExibirActionPerformed

    private void seletorDeDataInicialPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_seletorDeDataInicialPropertyChange
        try {
            if (!util.DateParse.dateToString(seletorDeDataInicial.getDate()).equals("dd/MM/yyyy")) {
                focoDataInicial();
                campoDataInicial.setText(util.DateParse.dateToString(seletorDeDataInicial.getDate()));
                validaDataInicial();
            }
        } catch (NullPointerException e) {
        }
        ativarExibir();
    }//GEN-LAST:event_seletorDeDataInicialPropertyChange

    private void seletorDeDataFinalPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_seletorDeDataFinalPropertyChange
        try {
            if (!util.DateParse.dateToString(seletorDeDataFinal.getDate()).equals("dd/MM/yyyy")) {
                focoDataFinal();
                campoDataFinal.setText(util.DateParse.dateToString(seletorDeDataFinal.getDate()));
                validaDataFinal();
            }
        } catch (NullPointerException e) {
        }
    }//GEN-LAST:event_seletorDeDataFinalPropertyChange

    private void campoDataInicialFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoDataInicialFocusGained
        focoDataInicial();
    }//GEN-LAST:event_campoDataInicialFocusGained

    private void campoDataInicialFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoDataInicialFocusLost
        String data = campoDataInicial.getText();
        data = data.replace("/", "");
        data = data.replace("_", "");
        data = data.replace("d", "");
        data = data.replace("m", "");
        data = data.replace("a", "");
        if (data.equals("")) {
            campoDataInicial.setForeground(Color.GRAY);
            campoDataInicial.setFormatterFactory(null);
            campoDataInicial.setText("dd/mm/aaaa");
        }
        validaDataInicial();
    }//GEN-LAST:event_campoDataInicialFocusLost

    private void campoDataFinalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoDataFinalFocusGained
        focoDataFinal();
    }//GEN-LAST:event_campoDataFinalFocusGained

    private void campoDataFinalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoDataFinalFocusLost
        String data = campoDataFinal.getText();
        data = data.replace("/", "");
        data = data.replace("_", "");
        data = data.replace("d", "");
        data = data.replace("m", "");
        data = data.replace("a", "");
        if (data.equals("")) {
            campoDataFinal.setForeground(Color.GRAY);
            campoDataFinal.setFormatterFactory(null);
            campoDataFinal.setText("dd/mm/aaaa");
        }
        validaDataFinal();
    }//GEN-LAST:event_campoDataFinalFocusLost

    private void botaoImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoImprimirActionPerformed
        boolean i = validaDataInicial();
        boolean f = validaDataFinal();
        if (!i && !f) {
            campoDataInicial.setBorder(new LineBorder(Color.RED));
            campoDataFinal.setBorder(new LineBorder(Color.RED));
            rotuloDataObrigatorio.setVisible(true);
        } else {
            rotuloDataObrigatorio.setVisible(false);
            campoDataInicial.setBorder(new LineBorder(Color.GRAY));
            campoDataFinal.setBorder(new LineBorder(Color.GRAY));
        }
    }//GEN-LAST:event_botaoImprimirActionPerformed

    private void campoDataInicialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDataInicialKeyReleased
        ativarExibir();
    }//GEN-LAST:event_campoDataInicialKeyReleased

    private void campoDataFinalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDataFinalKeyReleased
        ativarExibir();
    }//GEN-LAST:event_campoDataFinalKeyReleased

    private void seletorDeDataFinalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_seletorDeDataFinalKeyReleased
        ativarExibir();
    }//GEN-LAST:event_seletorDeDataFinalKeyReleased

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
            java.util.logging.Logger.getLogger(HistoricoReceita.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            HistoricoReceita dialog = new HistoricoReceita(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoExibir;
    private javax.swing.JButton botaoFechar;
    private javax.swing.JButton botaoImprimir;
    private javax.swing.JFormattedTextField campoDataFinal;
    private javax.swing.JFormattedTextField campoDataInicial;
    private javax.swing.JPanel painelBarras;
    private javax.swing.JPanel painelBotoes;
    private javax.swing.JLayeredPane painelMeio;
    private javax.swing.JScrollPane painelTabelaServicos;
    private javax.swing.JPanel painelTitulo;
    private javax.swing.JLabel rotuloDataFinal;
    private javax.swing.JLabel rotuloDataInicial;
    private javax.swing.JLabel rotuloDataObrigatorio;
    private javax.swing.JLabel rotuloTitulo;
    private com.toedter.calendar.JDateChooser seletorDeDataFinal;
    private com.toedter.calendar.JDateChooser seletorDeDataInicial;
    private javax.swing.JTable tabelaServicos;
    // End of variables declaration//GEN-END:variables
}
