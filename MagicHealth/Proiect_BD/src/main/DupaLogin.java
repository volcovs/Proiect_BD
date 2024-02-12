package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.*;

public class DupaLogin extends JFrame implements ActionListener {
    private JMenuBar menuBar;
    private JLabel WelcomeMsg;
    private JPanel AfisarePaginaUser;
    private JLabel Nume;
    private JLabel Prenume;
    private JLabel oras;
    private JLabel strada;
    private JLabel nr;
    private JLabel dataAngajarii;
    private JLabel cnp;
    private JLabel functie;
    private JLabel salariu;
    private JButton LogOutButton;
    private JLabel contIBAN;

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
    private JMenuItem pacientNou;
    private JMenuItem salarii;
    private JMenuItem orar;
    private JMenuItem servicii;
    private String role;
    private String url = "jdbc:mysql://localhost:3306/lant_policlinici";
    private String timezone = "?serverTimezone=UTC";
    private String uid;
    private String pw;
    private Connection con;

    public DupaLogin(String username, String role, Connection connection) {
        this.uid = username;
        this.role = role;
        this.con = connection;

        setTitle("Pagina personala");
        setContentPane(AfisarePaginaUser);
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
        pacientNou = creareMeniu.getPacientNou();
        salarii = creareMeniu.getSalarii();
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
        pacientNou.addActionListener(this);
        salarii.addActionListener(this);
        orar.addActionListener(this);
        servicii.addActionListener(this);

        String statement = "SELECT * FROM users, angajat WHERE users.username = '" + uid + "' AND users.cnp_user = angajat.cnp;";
        try {
            Statement stmt = con.createStatement();
            ResultSet rst = stmt.executeQuery(statement);
            rst.next();

            //se seteaza datele personale ale angajatului conectat la baza de date, obtinute in urma interogarii
            WelcomeMsg.setText("Bine ai venit, " + rst.getString("nume") + " " + rst.getString("prenume") + "!");

            cnp.setText("CNP: " + rst.getString("cnp"));
            Nume.setText("Nume: " + rst.getString("nume"));
            Prenume.setText("Prenume: " + rst.getString("prenume"));
            oras.setText("Oras: " + rst.getString("oras"));
            strada.setText("Strada: " + rst.getString("strada"));
            nr.setText("Nr: " + rst.getString("nr"));
            dataAngajarii.setText("Data angajarii: " + rst.getString("data_angajare"));
            functie.setText("Functie: " + rst.getString("functie"));
            salariu.setText("Salariul negociat: " + rst.getString("salariu_negociat"));
            contIBAN.setText("Cont IBAN: " + rst.getString("cont_IBAN"));
        }
        catch (SQLException ex) {
            System.err.println("SQLException: " + ex);
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setVisible(true);

        LogOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //se inchide conexiunea pentru user, se redeschide cea pentru root
                try {
                    con.close();
                } catch (SQLException ex) {
                    System.err.println("SQLException: " + ex);
                    System.exit(1);
                }

                con = null;
                try {
                    con = DriverManager.getConnection(url + timezone, "root", "TheHateUGive13$");
                } catch (SQLException ex) {
                    System.err.println("SQLException: " + ex);
                    System.exit(1);
                }

                dispose();
                //se revine la paginea de start, de unde se poate loga un alt utilizator
                PaginaStart fereastra = new PaginaStart(con);
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
        } else if (e.getSource() == pacientNou) {
            dispose();
            PacientNou fereastra = new PacientNou(uid, role, con);
            fereastra.setVisible(true);
        } else if (e.getSource() == salarii) {
            dispose();
            VizualizareSalarii fereastra = new VizualizareSalarii(uid, role, con);
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
