package models.data;

import java.io.*;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DatasetZipper {
    private final static Logger LOGGER = Logger.getLogger(DatasetZipper.class.getName());

    public static void unzipDatasetFiles(File source, File datasetDir) {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(source);
            Enumeration<? extends ZipEntry> e = zipFile.entries();

            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                File destinationPath = new File(datasetDir, entry.getName());

                destinationPath.getParentFile().mkdirs();

                if (entry.isDirectory() || (destinationPath.isFile() && destinationPath.exists())) {
                    continue;
                } else {
                    LOGGER.info("Extracting file: " + destinationPath);

                    BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

                    int b;
                    byte buffer[] = new byte[1024];

                    FileOutputStream fos = new FileOutputStream(destinationPath);
                    BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

                    while ((b = bis.read(buffer, 0, 1024)) != -1) {
                        bos.write(buffer, 0, b);
                    }

                    bos.close();
                    bis.close();
                }
            }
        } catch (IOException ioe) {
            LOGGER.warning("Error opening zip file" + ioe);
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException ioe) {
                LOGGER.warning("Error while closing zip file" + ioe);
            }
        }
    }
}
