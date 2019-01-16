package servlets;

import models.data.*;
import models.results.DataFileListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class DataManager implements ServletContextListener, DataFileListener {

    private final static Logger LOGGER = Logger.getLogger(DataManager.class.getName());
    private Set<DataFile> mainFileSet;

    private FileAppendUtils appendUtils;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        LOGGER.setLevel(Level.INFO);
        LOGGER.info("DataManager started.");

        ServletContext ctx = event.getServletContext();

        this.appendUtils = new FileAppendUtils();

        createDataFileSet(ctx);

        ctx.setAttribute("DataManager", this);
    }


    /**
     * Create initial DataFile set when context initialized.
     *
     * @param ctx
     */
    public void createDataFileSet(ServletContext ctx) {
        this.mainFileSet = new HashSet<>();
        Set<String> resourcePathsMain = ctx.getResourcePaths(MainDataFile.DIR_PATH);

        if (resourcePathsMain != null) {
            for (String resourcePath : resourcePathsMain) {
                MainDataFile mainFile = new MainDataFile(ctx.getRealPath(resourcePath));

                mainFileSet.add(mainFile);

                if (mainFile.getName().contains("test") || mainFile.getName().contains("all")) {
                    findOwnChildDataFiles(ctx, mainFile);
                }
            }
        }
    }

    private void findOwnChildDataFiles(ServletContext ctx, DataFile dataFile) {
        if (dataFile instanceof ResultsDataFile || dataFile.getName().contains("_hierarchy.txt")) {
            return;
        }

        //LOGGER.info("Trying to find children of " + dataFile.getName() + " [" + dataFile.getClass().getSimpleName() + "] ");

        if (dataFile instanceof GroupDataFile && !dataFile.getName().contains("_sorted")) {
            MainToGroupConverter converter = new MainToGroupConverter();
            File sorted = converter.sortGivenGroupFile(dataFile);
            dataFile = new GroupDataFile(sorted.getPath());
        }

        //LOGGER.info("Path to children: "+dataFile.getPathToChildren());
        Set<String> resourcePathsGroups = ctx.getResourcePaths(dataFile.getPathToChildren());

        if (resourcePathsGroups != null) {
            for (String resourcePath : resourcePathsGroups) {

                if (resourcePath.contains(".txt-debug.txt")) {
                    File f = new File(ctx.getRealPath(resourcePath));
                    f.delete();
                    continue;
                }

                if (!(dataFile instanceof MainDataFile)) {
                    if (!(resourcePath.matches("(.*)_" + dataFile.getGenderGroup().name().toLowerCase().charAt(0) + dataFile.getAgeGroup() + "_(.*)"))) {
                        continue;
                    }
                }

                File child = new File(ctx.getRealPath(resourcePath));
                if (child.isDirectory()) {
                    continue;
                }

                DataFile childDataFile = dataFile.createChildFile(child.getPath());

                if (childDataFile instanceof GroupDataFile) {
                    GroupDataFile groupFile = (GroupDataFile) childDataFile;
                    if (groupFile.getAgeGroup() < 8) {
                        groupFile.setSelected(true);
                    }
                }

                //LOGGER.info("Checking parent ("+dataFile.getName()+") and child ("+child.getName()+")");

                if (dataFile instanceof MainDataFile || dataFile.getGendederAgeGroup().equals(childDataFile.getGendederAgeGroup())) {

                    String parentNamePrefix = dataFile.getName().split("_")[0].replace("test", "").replace(".csv", "");
                    String childNamePrefix = childDataFile.getName().split("_")[0];

                    if (parentNamePrefix.equals(childNamePrefix)) {
                        LOGGER.info("Found child of " + dataFile.getName() + " [" + dataFile.getClass().getSimpleName() + "]  ->  " + childDataFile.getName() + " [" + childDataFile.getClass().getSimpleName() + "]");

                        if (childDataFile.getName().contains("_seq")) {
                            SeqDataFile seqDataFile = new SeqDataFile(childDataFile.getPath());
                            dataFile.makeChild(seqDataFile);
                            findOwnChildDataFiles(ctx, seqDataFile);
                        } else {
                            dataFile.makeChild(childDataFile);
                            findOwnChildDataFiles(ctx, childDataFile);
                        }

                    }
                }
            }
        }

        if (dataFile.getChildFiles().isEmpty()) {
            if (dataFile instanceof MainDataFile) {
                ((MainDataFile) dataFile).splitIntoSortedGroups();
            }
            if (dataFile instanceof GroupDataFile) {
                ((GroupDataFile) dataFile).getSeqDataFile(SEQFileType.SEQ_SPMF);
            }
            if (!(dataFile instanceof SeqDataFile)) {
                findOwnChildDataFiles(ctx, dataFile);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        LOGGER.info("DataManager destroyed.");
        mainFileSet.clear();

        appendUtils.closeAllWriters();
    }

    public Set<DataFile> getMainFileSet() {
        return mainFileSet;
    }

    public ArrayList<DataFile> getMainFileList() {
        ArrayList<DataFile> list = new ArrayList<>(getMainFileSet());
        Collections.sort(list, Comparator.comparing(DataFile::getName));
        return list;
    }

    public void toggleSelectedItem(String name) {
        for (DataFile mainFile : mainFileSet) {
            for (DataFile groupFile : mainFile.getChildFiles()) {
                if (groupFile.getName().equals(name)) {
                    boolean selected = ((GroupDataFile) groupFile).isSelected();
                    ((GroupDataFile) groupFile).setSelected(!selected);
                }
            }
        }
    }

    public void selectItems(String[] names) {
        Set<String> fileNamesSet = new HashSet<>(Arrays.asList(names));
        for (DataFile mainFile : mainFileSet) {
            for (DataFile groupFile : mainFile.getChildFiles()) {
                if (fileNamesSet.contains(groupFile.getName())) {
                    ((GroupDataFile) groupFile).setSelected(true);
                    LOGGER.info("Selected file > " + groupFile.getName());
                }
            }
        }
    }

    public void deSelectItems(String[] names) {
        Set<String> fileNamesSet = new HashSet<>(Arrays.asList(names));
        for (DataFile mainFile : mainFileSet) {
            for (DataFile groupFile : mainFile.getChildFiles()) {
                if (fileNamesSet.contains(groupFile.getName())) {
                    ((GroupDataFile) groupFile).setSelected(false);
                    LOGGER.info("Deselected file > " + groupFile.getName());
                }
            }
        }
    }

    public Set<GroupDataFile> getGroupsFileSet() {
        Set<GroupDataFile> children = new HashSet<>();
        for (DataFile file : mainFileSet) {
            for (DataFile groupFile : file.getChildFiles()) {
                if (groupFile instanceof GroupDataFile) {
                    children.add((GroupDataFile) groupFile);
                }
            }
        }
        return children;
    }

    public Set<DataFile> getResultFileSet() {
        return getResultFileSetRec(mainFileSet);
    }

    private Set<DataFile> getResultFileSetRec(Set<DataFile> dataFileSet) {
        Set<DataFile> children = new HashSet<>();

        for (DataFile file : dataFileSet) {
            if (file instanceof ResultsDataFile) {
                children.add(file);
            } else {
                children.addAll(getResultFileSetRec(file.getChildFiles()));
            }
        }
        return children;
    }

    @Override
    public void newFileAvailable(DataFile file) {
        if (file instanceof MainDataFile && file.getName().contains(".csv")) {
            mainFileSet.add(file);
            ((MainDataFile) file).splitIntoSortedGroups();
        }
    }
}
