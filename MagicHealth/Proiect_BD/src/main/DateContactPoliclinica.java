package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DateContactPoliclinica extends JFrame {
    private JButton BackButton;
    private JLabel NumePoliclinica;
    private JPanel Date_contact;
    private JLabel Adresa;
    private JLabel Program;
    private JLabel Telefon;
    private JLabel Servicii;
    private JLabel Imagine;
    private JLabel Nr_cabinete;


    public DateContactPoliclinica(String policlinica, Connection connection) {
        setTitle("Date de contact");

        setContentPane(Date_contact);
        BackButton.setIcon(createImageIcon("images/back2.jfif"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setVisible(true);
        Imagine.setIcon(createImageIcon("images/cardiogram.png"));

        String statement = "SELECT * FROM policlinica WHERE oras = '" + policlinica + "';";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rst = stmt.executeQuery(statement);
            rst.next();

            //se seteaza datele despre policlinica aleasa, obtinute efectuand interogarea
            NumePoliclinica.setText("Policlinica " + rst.getString("denumire") + " din " + rst.getString("oras"));
            Adresa.setText("Adresa: str. " + rst.getString("strada") + ", " + rst.getString("nr"));
            Program.setText("Program zilnic: " + rst.getString("program"));
            Telefon.setText("Telefon: " + rst.getString("telefon"));
            Servicii.setText("Servicii oferite: " + rst.getString("servicii"));
            Nr_cabinete.setText("Cabinete medicale: " + rst.getString("nr_cabinete"));
        }
        catch (SQLException ex) {
            System.err.println("SQLException: " + ex);
        }


        BackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                PaginaStart fereastra = new PaginaStart(connection);
                fereastra.setVisible(true);
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
