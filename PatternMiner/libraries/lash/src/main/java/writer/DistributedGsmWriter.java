package writer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;
import utils.IntArrayWritable;

import java.io.IOException;

public class DistributedGsmWriter implements GsmWriter {

    Reducer<?, ?, IntArrayWritable, LongWritable>.Context context;
    IntArrayWritable key = new IntArrayWritable();
    LongWritable value = new LongWritable();

    public DistributedGsmWriter() {

    }

    public void setContext(Reducer<?, ?, IntArrayWritable, LongWritable>.Context givenCont) {
        context = givenCont;
    }

    @Override
    public void write(int[] sequence, long count) throws IOException, InterruptedException {
        key.setContents(sequence);
        value.set(count);
        context.write(key, value);
    }


}
