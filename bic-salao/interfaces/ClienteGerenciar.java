package interfaces;

import java.awt.Color;
import dados.ClienteDados;
import dados.DadosException;
import entidades.Cliente;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;

/* @author BIC-Jr Guia de Estilo */
public class ClienteGerenciar extends javax.swing.JFrame {

    public ClienteGerenciar() {
        initComponents();
        redimencionarTela();
        alterarIcone();
        preencherTabela();
    }

    // <editor-fold defaultstate="collapsed" desc="Código de Criação">
    private void alterarIcone() {
        URL url = this.getClass().getResource("/imagens/usuario.png");
        Image iconeTitulo = Toolkit.getDefaultToolkit().getImage(url);
        this.setIconImage(iconeTitulo);
        this.setLocationRelativeTo(null);
        util.JanelaComum.setAcessibilidade(this);
    }

    private void preencherLista() {
        try {
            clientes = ClienteDados.consultarTodos();
            if (clientes.size() >= 50) {
                rotuloTabela.setText("Mostrando 50 clientes de um total de " + clientes.size());
            } else {
                rotuloTabela.setText("Mostrando " + clientes.size() + " clientes de um total de " + clientes.size());
            }
        } catch (DadosException ex) {
            Problema erroDeConexao = new Problema(new javax.swing.JFrame(), true, ex.getMessage());
            erroDeConexao.setVisible(true);
        }
    }

    private void preencherTabela() {
        preencherLista();
        if (clientes != null) {
            String[] titulo = new String[]{"Nome", "Telefone", "E-mail"};
            String[][] linhas = new String[clientes.size()][3];
            int tamanho = 50;
            if (clientes.size() < 50) {
                tamanho = clientes.size();
            }
            for (int posicao = 0; posicao < tamanho; posicao++) {
                Cliente cliente = clientes.get(posicao);
                linhas[posicao][0] = cliente.getNmCliente();
                linhas[posicao][1] = cliente.getTelefone();
                linhas[posicao][2] = cliente.getEmail();
            }
            tabelaClientes.setModel(new javax.swing.table.DefaultTableModel(linhas, titulo) {
                @Override
                public boolean isCellEditable(int rowIndex, int mColIndex) {
                    return false;
                }
            });
            tabelaClientes.getColumnModel().getColumn(0).setPreferredWidth(200);
            desabilitarBotoes();
        }
    }

    private void preencherTabela(List<Cliente> clientes) {
        if (clientes != null) {
            this.clientes = clientes;
            String[] titulo = new String[]{"Nome", "Telefone", "E-mail"};
            String[][] linhas = new String[clientes.size()][3];
            int tamanho = 50;
            if (clientes.size() < 50) {
                tamanho = clientes.size();
            }
            for (int posicao = 0; posicao < tamanho; posicao++) {
                Cliente cliente = clientes.get(posicao);
                linhas[posicao][0] = cliente.getNmCliente();
                linhas[posicao][1] = cliente.getTelefone();
                linhas[posicao][2] = cliente.getEmail();
            }
            tabelaClientes.setModel(new javax.swing.table.DefaultTableModel(linhas, titulo) {
                @Override
                public boolean isCellEditable(int rowIndex, int mColIndex) {
                    return false;
                }
            });
            tabelaClientes.getColumnModel().getColumn(0).setPreferredWidth(200);
            desabilitarBotoes();
        } else {
            preencherTabela();
        }
    }

    private void desabilitarBotoes() {
        botaoEditar.setEnabled(false);
        botaoExcluir.setEnabled(false);
        botaoVisualizar.setEnabled(false);
    }

    private void consultarPorNome() {
        try {
            List<Cliente> cliente = ClienteDados.consultarCampos(campoPesquisa.getText());
            if (cliente.size() >= 50) {
                rotuloTabela.setText("Mostrando 50 clientes de um total de " + cliente.size());
            } else {
                rotuloTabela.setText("Mostrando " + cliente.size() + " clientes de um total de " + cliente.size());
            }
            preencherTabela(cliente);
        } catch (DadosException ex) {
            Problema erroDeConexao = new Problema(new javax.swing.JFrame(), true, ex.getMessage());
            erroDeConexao.setVisible(true);
        }
    }

    private Cliente getClienteFromTable() {
        Cliente cliente;
        int linha = tabelaClientes.getSelectedRow();
        cliente = clientes.get(linha);
        return cliente;
    }

    private void redimencionarTela() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        java.awt.Dimension d = tk.getScreenSize();
        if (d.width <= 1050 || d.height <= 800) {
            if (d.width > 970 || d.height > 730) {
                this.setSize(950, 680);
            } else {
                this.setExtendedState(MAXIMIZED_BOTH);
            }
        }
    }

    // </editor-fold>
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        painelTopoPrincipal = new javax.swing.JPanel();
        painelRotuloClientes = new javax.swing.JPanel();
        rotuloNovoCliente = new javax.swing.JLabel();
        painelBotoes = new javax.swing.JPanel();
        botaoNovo = new javax.swing.JButton();
        botaoVisualizar = new javax.swing.JButton();
        botaoEditar = new javax.swing.JButton();
        botaoExcluir = new javax.swing.JButton();
        painelPesquisa = new javax.swing.JPanel();
        painelPesquisaMeio = new javax.swing.JPanel();
        painelPesquisaComponentes = new javax.swing.JPanel();
        painelRotuloPesquisa = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        painelCampoBotaoPesquisa = new javax.swing.JPanel();
        campoPesquisa = new javax.swing.JTextField();
        botaoPesquisa = new javax.swing.JButton();
        botaoLimpar = new javax.swing.JButton();
        painelPesquisaOeste = new javax.swing.JPanel();
        painelPesquisaLeste = new javax.swing.JPanel();
        painelTabela = new javax.swing.JScrollPane();
        tabelaClientes = new javax.swing.JTable();
        painelLestePrincipal = new javax.swing.JPanel();
        painelOestePrincipal = new javax.swing.JPanel();
        painelInferiorPrincipal = new javax.swing.JPanel();
        painelBotaoVoltar = new javax.swing.JPanel();
        botaoVoltar = new javax.swing.JButton();
        painelRotuloTabela = new javax.swing.JPanel();
        rotuloTabela = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gerenciar Clientes");
        setMinimumSize(new java.awt.Dimension(560, 330));

        painelTopoPrincipal.setLayout(new java.awt.BorderLayout());

        painelRotuloClientes.setBackground(new java.awt.Color(0, 0, 0));

        rotuloNovoCliente.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        rotuloNovoCliente.setForeground(new java.awt.Color(255, 255, 255));
        rotuloNovoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/usuario.png"))); // NOI18N
        rotuloNovoCliente.setText("CLIENTES");
        painelRotuloClientes.add(rotuloNovoCliente);

        painelTopoPrincipal.add(painelRotuloClientes, java.awt.BorderLayout.NORTH);

        botaoNovo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/novo.png"))); // NOI18N
        botaoNovo.setMnemonic('n');
        botaoNovo.setText("Novo");
        botaoNovo.setToolTipText("Criar novo cliente (Alt+N)");
        botaoNovo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoNovo.setFocusable(false);
        botaoNovo.setPreferredSize(new java.awt.Dimension(130, 35));
        botaoNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoNovoActionPerformed(evt);
            }
        });
        painelBotoes.add(botaoNovo);

        botaoVisualizar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoVisualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/olho.png"))); // NOI18N
        botaoVisualizar.setMnemonic('v');
        botaoVisualizar.setText("Visualizar");
        botaoVisualizar.setToolTipText("Visualizar detalhes do cliente selecionado (Alt+V)");
        botaoVisualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoVisualizar.setEnabled(false);
        botaoVisualizar.setFocusable(false);
        botaoVisualizar.setPreferredSize(new java.awt.Dimension(130, 35));
        botaoVisualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoVisualizarActionPerformed(evt);
            }
        });
        painelBotoes.add(botaoVisualizar);

        botaoEditar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/editar.png"))); // NOI18N
        botaoEditar.setMnemonic('d');
        botaoEditar.setText("Editar");
        botaoEditar.setToolTipText("Editar o cliente selecionado (Alt+D)");
        botaoEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoEditar.setEnabled(false);
        botaoEditar.setFocusable(false);
        botaoEditar.setPreferredSize(new java.awt.Dimension(130, 35));
        botaoEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoEditarActionPerformed(evt);
            }
        });
        painelBotoes.add(botaoEditar);

        botaoExcluir.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/apagar.png"))); // NOI18N
        botaoExcluir.setMnemonic('x');
        botaoExcluir.setText("Excluir");
        botaoExcluir.setToolTipText("Excluir o cliente selecionado (Alt+X)");
        botaoExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoExcluir.setEnabled(false);
        botaoExcluir.setFocusable(false);
        botaoExcluir.setPreferredSize(new java.awt.Dimension(130, 35));
        botaoExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoExcluirActionPerformed(evt);
            }
        });
        painelBotoes.add(botaoExcluir);

        painelTopoPrincipal.add(painelBotoes, java.awt.BorderLayout.CENTER);

        painelPesquisa.setLayout(new java.awt.BorderLayout());

        painelPesquisaMeio.setPreferredSize(new java.awt.Dimension(633, 60));
        painelPesquisaMeio.setLayout(new java.awt.BorderLayout());

        painelPesquisaComponentes.setLayout(new java.awt.BorderLayout());

        painelRotuloPesquisa.setPreferredSize(new java.awt.Dimension(100, 25));
        painelRotuloPesquisa.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText("Nome, telefone ou e-mail:");
        painelRotuloPesquisa.add(jLabel1);

        painelPesquisaComponentes.add(painelRotuloPesquisa, java.awt.BorderLayout.NORTH);

        painelCampoBotaoPesquisa.setPreferredSize(new java.awt.Dimension(100, 35));
        painelCampoBotaoPesquisa.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 5, 0));

        campoPesquisa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campoPesquisa.setForeground(new java.awt.Color(0, 0, 0));
        campoPesquisa.setPreferredSize(new java.awt.Dimension(280, 30));
        campoPesquisa.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoPesquisaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                campoPesquisaFocusLost(evt);
            }
        });
        campoPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoPesquisaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoPesquisaKeyTyped(evt);
            }
        });
        painelCampoBotaoPesquisa.add(campoPesquisa);

        botaoPesquisa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoPesquisa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/filtro.png"))); // NOI18N
        botaoPesquisa.setMnemonic('f');
        botaoPesquisa.setText("Filtrar");
        botaoPesquisa.setToolTipText("Filtrar lista com base nos termos digitados (Alt+F)");
        botaoPesquisa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoPesquisa.setEnabled(false);
        botaoPesquisa.setPreferredSize(new java.awt.Dimension(100, 32));
        botaoPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoPesquisaActionPerformed(evt);
            }
        });
        painelCampoBotaoPesquisa.add(botaoPesquisa);

        botaoLimpar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/vassoura.png"))); // NOI18N
        botaoLimpar.setMnemonic('l');
        botaoLimpar.setText("Limpar");
        botaoLimpar.setToolTipText("Limpar resultados do filtro (Alt+L)");
        botaoLimpar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoLimpar.setEnabled(false);
        botaoLimpar.setPreferredSize(new java.awt.Dimension(100, 32));
        botaoLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoLimparActionPerformed(evt);
            }
        });
        painelCampoBotaoPesquisa.add(botaoLimpar);

        painelPesquisaComponentes.add(painelCampoBotaoPesquisa, java.awt.BorderLayout.CENTER);

        painelPesquisaMeio.add(painelPesquisaComponentes, java.awt.BorderLayout.CENTER);

        painelPesquisa.add(painelPesquisaMeio, java.awt.BorderLayout.CENTER);

        painelPesquisaOeste.setPreferredSize(new java.awt.Dimension(45, 60));

        javax.swing.GroupLayout painelPesquisaOesteLayout = new javax.swing.GroupLayout(painelPesquisaOeste);
        painelPesquisaOeste.setLayout(painelPesquisaOesteLayout);
        painelPesquisaOesteLayout.setHorizontalGroup(
            painelPesquisaOesteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );
        painelPesquisaOesteLayout.setVerticalGroup(
            painelPesquisaOesteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        painelPesquisa.add(painelPesquisaOeste, java.awt.BorderLayout.WEST);

        painelPesquisaLeste.setPreferredSize(new java.awt.Dimension(50, 60));

        javax.swing.GroupLayout painelPesquisaLesteLayout = new javax.swing.GroupLayout(painelPesquisaLeste);
        painelPesquisaLeste.setLayout(painelPesquisaLesteLayout);
        painelPesquisaLesteLayout.setHorizontalGroup(
            painelPesquisaLesteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        painelPesquisaLesteLayout.setVerticalGroup(
            painelPesquisaLesteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        painelPesquisa.add(painelPesquisaLeste, java.awt.BorderLayout.EAST);

        painelTopoPrincipal.add(painelPesquisa, java.awt.BorderLayout.SOUTH);

        getContentPane().add(painelTopoPrincipal, java.awt.BorderLayout.NORTH);

        tabelaClientes.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tabelaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Aline Maria Machado", "(31)94569-5123", "aline@cefetmg.br"},
                {"Breno Ribeiro Fernandes", "(32)98746-3211", "breno@cefetmg.br"},
                {"Cíntia Souza Ferreira", "(33)90123-6458", "cintia@cefetmg.br"},
                {"Diego Araújo Oliveira", "(31)83645-5231", "diego@cefetmg.br"},
                {"Emily Castro Gomes", "(32)90321-4756", "emily@cefetmg.br"}
            },
            new String [] {
                "Nome", "Telefone", "E-mail"
            }
        ));
        tabelaClientes.setToolTipText("Dê um duplo clique num cliente para visualizar seus detalhes");
        tabelaClientes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabelaClientes.setRowHeight(30);
        tabelaClientes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabelaClientes.setShowVerticalLines(false);
        tabelaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaClientesMouseClicked(evt);
            }
        });
        painelTabela.setViewportView(tabelaClientes);
        JTableHeader cabecalho = tabelaClientes.getTableHeader();
        cabecalho.setFont(new java.awt.Font("Arial", 0, 14));
        tabelaClientes.getColumnModel().getColumn(0).setPreferredWidth(200);

        getContentPane().add(painelTabela, java.awt.BorderLayout.CENTER);

        painelLestePrincipal.setPreferredSize(new java.awt.Dimension(50, 401));

        javax.swing.GroupLayout painelLestePrincipalLayout = new javax.swing.GroupLayout(painelLestePrincipal);
        painelLestePrincipal.setLayout(painelLestePrincipalLayout);
        painelLestePrincipalLayout.setHorizontalGroup(
            painelLestePrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        painelLestePrincipalLayout.setVerticalGroup(
            painelLestePrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 375, Short.MAX_VALUE)
        );

        getContentPane().add(painelLestePrincipal, java.awt.BorderLayout.EAST);

        painelOestePrincipal.setPreferredSize(new java.awt.Dimension(50, 401));

        javax.swing.GroupLayout painelOestePrincipalLayout = new javax.swing.GroupLayout(painelOestePrincipal);
        painelOestePrincipal.setLayout(painelOestePrincipalLayout);
        painelOestePrincipalLayout.setHorizontalGroup(
            painelOestePrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        painelOestePrincipalLayout.setVerticalGroup(
            painelOestePrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 375, Short.MAX_VALUE)
        );

        getContentPane().add(painelOestePrincipal, java.awt.BorderLayout.WEST);

        painelInferiorPrincipal.setPreferredSize(new java.awt.Dimension(733, 50));
        painelInferiorPrincipal.setLayout(new java.awt.BorderLayout());

        painelBotaoVoltar.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 50, 5));

        botaoVoltar.setBackground(new java.awt.Color(135, 206, 235));
        botaoVoltar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoVoltar.setMnemonic('v');
        botaoVoltar.setText("Voltar");
        botaoVoltar.setToolTipText("Fechar esta janela (Alt+V)");
        botaoVoltar.setContentAreaFilled(false);
        botaoVoltar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoVoltar.setMaximumSize(new java.awt.Dimension(89, 25));
        botaoVoltar.setMinimumSize(new java.awt.Dimension(89, 25));
        botaoVoltar.setOpaque(true);
        botaoVoltar.setPreferredSize(new java.awt.Dimension(130, 35));
        botaoVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoVoltarActionPerformed(evt);
            }
        });
        painelBotaoVoltar.add(botaoVoltar);

        painelInferiorPrincipal.add(painelBotaoVoltar, java.awt.BorderLayout.EAST);

        painelRotuloTabela.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 50, 15));

        rotuloTabela.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloTabela.setText("Mostrando 12 clientes de um total de 12");
        painelRotuloTabela.add(rotuloTabela);

        painelInferiorPrincipal.add(painelRotuloTabela, java.awt.BorderLayout.CENTER);

        getContentPane().add(painelInferiorPrincipal, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoNovoActionPerformed
        ClienteNovo clienteNovo = new ClienteNovo(null, true);
        clienteNovo.setVisible(true);
        Cliente cliente = clienteNovo.getCliente();
        if (cliente != null) {
            preencherTabela();
        }
    }//GEN-LAST:event_botaoNovoActionPerformed

    private void botaoEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoEditarActionPerformed
        Cliente cliente = getClienteFromTable();
        ClienteEditar clienteEditar = new ClienteEditar(null, true, cliente);
        clienteEditar.setVisible(true);

        cliente = clienteEditar.getCliente();
        if (cliente != null) {
            int linha = tabelaClientes.getSelectedRow();
            preencherTabela();
            tabelaClientes.setRowSelectionInterval(linha, linha);
            tabelaClientes.setValueAt(cliente.getNmCliente(), linha, 0);
            tabelaClientes.setValueAt(cliente.getTelefone(), linha, 1);
            tabelaClientes.setValueAt(cliente.getEmail(), linha, 2);
            botaoEditar.setEnabled(true);
            botaoExcluir.setEnabled(true);
            botaoVisualizar.setEnabled(true);
        }
    }//GEN-LAST:event_botaoEditarActionPerformed

    private void botaoExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoExcluirActionPerformed
        Cliente cliente = getClienteFromTable();
        ClienteExcluir clienteExcluir = new ClienteExcluir(null, true, cliente);
        clienteExcluir.setVisible(true);

        if (clienteExcluir.getOpcao()) {
            desabilitarBotoes();
            if(campoPesquisa.getText().isEmpty()) {
                preencherTabela();
            } else {
                consultarPorNome();
            }
        }
    }//GEN-LAST:event_botaoExcluirActionPerformed

    private void botaoVisualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoVisualizarActionPerformed
        Cliente cliente = getClienteFromTable();
        ClienteVisualizar clienteVisualizar = new ClienteVisualizar(null, true, cliente);
        clienteVisualizar.setVisible(true);
    }//GEN-LAST:event_botaoVisualizarActionPerformed

    private void campoPesquisaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoPesquisaFocusGained
        campoPesquisa.setBorder(new LineBorder(Color.BLUE));
//        if (campoPesquisa.getText().equals("Digite o nome, telefone ou e-mail desejado")) {
//            campoPesquisa.setText("");
//            campoPesquisa.setForeground(Color.black);
//        }
    }//GEN-LAST:event_campoPesquisaFocusGained

    private void campoPesquisaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoPesquisaFocusLost
        campoPesquisa.setBorder(new LineBorder(Color.GRAY));
//        if (campoPesquisa.getText().equals("")) {
//            campoPesquisa.setText("Digite o nome, telefone ou e-mail desejado");
//            campoPesquisa.setForeground(Color.gray);
//        }
    }//GEN-LAST:event_campoPesquisaFocusLost

    private void botaoPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoPesquisaActionPerformed
        consultarPorNome();
        botaoLimpar.setEnabled(true);
    }//GEN-LAST:event_botaoPesquisaActionPerformed

    private void tabelaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaClientesMouseClicked
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
            botaoEditar.setEnabled(true);
            botaoExcluir.setEnabled(true);
            botaoVisualizar.setEnabled(true);
            if (evt.getClickCount() == 2) {
                Cliente cliente = getClienteFromTable();
                ClienteVisualizar clienteVisualizar = new ClienteVisualizar(null, true, cliente);
                clienteVisualizar.setVisible(true);
            }
        }
    }//GEN-LAST:event_tabelaClientesMouseClicked

    private void botaoVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoVoltarActionPerformed
        dispose();
    }//GEN-LAST:event_botaoVoltarActionPerformed

    private void botaoLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoLimparActionPerformed
        preencherTabela();
        campoPesquisa.setText("");
        botaoPesquisa.setEnabled(false);
        botaoLimpar.setEnabled(false);
    }//GEN-LAST:event_botaoLimparActionPerformed

    private void campoPesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoPesquisaKeyReleased
        if (evt.getKeyCode() == 10) {
            if (!campoPesquisa.getText().equals("")) {
                consultarPorNome();
                botaoLimpar.setEnabled(true);
            }
        }
        if (campoPesquisa.getText().isEmpty()) {
            botaoPesquisa.setEnabled(false);
        } else {
            botaoPesquisa.setEnabled(true);
        }
    }//GEN-LAST:event_campoPesquisaKeyReleased

    private void campoPesquisaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoPesquisaKeyTyped
        util.JanelaComum.campoTexto(evt);
    }//GEN-LAST:event_campoPesquisaKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Windows look and feel */
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
            java.util.logging.Logger.getLogger(ClienteGerenciar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ClienteGerenciar().setVisible(true);
        });
    }

    List<Cliente> clientes = new ArrayList<>();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoEditar;
    private javax.swing.JButton botaoExcluir;
    private javax.swing.JButton botaoLimpar;
    private javax.swing.JButton botaoNovo;
    private javax.swing.JButton botaoPesquisa;
    private javax.swing.JButton botaoVisualizar;
    private javax.swing.JButton botaoVoltar;
    private javax.swing.JTextField campoPesquisa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel painelBotaoVoltar;
    private javax.swing.JPanel painelBotoes;
    private javax.swing.JPanel painelCampoBotaoPesquisa;
    private javax.swing.JPanel painelInferiorPrincipal;
    private javax.swing.JPanel painelLestePrincipal;
    private javax.swing.JPanel painelOestePrincipal;
    private javax.swing.JPanel painelPesquisa;
    private javax.swing.JPanel painelPesquisaComponentes;
    private javax.swing.JPanel painelPesquisaLeste;
    private javax.swing.JPanel painelPesquisaMeio;
    private javax.swing.JPanel painelPesquisaOeste;
    private javax.swing.JPanel painelRotuloClientes;
    private javax.swing.JPanel painelRotuloPesquisa;
    private javax.swing.JPanel painelRotuloTabela;
    private javax.swing.JScrollPane painelTabela;
    private javax.swing.JPanel painelTopoPrincipal;
    private javax.swing.JLabel rotuloNovoCliente;
    private javax.swing.JLabel rotuloTabela;
    private javax.swing.JTable tabelaClientes;
    // End of variables declaration//GEN-END:variables
}
