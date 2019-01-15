package servlets;

import models.data.MainDataFile;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;


@WebServlet(name = "FileUploadServlet", urlPatterns = {"/upload"})
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "resources/datasets/input/csv/all/";
    private final static Logger LOGGER = Logger.getLogger(FileUploadServlet.class.getName());
    private boolean isMultipart;
    private String filePath;
    private long maxFileSize = 1024 * 1024 * 1024 * 30L;
    private int maxMemSize = 4 * 1024 * 1024;
    private File file;

    public void init() {
        // gets absolute path of the web application
        String applicationPath = this.getServletContext().getRealPath("");
        // constructs path of the directory to save uploaded file
        filePath = applicationPath + File.separator + UPLOAD_DIR;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Upload File Directory is " + filePath);

        isMultipart = ServletFileUpload.isMultipartContent(request);

        if (!isMultipart) {
            LOGGER.warning("Request was not made by multipart input.");
            return;
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();

        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);

        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new MainDataFile(filePath).getTempDir());

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // maximum file size to be uploaded.
        upload.setSizeMax(maxFileSize);

        try {
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);

            // Process the uploaded file items
            Iterator i = fileItems.iterator();

            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (!fi.isFormField()) {
                    // Get the uploaded file parameters
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    String contentType = fi.getContentType();
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();

                    // Write the file
                    if (fileName.lastIndexOf("\\") >= 0) {
                        file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\")));
                    } else {
                        file = new File(filePath + fileName.substring(fileName.lastIndexOf("\\") + 1));
                    }
                    fi.write(file);
                    LOGGER.info("File was successfully uploaded > " + file.getPath());

                    DataManager manager = (DataManager) this.getServletContext().getAttribute("DataManager");
                    manager.newFileAvailable(new MainDataFile(file.getPath()));
                }
            }
        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
        }

        response.sendRedirect("/data");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        throw new ServletException("GET method used with " +
                getClass().getName() + ": POST method required.");
    }
}
