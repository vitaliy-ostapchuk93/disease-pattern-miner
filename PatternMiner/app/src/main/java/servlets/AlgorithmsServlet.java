package servlets;

import models.algorithm.AlgorithmType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@WebServlet(name = "AlgorithmsServlet", urlPatterns = {"/algorithms"})
public class AlgorithmsServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AlgorithmManager algManager = (AlgorithmManager) this.getServletContext().getAttribute("AlgorithmManager");

        if (request.getParameter("addAlgorithm") != null) {
            DataFileManager resultsManager = (DataFileManager) this.getServletContext().getAttribute("DataFileManager");
            DataManager dataManager = (DataManager) this.getServletContext().getAttribute("DataManager");
            algManager.addAlgorithmRunnable(dataManager, resultsManager);
        }
        if (request.getParameter("createAlgorithmTestSuite") != null) {
            DataFileManager resultsManager = (DataFileManager) this.getServletContext().getAttribute("DataFileManager");
            DataManager dataManager = (DataManager) this.getServletContext().getAttribute("DataManager");
            algManager.createAlgorithTestSuite(dataManager, resultsManager);
        }
        if (request.getParameter("runAll") != null) {
            algManager.toggleAllRun();
        }
        if (request.getParameter("pauseAll") != null) {
            algManager.toggleAllPause();
        }
        if (request.getParameter("cancelAll") != null) {
            algManager.cancelAll();
        }

        doGet(request, response);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        updateAlgorithmsList(request);
        setAlgorithmsTypesList(request);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/algorithms.jsp");
        dispatcher.forward(request, response);
    }


    private void updateAlgorithmsList(HttpServletRequest request) {
        AlgorithmManager algManager = (AlgorithmManager) this.getServletContext().getAttribute("AlgorithmManager");
        request.setAttribute("algorithmsList", algManager.getAlgorithmsList());
    }

    private void setAlgorithmsTypesList(HttpServletRequest request) {
        List<String> typesList = new ArrayList<>();
        typesList.addAll(Arrays.asList(getAlgoTypes()));
        request.setAttribute("algorithmsTypesList", typesList);
    }

    public String[] getAlgoTypes() {
        return Stream.of(AlgorithmType.values()).map(AlgorithmType::name).toArray(String[]::new);
    }
}
