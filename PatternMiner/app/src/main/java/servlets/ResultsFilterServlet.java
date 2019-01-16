package servlets;

import models.data.Gender;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ResultsFilterServlet", urlPatterns = {"/resultsfilter", "/filterItem"})
public class ResultsFilterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataFileManager resultsManager = (DataFileManager) this.getServletContext().getAttribute("DataFileManager");

        if (request.getParameter("addFilter") != null) {
            resultsManager.createFilter();
        }

        if (request.getParameter("filterID") != null) {
            int id = Integer.parseInt(request.getParameter("filterID"));

            if (request.getParameter("resultFilterType") != null) {
                resultsManager.changeFilterType(id, request.getParameter("resultFilterType"));
            }
            if (request.getParameter("deleteFilter") != null) {
                resultsManager.deleteFilterItem(id);
            }
            if (request.getParameter("gender") != null) {
                Gender gender = Gender.valueOf(request.getParameter("gender"));
                resultsManager.setFilterGender(id, gender);
            }
            if (request.getParameter("age") != null) {
                String[] age = request.getParameterValues("age");
                resultsManager.setFilterAge(id, age);
            }
            if (request.getParameter("minRelSup") != null) {
                double minRelSups = Double.parseDouble(request.getParameter("minRelSup"));
                resultsManager.setFilterRelSup(id, minRelSups);
            }
            if (request.getParameter("minAbsSup") != null) {
                int minAbsSup = Integer.parseInt(request.getParameter("minAbsSup"));
                resultsManager.setFilterAbsSup(id, minAbsSup);
            }
            if (request.getParameter("minGroupsCountSlider") != null) {
                int minGroupCount = Integer.parseInt(request.getParameter("minGroupsCountSlider"));
                resultsManager.setFilterGroupCount(id, minGroupCount);
            }
            if (request.getParameter("topNSupGroupSlider") != null) {
                int minGroupCount = Integer.parseInt(request.getParameter("topNSupGroupSlider"));
                resultsManager.setFilterTopNSupGroup(id, minGroupCount);
            }
            if (request.getParameter("topNSupAllSlider") != null) {
                int minGroupCount = Integer.parseInt(request.getParameter("topNSupAllSlider"));
                resultsManager.setFilterTopNSupAll(id, minGroupCount);
            }
            if (request.getParameter("topPSupGroupSlider") != null) {
                float minGroupP = Float.parseFloat(request.getParameter("topPSupGroupSlider"));
                resultsManager.setFilterTopPSupGroup(id, minGroupP);
            }
            if (request.getParameter("topPSupAllSlider") != null) {
                float minGroupP = Float.parseFloat(request.getParameter("topPSupAllSlider"));
                resultsManager.setFilterTopPSupAll(id, minGroupP);
            }
            if (request.getParameter("pattern") != null) {
                String pattern = request.getParameter("pattern");
                resultsManager.setFilterPattern(id, pattern);
            }
            if (request.getParameter("minSeqLengthSlider") != null) {
                int seqLength = Integer.parseInt(request.getParameter("minSeqLengthSlider"));
                resultsManager.setFilterSeqLength(id, seqLength);
            }
        }

        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/results");
    }

}
