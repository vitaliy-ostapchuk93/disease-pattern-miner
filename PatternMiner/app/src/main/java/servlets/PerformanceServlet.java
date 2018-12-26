package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PerformanceServlet", urlPatterns = {"/performance"})
public class PerformanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PerformanceManager performanceManager = (PerformanceManager) this.getServletContext().getAttribute("PerformanceManager");
        performanceManager.updateStats();

        request.setAttribute("STATS", performanceManager.getStatsList());
        request.setAttribute("MainStatsJSON", performanceManager.getMainStatsAsJSON());

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/performance.jsp");
        dispatcher.forward(request, response);
    }
}
