package servlets;

import models.data.GroupDataFile;
import models.results.PatternScanner;
import models.results.ResultsEntry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ResultsCellServlet", urlPatterns = {"/resultscell"}, asyncSupported = true)
public class ResultsCellServlet extends HttpServlet {

    private final static Logger LOGGER = Logger.getLogger(ResultsCellServlet.class.getName());


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.setLevel(Level.INFO);


        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();

        if (request.getParameter("seqKey") != null && request.getParameter("groupKey") != null) {
            String seqKey = request.getParameter("seqKey");
            String groupKey = request.getParameter("groupKey");

            DataFileManager resultsManager = (DataFileManager) this.getServletContext().getAttribute("DataFileManager");


            ResultsEntry entry = resultsManager.getMapper().getCell(seqKey, groupKey);
            LOGGER.info("Fetched cell: " + entry);

            writer.append(entry.getTooltipStats());
            LOGGER.info("Fetched tooltip.");

            writer.append(resultsManager.getMapper().tTestGenderDifference(seqKey));
            LOGGER.info("Fetched tTest for gender.");

            PatternScanner scanner = resultsManager.getMapper().getPatternScanner(seqKey);
            GroupDataFile file = entry.getGroupFileOfResult();

            LOGGER.info("Fetched scanner and file: " + scanner + " | " + file);
            if (file != null) {
                scanner.createInverseSearchFiles(this.getServletContext(), entry);
                writer.append(scanner.scanForCommonICDCodes());
            }
        }
    }
}
