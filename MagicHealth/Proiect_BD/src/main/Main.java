package main;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends JFrame {
    private String url = "jdbc:mysql://localhost:3306/lant_policlinici";
    private String timezone = "?serverTimezone=UTC";
    private	String uid = "root";
    private	String pw = "TheHateUGive13$";
    private static Connection con;

    public static void main(String[] args)
    {
        Main mainApp = new Main();
        mainApp.init();
        mainApp.run();
    }

    private void init()
    {
        //conexiunea se initiliazeaza mai intai ca root, apoi se va inchide si redeschide pentru user
        con = null;
        try {
            con = DriverManager.getConnection(url+timezone, uid, pw);
        }
        catch (SQLException ex) {
            System.err.println("SQLException: " + ex);
            System.exit(1);
        }
    }

    private void run() {
        PaginaStart paginaStart = new PaginaStart(con);
    }

    public static Connection getCon() {
        return con;
    }

}