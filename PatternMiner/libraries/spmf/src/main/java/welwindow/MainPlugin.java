package welwindow;

import gui.Main;
import gui.PreferencesManager;
import test.MainTestApriori_saveToFile;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MainPlugin extends JFrame {

    private static final long serialVersionUID = 1L;
    public String fileName;
    DefaultTableModel tableModel;
    String[][] data = {
            {"remove_long_transactions_from_transaction_db",
                    "Philippe Fournier-viger", "Dataset tools", "No", "1",
                    "3ko"},
            {"remove_short_transactions_from_transaction_db",
                    "Philippe Fournier-viger", "Dataset tools", "No", "1",
                    "3ko"}};
    String[] title = {"Name", "Author", "Category", "Installed?", "Version",
            "Size"};
    private JButton jButton1;
    private JButton jButton2Install;
    private JButton jButton3Remove;
    private JButton jButton4Webpage;
    private JButton jButton5Reset;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTable jTable1;
    private JTextArea jTextArea1;
    private JTextField jTextField1;

    public MainPlugin() {
        initComponents();
    }

    public static void main(String args[]) {
        new MainPlugin().setVisible(true);
    }

    private void initComponents() {
        JFrame frame1 = new JFrame("SPMF-V." + Main.SPMF_VERSION
                + "-Add/Remove plugins");
        frame1.setLocation(500, 100);
        frame1.setSize(700, 400);
        frame1.setResizable(false);

        tableModel = new DefaultTableModel(data, title);

        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();
        jButton1 = new JButton();
        jButton2Install = new JButton();
        jButton3Remove = new JButton();
        jButton4Webpage = new JButton();
        jButton5Reset = new JButton();
        jTextArea1 = new JTextArea();
        jPanel1 = new JPanel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable(tableModel);

        jTextField1 = new JTextField();
        jTextField1 = new JTextField(
                "http://www.philippe-fournier-viger.com/spmf/plugins/");

        jLabel5.setText("Plugin repository:");
        jLabel6.setText("Documentation:");
        jLabel7.setText("Description:");
        jLabel8.setText("List of plugins:");
        jLabel9.setText("Select a folder for storing plugins on your computer:");

        jTextArea1.setLineWrap(true);
        jTextArea1.setFont(new Font("Arial", Font.PLAIN, 13));
        jScrollPane1.setViewportView(jTable1);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (jTable1.rowAtPoint(e.getPoint()) == jTable1
                            .getSelectedRow()) {
                        fileName = data[jTable1.getSelectedRow()][0] + ".jar";
                        String des = "remove_long_transactions_from_transaction_db.jar"
                                .equals(fileName) ? "This algorithm removes transactions that contain less "
                                + "than a maximum number of items from a transaction database"
                                : "This algorithm removes transactions that contain less "
                                + "than a minimum number of items from a transaction database";
                        jTextArea1.setText(des);
                    }

                    // if the user has selected something
                    if (jTable1.getSelectedRow() != -1) {
                        jButton2Install.setEnabled(true);
                        jButton3Remove.setEnabled(true);
                        jButton4Webpage.setEnabled(true);
                    }
                }
            }
        });

        jButton1.setText("...");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2Install.setText("Install/Update");
        jButton2Install.setEnabled(false);
        jButton2Install.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3Remove.setText("Remove");
        jButton3Remove.setEnabled(false);
        jButton3Remove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4Webpage.setText("Open webpage");
        jButton4Webpage.setEnabled(false);
        jButton4Webpage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5Reset.setText("Reset to default");
        jButton5Reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton1.setSize(40, 20);
        jButton1.setLocation(330, 20);
        frame1.add(jButton1);

        jButton2Install.setSize(140, 30);
        jButton2Install.setLocation(200, 300);
        frame1.add(jButton2Install);

        jButton3Remove.setSize(140, 30);
        jButton3Remove.setLocation(380, 300);
        frame1.add(jButton3Remove);

        jButton4Webpage.setSize(220, 20);
        jButton4Webpage.setLocation(250, 250);
        frame1.add(jButton4Webpage);

        jButton5Reset.setSize(140, 20);
        jButton5Reset.setLocation(450, 50);
        frame1.add(jButton5Reset);

        jLabel9.setBounds(25, 20, 300, 20);
        frame1.add(jLabel9);

        jLabel8.setBounds(25, 80, 300, 20);
        frame1.add(jLabel8);

        jLabel7.setBounds(140, 160, 300, 20);
        frame1.add(jLabel7);

        jLabel6.setBounds(140, 250, 300, 20);
        frame1.add(jLabel6);

        jLabel5.setBounds(25, 50, 120, 20);
        frame1.add(jLabel5);

        jTextField1.setBounds(140, 50, 300, 20);
        jTextField1.setOpaque(true);
        frame1.add(jTextField1);

        jTextArea1.setSize(430, 75);
        jTextArea1.setLocation(220, 160);
        frame1.add(jTextArea1);

        jScrollPane1.setSize(520, 65);
        jScrollPane1.setLocation(130, 85);
        frame1.add(jScrollPane1);

        jTable1.setShowGrid(false);
        jTable1.setFont(new Font("Menu.font", Font.PLAIN, 10));
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(350);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(250);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(150);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(120);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(100);

        frame1.add(jPanel1);
        frame1.setVisible(true);
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        File path;
        // Get the last path used by the user, if there is one
        String previousPath = PreferencesManager.getInstance()
                .getPluginFolderFilePath();

        // If there is no previous path (first time user),
        // show the files in the "examples" package of
        // the spmf distribution.
        if (previousPath == null) {
            URL main = MainTestApriori_saveToFile.class
                    .getResource("MainTestApriori_saveToFile.class");
            if (!"file".equalsIgnoreCase(main.getProtocol())) {
                path = null;
            } else {
                path = new File(main.getPath());
            }
        } else {
            // Otherwise, use the last path used by the user.
            path = new File(previousPath);
        }

        JFileChooser chooser = new JFileChooser(path.getAbsolutePath());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        if (f != null) {
            String selectedPath = f.getPath();
            PreferencesManager.getInstance().setPluginFolderFilePath(
                    selectedPath);

            System.out.println(selectedPath);
        }
    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        String path = PreferencesManager.getInstance()
                .getPluginFolderFilePath();
        if (this.jTextArea1.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null, "Please select a plug-in.");
            return;
        }
        new Xiazai(path, this).setVisible(true);
    }

    private void jButton3ActionPerformed(ActionEvent evt) {
        String path = PreferencesManager.getInstance()
                .getPluginFolderFilePath();
        path += "\\" + fileName;
        File file = new File(path);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "Null!");

            return;
        } else {
            if (file.delete()) {
                if ("remove_long_transactions_from_transaction_db.jar"
                        .equals(fileName)) {
                    data[0][3] = "No";
                    tableModel.setValueAt("No", 0, 3);
                } else {
                    data[1][3] = "No";
                    tableModel.setValueAt("No", 1, 3);
                }

                tableModel.fireTableDataChanged();
            } else {
                JOptionPane.showMessageDialog(null, "Null!");
            }
        }

    }

    public void downSuccess() {
        if ("remove_long_transactions_from_transaction_db.jar".equals(fileName)) {
            data[0][3] = "Yes";
            tableModel.setValueAt("Yes", 0, 3);
        } else {
            data[1][3] = "Yes";
            tableModel.setValueAt("Yes", 1, 3);
        }
        tableModel.fireTableDataChanged();
    }

    private void jButton4ActionPerformed(ActionEvent evt) {
        if (this.jTextArea1.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null, "Please select a plugin!");
            return;
        }
        try {
            String url = "remove_long_transactions_from_transaction_db.jar"
                    .equals(fileName) ? "http://www.philippe-fournier-viger.com/spmf/plugins/"
                    + "removelongtransactions/removelongtransactions/documentation.php"
                    : "http://www.philippe-fournier-viger.com/spmf/plugins/"
                    + "removeshorttransactions/removeshorttransactions/documentation.php";
            java.net.URI uri = java.net.URI.create(url);
            Desktop dp = Desktop.getDesktop();
            if (dp.isSupported(Desktop.Action.BROWSE)) {
                dp.browse(uri);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Null!");
        }
    }

    private void jButton5ActionPerformed(ActionEvent evt) {
        jTextField1
                .setText("http://www.philippe-fournier-viger.com/spmf/plugins/");
        jTextField1.requestFocus();
    }

}
