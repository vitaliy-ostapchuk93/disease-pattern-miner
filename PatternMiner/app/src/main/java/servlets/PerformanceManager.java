package servlets;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class PerformanceManager implements ServletContextListener {

    private final static Logger LOGGER = Logger.getLogger(PerformanceManager.class.getName());
    private final static String PATH_TO_STATS = "/resources/datasets/stats/algorithmStats.txt";

    private File statsFile;
    private List<String> stats;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOGGER.setLevel(Level.INFO);
        LOGGER.info("PerformanceManager started.");

        ServletContext ctx = servletContextEvent.getServletContext();

        this.stats = new ArrayList<>();
        this.statsFile = fillStats(ctx);

        ctx.setAttribute("PerformanceManager", this);
    }


    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        this.stats.clear();
        LOGGER.info("PerformanceManager destroyed.");
    }

    public List<String> getStatsList() {
        return stats;
    }

    private File fillStats(ServletContext ctx) {
        String statsPath = ctx.getRealPath(PATH_TO_STATS);
        LOGGER.info("Found StatsFile > " + statsPath);

        return new File(statsPath);
    }

    public void updateStats() {
        LOGGER.info("Update StatsFile > " + statsFile.getName());

        if (!statsFile.exists()) {
            try {
                statsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            List<String> stats = new ArrayList<>();

            LineIterator it = FileUtils.lineIterator(statsFile, "UTF-8");
            while (it.hasNext()) {
                String line = it.nextLine();
                stats.add(line);
            }
            this.stats = stats;
            LOGGER.info("Stats updated.");

            it.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getMainStatsAsJSON() {
        JSONObject mainStats = new JSONObject();

        for (String algoStatsOutput : stats) {
            String[] output = algoStatsOutput.split(";");

            JSONObject currentStats = new JSONObject();

            if (output[1].equals("null") || output[0].equals("TKS") || output[0].equals("TSP")) {
                continue;
            }

            currentStats.put("minSup", Float.parseFloat(output[1]));
            String count = output[2].split(",")[1].replace(" LineCount=", "");
            currentStats.put("sequenceCount", Integer.parseInt(count));
            String time = output[output.length - 1];
            long t = Long.parseLong(time);
            currentStats.put("timeInMillis", t);

            if (!mainStats.containsKey(output[0])) {
                mainStats.put(output[0], new JSONArray());
            }
            JSONArray algoStats = (JSONArray) mainStats.get(output[0]);
            algoStats.add(currentStats);
        }

        return mainStats;
    }
}
