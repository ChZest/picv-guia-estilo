package interfaces;

import dados.ClienteDados;
import dados.DadosException;
import entidades.Cliente;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/* @author BIC-Jr Guia de Estilo */
public class PagamentoRegistrar extends javax.swing.JDialog {

    /**
     * @param parent
     * @param modal
     */
    public PagamentoRegistrar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        alterarIcone();
        preencherLista();
        preencherTabela();
    }

    private void alterarIcone() {
        URL url = this.getClass().getResource("/imagens/pagamentos.png");
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

    private void preencher() {
        String[] titulo = new String[]{"Data do atendimento", "Valor", "Pagar", ""};
        String[][] linhas = new String[0][4];
        tabelaServicos.setModel(new javax.swing.table.DefaultTableModel(linhas, titulo) {
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 3;
            }
        });
        tabelaServicos.getColumnModel().getColumn(2).setPreferredWidth(10);
        tabelaServicos.getColumnModel().getColumn(3).setPreferredWidth(10);
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
            pago = false;
            return true;
        }
    }

    private void ativarExibir() {
        boolean c = validaCliente();
        if (c) {
            botaoExibir.setEnabled(true);
        } else {
            botaoExibir.setEnabled(false);
        }
    }

    private void tabela(Object[][] linhas) {
        String[] titulo = new String[]{"Data do atendimento", "Valor", "Pagar", ""};
        tabelaServicos.setModel(new javax.swing.table.DefaultTableModel(linhas, titulo) {
            // <editor-fold defaultstate="collapsed" desc="Acrescentar checkbox à tabela">
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                // coluna 2: checkbox; coluna 3: botao;
                return columnIndex == 2 || columnIndex == 3;
            }

            @Override
            public Class getColumnClass(int column) {
                Class c = String.class;
                if (column == 2) {
                    c = Boolean.class;
                }
                return c;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                if (col == 2) {
                    linhas[row][col] = (Boolean) value;
                }
            }

            @Override
            public Object getValueAt(int row, int col) {
                return linhas[row][col];
            }// </editor-fold>  
        });
        tabelaServicos.getColumnModel().getColumn(2).setPreferredWidth(15);
        tabelaServicos.getColumnModel().getColumn(3).setPreferredWidth(15);
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        direita.setHorizontalAlignment(SwingConstants.RIGHT);
        tabelaServicos.getColumnModel().getColumn(1).setCellRenderer(direita);
        new util.ButtonColumn(tabelaServicos, 3) {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ServicoVisualizar servicoVisualizar = new ServicoVisualizar(
                        new javax.swing.JFrame(), true, "Observação do serviço");
                servicoVisualizar.setVisible(true);
                fireEditingStopped();
            }
        };
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
        botaoRegistrar = new javax.swing.JButton();
        botaoFechar = new javax.swing.JButton();
        painelMeio = new javax.swing.JLayeredPane();
        rotuloClienteObrigatorio = new javax.swing.JLabel();
        botaoCombo = new javax.swing.JButton();
        campoNome = new javax.swing.JTextField();
        painelLista = new javax.swing.JScrollPane();
        lista = new javax.swing.JList<>();
        rotuloCliente = new javax.swing.JLabel();
        botaoPesquisar = new javax.swing.JButton();
        painelFormulario = new javax.swing.JPanel();
        painelTabelaServicos = new javax.swing.JScrollPane();
        tabelaServicos = new javax.swing.JTable();
        botaoExibir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pagamentos");
        setMinimumSize(new java.awt.Dimension(525, 550));
        setResizable(false);

        painelTitulo.setBackground(java.awt.Color.black);

        rotuloTitulo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        rotuloTitulo.setForeground(new java.awt.Color(255, 255, 255));
        rotuloTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pagamentoJanela.png"))); // NOI18N
        rotuloTitulo.setText("REGISTRAR PAGAMENTO");
        painelTitulo.add(rotuloTitulo);

        getContentPane().add(painelTitulo, java.awt.BorderLayout.NORTH);

        painelBotoes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

        botaoRegistrar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/caixa.png"))); // NOI18N
        botaoRegistrar.setMnemonic('r');
        botaoRegistrar.setText("Registrar");
        botaoRegistrar.setToolTipText("Registrar pagamentos dos atendimentos marcados (Alt-R)");
        botaoRegistrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoRegistrar.setEnabled(false);
        botaoRegistrar.setMaximumSize(new java.awt.Dimension(89, 25));
        botaoRegistrar.setMinimumSize(new java.awt.Dimension(89, 25));
        botaoRegistrar.setPreferredSize(new java.awt.Dimension(130, 35));
        botaoRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoRegistrarActionPerformed(evt);
            }
        });
        painelBotoes.add(botaoRegistrar);
        botaoRegistrar.setOpaque(true);

        botaoFechar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/fechar.png"))); // NOI18N
        botaoFechar.setMnemonic('f');
        botaoFechar.setText("Fechar");
        botaoFechar.setToolTipText("Fechar esta janela (Alt-F)");
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
        painelLista.setBounds(10, 65, 260, 20);
        painelLista.setVisible(false);

        rotuloCliente.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloCliente.setText("Cliente:*");
        painelMeio.add(rotuloCliente);
        rotuloCliente.setBounds(10, 10, 53, 25);

        botaoPesquisar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/zoom.png"))); // NOI18N
        botaoPesquisar.setMnemonic('p');
        botaoPesquisar.setToolTipText("Pesquisar cliente (Alt-P)");
        botaoPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoPesquisarActionPerformed(evt);
            }
        });
        painelMeio.add(botaoPesquisar);
        botaoPesquisar.setBounds(280, 35, 35, 31);

        painelFormulario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        tabelaServicos.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabelaServicos.setShowVerticalLines(false);
        tabelaServicos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaServicosMouseClicked(evt);
            }
        });
        painelTabelaServicos.setViewportView(tabelaServicos);
        tabelaServicos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Data do Atendimento", "Valor", "Pagar", ""
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
        tabelaServicos.getColumnModel().getColumn(2).setPreferredWidth(15);
        tabelaServicos.getColumnModel().getColumn(3).setPreferredWidth(15);

        painelFormulario.add(painelTabelaServicos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 500, 310));

        painelMeio.add(painelFormulario);
        painelFormulario.setBounds(0, 90, 520, 390);

        botaoExibir.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoExibir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/filtro.png"))); // NOI18N
        botaoExibir.setMnemonic('e');
        botaoExibir.setText("Exibir");
        botaoExibir.setToolTipText("Mostrar atendimentos do cliente selecionado (Alt-E)");
        botaoExibir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoExibir.setEnabled(false);
        botaoExibir.setPreferredSize(new java.awt.Dimension(100, 32));
        botaoExibir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoExibirActionPerformed(evt);
            }
        });
        painelMeio.add(botaoExibir);
        botaoExibir.setBounds(325, 35, 100, 32);

        getContentPane().add(painelMeio, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoPesquisarActionPerformed
        ClientePesquisar clientePesquisar = new ClientePesquisar(null, true);
        clientePesquisar.setVisible(true);
        Cliente cliente = clientePesquisar.getCliente();
        if (cliente != null) {
            campoNome.setText(cliente.getNmCliente());
            preencher();
        }
        ativarExibir();
    }//GEN-LAST:event_botaoPesquisarActionPerformed

    private void botaoFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoFecharActionPerformed
        this.dispose();
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
        preencher();
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

    private void botaoRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoRegistrarActionPerformed
        adt = true;
        if (exibir) {
            DefaultTableModel modelo = (DefaultTableModel) tabelaServicos.getModel();
            java.util.List<Object[]> linhas = new java.util.ArrayList<>();
            for (int i = 0; i < modelo.getRowCount(); i++) {
                if (!(Boolean) modelo.getValueAt(i, 2)) {
                    linhas.add(new Object[]{
                        modelo.getValueAt(i, 0),
                        modelo.getValueAt(i, 1),
                        modelo.getValueAt(i, 2),
                        modelo.getValueAt(i, 3)
                    });
                }
            }
            Object[][] novo = new Object[linhas.size()][4];
            for (int i = 0; i < linhas.size(); i++) {
                System.arraycopy(linhas.get(i), 0, novo[i], 0, 4);
            }
            tabela(novo);
            botaoRegistrar.setEnabled(false);
            pago = true;
        } else {
            validaCliente();
        }
    }//GEN-LAST:event_botaoRegistrarActionPerformed

    private void botaoExibirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoExibirActionPerformed
        adt = true;
        if (!pago) {
            validaCliente();
            if (selecao) {
                // java.util.List<entidades.Servico> s = new java.util.ArrayList<>();
                // ss = s;
                // preencher(s);
                Object[][] linhas = new Object[7][4];
                linhas[0][0] = "10/02/2019";
                linhas[0][1] = "R$ 150,00";
                linhas[0][2] = false;
                linhas[0][3] = "";
                linhas[1][0] = "25/03/2019";
                linhas[1][1] = "R$ 250,00";
                linhas[1][2] = false;
                linhas[1][3] = "";
                linhas[2][0] = "05/04/2019";
                linhas[2][1] = "R$ 90,00";
                linhas[2][2] = false;
                linhas[2][3] = "";
                linhas[3][0] = "17/05/2019";
                linhas[3][1] = "R$ 90,00";
                linhas[3][2] = false;
                linhas[3][3] = "";
                linhas[4][0] = "04/06/2019";
                linhas[4][1] = "R$ 90,00";
                linhas[4][2] = false;
                linhas[4][3] = "";
                linhas[5][0] = "21/06/2019";
                linhas[5][1] = "R$ 90,00";
                linhas[5][2] = false;
                linhas[5][3] = "";
                linhas[6][0] = "31/07/2019";
                linhas[6][1] = "R$ 90,00";
                linhas[6][2] = false;
                linhas[6][3] = "";
                tabela(linhas);
                exibir = true;
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

    private void tabelaServicosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaServicosMouseClicked
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
            //boolean preencher = false;
            DefaultTableModel modelo = (DefaultTableModel) tabelaServicos.getModel();
            for (int i = 0; i < modelo.getRowCount(); i++) {
                if ((Boolean) modelo.getValueAt(i, 2)) {
                    botaoRegistrar.setEnabled(true);
                    break;
                } else {
                    botaoRegistrar.setEnabled(false);
                }
            }
        }
    }//GEN-LAST:event_tabelaServicosMouseClicked

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
            java.util.logging.Logger.getLogger(PagamentoRegistrar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            PagamentoRegistrar dialog = new PagamentoRegistrar(new javax.swing.JFrame(), true);
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
    private boolean pago = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoCombo;
    private javax.swing.JButton botaoExibir;
    private javax.swing.JButton botaoFechar;
    private javax.swing.JButton botaoPesquisar;
    private javax.swing.JButton botaoRegistrar;
    private javax.swing.JTextField campoNome;
    private javax.swing.JList<String> lista;
    private javax.swing.JPanel painelBotoes;
    private javax.swing.JPanel painelFormulario;
    private javax.swing.JScrollPane painelLista;
    private javax.swing.JLayeredPane painelMeio;
    private javax.swing.JScrollPane painelTabelaServicos;
    private javax.swing.JPanel painelTitulo;
    private javax.swing.JLabel rotuloCliente;
    private javax.swing.JLabel rotuloClienteObrigatorio;
    private javax.swing.JLabel rotuloTitulo;
    private javax.swing.JTable tabelaServicos;
    // End of variables declaration//GEN-END:variables
}
