package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class CreareMeniu {
    private JMenuBar menuBar;
    private JMenu meniuProceduri;
    private JMenuItem paginaPersonala;
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
    private JMenuItem orar;
    private JMenuItem salarii;
    private JMenuItem pacientNou;
    private JMenuItem servicii;

    public void createMenuBar() {
        menuBar = new JMenuBar();
        meniuProceduri = new JMenu("Meniu");
        meniuProceduri.setMnemonic(KeyEvent.VK_A);
        menuBar.add(meniuProceduri);

        paginaPersonala = new JMenuItem("Pagina personala");
        paginaPersonala.setMnemonic(KeyEvent.VK_D);
        //valabil pentru orice fel de utilizator
        meniuProceduri.add(paginaPersonala);

        completareRaport = new JMenuItem("Completare raport nou");
        completareRaport.setMnemonic(KeyEvent.VK_D);
        pacientiProgramati = new JMenuItem("Pacienti programati");
        pacientiProgramati.setMnemonic(KeyEvent.VK_D);
        cautarePacient = new JMenuItem("Cautare pacient");
        cautarePacient.setMnemonic(KeyEvent.VK_D);
        parafareRapoarte = new JMenuItem("Rapoarte");
        parafareRapoarte.setMnemonic(KeyEvent.VK_D);
        pacientiProgramati = new JMenuItem("Pacienti programati");
        pacientiProgramati.setMnemonic(KeyEvent.VK_D);
        cautareAngajat = new JMenuItem("Cautare angajat");
        cautareAngajat.setMnemonic(KeyEvent.VK_D);
        profitCurent = new JMenuItem("Vizualizare profit curent");
        profitCurent.setMnemonic(KeyEvent.VK_D);
        vizualizareProfit = new JMenuItem("Profit general");
        vizualizareProfit.setMnemonic(KeyEvent.VK_D);
        vizualizareProfitMedic = new JMenuItem("Profit medici");
        vizualizareProfitMedic.setMnemonic(KeyEvent.VK_D);
        programari = new JMenuItem("Programari");
        programari.setMnemonic(KeyEvent.VK_D);
        emitereBon = new JMenuItem("Emitere bon");
        emitereBon.setMnemonic(KeyEvent.VK_D);
        userNou = new JMenuItem("Creare user nou");
        userNou.setMnemonic(KeyEvent.VK_D);
        totiUserii = new JMenuItem("Vizualizare useri");
        totiUserii.setMnemonic(KeyEvent.VK_D);
        orar = new JMenuItem("Orar");
        orar.setMnemonic(KeyEvent.VK_D);
        salarii = new JMenuItem("Salarii angajati");
        salarii.setMnemonic(KeyEvent.VK_D);
        pacientNou = new JMenuItem("Adaugare pacient");
        pacientNou.setMnemonic(KeyEvent.VK_D);
        servicii = new JMenuItem(("Adaugare serviciu specializat"));
        servicii.setMnemonic(KeyEvent.VK_D);
    }

    public JMenuBar createMenu(String role) {
        if (role.equals("asistent_medical")) {
            menuBar = createMenuBarAsistent();
        } else if (role.equals("medic")) {
            menuBar = createMenuBarMedic();
        } else if (role.equals("inspector_resurse_umane")) {
            menuBar = createMenuBarInspector();
        } else if (role.equals("contabil")) {
            menuBar = createMenuBarContabil();
        } else if (role.equals("receptioner")) {
            menuBar = createMenuBarReceptioner();
        } else if (role.equals("administrator")) {
            menuBar = createMenuBarAdmin();
        } else {
            menuBar = createMenuBarSuperAdmin();
        }

        return menuBar;
    }

    public JMenuBar createMenuBarAsistent() {
        createMenuBar();

        meniuProceduri.add(orar);
        meniuProceduri.add(completareRaport);
        meniuProceduri.add(pacientiProgramati);
        meniuProceduri.add(parafareRapoarte);

        return menuBar;
    }

    public JMenuBar createMenuBarMedic() {
        createMenuBar();

        meniuProceduri.add(orar);
        meniuProceduri.add(cautarePacient);
        meniuProceduri.add(parafareRapoarte);
        meniuProceduri.add(pacientiProgramati);

        return menuBar;
    }

    public JMenuBar createMenuBarInspector() {
        createMenuBar();

        meniuProceduri.add(cautareAngajat);
        meniuProceduri.add(servicii);

        return menuBar;
    }

    public JMenuBar createMenuBarContabil() {
        createMenuBar();


        meniuProceduri.add(profitCurent);
        meniuProceduri.add(vizualizareProfit);
        meniuProceduri.add(vizualizareProfitMedic);
        meniuProceduri.add(salarii);

        return menuBar;
    }

    public JMenuBar createMenuBarReceptioner() {
        createMenuBar();

        meniuProceduri.add(orar);
        meniuProceduri.add(programari);
        meniuProceduri.add(emitereBon);
        meniuProceduri.add(pacientNou);

        return menuBar;
    }

    public JMenuBar createMenuBarAdmin() {
       createMenuBar();

       meniuProceduri.add(userNou);
       meniuProceduri.add(totiUserii);

        return menuBar;
    }

    public JMenuBar createMenuBarSuperAdmin() {
        createMenuBar();

        meniuProceduri.add(userNou);
        meniuProceduri.add(totiUserii);

        return menuBar;
    }

    public JMenuItem getCautareAngajat() { return cautareAngajat; }
    public JMenuItem getCautarePacient() { return cautarePacient; }
    public JMenuItem getCompletareRaport() { return completareRaport; }
    public JMenuItem getPacientiProgramati() { return pacientiProgramati; }
    public JMenuItem getParafareRapoarte() { return parafareRapoarte; }
    public JMenuItem getProfitCurent() { return profitCurent; }
    public JMenuItem getVizualizareProfit() { return vizualizareProfit; }
    public JMenuItem getProgramari() { return programari; }
    public JMenuItem getVizualizareProfitMedic() { return vizualizareProfitMedic; }
    public JMenuItem getEmitereBon() { return emitereBon; }
    public JMenuItem getUserNou() { return userNou; }
    public JMenuItem getTotiUserii() { return totiUserii; }
    public JMenuItem getPaginaPersonala() { return paginaPersonala; }
    public JMenuItem getOrar() { return orar; }
    public JMenuItem getSalarii() { return salarii; }
    public JMenuItem getPacientNou() { return pacientNou; }
    public JMenuItem getServicii() { return servicii; }
}