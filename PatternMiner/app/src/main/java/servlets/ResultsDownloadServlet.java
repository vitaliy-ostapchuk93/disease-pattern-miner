package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

@WebServlet(name = "ResultsDownloadServlet", urlPatterns = {"/resultsdownload"})
public class ResultsDownloadServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataFileManager resultsManager = (DataFileManager) this.getServletContext().getAttribute("DataFileManager");

        if (resultsManager.getMapper().isInitialized()) {
            response.setContentType("text/plain");
            response.setHeader("Content-disposition", "attachment; filename=results.json");

            OutputStream out = response.getOutputStream();
            PrintStream printStream = new PrintStream(out);

            String results = resultsManager.getMapper().getTableAsJSON();
            printStream.print(results);

            printStream.close();
            out.close();
        }
    }
}
