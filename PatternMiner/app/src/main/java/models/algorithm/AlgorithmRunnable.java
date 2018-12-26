package models.algorithm;

import models.data.DataFile;
import models.data.GroupDataFile;
import models.data.MyFileWriter;
import models.data.ResultsDataFile;
import models.results.DataFileListener;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class AlgorithmRunnable implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(AlgorithmRunnable.class.getName());

    private int listID;

    private DateTime timeCreated;
    private DateTime timeStart;
    private DateTime timeEnd;
    private Period timeTotal;

    private AlgorithmType algorithmType;
    private AlgorithmStatus algorithmStatus;

    private Set<GroupDataFile> dataFiles;

    private DataFileListener dataFileListener;
    private AbstractAlgorithmTask algorithmTask;

    public AlgorithmRunnable(int listID, AlgorithmType type, Set<GroupDataFile> dataFiles, DataFileListener dataFileListener) {
        this.listID = listID;
        setAlgorithmType(type);
        this.timeCreated = DateTime.now();
        this.algorithmStatus = AlgorithmStatus.CREATED;
        this.dataFiles = dataFiles;
        this.dataFileListener = dataFileListener;

        LOGGER.info("Created new empty AlgorithmRunnable.");
    }


    @Override
    public void run() {
        LOGGER.info("Trying to run the algorithm.");
        try {
            if (checkStatus()) {
                while (algorithmStatus == AlgorithmStatus.PAUSED) {
                    waitForResume();
                }
                if (algorithmStatus == AlgorithmStatus.WAITING) {
                    setTimeStart(DateTime.now());
                    setAlgorithmStatus(AlgorithmStatus.RUNNING);
                }
                if (algorithmStatus == AlgorithmStatus.RUNNING) {
                    executeTask();
                    finishAlg();
                }
            }
        } catch (InterruptedException e) {
            algorithmStatus = AlgorithmStatus.CANCELED;
            LOGGER.warning("AlgorithmRunnable was interrupted.");
            LOGGER.warning(e.getMessage());
            return;
        }
        LOGGER.info("Exiting Algorithm.");
    }

    public synchronized boolean checkStatus() {
        return algorithmStatus != AlgorithmStatus.FINISHED && algorithmStatus != AlgorithmStatus.CANCELED && algorithmStatus != AlgorithmStatus.FAILED;
    }

    private void executeTask() throws InterruptedException {
        for (GroupDataFile inputFile : dataFiles) {
            if (inputFile.isSelected()) {
                runAlgorithmTask(inputFile);
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    private void runAlgorithmTask(GroupDataFile inputFile) {
        LOGGER.info(algorithmType.name() + " running on " + inputFile.getName());

        DateTime start = DateTime.now();
        ResultsDataFile output = (ResultsDataFile) algorithmTask.miningTask(inputFile);
        DateTime end = DateTime.now();

        if (output != null) {
            dataFileListener.newFileAvailable(output);
        } else {
            this.algorithmStatus = AlgorithmStatus.FAILED;
        }

        createPerformanceReport(output.getParentDataFile(), output, start, end);
    }


    private void finishAlg() {
        if (algorithmStatus != AlgorithmStatus.FAILED && algorithmStatus != AlgorithmStatus.CANCELED) {
            LOGGER.info("AbstractAlgorithmTask finished run successfully.");

            setTimeEnd(DateTime.now());
            setAlgorithmStatus(AlgorithmStatus.FINISHED);
        } else {
            LOGGER.info("AbstractAlgorithmTask did not finished right.");
        }
    }

    private void waitForResume() throws InterruptedException {
        TimeUnit.SECONDS.sleep(30);
    }

    public DateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(DateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public DateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(DateTime timeStart) {
        this.timeStart = timeStart;
    }

    public DateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(DateTime timeEnd) {
        this.timeEnd = timeEnd;
        setTimeTotal(new Period(timeStart, timeEnd));
    }

    public Period getTimeTotal() {
        return timeTotal;
    }

    public void setTimeTotal(Period timeTotal) {
        this.timeTotal = timeTotal;
    }

    public AlgorithmType getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;

        switch (algorithmType) {
            case UNKNOWN:
                break;
            case ClaSP:
                this.algorithmTask = new ClasSPTask();
                break;
            case CM_ClaSP:
                this.algorithmTask = new CmClaspTask();
                break;
            case CloFAST:
                this.algorithmTask = new CloFastTask();
                break;
            case CloSpan:
                this.algorithmTask = new CloSpanTask();
                break;
            case BIDE_PLUS:
                this.algorithmTask = new BidePlusTask();
                break;
            case VMSP:
                this.algorithmTask = new VMSPTask();
                break;
            case MaxSP:
                this.algorithmTask = new MaxSPTask();
                break;
            case TKS:
                this.algorithmTask = new TKSTask();
                break;
            case TSP:
                this.algorithmTask = new TSPTask();
                break;
            case Skopus:
                this.algorithmTask = new SkopusTask();
                break;
            case MG_FSM:
                this.algorithmTask = new MgFsmTask();
                break;
            case LASH:
                this.algorithmTask = new LashTask();
                break;
            case SPAM:
                this.algorithmTask = new SpamTask();
                break;
            case CM_SPAM:
                this.algorithmTask = new CmSpamTask();
                break;
            case AprioriClose:
                this.algorithmTask = new AprioriCloseTask();
                break;
        }
    }

    public synchronized AlgorithmStatus getAlgorithmStatus() {
        return algorithmStatus;
    }

    public synchronized void setAlgorithmStatus(AlgorithmStatus algorithmStatus) {
        this.algorithmStatus = algorithmStatus;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public synchronized void cancel() {
        this.timeEnd = DateTime.now();
        this.algorithmStatus = AlgorithmStatus.CANCELED;
    }

    public Set<GroupDataFile> getDataFiles() {
        return dataFiles;
    }

    public void setDataFiles(Set<GroupDataFile> dataFiles) {
        this.dataFiles = dataFiles;
    }


    public AlgorithmType[] getAlgorithmTypes() {
        return AlgorithmType.values();
    }


    public Map<String, Object> getAlgorithmParameters() {
        if (algorithmTask != null) {
            return algorithmTask.getAlgorithmParameters();
        }
        return Collections.emptyMap();
    }

    private void createPerformanceReport(DataFile inputFile, DataFile outputFile, DateTime start, DateTime end) {
        File statsFile = getStatsFile(outputFile);

        String report = getReport(inputFile, outputFile, start, end);

        MyFileWriter writer = new MyFileWriter();
        writer.appendToFile(statsFile, report);
        writer.closeAllWriters();
    }

    private File getStatsFile(DataFile outputFile) {
        File stats = new File(outputFile.getParent().replace("output", "stats") + File.separator + "algorithmStats.txt");
        return stats;
    }

    private String getReport(DataFile inputFile, DataFile outputFile, DateTime start, DateTime end) {
        String report = "";
        String separator = ";";

        //type
        report += this.algorithmType + separator;

        //minSup
        Object sup = null;
        if (this.algorithmTask.algorithmParameters.containsKey("Minimal Support")) {
            sup = this.algorithmTask.algorithmParameters.get("Minimal Support");
        }
        if (this.algorithmTask.algorithmParameters.containsKey("Minimal Support [rel.]")) {
            sup = this.algorithmTask.algorithmParameters.get("Minimal Support [rel.]");
        }
        if (this.algorithmTask.algorithmParameters.containsKey("K")) {
            sup = ((int) this.algorithmTask.algorithmParameters.get("K")) * 1.0f / inputFile.getFileLineCount();
        }

        report += sup + separator;

        //files
        report += inputFile.getStats() + separator;
        report += outputFile.getStats() + separator;

        //time
        report += start + separator;
        report += end + separator;
        Duration dur = new Duration(start, end);
        report += dur.getMillis() + separator;

        return report;
    }



}
