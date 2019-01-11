package servlets;


import models.data.GenderAgeGroup;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PatternExplorerServlet", urlPatterns = {"/explorer"})
public class PatternExplorerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataFileManager resultsManager = (DataFileManager) this.getServletContext().getAttribute("DataFileManager");
        request.setAttribute("MAPPER", resultsManager.getMapper());

        if (request.getParameter("patternKey") != null) {
            request.setAttribute("patternKey", request.getParameter("patternKey"));
        }
        if (request.getParameter("patternGroupKey") != null) {
            GenderAgeGroup group = new GenderAgeGroup(request.getParameter("patternGroupKey"));
            request.setAttribute("patternGender", group.getGender());
            request.setAttribute("ageValue", group.getAgeGroup() * 10);
        }

        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/explorer.jsp");
        dispatcher.forward(request, response);
    }
}
