package models.results;

import com.google.common.collect.Table;
import models.algorithm.AlgorithmRunnable;
import models.algorithm.AlgorithmStatus;
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
    private boolean used;

    public InverseSearchThread(ResultsMapper resultsMapper) {
        this.resultsMapper = resultsMapper;
        this.finished = false;
        this.used = false;
        resetTimestamp();
    }

    @Override
    public void run() {
        while (!finished) {
            Set<String> rowKeys = resultsMapper.getFilteredResultsTable().cellSet().parallelStream()
                    .filter(cell -> !cell.getValue().isInverseSearch())
                    .sorted(resultsMapper.getTableCellComporator())
                    .limit(10)
                    .map(Table.Cell::getRowKey)
                    .collect(Collectors.toSet());
            if (rowKeys.isEmpty()) {
                finished = true;
            } else {
                rowKeys.forEach(patternKey -> {
                    PatternScanner patternScanner = new PatternScanner(patternKey);
                    resultsMapper.getResultsTable().row(patternKey).forEach((groupKey, resultsEntry) -> {
                        try {
                            while (Seconds.secondsBetween(timestamp, DateTime.now()).getSeconds() < PAUSE_DURATION || checkForRunningAlg() || isUsed()) {
                                waitForResume(PAUSE_DURATION - Seconds.secondsBetween(timestamp, DateTime.now()).getSeconds() + DURATION);
                            }
                            LOGGER.info("Creating Inverse-Search-Files for cell [" + patternKey + " , " + groupKey + "]");
                            patternScanner.createInverseSearchFiles(resultsEntry);
                        } catch (InterruptedException e) {
                            LOGGER.warning(e.getMessage());
                            return;
                        }
                    });
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

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
