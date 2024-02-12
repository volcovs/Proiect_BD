package main;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParafareRapoarte extends JFrame implements ActionListener {
    private JList listRapoarte;
    private JLabel Text;
    private JPanel mainPanel;
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


    public ParafareRapoarte(String username, String role, Connection connection) {
        this.uid = username;
        this.role = role;
        this.con = connection;

        setTitle("Rapoarte neparafate");
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
        servicii = creareMeniu.getServicii();
        orar = creareMeniu.getOrar();

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

        if (role.equals("medic")) {
            try {
                Integer count = 0;
                String statement = "CALL afisare_rapoarte_neparafate(?);";

                String findDoctor = "SELECT cnp FROM medic, users WHERE medic.cnp = users.cnp_user AND users.username = '" + uid + "';";
                Statement find = con.createStatement();
                ResultSet found = find.executeQuery(findDoctor);
                found.next();

                CallableStatement stmt = con.prepareCall(statement);
                stmt.setInt(1, found.getInt("cnp"));
                List<String> list = new ArrayList<String>();
                List<Integer> indexList = new ArrayList<Integer>();
                ResultSet rst = stmt.executeQuery();
                while (rst.next()) {
                    count++;
                    //salvez indicii rapoartelor intr-o lista separata, pentru a putea transmite parametrul corect la vizualizare
                    indexList.add(rst.getInt("id"));
                    list.add(rst.getString("id") + " " + rst.getString("nume_pacient") + " " +
                            rst.getString("prenume_pacient") + " " + rst.getString("data_consultatie"));
                }

                Text.setText("Aveti " + count + " rapoarte neparafate.");
                listRapoarte.setListData(list.toArray());

                listRapoarte.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent evt) {
                        JList list = (JList) evt.getSource();
                        if (evt.getClickCount() == 2) {

                            // Double-click detected
                            int index = list.locationToIndex(evt.getPoint());

                            dispose();
                            VizualizareRaport fereastra = new VizualizareRaport(uid, role, con, indexList.get(index));
                            fereastra.setVisible(true);
                        }
                    }
                });

            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex);
                System.exit(1);
            }
        }
        else {
            try {
                Text.setText("Rapoarte disponibile: ");
                String statement = "SELECT * FROM angajat, users WHERE users.cnp_user = angajat.cnp AND users.username = '" + uid + "';";
                Statement find = con.createStatement();
                ResultSet found = find.executeQuery(statement);
                found.next();

                String statement2 = "SELECT * FROM raport_medical WHERE nume_asistent = '" + found.getString("nume") + "' AND " +
                        "prenume_asistent = '" + found.getString("prenume") + "';";
                Statement stmt = con.createStatement();
                ResultSet rapoarte = stmt.executeQuery(statement2);

                List<String> listAsistent = new ArrayList<String>();

                while (rapoarte.next()) {
                    listAsistent.add(rapoarte.getString("id") + " " + rapoarte.getString("nume_pacient") + " " +
                            rapoarte.getString("prenume_pacient") + " " + rapoarte.getString("data_consultatie") +
                            "    " + rapoarte.getString("parafat"));
                }

                listRapoarte.setListData(listAsistent.toArray());

            }catch(SQLException exception) {
                System.out.println("SQL Exception: " + exception);
                System.exit(1);
            }
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
