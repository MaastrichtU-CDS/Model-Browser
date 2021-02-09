package nl.maastrichtuniversity.cds.modelcommissioningstation.services;

import nl.maastrichtuniversity.cds.modelcommissioningstation.helperObjects.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ValidationService extends RdfFactory {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ValidationService(AppProperties properties) {
        super();
        this.initializeRdfStore(properties.getValidationRepoType(),
                properties.getValidationRepoUrl(),
                properties.getValidationRepoId(),
                properties.getValidationRepoUser(),
                properties.getValidationRepoPass());
    }
}
