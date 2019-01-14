package servlets;

import models.data.DiagnosesGroupHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SankeyIcdNodesServlet", urlPatterns = {"/icdnodes"})
public class SankeyIcdNodesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        DiagnosesGroupHelper diagnosesGroupHelper = new DiagnosesGroupHelper();
        out.print(diagnosesGroupHelper.createIcdNodesList(this.getServletContext()));
    }
}
