package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;

public class VizualizareProfitMedic extends JFrame implements ActionListener {

    private JPanel mainPanel;
    private JButton vizualizareProfitPeLocatieButton;
    private JButton vizualizareProfitPeSpecialitateButton;
    private JTable tableProfitMedici;
    private JTextField textFieldText;
    private JLabel label;

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

    public VizualizareProfitMedic(String user, String role, Connection connection) {
        this.uid = user;
        this.role = role;
        this.con = connection;

        setTitle("Vizualizarea profitului medicilor");
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

        label.setVisible(false);

        vizualizareProfitPeLocatieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String locatie = textFieldText.getText();
                    if(locatie.isEmpty()) {
                        JOptionPane.showMessageDialog(mainPanel, "Introduceti locatia");
                    }
                    else {
                        String[] coloane = {"CNP medic", "suma"};

                        String statement = "CALL profit_lunar_medic_locatie(?, ?, ?);";
                        CallableStatement stmt = con.prepareCall(statement);

                        String find = "SELECT * FROM policlinica WHERE denumire = '" + locatie + "';";
                        Statement stmt2 = con.createStatement();
                        ResultSet found = stmt2.executeQuery(find);
                        found.next();

                        stmt.setInt(1, LocalDate.now().getYear());
                        stmt.setInt(2, LocalDate.now().getMonthValue());
                        stmt.setInt(3, found.getInt("id"));

                        String maxCount = "SELECT count(*) FROM medic;";
                        Statement count = con.createStatement();
                        ResultSet max = count.executeQuery(maxCount);
                        max.next();

                        ResultSet rst = stmt.executeQuery();
                        Object[][] data = new Object[max.getInt("count(*)")][2];
                        int i = 0;

                        while(rst.next()) {
                            data[i][0] = rst.getString("cnp_medic");
                            data[i][1] = rst.getString("suma");
                            i++;
                        }

                        JTable table = new JTable(data, coloane);
                        tableProfitMedici.setModel(table.getModel());
                        label.setVisible(true);
                    }

                } catch(SQLException ex) {
                    System.out.println("SQL Exception: " + ex);
                    JOptionPane.showMessageDialog(mainPanel, "Locatie inexistenta");
                }
            }
        });


        vizualizareProfitPeSpecialitateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String specialitate = textFieldText.getText();

                    if (specialitate.isEmpty()) {
                        JOptionPane.showMessageDialog(mainPanel, "Completati campul de specialitate");
                    }
                    else {
                        String[] coloane = {"CNP medic", "suma"};

                        String statement = "CALL profit_lunar_medic_specialitate(?, ?, ?);";
                        CallableStatement stmt = con.prepareCall(statement);

                        stmt.setInt(1, LocalDate.now().getYear());
                        stmt.setInt(2, LocalDate.now().getMonthValue());
                        stmt.setString(3, specialitate);

                        String maxCount = "SELECT count(*) FROM medic;";
                        Statement count = con.createStatement();
                        ResultSet max = count.executeQuery(maxCount);
                        max.next();

                        ResultSet rst = stmt.executeQuery();
                        Object[][] data = new Object[max.getInt("count(*)")][2];
                        int i = 0;

                        while (rst.next()) {
                            data[i][0] = rst.getString("cnp_medic");
                            data[i][1] = rst.getString("suma");
                            i++;
                        }

                        JTable table = new JTable(data, coloane);
                        tableProfitMedici.setModel(table.getModel());
                        label.setVisible(true);
                    }
                } catch(SQLException ex) {
                    System.out.println("SQL Exception: " + ex);
                    JOptionPane.showMessageDialog(mainPanel, "Specialitate inexistenta");
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
