package servlets;

import models.data.DatasetZipper;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ExternalSourceServlet", urlPatterns = {"/externalSource"})
public class ExternalSourceServlet extends HttpServlet {

    public final static String DOWNLOAD_DIR_PATH = "/resources/datasets/";
    private final static Logger LOGGER = Logger.getLogger(ExternalSourceServlet.class.getName());


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.setLevel(Level.INFO);

        request.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json");

        if (request.getParameter("externalURL") != null) {
            String externalURL = request.getParameter("externalURL");
            LOGGER.info("Downloading from external source: " + externalURL);

            File datasetDir = new File(this.getServletContext().getRealPath(DOWNLOAD_DIR_PATH));
            datasetDir.mkdirs();

            File downloadFile = new File(datasetDir.getPath() + File.separator + "external.zip");

            if (downloadFile.exists()) {
                downloadFile.delete();
            }
            try {
                FileUtils.copyURLToFile(new URL(externalURL), downloadFile, 10000, 10000);

                LOGGER.info("Downloaded files to " + downloadFile.getPath());

                LOGGER.info("Unzipping external source files.");
                DatasetZipper.unzipDatasetFiles(downloadFile, datasetDir);
                LOGGER.info("Finished unzipping.");

                response.getWriter().write("Successfully uploaded file.");
            } catch (IOException e) {
                LOGGER.warning(e.getMessage());
                response.getWriter().write(e.getMessage());
            }
        } else {
            LOGGER.warning("No external URL given.");
            response.getWriter().write("Please provide some valid URL.");
        }
    }


}
