package servlets;

import models.algorithm.AlgorithmType;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AlgorithmsListItemServlet", urlPatterns = {"/algorithmitem"})
public class AlgorithmsListItemServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("algorithmID"));

        if (request.getParameter("pause") != null || request.getParameter("run") != null) {
            togglePauseRun(id);
        }
        if (request.getParameter("cancel") != null) {
            cancelAlgorithm(id);
        }
        if (request.getParameter("algorithmType") != null) {
            changeType(id, request.getParameter("algorithmType"));
        }
        if (request.getParameter("algorithmParamKey") != null) {
            String paramKey = request.getParameter("algorithmParamKey");
            int paramID = Integer.parseInt(request.getParameter("algorithmParamID"));

            String paramValue = "";

            if (request.getParameter("param_N" + paramID) != null) {
                paramValue = request.getParameter("param_N" + paramID);
            }
            if (request.getParameter("param_C" + paramID) != null) {
                String[] values = request.getParameterValues("param_C" + paramID);
                paramValue = values[0];
            }
            changeAlgorithmParameter(id, paramKey, paramValue);
        }

        doGet(request, response);
    }

    private void changeAlgorithmParameter(int algID, String paramKey, String paramValue) {
        AlgorithmManager algManager = (AlgorithmManager) this.getServletContext().getAttribute("AlgorithmManager");
        algManager.changeAlgorithmParameter(algID, paramKey, paramValue);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/algorithms");
    }

    private void togglePauseRun(int algID) {
        AlgorithmManager algManager = (AlgorithmManager) this.getServletContext().getAttribute("AlgorithmManager");
        algManager.togglePauseRun(algID);
    }


    private void cancelAlgorithm(int algID) {
        AlgorithmManager algManager = (AlgorithmManager) this.getServletContext().getAttribute("AlgorithmManager");
        algManager.cancelAlg(algID);
    }

    private void changeType(int id, String type) {
        AlgorithmManager algManager = (AlgorithmManager) this.getServletContext().getAttribute("AlgorithmManager");
        algManager.changeType(id, AlgorithmType.valueOf(type));
    }


}
