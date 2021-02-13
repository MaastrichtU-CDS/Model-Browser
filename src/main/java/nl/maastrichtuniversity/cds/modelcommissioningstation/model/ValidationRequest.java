package nl.maastrichtuniversity.cds.modelcommissioningstation.model;

import nl.maastrichtuniversity.cds.modelcommissioningstation.model.ontology.FML;
import nl.maastrichtuniversity.cds.modelcommissioningstation.services.RdfFactory;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;

import java.util.List;

public class ValidationRequest extends RdfRepresentation {
    public static final IRI CLASS_URI = FML.VALIDATION_REQUEST;

    public ValidationRequest(Resource identifier, List<Statement> statements, RdfFactory rdfFactory) {
        super(identifier, statements, rdfFactory);
    }

    public String getDateTime() {
        List<Object> properties = this.properties.get(FML.AT_TIME);

        if (properties.size() > 0) {
            return properties.get(0).toString();
        }

        return "";
    }

    public String getStatus() {
        List<Object> statusObjects = this.properties.get(FML.HAS_STATUS);
        if (statusObjects.size() > 0) {
            Resource statusObjectIdentifier = (Resource) statusObjects.get(0);
            RdfRepresentation statusBNodeObject = this.rdfFactory.getObjectForUri(statusObjectIdentifier);
            List typesFound = statusBNodeObject.getTypes();
            return typesFound.get(0).toString();
        }

        return "Unknown";
    }
}
