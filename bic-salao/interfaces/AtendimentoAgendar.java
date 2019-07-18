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
import java.awt.Color;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import util.DateParse;

/**
 * @author BIC-Jr Guia de Estilo
 */
public class AtendimentoAgendar extends javax.swing.JDialog {

    /**
     * @param parent
     * @param modal
     */
    public AtendimentoAgendar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        alterarIcone();
        preencherListaCliente();
        preencherListaServico();
    }

    private void alterarIcone() {
        URL url = this.getClass().getResource("/imagens/calendario.png");
        Image iconeTitulo = Toolkit.getDefaultToolkit().getImage(url);
        this.setIconImage(iconeTitulo);
        this.setLocationRelativeTo(null);
        util.JanelaComum.setAcessibilidade(this);
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

    private void preencherTabela() {
        String[] titulo = new String[]{"Serviço", "Profissional", "Horário"};
        String[][] linhas = new String[servicosTabela.size()][3];
        servicosTabela.forEach((servico) -> {
            int posicao = servicosTabela.indexOf(servico);
            linhas[posicao][0] = servico.getNmServico();
            linhas[posicao][1] = servico.getValor();
            linhas[posicao][2] = servico.getHorario();
        });
        tabelaServicos.setModel(new javax.swing.table.DefaultTableModel(linhas, titulo) {
            @Override
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        });
        tabelaServicos.getColumnModel().getColumn(0).setPreferredWidth(240);
        tabelaServicos.getColumnModel().getColumn(1).setPreferredWidth(240);
        botaoRemoverServico.setEnabled(false);
    }

    public List<Servico> getServicoFromTable() {
        servicosTabela = new ArrayList<>();
        for (int linha = 0; linha < tabelaServicos.getRowCount(); linha++) {
            TableModel modeloTabela = tabelaServicos.getModel();
            String nmServico = modeloTabela.getValueAt(linha, 0).toString();
            String profissional = modeloTabela.getValueAt(linha, 1).toString();
            String horario = modeloTabela.getValueAt(linha, 2).toString();
            servicosTabela.add(new Servico(nmServico, profissional, horario));
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

    private boolean validaHorario() {
        if (selecaoHorario) {
            campoHorario.setBorder(new LineBorder(Color.GRAY));
            rotuloHorarioObrigatorio.setVisible(false);
            return true;
        } else {
            if (ads) {
                campoHorario.setBorder(new LineBorder(Color.RED));
                rotuloHorarioObrigatorio.setVisible(true);
            } else {
                campoHorario.setBorder(new LineBorder(Color.GRAY));
                rotuloHorarioObrigatorio.setVisible(false);
            }
            return false;
        }
//        try {
//            for (int i = 0; i < horarios.size(); i++) {
//                if (horarios.get(i).equals(campoHorario.getText()) && selecaoHorario) {
//                    campoHorario.setBorder(new LineBorder(Color.GRAY));
//                    rotuloHorarioObrigatorio.setVisible(false);
//                    return true;
//                }
//            }
//        } catch (NullPointerException e) {
//        }
//        if (ads) {
//            campoHorario.setBorder(new LineBorder(Color.RED));
//            rotuloHorarioObrigatorio.setVisible(true);
//        } else {
//            campoHorario.setBorder(new LineBorder(Color.GRAY));
//            rotuloHorarioObrigatorio.setVisible(false);
//        }
//        return false;
    }

    private void anularServico() {
        campoServico.setText("");
        preencherListaServico();
        campoProfissional.setText("");
        campoHorario.setText("");
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
        campoHorario.setText("");
        modeloServico.removeAllElements();
        int contador = 0;
        for (int i = 0; i < servicos.size(); i++) {
            if (servicos.get(i).toLowerCase().startsWith(campoServico.getText().toLowerCase())) {
                modeloServico.addElement(servicos.get(i));
                contador++;
            }
        }
        if (contador <= 6) {
            painelListaServico.setBounds(25, 180, 220, contador * 20 + 1);
        } else {
            painelListaServico.setBounds(25, 180, 220, 141);
        }
        painelListaServico.setVisible(true);
        selecaoProfissional = false;
        selecaoHorario = false;
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
            campoHorario.setText("");
            modeloProfissional.removeAllElements();
            int contador = 0;
            for (int i = 0; i < profissionais.size(); i++) {
                if (profissionais.get(i).toLowerCase().startsWith(campoProfissional.getText().toLowerCase()) && selecaoServico) {
                    modeloProfissional.addElement(profissionais.get(i));
                    contador++;
                }
            }
            if (contador <= 6) {
                painelListaProfissional.setBounds(260, 180, 220, contador * 20 + 1);
            } else {
                painelListaProfissional.setBounds(260, 180, 220, 141);
            }
        } catch (NullPointerException e) {
        }
        painelListaProfissional.setVisible(true);
        selecaoHorario = false;
    }

    private void preencherListaHorario() {
        horarios = new ArrayList<>();
        horarios.add("08:00");
        horarios.add("08:30");
        horarios.add("09:00");
        horarios.add("09:30");
        horarios.add("10:00");
        horarios.add("10:30");
        horarios.add("11:00");
        horarios.add("11:30");
        horarios.add("13:00");
        horarios.add("13:30");
        horarios.add("14:00");
        horarios.add("14:30");
        modeloHorario = new DefaultListModel();
        listaHorario.setModel(modeloHorario);
        horarios.forEach((horario) -> {
            modeloHorario.addElement(horario);
        });
    }

    private void pesquisarHorario() {
        modeloHorario.removeAllElements();
        int contador = 0;
        for (int i = 0; i < horarios.size(); i++) {
            if (horarios.get(i).toLowerCase().startsWith(campoHorario.getText().toLowerCase()) && selecaoProfissional) {
                modeloHorario.addElement(horarios.get(i));
                contador++;
            }
        }
        if (contador <= 6) {
            painelListaHorario.setBounds(495, 180, 105, contador * 20 + 1);
        } else {
            painelListaHorario.setBounds(495, 180, 105, 141);
        }
        if (campoHorario.getText().equals("")) {
            painelListaHorario.setBounds(495, 180, 105, 141);
        }
        painelListaHorario.setVisible(true);
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

    private boolean validaTabelas() {
        if (tabelaServicos.getRowCount() > 0) {
            rotuloTabelaObrigatorio.setForeground(new java.awt.Color(0, 0, 0));
            return true;
        } else {
            campoServico.setBorder(new LineBorder(Color.RED));
            rotuloServicoObrigatorio.setVisible(true);
            campoProfissional.setBorder(new LineBorder(Color.RED));
            rotuloProfissionalObrigatorio.setVisible(true);
            campoHorario.setBorder(new LineBorder(Color.RED));
            rotuloHorarioObrigatorio.setVisible(true);
            rotuloTabelaObrigatorio.setForeground(new java.awt.Color(204, 0, 0));
            return false;
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
        rotuloNovoAgendamento = new javax.swing.JLabel();
        painelRolavel = new javax.swing.JScrollPane();
        painelFormulario = new javax.swing.JPanel();
        painelMeio = new javax.swing.JLayeredPane();
        painelBotoes = new javax.swing.JPanel();
        botaoCancelar = new javax.swing.JButton();
        botaoSalvar = new javax.swing.JButton();
        rotuloCliente = new javax.swing.JLabel();
        rotuloData = new javax.swing.JLabel();
        campoData = new javax.swing.JFormattedTextField();
        seletorDeData = new com.toedter.calendar.JDateChooser();
        rotuloDataObrigatorio = new javax.swing.JLabel();
        botaoAdicionarCliente = new javax.swing.JButton();
        bordaMer = new javax.swing.JSeparator();
        botaoComboCliente = new javax.swing.JButton();
        campoNome = new javax.swing.JTextField();
        painelListaCliente = new javax.swing.JScrollPane();
        listaCliente = new javax.swing.JList<>();
        rotuloClienteObrigatorio = new javax.swing.JLabel();
        botaoPesquisar = new javax.swing.JButton();
        bordaSet = new javax.swing.JSeparator();
        bordaOri = new javax.swing.JSeparator();
        rotuloDados = new javax.swing.JLabel();
        bordaOci = new javax.swing.JSeparator();
        bordaCen = new javax.swing.JSeparator();
        botaoComboServico = new javax.swing.JButton();
        rotuloServicoObrigatorio = new javax.swing.JLabel();
        campoServico = new javax.swing.JTextField();
        painelListaServico = new javax.swing.JScrollPane();
        listaServico = new javax.swing.JList<>();
        botaoComboProfissional = new javax.swing.JButton();
        rotuloProfissionalObrigatorio = new javax.swing.JLabel();
        campoProfissional = new javax.swing.JTextField();
        painelListaProfissional = new javax.swing.JScrollPane();
        listaProfissional = new javax.swing.JList<>();
        botaoComboHorario = new javax.swing.JButton();
        rotuloHorarioObrigatorio = new javax.swing.JLabel();
        campoHorario = new javax.swing.JTextField();
        painelListaHorario = new javax.swing.JScrollPane();
        listaHorario = new javax.swing.JList<>();
        painelServicos = new javax.swing.JPanel();
        rotuloServico = new javax.swing.JLabel();
        rotuloProfissional = new javax.swing.JLabel();
        rotuloHorario = new javax.swing.JLabel();
        painelTabela = new javax.swing.JScrollPane();
        tabelaServicos = new javax.swing.JTable();
        botaoAdicionarServico = new javax.swing.JButton();
        botaoRemoverServico = new javax.swing.JButton();
        rotuloTabelaObrigatorio = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gerenciar Atendimentos");
        setMinimumSize(new java.awt.Dimension(680, 560));
        setResizable(false);

        painelTopo.setLayout(new java.awt.BorderLayout());

        painelDadosFora.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        painelTopo.add(painelDadosFora, java.awt.BorderLayout.CENTER);

        painelTitulo.setBackground(java.awt.Color.black);

        rotuloNovoAgendamento.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        rotuloNovoAgendamento.setForeground(new java.awt.Color(255, 255, 255));
        rotuloNovoAgendamento.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rotuloNovoAgendamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/calendarioEditar.png"))); // NOI18N
        rotuloNovoAgendamento.setText("AGENDAR ATENDIMENTO");
        painelTitulo.add(rotuloNovoAgendamento);

        painelTopo.add(painelTitulo, java.awt.BorderLayout.NORTH);

        getContentPane().add(painelTopo, java.awt.BorderLayout.NORTH);

        painelFormulario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        painelMeio.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        painelBotoes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

        botaoCancelar.setBackground(new java.awt.Color(135, 206, 235));
        botaoCancelar.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        botaoCancelar.setMnemonic('c');
        botaoCancelar.setText("Cancelar");
        botaoCancelar.setToolTipText("Cancelar este agendamento (Alt+C)");
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
        botaoSalvar.setToolTipText("Salvar este agendamento (Alt+S)");
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

        painelMeio.add(painelBotoes, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 410, 655, 50));

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

        rotuloDataObrigatorio.setForeground(new java.awt.Color(204, 0, 0));
        rotuloDataObrigatorio.setText("Selecione uma data");
        painelMeio.add(rotuloDataObrigatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 80, 150, -1));
        rotuloDataObrigatorio.setVisible(false);

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

        listaCliente.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        listaCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        listaCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listaClienteMousePressed(evt);
            }
        });
        painelListaCliente.setViewportView(listaCliente);

        painelMeio.setLayer(painelListaCliente, javax.swing.JLayeredPane.POPUP_LAYER);
        painelMeio.add(painelListaCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 80, 260, 40));
        painelListaCliente.setVisible(false);

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
        painelMeio.add(botaoComboServico, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 150, 35, 31));

        rotuloServicoObrigatorio.setForeground(new java.awt.Color(204, 0, 0));
        rotuloServicoObrigatorio.setText("Selecione um serviço");
        painelMeio.add(rotuloServicoObrigatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 180, 150, -1));
        rotuloServicoObrigatorio.setVisible(false);

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
        painelMeio.add(campoServico, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 150, 220, 30));

        listaServico.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        listaServico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        listaServico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listaServicoMousePressed(evt);
            }
        });
        painelListaServico.setViewportView(listaServico);

        painelMeio.setLayer(painelListaServico, javax.swing.JLayeredPane.POPUP_LAYER);
        painelMeio.add(painelListaServico, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 180, 220, 101));
        painelListaServico.setVisible(false);

        botaoComboProfissional.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/seta.png"))); // NOI18N
        botaoComboProfissional.setContentAreaFilled(false);
        botaoComboProfissional.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoComboProfissional.setFocusable(false);
        botaoComboProfissional.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoComboProfissionalActionPerformed(evt);
            }
        });
        painelMeio.add(botaoComboProfissional, new org.netbeans.lib.awtextra.AbsoluteConstraints(445, 150, 35, 31));

        rotuloProfissionalObrigatorio.setForeground(new java.awt.Color(204, 0, 0));
        rotuloProfissionalObrigatorio.setText("Selecione um profissional");
        painelMeio.add(rotuloProfissionalObrigatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, 150, -1));
        rotuloProfissionalObrigatorio.setVisible(false);

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
        painelMeio.add(campoProfissional, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 150, 220, 30));

        listaProfissional.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        listaProfissional.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        listaProfissional.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listaProfissionalMousePressed(evt);
            }
        });
        painelListaProfissional.setViewportView(listaProfissional);

        painelMeio.setLayer(painelListaProfissional, javax.swing.JLayeredPane.POPUP_LAYER);
        painelMeio.add(painelListaProfissional, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, 220, 101));
        painelListaProfissional.setVisible(false);

        botaoComboHorario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/seta.png"))); // NOI18N
        botaoComboHorario.setContentAreaFilled(false);
        botaoComboHorario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoComboHorario.setFocusable(false);
        botaoComboHorario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoComboHorarioActionPerformed(evt);
            }
        });
        painelMeio.add(botaoComboHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(565, 150, 35, 31));

        rotuloHorarioObrigatorio.setForeground(new java.awt.Color(204, 0, 0));
        rotuloHorarioObrigatorio.setText("Selecione um horário");
        painelMeio.add(rotuloHorarioObrigatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(495, 180, 110, -1));
        rotuloHorarioObrigatorio.setVisible(false);

        campoHorario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campoHorario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoHorarioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                campoHorarioFocusLost(evt);
            }
        });
        campoHorario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                campoHorarioMouseClicked(evt);
            }
        });
        campoHorario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoHorarioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                campoHorarioKeyTyped(evt);
            }
        });
        painelMeio.add(campoHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(495, 150, 105, 30));

        listaHorario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        listaHorario.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        listaHorario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listaHorarioMousePressed(evt);
            }
        });
        painelListaHorario.setViewportView(listaHorario);

        painelMeio.setLayer(painelListaHorario, javax.swing.JLayeredPane.POPUP_LAYER);
        painelMeio.add(painelListaHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(495, 180, 105, 141));
        painelListaHorario.setVisible(false);

        painelServicos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Serviços Agendados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 14))); // NOI18N
        painelServicos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        painelServicos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rotuloServico.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloServico.setText("Serviço*:");
        painelServicos.add(rotuloServico, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 19, -1, -1));

        rotuloProfissional.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloProfissional.setText("Profissional*:");
        painelServicos.add(rotuloProfissional, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, -1, -1));

        rotuloHorario.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloHorario.setText("Horário*:");
        painelServicos.add(rotuloHorario, new org.netbeans.lib.awtextra.AbsoluteConstraints(485, 20, -1, -1));

        tabelaServicos.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tabelaServicos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serviço", "Profissional", "Horário"
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
        painelTabela.setViewportView(tabelaServicos);
        JTableHeader cabecalho = tabelaServicos.getTableHeader();
        cabecalho.setFont(new java.awt.Font("Arial", 0, 14));
        tabelaServicos.getColumnModel().getColumn(0).setPreferredWidth(240);
        tabelaServicos.getColumnModel().getColumn(1).setPreferredWidth(240);

        painelServicos.add(painelTabela, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 85, 575, 185));

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
        botaoRemoverServico.setToolTipText("Remover serviço selecionado (Alt+R)");
        botaoRemoverServico.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoRemoverServico.setEnabled(false);
        botaoRemoverServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoRemoverServicoActionPerformed(evt);
            }
        });
        painelServicos.add(botaoRemoverServico, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 80, 35, 35));

        rotuloTabelaObrigatorio.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        rotuloTabelaObrigatorio.setText("Insira pelo menos serviço na tabela");
        painelServicos.add(rotuloTabelaObrigatorio, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 275, 320, -1));

        painelMeio.add(painelServicos, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 650, 300));

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
        boolean h = validaHorario();
        if (s && p && h) {
            ads = false;
            servicosTabela.add(new Servico(campoServico.getText(),
                    campoProfissional.getText(), campoHorario.getText()));
            anularServico();
            preencherTabela();
            selecaoServico = false;
            selecaoProfissional = false;
            selecaoHorario = false;
            painelListaProfissional.setBounds(260, 180, 220, 1);
            painelListaHorario.setBounds(495, 180, 105, 1);
            rotuloTabelaObrigatorio.setVisible(false);
        }
    }//GEN-LAST:event_botaoAdicionarServicoActionPerformed

    private void botaoRemoverServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoRemoverServicoActionPerformed
        int linha = tabelaServicos.getSelectedRow();
        DefaultTableModel m = (DefaultTableModel) tabelaServicos.getModel();
        m.removeRow(linha);
        servicosTabela = getServicoFromTable();
        preencherTabela();
        if (tabelaServicos.getRowCount() == 0) {
            rotuloTabelaObrigatorio.setVisible(true);
        }
    }//GEN-LAST:event_botaoRemoverServicoActionPerformed

    private void botaoAdicionarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoAdicionarClienteActionPerformed
        ClienteNovo clienteNovo = new ClienteNovo(null, true);
        clienteNovo.setVisible(true);
        Cliente cliente = clienteNovo.getCliente();
        if (cliente != null) {
            campoNome.setText(cliente.getNmCliente());
            campoNome.setBorder(new LineBorder(Color.GRAY));
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

    private void listaClienteMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaClienteMousePressed
        campoNome.setText(listaCliente.getSelectedValue());
        selecaoCliente = true;
    }//GEN-LAST:event_listaClienteMousePressed

    private void botaoPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoPesquisarActionPerformed
        ClientePesquisar clientePesquisar = new ClientePesquisar(null, true);
        clientePesquisar.setVisible(true);
        Cliente cliente = clientePesquisar.getCliente();
        if (cliente != null) {
            campoNome.setText(cliente.getNmCliente());
            campoNome.setBorder(new LineBorder(Color.GRAY));
            selecaoCliente = true;
        }
    }//GEN-LAST:event_botaoPesquisarActionPerformed

    private void campoServicoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoServicoFocusLost
        painelListaServico.setVisible(false);
        if (selecaoServico) {
            campoServico.setBorder(new LineBorder(Color.GRAY));
            rotuloServicoObrigatorio.setVisible(false);
        } else {
            if (ads) {
                campoServico.setBorder(new LineBorder(Color.RED));
                rotuloServicoObrigatorio.setVisible(true);
            } else {
                campoServico.setBorder(new LineBorder(Color.GRAY));
                rotuloServicoObrigatorio.setVisible(false);
            }
        }
    }//GEN-LAST:event_campoServicoFocusLost

    private void campoServicoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoServicoFocusGained
        campoServico.setBorder(new LineBorder(Color.BLUE));
//        if (!selecaoServico) {
//            painelListaServico.setVisible(true);
//        } else {
//            painelListaServico.setVisible(false);
//        }
    }//GEN-LAST:event_campoServicoFocusGained

    private void campoServicoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoServicoKeyReleased
        selecaoServico = false;
        painelListaProfissional.setBounds(260, 180, 220, 1);
        pesquisarServico();
        painelListaHorario.setBounds(495, 180, 105, 1);
    }//GEN-LAST:event_campoServicoKeyReleased

    private void listaServicoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaServicoMousePressed
        campoServico.setText(listaServico.getSelectedValue());
        selecaoServico = true;
        campoProfissional.setText("");
        painelListaProfissional.setBounds(260, 180, 220, 101);
        preencherListaProfissional();
        campoHorario.setText("");
        preencherListaHorario();
    }//GEN-LAST:event_listaServicoMousePressed

    private void listaProfissionalMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaProfissionalMousePressed
        campoProfissional.setText(listaProfissional.getSelectedValue());
        selecaoProfissional = true;
        campoHorario.setText("");
        painelListaHorario.setBounds(495, 180, 105, 141);
        preencherListaHorario();
    }//GEN-LAST:event_listaProfissionalMousePressed

    private void campoProfissionalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoProfissionalFocusLost
        painelListaProfissional.setVisible(false);
        if (selecaoProfissional) {
            campoProfissional.setBorder(new LineBorder(Color.GRAY));
            rotuloProfissionalObrigatorio.setVisible(false);
        } else {
            if (ads) {
                campoProfissional.setBorder(new LineBorder(Color.RED));
                rotuloProfissionalObrigatorio.setVisible(true);
            } else {
                campoProfissional.setBorder(new LineBorder(Color.GRAY));
            }
        }
    }//GEN-LAST:event_campoProfissionalFocusLost

    private void campoProfissionalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoProfissionalFocusGained
        campoProfissional.setBorder(new LineBorder(Color.BLUE));
        if (!selecaoServico) {
            painelListaProfissional.setBounds(260, 180, 220, 1);
        }
//        if (!selecaoProfissional) {
//            painelListaProfissional.setVisible(true);
//        } else {
//            painelListaProfissional.setVisible(false);
//        }
    }//GEN-LAST:event_campoProfissionalFocusGained

    private void campoProfissionalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoProfissionalKeyReleased
        selecaoProfissional = false;
        painelListaHorario.setBounds(495, 180, 105, 1);
        pesquisarProfissional();
    }//GEN-LAST:event_campoProfissionalKeyReleased

    private void listaHorarioMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaHorarioMousePressed
        campoHorario.setText(listaHorario.getSelectedValue());
        selecaoHorario = true;
    }//GEN-LAST:event_listaHorarioMousePressed

    private void campoHorarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoHorarioFocusLost
        painelListaHorario.setVisible(false);
        validaHorario();
    }//GEN-LAST:event_campoHorarioFocusLost

    private void campoHorarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_campoHorarioFocusGained
        campoHorario.setBorder(new LineBorder(Color.BLUE));
        if (!selecaoProfissional) {
            painelListaHorario.setBounds(495, 180, 105, 1);
        }
//        if (!selecaoHorario) {
//            painelListaHorario.setVisible(true);
//        } else {
//            painelListaHorario.setVisible(false);
//        }
    }//GEN-LAST:event_campoHorarioFocusGained

    private void campoHorarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoHorarioKeyReleased
        selecaoHorario = false;
        pesquisarHorario();
    }//GEN-LAST:event_campoHorarioKeyReleased

    private void campoNomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoNomeMouseClicked
        pesquisarCliente();
        painelListaCliente.setVisible(true);
    }//GEN-LAST:event_campoNomeMouseClicked

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

    private void botaoComboHorarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoComboHorarioActionPerformed
        if (!painelListaHorario.isVisible()) {
            painelListaHorario.setVisible(true);
            campoHorario.requestFocusInWindow();
            pesquisarHorario();
        } else {
            painelListaHorario.setVisible(false);
        }
    }//GEN-LAST:event_botaoComboHorarioActionPerformed

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

    private void campoHorarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_campoHorarioMouseClicked
        if (selecaoProfissional) {
            painelListaHorario.setVisible(true);
        }
        pesquisarHorario();
    }//GEN-LAST:event_campoHorarioMouseClicked

    private void botaoSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSalvarActionPerformed
        adt = true;
        boolean cliente = validaCliente();
        boolean data = validaData();
        boolean tabelas = validaTabelas();
        if (cliente && data && tabelas) {
            this.dispose();
        }
    }//GEN-LAST:event_botaoSalvarActionPerformed

    private void campoNomeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNomeKeyTyped
        util.JanelaComum.campoTexto(evt);
    }//GEN-LAST:event_campoNomeKeyTyped

    private void campoServicoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoServicoKeyTyped
        util.JanelaComum.campoTexto(evt);
    }//GEN-LAST:event_campoServicoKeyTyped

    private void campoProfissionalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoProfissionalKeyTyped
        util.JanelaComum.campoTexto(evt);
    }//GEN-LAST:event_campoProfissionalKeyTyped

    private void campoHorarioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoHorarioKeyTyped
        String caracteres = "0123456789";
        String caracter = ".,";
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            if (campoHorario.getText().contains(":")
                    || !caracter.contains(evt.getKeyChar() + "")) {
                evt.consume();
            }
        }
    }//GEN-LAST:event_campoHorarioKeyTyped

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
            java.util.logging.Logger.getLogger(AtendimentoAgendar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            AtendimentoAgendar dialog = new AtendimentoAgendar(new javax.swing.JFrame(), true);
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
    private List<String> profissionais;
    private DefaultListModel modeloProfissional;
    private boolean selecaoProfissional = false;
    private List<String> horarios;
    private DefaultListModel modeloHorario;
    private boolean selecaoHorario = false;
    private boolean ads = false;
    private boolean adt = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator bordaCen;
    private javax.swing.JSeparator bordaMer;
    private javax.swing.JSeparator bordaOci;
    private javax.swing.JSeparator bordaOri;
    private javax.swing.JSeparator bordaSet;
    private javax.swing.JButton botaoAdicionarCliente;
    private javax.swing.JButton botaoAdicionarServico;
    private javax.swing.JButton botaoCancelar;
    private javax.swing.JButton botaoComboCliente;
    private javax.swing.JButton botaoComboHorario;
    private javax.swing.JButton botaoComboProfissional;
    private javax.swing.JButton botaoComboServico;
    private javax.swing.JButton botaoPesquisar;
    private javax.swing.JButton botaoRemoverServico;
    private javax.swing.JButton botaoSalvar;
    private javax.swing.JFormattedTextField campoData;
    private javax.swing.JTextField campoHorario;
    private javax.swing.JTextField campoNome;
    private javax.swing.JTextField campoProfissional;
    private javax.swing.JTextField campoServico;
    private javax.swing.JList<String> listaCliente;
    private javax.swing.JList<String> listaHorario;
    private javax.swing.JList<String> listaProfissional;
    private javax.swing.JList<String> listaServico;
    private javax.swing.JPanel painelBotoes;
    private javax.swing.JPanel painelDadosFora;
    private javax.swing.JPanel painelFormulario;
    private javax.swing.JScrollPane painelListaCliente;
    private javax.swing.JScrollPane painelListaHorario;
    private javax.swing.JScrollPane painelListaProfissional;
    private javax.swing.JScrollPane painelListaServico;
    private javax.swing.JLayeredPane painelMeio;
    private javax.swing.JScrollPane painelRolavel;
    private javax.swing.JPanel painelServicos;
    private javax.swing.JScrollPane painelTabela;
    private javax.swing.JPanel painelTitulo;
    private javax.swing.JPanel painelTopo;
    private javax.swing.JLabel rotuloCliente;
    private javax.swing.JLabel rotuloClienteObrigatorio;
    private javax.swing.JLabel rotuloDados;
    private javax.swing.JLabel rotuloData;
    private javax.swing.JLabel rotuloDataObrigatorio;
    private javax.swing.JLabel rotuloHorario;
    private javax.swing.JLabel rotuloHorarioObrigatorio;
    private javax.swing.JLabel rotuloNovoAgendamento;
    private javax.swing.JLabel rotuloProfissional;
    private javax.swing.JLabel rotuloProfissionalObrigatorio;
    private javax.swing.JLabel rotuloServico;
    private javax.swing.JLabel rotuloServicoObrigatorio;
    private javax.swing.JLabel rotuloTabelaObrigatorio;
    private com.toedter.calendar.JDateChooser seletorDeData;
    private javax.swing.JTable tabelaServicos;
    // End of variables declaration//GEN-END:variables
}
