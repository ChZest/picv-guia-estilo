package interfaces;

import dados.ClienteDados;
import dados.DadosException;
import entidades.Cliente;
import entidades.Servico;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import util.DateParse;

/* @author BIC-Jr Guia de Estilo */
public class HistoricoAtendimento extends javax.swing.JDialog {

    /**
     * @param parent
     * @param modal
     */
    public HistoricoAtendimento(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        alterarIcone();
        preencherLista();
        preencherTabela();
    }

    private void alterarIcone() {
        URL url = this.getClass().getResource("/imagens/arquivo.png");
        Image iconeTitulo = Toolkit.getDefaultToolkit().getImage(url);
        this.setIconImage(iconeTitulo);
        this.setLocationRelativeTo(null);
        util.JanelaComum.setAcessibilidade(this);
    }

    private void preencherLista() {
        modeloCliente = new DefaultListModel();
        lista.setModel(modeloCliente);
        try {
            clientes = ClienteDados.consultarTodos();
            clientes.forEach((cliente) -> {
                modeloCliente.addElement(cliente.toString());
            });
        } catch (DadosException ex) {
            Problema erroDeConexao = new Problema(new javax.swing.JFrame(), true, ex.getMessage());
            erroDeConexao.setVisible(true);
        }
        painelLista.setVisible(false);
    }

    private void preencher(java.util.List<entidades.Servico> servicos) {
        String[] titulo = new String[]{"Data", "Serviço", "Profissional", "Valor", ""};
        String[][] linhas = new String[0][5];
        if (servicos != null) {
            linhas = new String[servicos.size()][5];
            for (int posicao = 0; posicao < servicos.size(); posicao++) {
                entidades.Servico servico = servicos.get(posicao);
                linhas[posicao][0] = util.DateParse.dateToString(servico.getDtServicos());
                linhas[posicao][1] = servico.getNmServico();
                linhas[posicao][2] = servico.getHorario();
                linhas[posicao][3] = "R$ " + servico.getValores() + "0";
                linhas[posicao][4] = null;
            }
        }
        tabelaServicos.setModel(new javax.swing.table.DefaultTableModel(linhas, titulo) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 4;
            }
        });
        tabelaServicos.getColumnModel().getColumn(0).setPreferredWidth(70);
        tabelaServicos.getColumnModel().getColumn(1).setPreferredWidth(100);
        tabelaServicos.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabelaServicos.getColumnModel().getColumn(3).setPreferredWidth(50);
        tabelaServicos.getColumnModel().getColumn(4).setPreferredWidth(10);
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        direita.setHorizontalAlignment(SwingConstants.RIGHT);
        tabelaServicos.getColumnModel().getColumn(3).setCellRenderer(direita);
        new util.ButtonColumn(tabelaServicos, 4) {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ServicoVisualizar servicoVisualizar = new ServicoVisualizar(
                        new javax.swing.JFrame(), true, servicos.get(tabelaServicos.getSelectedRow()).getDtServico());
                servicoVisualizar.setVisible(true);
                fireEditingStopped();
            }
        };
    }

    private void pesquisar() {
        try {
            modeloCliente.removeAllElements();
            clientes = ClienteDados.consultarNome(campoNome.getText());
            for (int i = 0; i < clientes.size(); i++) {
                modeloCliente.addElement(clientes.get(i).getNmCliente());
            }
            if (clientes.size() <= 6) {
                painelLista.setBounds(10, 65, 260, clientes.size() * 20 + 1);
            } else {
                painelLista.setBounds(10, 65, 260, 141);
            }
            painelLista.setVisible(true);
        } catch (DadosException e) {
            Problema erroDeConexao = new Problema(new javax.swing.JFrame(), true, e.getMessage());
            erroDeConexao.setVisible(true);
        }
    }

    private void preencherTabela() {
        this.setLocationRelativeTo(null);
        new util.ButtonColumn(tabelaServicos, 2) {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ServicoVisualizar servicoVisualizar = new ServicoVisualizar(
                        new javax.swing.JFrame(), true, "Lorem");
                servicoVisualizar.setVisible(true);
                fireEditingStopped();
            }
        };
    }

    private boolean validaCliente() {
        if (!selecao) {
            if (adt) {
                campoNome.setBorder(new LineBorder(Color.RED));
                rotuloClienteObrigatorio.setVisible(true);
            }
            return false;
        } else {
            campoNome.setBorder(new LineBorder(Color.GRAY));
            rotuloClienteObrigatorio.setVisible(false);
            return true;
        }
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

    private List<entidades.Servico> preencherServicos() {
        List<entidades.Servico> servicos = null;
        try {
            servicos = ClienteDados.consultarServicos();
        } catch (dados.DadosException e) {
        }
        return servicos;
    }

    private void ativarExibir() {
        boolean c = validaCliente();
        boolean i = validaDataInicial();
        boolean f = validaDataFinal();

        if (c && (i || f)) {
            botaoExibir.setEnabled(true);
        } else {
            botaoExibir.setEnabled(false);
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

        painelTitulo = new javax.swing.JPanel();
        rotuloTitulo = new javax.swing.JLabel();
        painelBotoes = new javax.swing.JPanel();
        rotuloCarregar = new javax.swing.JLabel();
        botaoImprimir = new javax.swing.JButton();
        botaoFechar = new javax.swing.JButton();
        painelMeio = new javax.swing.JLayeredPane();
        rotuloClienteObrigatorio = new javax.swing.JLabel();
        campoDataInicial = new javax.swing.JFormattedTextField();
        campoDataFinal = new javax.swing.JFormattedTextField();
        botaoCombo = new javax.swing.JButton();
        campoNome = new javax.swing.JTextField();
        painelLista = new javax.swing.JScrollPane();
        lista = new javax.swing.JList<>();
        rotuloCliente = new javax.swing.JLabel();
        botaoPesquisar = new javax.swing.JButton();
        painelFormulario = new javax.swing.JPanel();
        seletorDeDataInicial = new com.toedter.calendar.JDateChooser();
        rotuloDataInicial = new javax.swing.JLabel();
        seletorDeDataFinal = new com.toedter.calendar.JDateChooser();
        rotuloDataFinal = new javax.swing.JLabel();
        painelTabelaServicos = new javax.swing.JScrollPane();
        tabelaServicos = new javax.swing.JTable();
        botaoExibir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Relatórios");
        setMaximumSize(new java.awt.Dimension(525, 1080));
        setMinimumSize(new java.awt.Dimension(525, 540));
        setPreferredSize(new java.awt.Dimension(525, 540));
        setResizable(false);

        painelTitulo.setBackground(java.awt.Color.black);

        rotuloTitulo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        rotuloTitulo.setForeground(new java.awt.Color(255, 255, 255));
        rotuloTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/arquivo.png"))); // NOI18N
        rotuloTitulo.setText("HISTÓRICO DE ATENDIMENTOS");
        painelTitulo.add(rotuloTitulo);

        getContentPane().add(painelTitulo, java.awt.BorderLayout.NORTH);

        painelBotoes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

        rotuloCarregar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloCarregar.setText("Carregando relatório...");
        painelBotoes.add(rotuloCarregar);
        rotuloCarregar.setVisible(false);

        botaoImprimir.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/imprimir.png"))); // NOI18N
        botaoImprimir.setMnemonic('g');
        botaoImprimir.setText("Gerar Relatório");
        botaoImprimir.setToolTipText("Gerar relatório referente aos serviços exibidos (Alt+G)");
        botaoImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoImprimir.setEnabled(false);
        botaoImprimir.setMaximumSize(new java.awt.Dimension(89, 25));
        botaoImprimir.setMinimumSize(new java.awt.Dimension(89, 25));
        botaoImprimir.setPreferredSize(new java.awt.Dimension(165, 35));
        botaoImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoImprimirActionPerformed(evt);
            }
        });
        painelBotoes.add(botaoImprimir);
        botaoImprimir.setOpaque(true);

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

        rotuloClienteObrigatorio.setForeground(new java.awt.Color(204, 0, 0));
        rotuloClienteObrigatorio.setText("Selecione um cliente");
        painelMeio.add(rotuloClienteObrigatorio);
        rotuloClienteObrigatorio.setBounds(10, 65, 260, 15);
        rotuloClienteObrigatorio.setVisible(false);

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
        campoDataInicial.setBounds(10, 100, 128, 30);

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
        campoDataFinal.setBounds(170, 100, 128, 30);

        botaoCombo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/seta.png"))); // NOI18N
        botaoCombo.setContentAreaFilled(false);
        botaoCombo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoCombo.setFocusable(false);
        botaoCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoComboActionPerformed(evt);
            }
        });
        painelMeio.add(botaoCombo);
        botaoCombo.setBounds(235, 35, 35, 31);

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
        painelMeio.add(campoNome);
        campoNome.setBounds(10, 35, 260, 30);

        lista.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lista.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lista.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listaMousePressed(evt);
            }
        });
        painelLista.setViewportView(lista);

        painelMeio.setLayer(painelLista, javax.swing.JLayeredPane.POPUP_LAYER);
        painelMeio.add(painelLista);
        painelLista.setBounds(10, 65, 260, 30);
        painelLista.setVisible(false);

        rotuloCliente.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloCliente.setText("Cliente:*");
        painelMeio.add(rotuloCliente);
        rotuloCliente.setBounds(10, 10, 53, 25);

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
        painelMeio.add(botaoPesquisar);
        botaoPesquisar.setBounds(280, 35, 35, 31);

        painelFormulario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        painelFormulario.add(seletorDeDataInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 149, 30));
        seletorDeDataInicial.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        rotuloDataInicial.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloDataInicial.setText("Data Inicial:");
        painelFormulario.add(rotuloDataInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, -1, 25));

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
        painelFormulario.add(seletorDeDataFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 149, 30));
        seletorDeDataFinal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        rotuloDataFinal.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloDataFinal.setText("Data Final:");
        painelFormulario.add(rotuloDataFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 15, -1, 25));

        tabelaServicos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tabelaServicos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"30/01/2018", "Corte", null},
                {"31/01/2018", "Maquiagem", null},
                {"14/02/2018", "Manicure", null},
                {"14/02/2018", "Pedicure", null},
                {"26/03/2018", "Matização", null},
                {"28/03/2018", "Corte", null},
                {"30/03/2018", "Maquiagem", null},
                {"31/03/2018", "Manicure", null},
                {"11/04/2018", "Pedicure", null},
                {"05/05/2018", "Matização", null},
                {"07/05/2018", "Corte", null},
                {"16/05/2018", "Maquigem", null},
                {"25/05/2018", "Manicure", null},
                {"29/05/2018", "Pedicure", null},
                {"30/05/2018", "Matização", null},
                {"07/06/2018", "Corte", null},
                {"08/06/2018", "Maquiagem", null},
                {"09/06/2018", "Manicure", null},
                {"10/06/2018", "Pedicure", null},
                {"11/06/2018", "Matização", null},
                {"12/07/2018", "Corte", null},
                {"18/08/2018", "Manicure", null},
                {"24/08/2018", "Pedicure", null},
                {"09/09/2018", "Maquigem", null},
                {"17/09/2018", null, null}
            },
            new String [] {
                "Data", "Serviço", ""
            }
        ));
        tabelaServicos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabelaServicos.setRowHeight(30);
        tabelaServicos.setShowVerticalLines(false);
        painelTabelaServicos.setViewportView(tabelaServicos);
        tabelaServicos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Data", "Serviço", "Profissional", "Valor", ""
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
        tabelaServicos.getColumnModel().getColumn(0).setPreferredWidth(70);
        tabelaServicos.getColumnModel().getColumn(1).setPreferredWidth(50);
        tabelaServicos.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelaServicos.getColumnModel().getColumn(3).setPreferredWidth(50);
        tabelaServicos.getColumnModel().getColumn(4).setPreferredWidth(10);

        painelFormulario.add(painelTabelaServicos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 500, 250));

        botaoExibir.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoExibir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/filtro.png"))); // NOI18N
        botaoExibir.setMnemonic('e');
        botaoExibir.setText("Exibir");
        botaoExibir.setToolTipText("Mostrar serviços do cliente selecionado no intervalo escolhido (Alt+E)");
        botaoExibir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoExibir.setEnabled(false);
        botaoExibir.setPreferredSize(new java.awt.Dimension(100, 32));
        botaoExibir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoExibirActionPerformed(evt);
            }
        });
        painelFormulario.add(botaoExibir, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 35, 110, 35));

        painelMeio.add(painelFormulario);
        painelFormulario.setBounds(0, 60, 520, 420);

        getContentPane().add(painelMeio, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoPesquisarActionPerformed
        ClientePesquisar clientePesquisar = new ClientePesquisar(null, true);
        clientePesquisar.setVisible(true);
        Cliente cliente = clientePesquisar.getCliente();
        if (cliente != null) {
            campoNome.setText(cliente.getNmCliente());
            preencher(null);
            selecao = true;
        }
        ativarExibir();
    }//GEN-LAST:event_botaoPesquisarActionPerformed

    private void botaoFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoFecharActionPerformed
        dispose();
    }//GEN-LAST:event_botaoFecharActionPerformed

    private void campoNomeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoNomeFocusLost
        painelLista.setVisible(false);
        if (adt) {
            validaCliente();
        } else {
            campoNome.setBorder(new LineBorder(Color.GRAY));
        }
    }//GEN-LAST:event_campoNomeFocusLost

    private void campoNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNomeKeyReleased
        selecao = false;
        pesquisar();
    }//GEN-LAST:event_campoNomeKeyReleased

    private void listaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaMousePressed
//        campoNome.setBorder(new LineBorder(Color.GRAY));
        campoNome.setText(lista.getSelectedValue());
        selecao = true;
        preencher(null);
        ativarExibir();
//        painelLista.setVisible(false);
    }//GEN-LAST:event_listaMousePressed

    private void campoNomeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoNomeFocusGained
        campoNome.setBorder(new LineBorder(Color.BLUE));
//        if (!selecao) {
//            painelLista.setVisible(true);
//        } else {
//            painelLista.setVisible(false);
//        }
    }//GEN-LAST:event_campoNomeFocusGained

    private void botaoImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoImprimirActionPerformed
        rotuloCarregar.setVisible(true);
        botaoImprimir.setEnabled(false);
        adt = true;
        boolean i = validaDataInicial();
        boolean f = validaDataFinal();
        if (!i && !f) {
            campoDataInicial.setBorder(new LineBorder(Color.RED));
            campoDataFinal.setBorder(new LineBorder(Color.RED));
        } else {
            campoDataInicial.setBorder(new LineBorder(Color.GRAY));
            campoDataFinal.setBorder(new LineBorder(Color.GRAY));
        }
        if ((i || f) && selecao && exibir) {
            String cabecalho = "";
            if (i && f) {
                cabecalho = "Cliente: " + campoNome.getText()
                        + " — Dara Inicial: " + campoDataInicial.getText() + " — Data final: " + campoDataFinal.getText();
            } else if (i && !f) {
                cabecalho = "Cliente: " + campoNome.getText() + " — Dara Inicial: " + campoDataInicial.getText();
            } else if (!i && f) {
                cabecalho = "Cliente: " + campoNome.getText() + " — Data final: " + campoDataFinal.getText();
            }
            util.RelatorioAtendimento.emitirRelatorio(ss, "Serviços.jasper", cabecalho);
            rotuloCarregar.setVisible(false);
            botaoImprimir.setEnabled(true);
        } else {
            validaCliente();
        }
    }//GEN-LAST:event_botaoImprimirActionPerformed

    private void botaoExibirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoExibirActionPerformed
        adt = true;
        boolean i = validaDataInicial();
        boolean f = validaDataFinal();
        if (validaCliente() && (i || f)) {
            List<entidades.Servico> servicos = preencherServicos();
            java.util.List<entidades.Servico> s = new java.util.ArrayList<>();
            for (int j = 0; j < servicos.size(); j++) {
                entidades.Servico servico = servicos.get(j);
                if (!i && !f) {
                    s.add(servico);
                } else {
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
                    if (i && !f && selecao) {
                        if (util.DateParse.equalsDate(data, inicial)
                                || util.DateParse.biggerDate(inicial, data)) {
                            s.add(servico);
                        }
                    } else if (f && !i && selecao) {
                        if (util.DateParse.equalsDate(data, finale)
                                || util.DateParse.biggerDate(data, finale)) {
                            s.add(servico);
                        }
                    } else if (i && f && selecao) {
                        if (util.DateParse.equalsDate(data, inicial)
                                || util.DateParse.equalsDate(data, finale)
                                || (util.DateParse.biggerDate(inicial, data) && util.DateParse.biggerDate(data, finale))) {
                            s.add(servico);
                        }
                    }
                }
            }
            s.forEach((item) -> {
                String datas = util.DateParse.dateToString(item.getDtServicos());
                String profissionais = "R$" + item.getValores() + "0";
                ss.add(new Servico(item.getNmServico(), item.getHorario(), datas, profissionais));
            });
            preencher(s);
            exibir = true;
            if (tabelaServicos.getRowCount() != 0) {
                botaoImprimir.setEnabled(true);
            }
        }
    }//GEN-LAST:event_botaoExibirActionPerformed

    private void botaoComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoComboActionPerformed
        if (!painelLista.isVisible()) {
            painelLista.setVisible(true);
            campoNome.requestFocusInWindow();
            pesquisar();
        } else {
            painelLista.setVisible(false);
        }
    }//GEN-LAST:event_botaoComboActionPerformed

    private void campoNomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoNomeMouseClicked
        pesquisar();
        painelLista.setVisible(true);
    }//GEN-LAST:event_campoNomeMouseClicked

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

    private void seletorDeDataFinalPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_seletorDeDataFinalPropertyChange
        try {
            if (!util.DateParse.dateToString(seletorDeDataFinal.getDate()).equals("dd/MM/yyyy")) {
                focoDataFinal();
                campoDataFinal.setText(util.DateParse.dateToString(seletorDeDataFinal.getDate()));
                validaDataFinal();
            }
        } catch (NullPointerException e) {
        }
        ativarExibir();
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

    private void campoDataInicialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDataInicialKeyReleased
        ativarExibir();
    }//GEN-LAST:event_campoDataInicialKeyReleased

    private void campoDataFinalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDataFinalKeyReleased
        ativarExibir();
    }//GEN-LAST:event_campoDataFinalKeyReleased

    private void campoNomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNomeKeyTyped
        util.JanelaComum.campoTexto(evt);
    }//GEN-LAST:event_campoNomeKeyTyped

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
            java.util.logging.Logger.getLogger(HistoricoAtendimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            HistoricoAtendimento dialog = new HistoricoAtendimento(new javax.swing.JFrame(), true);
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
    private boolean selecao = false;
    java.util.List<entidades.Servico> ss = new java.util.ArrayList<>();
    private boolean adt = false;
    private boolean exibir = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoCombo;
    private javax.swing.JButton botaoExibir;
    private javax.swing.JButton botaoFechar;
    private javax.swing.JButton botaoImprimir;
    private javax.swing.JButton botaoPesquisar;
    private javax.swing.JFormattedTextField campoDataFinal;
    private javax.swing.JFormattedTextField campoDataInicial;
    private javax.swing.JTextField campoNome;
    private javax.swing.JList<String> lista;
    private javax.swing.JPanel painelBotoes;
    private javax.swing.JPanel painelFormulario;
    private javax.swing.JScrollPane painelLista;
    private javax.swing.JLayeredPane painelMeio;
    private javax.swing.JScrollPane painelTabelaServicos;
    private javax.swing.JPanel painelTitulo;
    private javax.swing.JLabel rotuloCarregar;
    private javax.swing.JLabel rotuloCliente;
    private javax.swing.JLabel rotuloClienteObrigatorio;
    private javax.swing.JLabel rotuloDataFinal;
    private javax.swing.JLabel rotuloDataInicial;
    private javax.swing.JLabel rotuloTitulo;
    private com.toedter.calendar.JDateChooser seletorDeDataFinal;
    private com.toedter.calendar.JDateChooser seletorDeDataInicial;
    private javax.swing.JTable tabelaServicos;
    // End of variables declaration//GEN-END:variables
}
