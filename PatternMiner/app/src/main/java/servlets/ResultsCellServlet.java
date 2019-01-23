package servlets;

import com.google.gson.JsonObject;
import models.results.PatternScanner;
import models.results.ResultsEntry;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ResultsCellServlet", urlPatterns = {"/resultscell"}, asyncSupported = true)
public class ResultsCellServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DataFileManager resultsManager = (DataFileManager) this.getServletContext().getAttribute("DataFileManager");

        request.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();

        if (request.getParameter("seqKey") != null && request.getParameter("groupKey") != null) {
            String seqKey = request.getParameter("seqKey");
            String groupKey = request.getParameter("groupKey");

            ResultsEntry entry = resultsManager.getMapper().getCell(seqKey, groupKey);
            resultsManager.getMapper().createInverseSearchFiles(seqKey, entry);

            JsonObject obj = new JsonObject();
            PatternScanner patternScanner = new PatternScanner(seqKey);
            resultsManager.getMapper().getInverseSearch().setUsed(true);

            obj.add("stats", entry.getTooltipStats());
            obj.addProperty("tTest", resultsManager.getMapper().tTestGenderDifference(seqKey));
            obj.add("commonCodes", patternScanner.getCommonCodesJSON(entry));

            resultsManager.getMapper().getInverseSearch().setUsed(false);

            out.print(obj);
        }
    }
}
