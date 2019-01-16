import models.data.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class TestFileBuilder {
    private static final String COMMA = ",";
    private final static Logger LOGGER = Logger.getLogger(TestFileBuilder.class.getName());

    public static void main(String[] arg) {
        File input = new File("C:\\Users\\vital\\Desktop\\csv\\all\\cd_all.csv");
        File outputDir = new File("C:\\Users\\vital\\Desktop\\csv\\all\\");

        //createSubsets(input, outputDir);
        createSubsetsOfInterest(input, outputDir);
    }

    public static void createSubsetsOfInterest(File input, File outputDir) {
        LOGGER.info("Creating SubsetOfInterest ICD Codes.");

        MainDataFile mainDataFile = createDataFiles(input, outputDir);

        Set<ICDCode> icdCodesOfInterest = new HashSet<>();
        icdCodesOfInterest.add(new ICDCode("346"));                     // Migraine
        icdCodesOfInterest.add(new ICDCode("249"));                     // Secondary diabetes mellitus
        icdCodesOfInterest.add(new ICDCode("250"));                     // Diabetes mellitus
        icdCodesOfInterest.add(new ICDCode("410"));                     // Acute myocardial infarction
        icdCodesOfInterest.add(new ICDCode("412"));                     // Old myocardial infarction
        icdCodesOfInterest.add(new ICDCode("585"));                     // ChronicKidneyDisease


        File output = new File(outputDir.getPath() + File.separator + "testDiseasesOfInterest.csv");
        LOGGER.info("Creating SubsetOfInterest > " + output.getPath());

        int counter = 0;

        if (output.exists()) {
            output.delete();
            try {
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileAppendUtils appendUtils = new FileAppendUtils();
        for (DataFile groupFile : mainDataFile.getChildFiles()) {
            LOGGER.info("Scanning " + groupFile.getName() + " for SubsetsOfInterest.");

            try {
                LineIterator it = FileUtils.lineIterator(groupFile, "UTF-8");
                ICDSequence sequence = null;
                List<String> lines = new ArrayList<>();

                while (it.hasNext()) {
                    String line = it.nextLine();
                    String[] p = line.split(COMMA);

                    //first sequence
                    if (sequence == null) {
                        sequence = new ICDSequence(p[0]);
                        lines = new ArrayList<>();
                    }

                    //different sequence id
                    if (!p[0].equals(sequence.getId())) {
                        if (!Collections.disjoint(sequence.getAllIcdCodes(), icdCodesOfInterest)) {
                            counter++;
                            for (String transaction : lines) {
                                appendUtils.appendToFile(output, transaction);
                            }
                        }

                        sequence = new ICDSequence(p[0]);
                        lines = new ArrayList<>();
                    }

                    //same sequence
                    if (p.length >= 2) {
                        sequence.addDiagnoses(p[1], Arrays.copyOfRange(p, 2, p.length));
                        lines.add(line);
                    }
                }

                it.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        appendUtils.closeAllWriters();
        LOGGER.info("Found " + counter + " patients with codes of Interest.");
    }


    private static MainDataFile createDataFiles(File input, File outputDir) {
        MainDataFile mainDataFile = new MainDataFile(input.getPath());
        File childrenDir = new File(outputDir.getParent() + File.separator + "groups");
        childrenDir.mkdirs();
        File[] listFiles = childrenDir.listFiles();

        if (listFiles.length != 0) {
            LOGGER.info("Found existing child files.");

            for (File file : listFiles) {
                GroupDataFile groupDataFile = new GroupDataFile(file.getPath());
                mainDataFile.makeChild(groupDataFile);
            }
        } else {
            LOGGER.info("Creating group child files.");

            mainDataFile.splitIntoSortedGroups();
        }

        return mainDataFile;
    }

    public static void createSubsets(File input, File outputDir) {
        //int[] testFileSize = {20, 50, 100, 200, 500, 750, 1000, 1250, 1500, 2000, 2500, 3000, 5000, 7500, 10000, 15000, 20000, 50000};
        int[] testFileSize = {100};

        for (int s : testFileSize) {
            int size = s * 1000;
            File output = new File(outputDir.getPath() + File.separator + "test" + s + "K.csv");

            Map<GenderAgeGroup, Long> counterMap = new HashMap<>();

            for (int i = 0; i < 10; i++) {
                for (Gender gender : Gender.values()) {
                    GenderAgeGroup group = new GenderAgeGroup(gender, i);
                    counterMap.put(group, (long) 0);
                }
            }
            findAndCreateTestGroup(input, output, counterMap, size);
        }
    }

    private static void findAndCreateTestGroup(File input, File output, Map<GenderAgeGroup, Long> counterMap, long N) {
        FileAppendUtils appendUtils = new FileAppendUtils();

        try {
            LineIterator it = FileUtils.lineIterator(input, "UTF-8");
            while (it.hasNext()) {
                try {
                    String line = it.nextLine();

                    String g = line.substring(0, 1);
                    Gender gender = null;
                    if (g.equals("f")) {
                        gender = Gender.FEMALE;
                    }
                    if (g.equals("m")) {
                        gender = Gender.MALE;
                    }

                    String a = line.substring(1, 2);
                    int age = Integer.parseInt(a);

                    GenderAgeGroup group = new GenderAgeGroup(gender, age);
                    long count = counterMap.get(group);

                    if (count < N) {
                        appendUtils.appendToFile(output, line);
                        counterMap.put(group, count + 1);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            appendUtils.closeAllWriters();
            it.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
