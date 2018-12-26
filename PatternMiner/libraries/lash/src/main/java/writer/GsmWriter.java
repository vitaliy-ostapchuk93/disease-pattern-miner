package writer;

import java.io.IOException;

public interface GsmWriter {
    void write(int[] sequence, long count) throws IOException, InterruptedException;
}
