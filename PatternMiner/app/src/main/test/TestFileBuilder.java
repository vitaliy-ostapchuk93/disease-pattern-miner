import models.data.Gender;
import models.data.GenderAgeGroup;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestFileBuilder {

    public static void main(String[] arg) {
        File input = new File("C:\\Users\\vital\\Desktop\\cd_all.csv");
        String outputDir = "C:\\Users\\vital\\Desktop\\";
        //int[] testFileSize = {20, 50, 100, 200, 500, 750, 1000, 1250, 1500, 2000, 2500, 3000, 5000, 7500, 10000, 15000, 20000, 50000};
        int[] testFileSize = {100, 200, 500, 750, 1000};
        for (int s : testFileSize) {
            int size = s * 1000;
            File output = new File(outputDir + "test" + s + "K.csv");

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
        BufferedWriter writer = getWriter(output);
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
                        writer.write(line);
                        writer.newLine();
                        counterMap.put(group, count + 1);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            it.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static BufferedWriter getWriter(File file) {
        try {
            if (file.exists()) {
                file.delete();
            }
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            // true = append file
            FileWriter fileWriter = new FileWriter(file.getPath(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            return bufferedWriter;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
