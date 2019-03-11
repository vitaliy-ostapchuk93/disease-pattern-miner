package models.data;

import one.util.streamex.StreamEx;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StreamedDatasetCreator {

    private final static Logger LOGGER = Logger.getLogger(StreamedDatasetCreator.class.getName());
    private final static String COMMA = ",";

    private Path filePath;

    public StreamedDatasetCreator(Path filePath) {
        LOGGER.setLevel(Level.INFO);
        this.filePath = filePath;

        try {
            Optional<String> lineOptional = getStream().findAny();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StreamedDatasetCreator(String path) {
        this.filePath = Paths.get(path);
    }

    public StreamEx<String> getStream() throws IOException {
        return StreamEx.ofLines(filePath);
    }

    public StreamEx<String[]> getFilteredStream() throws IOException {
        return StreamEx.ofLines(filePath)
                .map(line -> line.split(COMMA))
                .filter(transaction -> transaction.length > 3)
                .filter(transaction -> transaction[1].length() == 8)
                .filter(transaction -> transaction[1].startsWith("20"));
    }


    public StreamEx<ICDTransaction> getTransactionStream(StreamEx<String> streamEx) throws IOException {
        return streamEx
                .map(line -> line.split(COMMA))
                .filter(transaction -> transaction.length > 3)
                .filter(transaction -> transaction[1].length() == 8)
                .filter(transaction -> transaction[1].startsWith("20"))
                .map(strings -> new ICDTransaction(strings[0], strings[1], strings[2], Arrays.copyOfRange(strings, 2, strings.length)));
    }

/*
    public StreamEx<ICDSequence> getSequenceStream(StreamEx<ICDTransaction> streamEx) throws IOException {
        return streamEx
                .groupRuns((transaction1, transaction2) -> transaction1.getId().equals(transaction2.getId()))
                .map(ICDSequence::new)
                .filter(sequence -> sequence.getFilteredDiagnosesCount() > 2);
    }
*/

    public StreamEx<String> getGroupStream(StreamEx<String> streamEx, String group) throws IOException {
        return streamEx.filter(line -> line.split(COMMA)[0].equals(group));
    }

    public StreamEx<ICDTransaction> filterByGender(StreamEx<ICDTransaction> streamEx, Gender gender) throws IOException {
        return streamEx.filter(transaction -> transaction.getGender() == gender);
    }

    public StreamEx<ICDTransaction> filterByAge(StreamEx<ICDTransaction> streamEx, int age) throws IOException {
        return streamEx.filter(transaction -> transaction.getAge() == age);
    }

    public StreamEx<ICDTransaction> filterByGroup(StreamEx<ICDTransaction> streamEx, GenderAgeGroup group) throws IOException {
        return streamEx.filter(transaction -> transaction.getGenderAgeGroup() == group);
    }

    public StreamEx<ICDTransaction> filterByGroup(StreamEx<ICDTransaction> streamEx, Gender gender, int age) throws IOException {
        return filterByGroup(streamEx, new GenderAgeGroup(gender, age));
    }

    public Path getFilePath() {
        return filePath;
    }

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }
}
