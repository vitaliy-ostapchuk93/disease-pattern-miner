package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DataServlet", urlPatterns = {"/data"})
public class DataServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataManager manager = (DataManager) this.getServletContext().getAttribute("DataManager");

        if (request.getParameterValues("selected[]") != null) {
            manager.selectItems(request.getParameterValues("selected[]"));
        }
        if (request.getParameterValues("deselected[]") != null) {
            manager.deSelectItems(request.getParameterValues("deselected[]"));
        }


        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataManager dataManager = (DataManager) this.getServletContext().getAttribute("DataManager");
        request.setAttribute("DATAFILES", dataManager.getMainFileList());

        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/data.jsp");
        dispatcher.forward(request, response);
    }
}
