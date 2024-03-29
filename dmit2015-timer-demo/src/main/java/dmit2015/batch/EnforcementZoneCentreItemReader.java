package dmit2015.batch;

import jakarta.batch.api.chunk.AbstractItemReader;
import jakarta.batch.runtime.context.JobContext;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Properties;

/**
 * The sequence for a batch chunk step are: ItemReader --> ItemProcessor --> ItemWriter
 */
@Named
@Dependent
public class EnforcementZoneCentreItemReader extends AbstractItemReader {

    @Inject
    private JobContext _jobContext;

    private BufferedReader _reader;

    /**
     * The open method is used to open a data source for reading.
     */
    @Override
    public void open(Serializable checkpoint) throws Exception {
        Properties jobParameters = _jobContext.getProperties();
        String inputFile = jobParameters.getProperty("input_file");
//        _reader = new BufferedReader(new FileReader(Paths.get(inputFile).toFile()));
        _reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(inputFile)));
        // Skip the first line as it contains field name headers
        _reader.readLine();
    }

    /**
     * Read from the data source one item at a time.
     * Return null to trigger the end of the file.
     */
    @Override
    public String readItem() throws Exception {
        try {
            String line = _reader.readLine();
            return line;
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}