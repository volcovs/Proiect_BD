package main;

import main.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VizualizareConcediu extends JFrame implements ActionListener {
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
    private JPanel mainPanel;
    private JLabel labelConcediu;
    private JLabel labelData;

    public VizualizareConcediu(String user, String role, Connection connection, Integer cnp) {
        this.uid = user;
        this.role = role;
        this.con = connection;

        setTitle("Vizualizare concediu angajat");
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

        try {
            String statement = "SELECT * FROM angajat WHERE cnp = " + cnp + ";";
            Statement stmt = con.createStatement();
            ResultSet rst = stmt.executeQuery(statement);
            rst.next();

            labelConcediu.setText("Concediul pentru angajatul " + rst.getString("nume") + " " + rst.getString("prenume")
            + ":");

            labelData.setText("De pe " + rst.getString("data_inceput_concediu") + " pana pe " + rst.getString("data_final_concediu"));

        } catch(SQLException ex) {
            System.out.println("SQL Exception: " + ex);
            System.exit(1);
        }

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
