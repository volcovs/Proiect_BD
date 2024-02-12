package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ProgramareNoua extends JFrame implements ActionListener {
    private JPanel mainPanel;
    private JTextField textFieldNumeP;
    private JTextField textFieldPrenumeP;
    private JTextField textFieldData;
    private JTextField textFieldOra;
    private JTextField textFieldNumeM;
    private JTextField textFieldPoliclinica;
    private JTextField textFieldServiciu;
    private JTextField textFieldPrenumeM;
    private JTextField textFieldCabinet;
    private JButton salvareButton;
    private JButton anulareButton;
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

    public ProgramareNoua(String user, String role, Connection connection) {
        this.uid = user;
        this.role = role;
        this.con = connection;

        setTitle("Programare noua");
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
                DupaLogin fereastra = new DupaLogin(user, role, con);
                fereastra.setVisible(true);
            }
        });


        salvareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = textFieldData.getText();
                String timp = textFieldOra.getText();
                String numeP = textFieldNumeP.getText();
                String prenumeP = textFieldPrenumeP.getText();
                String numeM = textFieldNumeM.getText();
                String prenumeM = textFieldPrenumeM.getText();
                String serviciu = textFieldServiciu.getText();
                String locatie = textFieldPoliclinica.getText();
                String cabinet = textFieldCabinet.getText();

                if(data.isEmpty() || timp.isEmpty() || numeP.isEmpty() || prenumeP.isEmpty() || numeM.isEmpty() ||
                prenumeM.isEmpty() || serviciu.isEmpty() || locatie.isEmpty() || cabinet.isEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel, "Completati toate campurile");
                }
                else {
                    try {
                        String findPatient = "SELECT * FROM pacient WHERE nume = '" + numeP + "' AND prenume = '" + prenumeP
                        + "';";
                        Statement find1 = con.createStatement();
                        ResultSet patient = find1.executeQuery(findPatient);
                        patient.next();

                        String findDoctor = "SELECT * FROM medic WHERE nume = '" + numeM + "' AND prenume = '" + prenumeM +
                                "';";
                        Statement find2 = con.createStatement();
                        ResultSet doctor = find2.executeQuery(findDoctor);
                        doctor.next();

                        String findService = "SELECT * FROM serviciu_medical WHERE nume = '" + serviciu + "';";
                        Statement find3 = con.createStatement();
                        ResultSet service = find3.executeQuery(findService);
                        service.next();

                        String statement = "CALL creare_programare(?, ?, ?, ?, ?, ?, ?);";
                        CallableStatement stmt = con.prepareCall(statement);
                        stmt.setString(1, data);
                        stmt.setString(2, timp);
                        stmt.setInt(3, patient.getInt("cnp"));
                        stmt.setInt(4, doctor.getInt("cnp"));
                        stmt.setInt(5, service.getInt("id"));
                        stmt.setInt(6, Integer.parseInt(cabinet));
                        stmt.setString(7, locatie);

                    } catch(Exception ex) {
                        System.out.println("SQL Exception: " + ex);
                        JOptionPane.showMessageDialog(mainPanel, "Nu s-a putut adauga programarea!");
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
