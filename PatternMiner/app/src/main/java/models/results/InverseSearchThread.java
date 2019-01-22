package models.results;

import com.google.common.collect.Table;
import models.algorithm.AlgorithmRunnable;
import models.algorithm.AlgorithmStatus;
import models.data.GenderAgeGroup;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import servlets.AlgorithmManager;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class InverseSearchThread extends Thread {
    protected static Logger LOGGER = Logger.getLogger(InverseSearchThread.class.getName());

    private static final int PAUSE_DURATION = 300;
    private static final int DURATION = 100;

    private DateTime timestamp;
    private ResultsMapper resultsMapper;
    private AlgorithmManager algManager;

    private boolean finished;

    public InverseSearchThread(ResultsMapper resultsMapper) {
        this.resultsMapper = resultsMapper;
        this.finished = false;
        resetTimestamp();
    }

    @Override
    public void run() {
        while (!finished) {
            Set<Table.Cell<String, GenderAgeGroup, ResultsEntry>> cellSet = resultsMapper.getFilteredResultsTable().cellSet().parallelStream()
                    .filter(cell -> !cell.getValue().isInverseSearch())
                    .sorted(resultsMapper.getTableCellComporator().reverse())
                    .limit(50)
                    .collect(Collectors.toSet());
            if (cellSet.isEmpty()) {
                finished = true;
            } else {
                cellSet.forEach(cell -> {
                    try {
                        while (Seconds.secondsBetween(timestamp, DateTime.now()).getSeconds() < PAUSE_DURATION || checkForRunningAlg()) {                             //wait 3 min with no user action
                            waitForResume(PAUSE_DURATION - Seconds.secondsBetween(timestamp, DateTime.now()).getSeconds() + DURATION);
                        }
                        LOGGER.info("Creating Inverse-Search-Files for cell [" + cell.getRowKey() + " , " + cell.getColumnKey() + "]");

                        PatternScanner patternScanner = new PatternScanner(cell.getRowKey());
                        patternScanner.createInverseSearchFiles(cell.getValue());

                    } catch (InterruptedException e) {
                        LOGGER.warning(e.getMessage());
                        return;
                    }
                });
            }

        }

        LOGGER.info("Exiting InverseSearchThread.");
    }

    private boolean checkForRunningAlg() {
        for (AlgorithmRunnable runnable : algManager.getAlgorithmsList()) {
            if (runnable.getAlgorithmStatus() == AlgorithmStatus.RUNNING) {
                return true;
            }
        }
        return false;
    }

    private void waitForResume(int seconds) throws InterruptedException {
        LOGGER.info("Waiting for few seconds...." + seconds);
        TimeUnit.SECONDS.sleep(seconds);
    }

    public void resetTimestamp() {
        this.timestamp = DateTime.now();
    }

    public void setAlgManager(AlgorithmManager algManager) {
        this.algManager = algManager;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

}
