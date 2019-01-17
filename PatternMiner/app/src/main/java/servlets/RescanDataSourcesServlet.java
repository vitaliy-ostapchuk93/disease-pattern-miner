package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "RescanDataSourcesServlet", urlPatterns = "/createLinks")
public class RescanDataSourcesServlet extends HttpServlet {

    private final static Logger LOGGER = Logger.getLogger(RescanDataSourcesServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Create FullLinks-Files.");

        DataFileManager resultsManager = (DataFileManager) this.getServletContext().getAttribute("DataFileManager");
        resultsManager.getMapper().createFullICDLinksFiles(this.getServletContext());

        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
        dispatcher.forward(request, response);
    }
}
