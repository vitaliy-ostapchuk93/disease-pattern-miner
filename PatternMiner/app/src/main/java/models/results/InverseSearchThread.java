package models.results;

import com.google.common.collect.Table;
import models.data.GenderAgeGroup;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import javax.servlet.ServletContext;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class InverseSearchThread extends Thread {
    protected static Logger LOGGER = Logger.getLogger(InverseSearchThread.class.getName());

    private DateTime timestamp;
    private ResultsMapper resultsMapper;
    private ServletContext servletContext;

    public InverseSearchThread(ResultsMapper resultsMapper) {
        this.resultsMapper = resultsMapper;
        resetTimestamp();
    }

    @Override
    public void run() {
        if (servletContext == null) {
            return;
        }
        for (Table.Cell<String, GenderAgeGroup, ResultsEntry> cell : resultsMapper.getFilteredResultsTable().cellSet()) {
            try {
                while (Seconds.secondsBetween(timestamp, DateTime.now()).getSeconds() < 60 * 3) {                             //wait 3 min with no user action
                    waitForResume(Seconds.secondsBetween(timestamp, DateTime.now()).getSeconds() + 5);
                }
                LOGGER.info("Creating Inverse-Search-Files for cell [" + cell.getRowKey() + " , " + cell.getColumnKey() + "]");

                PatternScanner patternScanner = resultsMapper.getPatternScanner(cell.getRowKey());
                patternScanner.createInverseSearchFiles(servletContext, cell);

            } catch (InterruptedException e) {
                LOGGER.warning(e.getMessage());
                return;
            }
        }
        LOGGER.info("Exiting InverseSearchThread.");
    }

    private void waitForResume(int seconds) throws InterruptedException {
        LOGGER.info("Waiting for few seconds...." + seconds);
        TimeUnit.SECONDS.sleep(seconds);
    }

    public void resetTimestamp() {
        this.timestamp = DateTime.now();
    }

    public void setContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
