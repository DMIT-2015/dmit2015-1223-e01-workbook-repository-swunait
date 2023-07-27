package dmit2015.batch;

import jakarta.batch.api.AbstractBatchlet;
import jakarta.batch.runtime.BatchStatus;
import jakarta.batch.runtime.context.JobContext;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.logging.Logger;

/**
 * Batchlets are task oriented step that is called once.
 * It either succeeds or fails. If it fails, it CAN be restarted and it runs again.
 */
@Named
@Dependent
public class FileUploadBatchlet extends AbstractBatchlet {

    @Inject
    private JobContext _jobContext;

    private Logger _logger = Logger.getLogger(FileUploadBatchlet.class.getName());

    @Inject
    @ConfigProperty(name = "dmit2015.batch.uploadfile")
    private String _fileToUpload;

    @Inject
    @ConfigProperty(name = "dmit2015.batch.uploaduri")
    private String _fileUploadUri;

    /**
     * Perform a task and return "COMPLETED" if the job has successfully completed
     * otherwise return "FAILED" to indicate the job failed to complete.
     */
    @Transactional
    @Override
    public String process() throws Exception {
        String batchStatus = BatchStatus.COMPLETED.toString();

        try {
            _logger.info("Uploading file " + _fileToUpload);
            // Simulate taking 3 second to upload a large file
            Thread.sleep(3000);
            _logger.info("Finished uploading file to " + _fileUploadUri);

        } catch (Exception ex) {
            batchStatus = BatchStatus.FAILED.toString();
            ex.printStackTrace();
            _logger.fine("Batchlet failed with exception: " + ex.getMessage());
        }

        return batchStatus;
    }
}