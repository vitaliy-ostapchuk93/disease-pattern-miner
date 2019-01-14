package servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import models.data.DiagnosesGroup;
import models.data.DiagnosesGroupHelper;
import models.data.ICDLookupException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ICDGroupsServlet", urlPatterns = {"/icdgroups"})
public class ICDGroupsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json");

        JsonArray groups = new JsonArray();

        for (DiagnosesGroup diagnosesGroup : DiagnosesGroupHelper.getDiagnosesGroups()) {
            JsonObject group = new JsonObject();

            group.addProperty("name", diagnosesGroup.name());

            if (diagnosesGroup == DiagnosesGroup.TIME_GAP) {
                group.addProperty("id", diagnosesGroup.ordinal());
            } else {
                group.addProperty("id", -1);
            }

            group.addProperty("color", DiagnosesGroupHelper.getColorByGroup(diagnosesGroup));

            if (diagnosesGroup != DiagnosesGroup.TIME_GAP && diagnosesGroup != DiagnosesGroup.UNKNOWN) {
                try {
                    group.addProperty("min", DiagnosesGroupHelper.lookupMin(diagnosesGroup));
                    group.addProperty("max", DiagnosesGroupHelper.lookupMax(diagnosesGroup));
                } catch (ICDLookupException e) {
                    e.printStackTrace();
                }
            } else {
                group.add("min", JsonNull.INSTANCE);
                group.add("max", JsonNull.INSTANCE);
            }

            groups.add(group);
        }

        PrintWriter out = response.getWriter();
        out.print(groups);
    }
}
