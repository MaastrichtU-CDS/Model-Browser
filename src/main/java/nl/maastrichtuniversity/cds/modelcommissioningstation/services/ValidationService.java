package nl.maastrichtuniversity.cds.modelcommissioningstation.services;

import nl.maastrichtuniversity.cds.modelcommissioningstation.helperObjects.AppProperties;
import nl.maastrichtuniversity.cds.modelcommissioningstation.model.RdfRepresentation;
import nl.maastrichtuniversity.cds.modelcommissioningstation.model.ontology.DateTimeHelper;
import nl.maastrichtuniversity.cds.modelcommissioningstation.model.ontology.FML;
import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
        this.context = vf.createIRI("http://" + this.getHostname() + "/validation/request");
    }

    /**
     * Creates and stores a request to validate the prediction model with the given URI
     * @param modelUri: URI of the model to validate
     * @return String containing the URI of the specific validation
     */
    public String requestValidation(String modelUri) {
        IRI modelIdentifier = vf.createIRI(modelUri);
        LocalDateTime curTime = LocalDateTime.now();

        IRI validationRequestIri = vf.createIRI("http://" +
                this.getHostname() +
                "/validation/request#" +
                UUID.randomUUID());

        this.addStatement(validationRequestIri, RDF.TYPE, FML.VALIDATION_REQUEST);
        this.addStatement(validationRequestIri, FML.AT_TIME,
                vf.createLiteral(DateTimeHelper.xsdDateTimeFormat.format(curTime), "xsd:dateTime"));
        this.addStatement(validationRequestIri, FML.ABOUT_MODEL, modelIdentifier);
        BNode statusInstance = vf.createBNode();
        this.addStatement(validationRequestIri, FML.HAS_STATUS, statusInstance);
        this.addStatement(statusInstance, RDF.TYPE, FML.REQUESTED);

        return validationRequestIri.toString();
    }

    @Override
    public RdfRepresentation determineClassType(List<IRI> classTypes, IRI uri, List<Statement> allStatements) {
        return null;
    }
}
