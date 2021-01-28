package nl.maastrichtuniversity.cds.modelcommissioningstation.model;

import nl.maastrichtuniversity.cds.modelcommissioningstation.services.IndexService;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;

import java.util.List;

public class Model extends RdfRepresentation {
    public static final IRI CLASS_URI = FML.MODEL;

    public Model (IRI identifier, List<Statement> statements, IndexService indexService) {
        super(identifier, statements, indexService);
    }

    public List getObjectives() {
        return this.properties.get(FML.HAS_OBJECTIVE);
    }

    public List getAlgorithms() { return this.properties.get(FML.CONTAINS_ALGORITHM); }

    public List getInformationElements() { return this.properties.get(FML.NEEDS_INFORMATION_ELEMENT); }
}
