package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SankeyLinksServlet", urlPatterns = {"/icdlinks"})
public class SankeyLinksServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataFileManager resultsManager = (DataFileManager) this.getServletContext().getAttribute("DataFileManager");

        request.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        if (request.getParameter("patternKey") != null) {
            String patternKey = request.getParameter("patternKey");

            out.print(resultsManager.getMapper().getIcdLinksData(patternKey));
        }
    }
}
