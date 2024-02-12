package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CautareAngajat extends JFrame implements ActionListener {
    private JTextField textFieldNume;
    private JTextField textFieldPrenume;
    private JTextField textFieldFunctie;
    private JButton cautareButton;
    private JButton vizualizareOrarButton;
    private JPanel mainPanel;
    private JButton vizualizareConcediuButton;
    private JLabel labelCNP;
    private JLabel labelNumePrenume;
    private JLabel labelAdresa;
    private JLabel labelNrContract;
    private JLabel labelDataAng;
    private JLabel labelDataConc;
    private JLabel labelTelefon;
    private JLabel labelEmail;
    private JLabel labelDepartament;
    private JLabel labelSpeciale;
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

    private String numeAngajat;
    private String prenumeAngajat;
    private String functieAngajat;
    private Integer cnpAngajat;

    public CautareAngajat(String user, String role, Connection connection) {
        this.uid = user;
        this.role = role;
        this.con = connection;

        setTitle("Cautare angajat");

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
        setSize(900, 700);
        setVisible(true);

        //la inceput, toate campurile sunt ascunse
        vizualizareOrarButton.setVisible(false);
        vizualizareConcediuButton.setVisible(false);
        labelCNP.setVisible(false);
        labelNumePrenume.setVisible(false);
        labelAdresa.setVisible(false);
        labelDataAng.setVisible(false);
        labelDataConc.setVisible(false);
        labelDepartament.setVisible(false);
        labelEmail.setVisible(false);
        labelTelefon.setVisible(false);
        labelNrContract.setVisible(false);
        labelSpeciale.setVisible(false);

        cautareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //se ia preiau datele introduse de utilizator
                    String nume = textFieldNume.getText();
                    String prenume = textFieldPrenume.getText();
                    String functie = textFieldFunctie.getText();

                    if (nume.isEmpty() || prenume.isEmpty() || functie.isEmpty()) {
                        JOptionPane.showMessageDialog(mainPanel, "Completati toate campurile");
                    }
                    else {
                        //are loc apelul de procedura pentru a gasi angajatul cu numele, prenumele si functia indicate
                        String statement = "CALL gasire_angajat(?, ?, ?)";
                        CallableStatement stmt = con.prepareCall(statement);
                        //setarea parametrilor de intrare ai procedurii
                        stmt.setString(1,  nume);
                        stmt.setString(2,  prenume);
                        stmt.setString(3, functie);

                        ResultSet rst = stmt.executeQuery();
                        //rst pointeaza de fapt spre niste data de dinainte de tuplele rezultat necesare,
                        //de aceea trebuie mutat cu metoda next()
                        rst.next();

                        //se seteaza datele personale ale angajatului cautat
                        cnpAngajat = rst.getInt("cnp");
                        numeAngajat = rst.getString("nume");
                        prenumeAngajat = rst.getString("prenume");
                        functieAngajat = functie;
                        labelCNP.setText("CNP: " + rst.getString("cnp"));
                        labelNumePrenume.setText("Angajat: " + rst.getString("nume") + " " + rst.getString("prenume"));
                        labelAdresa.setText("Adresa: str." + rst.getString("strada") + " " + rst.getString("nr") + ", " + rst.getString("oras"));
                        labelDataAng.setText("Data angajarii: " + rst.getString("data_angajare"));
                        labelDataConc.setText("Data concedierii: " + rst.getString("data_concediere"));
                        labelNrContract.setText("Nr. contract: " + rst.getString("nr_contract"));
                        labelEmail.setText("E-mail: " + rst.getString("email"));
                        labelTelefon.setText("Telefon: " + rst.getString("telefon"));

                        labelCNP.setVisible(true);
                        labelNumePrenume.setVisible(true);
                        labelEmail.setVisible(true);
                        labelTelefon.setVisible(true);
                        labelNrContract.setVisible(true);
                        labelDataConc.setVisible(true);
                        labelDataAng.setVisible(true);
                        labelAdresa.setVisible(true);

                        if(functie.equals("medic")) {
                            labelSpeciale.setText("Grad: " + rst.getString("grad") + "   Titlu stiintific: " + rst.getString("titlu_stiintific") +
                                    "    Post didactic: " + rst.getString("post_didactic") + "   Cod parafa: " + rst.getString("cod_parafa"));
                            labelSpeciale.setVisible(true);
                        }
                        else if(functie.equals("asistent_medical")) {
                            labelSpeciale.setText("Tip: " + rst.getString("tip") + "    Grad: " + rst.getString("grad"));
                            labelSpeciale.setVisible(true);
                        }
                        else {
                            String statementDep = "SELECT * FROM departament WHERE id = " + rst.getString("id_departament");
                            Statement stmtDep = con.createStatement();
                            ResultSet rst2 = stmtDep.executeQuery(statementDep);
                            rst2.next();
                            labelDepartament.setText("Departament: " + rst2.getString("nume"));
                            labelDepartament.setVisible(true);
                        }

                        vizualizareConcediuButton.setVisible(true);
                        vizualizareOrarButton.setVisible(true);
                    }

                } catch(SQLException ex) {
                    System.out.println("SQL Exception: " + ex);
                    JOptionPane.showMessageDialog(mainPanel, "Angajatul nu a fost gasit");
                }
            }
        });

        vizualizareOrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                //se trece in fereastra de vizualizare a orarului angajatului gasit (daca a fost gasit)
                OrarAngajat fereastra = new OrarAngajat(uid, role, con, numeAngajat, prenumeAngajat, functieAngajat);
                fereastra.setVisible(true);
            }
        });

        vizualizareConcediuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                //se trece in fereastra de vizualizare a concediului angajatului gasit
                VizualizareConcediu fereastra = new VizualizareConcediu(uid, role, con, cnpAngajat);
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
