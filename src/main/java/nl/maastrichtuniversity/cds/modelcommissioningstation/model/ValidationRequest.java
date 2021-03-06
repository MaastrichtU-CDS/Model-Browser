package nl.maastrichtuniversity.cds.modelcommissioningstation.model;

import nl.maastrichtuniversity.cds.modelcommissioningstation.model.ontology.FML;
import nl.maastrichtuniversity.cds.modelcommissioningstation.services.RdfFactory;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ValidationRequest extends RdfRepresentation {
    public static final IRI CLASS_URI = FML.VALIDATION_REQUEST;

    public ValidationRequest(Resource identifier, List<Statement> statements, RdfFactory rdfFactory) {
        super(identifier, statements, rdfFactory);
    }

    public String getDateTime() {
        List<Object> properties = this.getProperties().get(FML.AT_TIME);

        if (properties.size() > 0) {
            LocalDateTime dt = (LocalDateTime) properties.get(0);
            return dt.format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm:ss"));
        }

        return "";
    }

    public RdfRepresentation getStatus() {
        List<Object> statusObjects = this.getProperties().get(FML.HAS_STATUS);
        if (statusObjects.size() > 0) {
            Resource statusObjectIdentifier = (Resource) statusObjects.get(0);
            RdfRepresentation statusBNodeObject = this.rdfFactory.getObjectForUri(statusObjectIdentifier);
            List<RdfRepresentation> typesFound = statusBNodeObject.getTypes();
            return typesFound.get(0);
        }

        return null;
    }
}
