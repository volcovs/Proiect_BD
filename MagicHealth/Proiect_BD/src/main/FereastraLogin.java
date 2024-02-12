package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.net.URL;
import java.sql.*;

public class FereastraLogin extends JFrame {
    private JPanel panel1;
    private JTextField UsernameInput;
    private JPasswordField PasswordInput;
    private JButton ButonLogin;
    private JLabel UsernameField;
    private JLabel PasswordField;
    private JLabel Titlu;
    private JButton backButton;
    private JRadioButton ParolaVizibila;

    private String url = "jdbc:mysql://localhost:3306/lant_policlinici";
    private String timezone = "?serverTimezone=UTC";
    private String uid;
    private String pw;
    private Connection con;

    public FereastraLogin(Connection connection) {
        setTitle("Pagina login");

        setContentPane(panel1);
        backButton.setIcon(createImageIcon("images/back2.jfif"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setVisible(true);
        this.con = connection;

        backButton.addActionListener(new ActionListener() {
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
                //se revine la pagina de start
                PaginaStart fereastra = new PaginaStart(con);
                fereastra.setVisible(true);
            }
        });

        ButonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //se iau datele din campurile de text pentru crearea unei noi conexiuni,
                //cea veche se inchide
                try {
                    con.close();
                } catch (SQLException ex) {
                    System.err.println("SQLException: " + ex);
                    System.exit(1);
                }

                try {
                    con = null;
                    uid = UsernameInput.getText();
                    pw = PasswordInput.getText();

                    con = DriverManager.getConnection(url + timezone, uid, pw);

                    String statement = "SELECT rol FROM users WHERE users.username = '" + uid + "';";
                    try {
                        Statement stmt = con.createStatement();
                        ResultSet rst = stmt.executeQuery(statement);
                        rst.next();
                        dispose();
                        DupaLogin fereastra = new DupaLogin(uid, rst.getString("rol"), con);
                        fereastra.setVisible(true);
                    }
                    catch (SQLException ex) {
                        System.err.println("SQLException: " + ex);
                    }
                } catch (SQLException ex) {
                    //se redeschide prima conexiune in caz de eroare
                    JOptionPane.showMessageDialog(panel1, "Date invalide de conectare");
                    con = null;
                    try {
                        con = DriverManager.getConnection(url + timezone, "root", "TheHateUGive13$");
                    } catch (SQLException except) {
                        System.err.println("SQLException: " + except);
                        System.exit(1);
                    }
                }
            }
        });

        ParolaVizibila.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ParolaVizibila.isSelected()) {
                    PasswordInput.setEchoChar((char)0);
                }
                else {
                    PasswordInput.setEchoChar('•');
                }
            }
        });
    }

    protected static ImageIcon createImageIcon(String path) {
        URL imgURL = Main.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}
