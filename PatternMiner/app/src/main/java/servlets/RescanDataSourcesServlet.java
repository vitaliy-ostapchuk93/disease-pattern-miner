package servlets;

import models.data.DataFile;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

@WebServlet(name = "RescanDataSourcesServlet", urlPatterns = "/rescanDataSources")
public class RescanDataSourcesServlet extends HttpServlet {

    private final static Logger LOGGER = Logger.getLogger(RescanDataSourcesServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Reinit FileStructure.");

        DataManager dataManager = (DataManager) this.getServletContext().getAttribute("DataManager");
        dataManager.createDataFileSet(this.getServletContext());

        DataFileManager resultsManager = (DataFileManager) this.getServletContext().getAttribute("DataFileManager");

        Set<DataFile> set = dataManager.getResultFileSet();
        resultsManager.fillResultMapper(set);

        resultsManager.getMapper().createFullICDLinksFiles(this.getServletContext());

        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
        dispatcher.forward(request, response);
    }
}
