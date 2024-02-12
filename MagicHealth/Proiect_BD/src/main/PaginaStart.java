package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.Connection;

public class PaginaStart extends JFrame implements ActionListener {
    private JButton button1;
    private JPanel panel1;
    private JLabel Label;
    private JLabel imagine;
    private JPanel panel3;
    private JPanel panel2;
    private JMenuBar menuBar;
    private JMenu detalii;
    private JMenuItem policlinicaCluj;
    private JMenuItem policlinicaBistrita;
    private JMenuItem policlinicaBucuresti;
    private Connection con;

    public PaginaStart(Connection connection) {
        setTitle("Pagina lantului de policlinici MagicHealth");

        setContentPane(panel1);
        menuBar = createMenuBar();
        this.setJMenuBar(menuBar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setVisible(true);
        this.con = connection;

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                FereastraLogin fereastra = new FereastraLogin(con);
                fereastra.setVisible(true);
            }
        });
    }

    public JMenuBar createMenuBar() {

        ImageIcon icon = createImageIcon("images/medicina.jfif");
        imagine.setIcon(icon);
        Dimension size = imagine.getPreferredSize();
        //imagine.setBounds(0, 0, size.width, size.height);

        menuBar = new JMenuBar();
        detalii = new JMenu("Detalii policlinici");
        detalii.setMnemonic(KeyEvent.VK_A);
        menuBar.add(detalii);

        policlinicaCluj = new JMenuItem("Cluj-Napoca");
        policlinicaCluj.setMnemonic(KeyEvent.VK_D);
        detalii.add(policlinicaCluj);

        policlinicaBistrita = new JMenuItem("Bistrita");
        policlinicaBistrita.setMnemonic(KeyEvent.VK_D);
        detalii.add(policlinicaBistrita);

        policlinicaBucuresti = new JMenuItem("Bucuresti");
        policlinicaBucuresti.setMnemonic(KeyEvent.VK_D);
        detalii.add(policlinicaBucuresti);

        policlinicaCluj.addActionListener(this);
        policlinicaBistrita.addActionListener(this);
        policlinicaBucuresti.addActionListener(this);

        return menuBar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == policlinicaCluj) {
            dispose();
            DateContactPoliclinica fereastra = new DateContactPoliclinica("Cluj-Napoca", con);
            fereastra.setVisible(true);
        } else if (e.getSource() == policlinicaBistrita) {
            dispose();
            DateContactPoliclinica fereastra = new DateContactPoliclinica("Bistrita", con);
            fereastra.setVisible(true);
        } else if (e.getSource() == policlinicaBucuresti) {
            dispose();
            DateContactPoliclinica fereastra = new DateContactPoliclinica("Bucuresti", con);
            fereastra.setVisible(true);
        }
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