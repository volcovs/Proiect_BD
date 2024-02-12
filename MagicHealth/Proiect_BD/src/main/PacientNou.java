package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PacientNou extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JTextField textFieldNume;
    private JTextField textFieldPrenume;
    private JTextField textFieldCNP;
    private JTextField textFieldDataNasterii;
    private JTextField textFieldStrada;
    private JButton salvareButton;
    private JButton anulareButton;
    private JTextField textFieldNr;
    private JTextField textFieldOras;
    private JTextField textFieldEmail;
    private JTextField textFieldTelefon;
    private Connection con;

    private JMenuBar menuBar;
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

    public PacientNou(String user, String role, Connection connection) {
        this.uid = user;
        this.role = role;
        this.con = connection;

        setTitle("Pacient nou");
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


        anulareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                DupaLogin fereastra = new DupaLogin(uid, role, con);
                fereastra.setVisible(true);
            }
        });


        salvareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nume = textFieldNume.getText();
                String prenume = textFieldPrenume.getText();
                String cnp = textFieldCNP.getText();
                String data = textFieldDataNasterii.getText();
                String email = textFieldEmail.getText();
                String telefon = textFieldTelefon.getText();
                String strada = textFieldStrada.getText();
                String numar = textFieldNr.getText();
                String oras = textFieldOras.getText();

                if (nume.isEmpty() || prenume.isEmpty() || cnp.isEmpty() || data.isEmpty() || strada.isEmpty() ||
                        numar.isEmpty() || oras.isEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel, "Completati toate campurile obligatorii");
                }
                else {
                    try {
                        String statement;
                        if (telefon.isEmpty() && email.isEmpty()) {
                            statement = "INSERT INTO pacient VALUES (" + cnp + ", '" + nume + "', '" + prenume +
                                    "', '" + Date.valueOf(data) + "', '" + oras + "', '" + strada + "', " + numar +
                                    ", NULL, NULL);";

                        } else if (telefon.isEmpty()) {
                            statement = "INSERT INTO pacient VALUES (" + cnp + ", '" + nume + "', '" + prenume +
                                    "', '" + Date.valueOf(data) + "', '" + oras + "', '" + strada + "', " + numar +
                                    ", NULL, '" + email + "');";
                        } else if (email.isEmpty()) {
                            statement = "INSERT INTO pacient VALUES (" + cnp + ", '" + nume + "', '" + prenume +
                                    "', '" + Date.valueOf(data) + "', '" + oras + "', '" + strada + "', " + numar +
                                    ", " + telefon + ", NULL);";
                        } else {
                            statement = "INSERT INTO pacient VALUES (" + cnp + ", '" + nume + "', '" + prenume +
                                    "', '" + Date.valueOf(data) + "', '" + oras + "', '" + strada + "', " + numar +
                                    ", " + telefon + ", '" + email + "');";
                        }

                        PreparedStatement stmt = con.prepareStatement(statement);
                        stmt.execute();

                    } catch (SQLException ex) {
                        System.out.println("SQL Exception: " + ex);
                        System.exit(1);
                    }
                }
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
