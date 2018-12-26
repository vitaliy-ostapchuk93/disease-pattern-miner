package servlets;

import models.data.DataFile;
import models.data.Gender;
import models.data.ResultsDataFile;
import models.results.DataFileListener;
import models.results.ResultsFilter;
import models.results.ResultsMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class DataFileManager implements ServletContextListener, DataFileListener {

    private final static Logger LOGGER = Logger.getLogger(DataFileManager.class.getName());

    private static int nextId = 0;

    private ResultsMapper mapper;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        LOGGER.setLevel(Level.INFO);
        LOGGER.info("DataFileManager started.");

        this.mapper = new ResultsMapper();
        this.mapper.setInitialized(false);

        ServletContext ctx = event.getServletContext();
        ctx.setAttribute("DataFileManager", this);
    }


    @Override
    public void contextDestroyed(ServletContextEvent event) {
        LOGGER.info("DataFileManager destroyed.");
    }

    /**
     * Initial result set. Just one time, later by listeners!
     */
    public void fillResultMapper(Set<DataFile> outputSet) {
        for (DataFile file : outputSet) {
            if (file instanceof ResultsDataFile) {
                fillMapper((ResultsDataFile) file);
            } else {
                LOGGER.warning("May be wrong type! RESULTS required!");
            }
        }
        mapper.setInitialized(true);
    }


    /**
     * Fill the mapper with a given result data file.
     *
     * @param dataFile
     */
    private void fillMapper(ResultsDataFile dataFile) {
        LOGGER.info("Filling result mapper > " + dataFile.getName());
        try {
            LineIterator it = FileUtils.lineIterator(dataFile, "UTF-8");
            while (it.hasNext()) {
                String line = it.nextLine();

                if (line.contains("#SUP:")) {
                    String[] seqSplit;
                    if (line.contains("-2")) {
                        seqSplit = line.split("-2");
                    } else {
                        seqSplit = line.split("#SUP:");
                    }
                    String seq = seqSplit[0].trim();

                    try {
                        int sup = Integer.parseInt(seqSplit[1].replace("#SUP:", "").trim());

                        if (seq.endsWith("-1")) {
                            seq = seq.substring(0, seq.length() - 2).trim();
                        }

                        Set<String> seqSet = new HashSet<>(Arrays.asList(seq.split(" ")));
                        seqSet.remove("-1");
                        if (seqSet.size() > 2) {
                            mapper.addEntry(seq, dataFile.getGenderGroup(), dataFile.getAgeGroup(), dataFile, sup);
                        }
                    } catch (Exception e) {
                        LOGGER.warning("Some Error during filling mapper: " + e.getMessage());
                    }
                }
            }

            LOGGER.info("Results updated.");
            it.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResultsMapper getMapper() {
        return mapper;
    }


    @Override
    public void newFileAvailable(DataFile file) {
        if (file instanceof ResultsDataFile) {
            fillMapper((ResultsDataFile) file);
        }
    }

    public void createFilter() {
        ResultsFilter filter = new ResultsFilter(nextId++);
        mapper.getFilterList().add(filter);

        LOGGER.info("Created new filter (id= " + filter.getId() + ")");
    }

    public void changeFilterType(int id, String filterType) {
        LOGGER.info("Change type of filter #" + id + " to " + filterType);
        mapper.getFilter(id).setFilterType(filterType);
    }

    public void deleteFilterItem(int id) {
        LOGGER.info("Deleting filter (id= " + id + ")");
        ResultsFilter filter = mapper.getFilter(id);
        mapper.getFilterList().remove(filter);
    }

    public void setFilterGender(int id, Gender gender) {
        LOGGER.info("Setting new gender Filter: " + gender);
        ResultsFilter filter = mapper.getFilter(id);

        for (Gender g : Gender.values()) {
            if (g == gender) {
                filter.getFilterGender().replace(g, true);
            } else {
                filter.getFilterGender().replace(g, false);
            }
        }
    }

    public void setFilterAge(int id, String[] age) {
        LOGGER.info("Setting filter agegroups.");

        ResultsFilter filter = mapper.getFilter(id);

        Set<Integer> selected = new HashSet<>();
        for (String ageGroup : age) {
            int i = Integer.parseInt(ageGroup);
            selected.add(i);
        }

        for (Integer ageGroup : filter.getFilterAgeGroups().keySet()) {
            if (selected.contains(ageGroup)) {
                filter.getFilterAgeGroups().replace(ageGroup, true);
            } else {
                filter.getFilterAgeGroups().replace(ageGroup, false);
            }
        }
    }

    public void setFilterGroupCount(int id, int minGroupCount) {
        LOGGER.info("Setting filter minGroupCount: " + minGroupCount);
        ResultsFilter filter = mapper.getFilter(id);
        filter.setFilterGroupsCount(minGroupCount);
    }

    public void setFilterTopNSupGroup(int id, int n) {
        LOGGER.info("Setting filter TopNSupGroup: " + n);
        ResultsFilter filter = mapper.getFilter(id);
        filter.setFilterTopNGroups(n);
    }

    public void setFilterTopNSupAll(int id, int n) {
        LOGGER.info("Setting filter TopNSupAll: " + n);
        ResultsFilter filter = mapper.getFilter(id);
        filter.setFilterTopNAll(n);
    }

    public void setFilterRelSup(int id, double minRelSups) {
        LOGGER.info("Setting filter RelSup: " + minRelSups);
        ResultsFilter filter = mapper.getFilter(id);
        filter.setFilterRelSup(minRelSups);
    }

    public void setFilterAbsSup(int id, int minAbsSup) {
        LOGGER.info("Setting filter AbsSup: " + minAbsSup);
        ResultsFilter filter = mapper.getFilter(id);
        filter.setFilterAbsSup(minAbsSup);
    }


    public void setFilterTopPSupGroup(int id, float n) {
        LOGGER.info("Setting filter TopPSupGroup: " + n);
        ResultsFilter filter = mapper.getFilter(id);
        filter.setFilterTopPGroups(n);
    }

    public void setFilterTopPSupAll(int id, float n) {
        LOGGER.info("Setting filter TopPSupAll: " + n);
        ResultsFilter filter = mapper.getFilter(id);
        filter.setFilterTopPAll(n);
    }

    public void setFilterPattern(int id, String pattern) {
        LOGGER.info("Setting filter Pattern: " + pattern);
        ResultsFilter filter = mapper.getFilter(id);
        filter.setFilterPattern(pattern);
    }

    public void setFilterSeqLength(int id, int seqLength) {
        LOGGER.info("Setting filter SeqLength: " + seqLength);
        ResultsFilter filter = mapper.getFilter(id);
        filter.setFilterSeqLength(seqLength);
    }

}
