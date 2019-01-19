package servlets;

import models.data.DataFile;
import models.data.DiagnosesGroupHelper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebServlet(name = "ResultsServlet", urlPatterns = {"/results"})
public class ResultsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataFileManager resultsManager = (DataFileManager) this.getServletContext().getAttribute("DataFileManager");

        if (!resultsManager.getMapper().isInitialized()) {
            DataManager dataManager = (DataManager) this.getServletContext().getAttribute("DataManager");

            Set<DataFile> set = dataManager.getResultFileSet();
            resultsManager.fillResultMapper(set);

            resultsManager.getMapper().getInverseSearch().setContext(this.getServletContext());
            resultsManager.getMapper().getInverseSearch().start();
        }

        resultsManager.getMapper().getInverseSearch().resetTimestamp();

        request.setAttribute("MAPPER", resultsManager.getMapper());
        request.setAttribute("DiagnosesGroupsHelper", new DiagnosesGroupHelper());

        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/results.jsp");
        dispatcher.forward(request, response);
    }
}
