package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CompletareRaport extends JFrame implements ActionListener {
    private JMenuBar menuBar;
    private JMenu meniuProceduri;
    private JTextField textFieldNumeP;
    private JTextField textFieldPrenumeP;
    private JTextField textFieldNumeM;
    private JButton salvareButton;
    private JTextField textFieldPrenumeM;
    private JTextField textFieldData;
    private JButton anulareButton;
    private JPanel mainPanel;
    private JTextField textFieldServiciu;
    private JTextField textFieldIstoric;
    private JTextField textFieldSimptome;
    private JTextField textFieldDiagnostic;
    private JTextField textFieldRecomandari;
    private Connection con;

    private JMenuItem cautareAngajat;
    private JMenuItem cautarePacient;
    private JMenuItem completareRaport;
    private JMenuItem pacientiProgramati;
    private JMenuItem parafareRapoarte;
    private JMenuItem profitCurent;
    private JMenuItem vizualizareProfit;
    private JMenuItem programari;
    private JMenuItem vizualizareProfitMedic;
    private JMenuItem emitereBon;
    private JMenuItem userNou;
    private JMenuItem totiUserii;
    private JMenuItem paginaPersonala;
    private JMenuItem salarii;
    private JMenuItem pacientNou;
    private JMenuItem orar;
    private JMenuItem servicii;
    private String role;
    private String uid;

    public CompletareRaport(String username, String role, Connection connection) {
        this.con = connection;
        this.uid = username;
        this.role = role;

        setTitle("Raport nou");
        setContentPane(mainPanel);
        CreareMeniu creareMeniu = new CreareMeniu();
        menuBar = creareMeniu.createMenu(role);
        this.setJMenuBar(menuBar);

        paginaPersonala = creareMeniu.getPaginaPersonala();
        cautareAngajat = creareMeniu.getCautareAngajat();
        cautarePacient = creareMeniu.getCautarePacient();
        completareRaport = creareMeniu.getCompletareRaport();
        emitereBon = creareMeniu.getEmitereBon();
        parafareRapoarte = creareMeniu.getParafareRapoarte();
        profitCurent = creareMeniu.getProfitCurent();
        programari = creareMeniu.getProgramari();
        pacientiProgramati = creareMeniu.getPacientiProgramati();
        vizualizareProfit = creareMeniu.getVizualizareProfit();
        vizualizareProfitMedic = creareMeniu.getVizualizareProfitMedic();
        userNou = creareMeniu.getUserNou();
        totiUserii = creareMeniu.getTotiUserii();
        salarii = creareMeniu.getSalarii();
        pacientNou = creareMeniu.getPacientNou();
        orar = creareMeniu.getOrar();
        servicii = creareMeniu.getServicii();

        paginaPersonala.addActionListener(this);
        cautareAngajat.addActionListener(this);
        cautarePacient.addActionListener(this);
        completareRaport.addActionListener(this);
        emitereBon.addActionListener(this);
        pacientiProgramati.addActionListener(this);
        programari.addActionListener(this);
        parafareRapoarte.addActionListener(this);
        profitCurent.addActionListener(this);
        vizualizareProfit.addActionListener(this);
        vizualizareProfitMedic.addActionListener(this);
        userNou.addActionListener(this);
        totiUserii.addActionListener(this);
        salarii.addActionListener(this);
        pacientNou.addActionListener(this);
        orar.addActionListener(this);
        servicii.addActionListener(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setVisible(true);

        salvareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String statement = "CALL completare_raport(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                    String find = "SELECT nume, prenume FROM asistent_medical, users WHERE asistent_medical.cnp = users.cnp_user AND users.username = '" + uid + "';";

                    Statement findAssistant = con.createStatement();
                    ResultSet rst = findAssistant.executeQuery(find);
                    rst.next();

                    String numeAsistent = rst.getString("nume");
                    String prenumeAsistent = rst.getString("prenume");

                    //se preiau datele raportului care urmeaza a fi salvat
                    String numePacient = textFieldNumeP.getText();
                    String prenumePacient = textFieldPrenumeP.getText();
                    String numeMedic = textFieldNumeM.getText();
                    String prenumeMedic = textFieldPrenumeM.getText();
                    String dataCons = textFieldData.getText();
                    String serviciu = textFieldServiciu.getText();
                    String istoric = textFieldIstoric.getText();
                    String simptome = textFieldSimptome.getText();
                    String diagnostic = textFieldDiagnostic.getText();
                    String recom = textFieldRecomandari.getText();

                    if(numePacient.isEmpty() || prenumePacient.isEmpty() || numeMedic.isEmpty() || prenumeMedic.isEmpty() ||
                    dataCons.isEmpty() || serviciu.isEmpty() || istoric.isEmpty() || simptome.isEmpty() || diagnostic.isEmpty() ||
                    recom.isEmpty()) {
                        JOptionPane.showMessageDialog(mainPanel, "Completati toate campurile");
                    }
                    else {
                        //daca nu se depisteaza campuri ramase libere, are loc apelul procedurii de creare a raportului medical
                        CallableStatement stmt = con.prepareCall(statement);
                        stmt.setString(1, numePacient);
                        stmt.setString(2, prenumePacient);
                        stmt.setString(3, numeMedic);
                        stmt.setString(4, prenumeMedic);
                        stmt.setString(5, numeAsistent);
                        stmt.setString(6, prenumeAsistent);
                        stmt.setString(7, dataCons);
                        stmt.setString(8, istoric);
                        stmt.setString(9, simptome);
                        stmt.setInt(10, Integer.parseInt(serviciu));
                        stmt.setString(11, diagnostic);
                        stmt.setString(12, recom);

                        stmt.execute();
                    }
                } catch(SQLException ex) {
                    System.out.println("SQL Exception: " + ex);
                    System.exit(1);
                }
            }
        });


        anulareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                DupaLogin fereastra = new DupaLogin(uid, role, con);
                fereastra.setVisible(true);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cautareAngajat) {
            dispose();
            CautareAngajat fereastra = new CautareAngajat(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == cautarePacient) {
            dispose();
            CautarePacient fereastra = new CautarePacient(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == completareRaport) {
            dispose();
            CompletareRaport fereastra = new CompletareRaport(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == pacientiProgramati) {
            dispose();
            PacientiProgramati fereastra = new PacientiProgramati(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == parafareRapoarte) {
            dispose();
            ParafareRapoarte fereastra = new ParafareRapoarte(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == profitCurent) {
            dispose();
            ProfitCurent fereastra = new ProfitCurent(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == programari) {
            dispose();
            Programari fereastra = new Programari(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == vizualizareProfit) {
            dispose();
            VizualizareaProfitului fereastra = new VizualizareaProfitului(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == vizualizareProfitMedic) {
            dispose();
            VizualizareProfitMedic fereastra = new VizualizareProfitMedic(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == emitereBon) {
            dispose();
            EmitereBon fereastra = new EmitereBon(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == userNou) {
            dispose();
            CreareUserNou fereastra = new CreareUserNou(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == totiUserii) {
            dispose();
            VizualizareUseri fereastra = new VizualizareUseri(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == paginaPersonala) {
            dispose();
            DupaLogin fereastra = new DupaLogin(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == salarii) {
            dispose();
            VizualizareSalarii fereastra = new VizualizareSalarii(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == pacientNou) {
            dispose();
            PacientNou fereastra = new PacientNou(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == servicii) {
            dispose();
            Servicii fereastra = new Servicii(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == orar) {
            dispose();
            OrarMedical fereastra = new OrarMedical(uid, role, con);
            fereastra.setVisible(true);
        }
    }
}
