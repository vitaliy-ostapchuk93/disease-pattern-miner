package welwindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Xiazai extends JFrame {
    private static final long serialVersionUID = 1L;
    MainPlugin mainPlugin;
    private JButton jButton1;
    private JProgressBar jProgressBar1;
    private boolean state = false;
    private int count = 0;
    private Thread workThead = null;

    public Xiazai(String path, MainPlugin mainPlugin) {
        this.mainPlugin = mainPlugin;
        initComponents();
        Thread thread = new Thread() {
            public void run() {
                try {
                    String url = "remove_long_transactions_from_transaction_db.jar"
                            .equals(mainPlugin.fileName) ? "http://www.philippe-fournier-viger.com/spmf/plugins/removelongtransactions/removelongtransactions.jar"
                            : "http://www.philippe-fournier-viger.com/spmf/plugins/removeshorttransactions/removeshorttransactions.jar";
                    downLoadFromUrl(url, mainPlugin.fileName, path);
                } catch (IOException e) {
                    e.printStackTrace();
                    state = false;
                    JOptionPane.showMessageDialog(null,
                            "Failed!" + e.getMessage());
                }
            }
        };
        thread.start();
    }

    public static byte[] readInputStream(InputStream inputStream)
            throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    private void initComponents() {

        jProgressBar1 = new JProgressBar();
        jButton1 = new JButton();
        setTitle("SPMF-downloading plugin-please wait...");
        jButton1.setText("Cancel");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
                .createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addComponent(jProgressBar1,
                                        GroupLayout.PREFERRED_SIZE, 288,
                                        GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(71, Short.MAX_VALUE))
                .addGroup(
                        GroupLayout.Alignment.TRAILING,
                        layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE).addComponent(jButton1)
                                .addGap(28, 28, 28)));
        layout.setVerticalGroup(layout.createParallelGroup(
                GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jProgressBar1,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(
                                LayoutStyle.ComponentPlacement.RELATED, 50,
                                Short.MAX_VALUE).addComponent(jButton1)
                        .addContainerGap()));

        jButton1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                state = false;
                setVisible(false);
            }
        });
        pack();
        setLocationRelativeTo(null);
    }

    public void downLoadFromUrl(String urlStr, String fileName, String savePath)
            throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setConnectTimeout(3 * 1000);

        conn.setRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        state = true;
        if (workThead == null) {
            workThead = new WorkThead();
            workThead.start();
        }

        InputStream inputStream = conn.getInputStream();

        byte[] getData = readInputStream(inputStream);

        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        System.out.println("info:" + url + " download success");
    }

    class WorkThead extends Thread {
        public void run() {
            while (count < 100) {
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
                if (state) {
                    count++;
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {

                            jProgressBar1.setValue(count);
                        }
                    });
                }
            }
            mainPlugin.downSuccess();

        }
    }

}
