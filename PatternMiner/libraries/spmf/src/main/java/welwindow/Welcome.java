package welwindow;

import gui.Main;
import gui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Welcome extends JFrame {
    public static final long serialVersionUID = 1L;
    private JButton b1;
    private JButton b2;
    private JButton b3;
    private JButton b4;
    private JButton b5;
    private JButton b6;
    private JButton b7;
    private JLabel j1;
    private JLabel j2;
    private JPanel jPanel;

    private MainWindow mainWindowTools;
    private MainWindow mainWindowAlgorithms;
    private MainPlugin plugin;

    public Welcome() {
        try {
            mainWindowTools = new MainWindow(true, false);
            mainWindowAlgorithms = new MainWindow(false, true);
            setVisible(true);
            mainWindowTools.setDefaultCloseOperation(Welcome.HIDE_ON_CLOSE);
            mainWindowAlgorithms
                    .setDefaultCloseOperation(Welcome.HIDE_ON_CLOSE);
            initComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Welcome().setVisible(true);
    }

    private void initComponents() {
        JFrame frame = new JFrame("SPMF v." + Main.SPMF_VERSION + " - Welcome");
        frame.setLocation(500, 500);
        frame.setSize(900, 200);
        frame.setResizable(false);

        b1 = new JButton();
        b2 = new JButton();
        b3 = new JButton();
        b4 = new JButton();
        b5 = new JButton();
        b6 = new JButton();
        b7 = new JButton();
        j1 = new JLabel();
        j2 = new JLabel();
        jPanel = new JPanel();
        b1.setText("Prepare data (dataset tools)");
        b2.setText("Run an algorithm");
        b3.setText("Add/Remove plugins");
        b4.setText("Run several algorithms");
        b5.setText("Online documentation");
        b6.setText("Preferences");
        b7.setText("About SPMF");

        j1.setText("What would you like to do?");
        j1.setBounds(15, 70, 300, 20);
        frame.add(j1);

        j2.setBounds(15, 15, 150, 50);
        j2.setIcon(new ImageIcon(Welcome.class.getResource("spmf.png")));
        frame.add(j2);

        b1.setBounds(0, 100, 200, 30);
        frame.add(b1);
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                b1ActionPerformed(evt);
            }
        });

        b2.setBounds(220, 100, 140, 30);
        frame.add(b2);
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                b2ActionPerformed(evt);
            }
        });

        b3.setBounds(370, 100, 150, 30);
        frame.add(b3);
        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                b3ActionPerformed(evt);
            }
        });

        b4.setBounds(530, 100, 170, 30);
        frame.add(b4);

        b5.setBounds(710, 100, 160, 30);
        frame.add(b5);
        b5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                b5ActionPerformed(evt);
            }
        });

        b6.setBounds(710, 40, 160, 25);
        frame.add(b6);

        b7.setBounds(710, 10, 160, 25);
        frame.add(b7);
        b7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                b7ActionPerformed(evt);
            }
        });

        frame.add(jPanel);
        frame.setVisible(true);
    }

    private void b1ActionPerformed(ActionEvent evt) {
        mainWindowTools.setVisible(true);
    }

    private void b2ActionPerformed(ActionEvent evt) {
        mainWindowAlgorithms.setVisible(true);
    }

    private void b3ActionPerformed(ActionEvent evt) {
        plugin = new MainPlugin();
        plugin.setDefaultCloseOperation(Welcome.HIDE_ON_CLOSE);
    }

    private void b5ActionPerformed(ActionEvent evt) {

        String url = "http://www.philippe-fournier-viger.com/spmf/index.php?link=documentation.php";
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void b7ActionPerformed(ActionEvent evt) {

        String url = "http://www.philippe-fournier-viger.com/spmf/";
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
