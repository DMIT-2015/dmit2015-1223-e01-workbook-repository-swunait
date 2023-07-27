package dmit2015.batch;

import dmit2015.entity.EnforcementZoneCentre;
import dmit2015.repository.EnforcementZoneCentreRepository;
import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

/**
 * An ItemWriter is executed after an ItemProcessor has executed.
 */
@Named
@Dependent
public class EnforcementZoneCentreEntityToDatabaseItemWriter extends AbstractItemWriter {

    @Inject
    private EnforcementZoneCentreRepository _zoneCentreRepository;
    /**
     * Write a list of items returned from the ItemProcessor to a destination data source..
     */
    @Override
    public void writeItems(List<Object> items) throws Exception {
        for (Object item : items) {

            EnforcementZoneCentre currentItem = (EnforcementZoneCentre) item;

            _zoneCentreRepository.add(currentItem);

        }

    }

}