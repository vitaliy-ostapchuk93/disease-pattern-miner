package servlets;

import models.data.DiagnosesGroupHelper;
import models.data.GenderAgeGroup;
import net.minidev.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "LinkValuesServlet", urlPatterns = {"/linkValues"})
public class LinkValuesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DataFileManager resultsManager = (DataFileManager) this.getServletContext().getAttribute("DataFileManager");

        if (request.getParameter("patternKey") != null && request.getParameter("patternGroupKey") != null && request.getParameter("ageValue") != null) {
            String patternKey = request.getParameter("patternKey");

            GenderAgeGroup group = new GenderAgeGroup(request.getParameter("patternGroupKey"));
            int ageValue = Integer.parseInt(request.getParameter("ageValue"));

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            JSONObject obj = new JSONObject();

            obj.put("patternKey", patternKey);
            obj.put("patternGender", group.getGender().name());
            obj.put("ageValue", ageValue);

            DiagnosesGroupHelper diagnosesGroupHelper = new DiagnosesGroupHelper();
            obj.put("icdNamesMap", diagnosesGroupHelper.createIcdNamesMap(this.getServletContext()));

            JSONObject jsonLinks = resultsManager.getMapper().getIcdLinksAsJSON(patternKey, group.getGender(), ageValue);
            obj.put("icdLinks", jsonLinks);

            response.getWriter().write(obj.toJSONString());
        }
    }
}
