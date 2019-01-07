package servlets;

import models.algorithm.AlgorithmRunnable;
import models.algorithm.AlgorithmStatus;
import models.algorithm.AlgorithmType;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Logger;

@WebListener
public class AlgorithmManager implements ServletContextListener {

    private final static Logger LOGGER = Logger.getLogger(AlgorithmManager.class.getName());
    private ExecutorService executorService;
    private ArrayList<AlgorithmRunnable> algorithmsList;
    private Map<Integer, Future> algorithmFuture;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        LOGGER.info("AlgorithmManager started.");

        executorService = Executors.newSingleThreadExecutor();
        algorithmsList = new ArrayList<>();
        algorithmFuture = new ConcurrentHashMap<>();

        ServletContext ctx = event.getServletContext();
        ctx.setAttribute("AlgorithmManager", this);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        LOGGER.info("AlgorithmManager destroyed.");

        try {
            cancelAll();
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.warning(e.getMessage());
        } finally {
            if (!executorService.isTerminated()) {
                LOGGER.warning("cancel non-finished tasks");
            }
            executorService.shutdownNow();
            LOGGER.warning("shutdown finished");
        }

        algorithmsList.clear();
    }

    private void addAlgorithmRunnable(AlgorithmRunnable algorithm) {
        LOGGER.info("Add new algorithm.");
        algorithmsList.add(algorithm);
    }

    private void executeAlgorithmRunnable(AlgorithmRunnable algorithm) {
        LOGGER.info("Running algorithm: #" + algorithm.getListID() + " of type " + algorithm.getAlgorithmType().name());
        Future<?> future = executorService.submit(algorithm);
        algorithmFuture.put(algorithm.getListID(), future);
    }

    public void executeAlgorithmRunnable(int algorithmID) {
        executeAlgorithmRunnable(algorithmsList.get(algorithmID));
    }

    public List<AlgorithmRunnable> getAlgorithmsList() {
        return algorithmsList;
    }

    public void togglePauseRun(AlgorithmRunnable alg) {
        switch (alg.getAlgorithmStatus()) {
            case CREATED:
                alg.setAlgorithmStatus(AlgorithmStatus.WAITING);
                executeAlgorithmRunnable(alg);
                break;
            case WAITING:
                alg.setAlgorithmStatus(AlgorithmStatus.PAUSED);
                break;
            case RUNNING:
                alg.setAlgorithmStatus(AlgorithmStatus.PAUSED);
                break;
            case PAUSED:
                alg.setAlgorithmStatus(AlgorithmStatus.RUNNING);
                break;
            case FINISHED:
                break;
            case FAILED:
                break;
            case CANCELED:
                break;
        }

        LOGGER.info("Toggle AlgoRunnable. New status: " + alg.getAlgorithmStatus().name());
    }

    public void togglePauseRun(int algID) {
        AlgorithmRunnable alg = algorithmsList.get(algID);
        if (alg.getAlgorithmType() != AlgorithmType.UNKNOWN) {
            togglePauseRun(alg);
        }
    }

    public void cancelAlg(int algID) {
        AlgorithmRunnable alg = algorithmsList.get(algID);
        alg.cancel();

        if (algorithmFuture.containsKey(algID)) {
            algorithmFuture.get(algID).cancel(true);
        }
    }

    public void addAlgorithmRunnable(DataManager dataManager, DataFileManager resultsManager) {
        AlgorithmRunnable alg = new AlgorithmRunnable(algorithmsList.size(), AlgorithmType.UNKNOWN, dataManager.getGroupsFileSet(), resultsManager);
        addAlgorithmRunnable(alg);
    }

    public void toggleAllRun() {
        for (AlgorithmRunnable alg : algorithmsList) {
            if (alg.getAlgorithmStatus() == AlgorithmStatus.CREATED || alg.getAlgorithmStatus() == AlgorithmStatus.PAUSED) {
                togglePauseRun(alg);
            }
        }
    }

    public void toggleAllPause() {
        for (AlgorithmRunnable alg : algorithmsList) {
            if (alg.getAlgorithmStatus() == AlgorithmStatus.WAITING || alg.getAlgorithmStatus() == AlgorithmStatus.RUNNING) {
                togglePauseRun(alg);
            }
        }
    }

    public void cancelAll() {
        for (AlgorithmRunnable alg : algorithmsList) {
            cancelAlg(alg.getListID());
        }
    }

    public void changeType(int algID, AlgorithmType type) {
        AlgorithmRunnable alg = algorithmsList.get(algID);
        if (alg.getAlgorithmStatus() == AlgorithmStatus.CREATED) {
            alg.setAlgorithmType(type);
        }
    }

    public void changeAlgorithmParameter(int algID, String paramKey, String paramValue) {
        AlgorithmRunnable alg = algorithmsList.get(algID);

        if (alg.getAlgorithmStatus() == AlgorithmStatus.CREATED) {
            if (alg.getAlgorithmParameters().get(paramKey) instanceof Boolean) {
                if (paramValue.equals("on")) {
                    alg.getAlgorithmParameters().put(paramKey, true);
                } else {
                    alg.getAlgorithmParameters().put(paramKey, false);
                }
            }
            if (alg.getAlgorithmParameters().get(paramKey) instanceof Float) {
                alg.getAlgorithmParameters().put(paramKey, Float.parseFloat(paramValue));
            }
            if (alg.getAlgorithmParameters().get(paramKey) instanceof Integer) {
                alg.getAlgorithmParameters().put(paramKey, Integer.parseInt(paramValue));
            }
        }
    }

    public void createAlgorithTestSuite(DataManager dataManager, DataFileManager resultsManager) {
        //float[] minSupValues = {0.1f, 0.05f, 0.02f, 0.01f, 0.005f, 0.002f, 0.001f};
        //float[] minSupValues = {0.1f, 0.05f, 0.02f, 0.01f, 0.005f};

        for (AlgorithmType type : AlgorithmType.values()) {
            if (type != AlgorithmType.UNKNOWN) {
                //if (type == AlgorithmType.TKS || type == AlgorithmType.TSP || type == AlgorithmType.VMSP || type == AlgorithmType.AprioriClose) {
                int counter = 0;
                for (int i = 0; i < 5; i++) {
                    AlgorithmRunnable alg = new AlgorithmRunnable(algorithmsList.size(), type, dataManager.getGroupsFileSet(), resultsManager);

                    if (alg.getAlgorithmParameters().containsKey("Minimal Support")) {
                        alg.getAlgorithmParameters().put("Minimal Support", (float) (0.1 * (Math.pow(0.5, counter))));
                    }
                    if (alg.getAlgorithmParameters().containsKey("Minimal Support [rel.]")) {
                        alg.getAlgorithmParameters().put("Minimal Support [rel.]", (float) (0.1 * (Math.pow(0.5, counter))));
                    }
                    if (alg.getAlgorithmParameters().containsKey("K")) {
                        alg.getAlgorithmParameters().put("K", (int) (10 * (Math.pow(1.5, counter))));
                    }
                    addAlgorithmRunnable(alg);
                    counter++;
                }
            }
        }
    }
}
