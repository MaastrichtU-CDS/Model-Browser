package nl.maastrichtuniversity.cds.modelcommissioningstation.model;

import nl.maastrichtuniversity.cds.modelcommissioningstation.model.ontology.FML;
import nl.maastrichtuniversity.cds.modelcommissioningstation.services.IndexService;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;

import java.util.List;

public class Model extends RdfRepresentation {
    public static final IRI CLASS_URI = FML.MODEL;

    public Model (Resource identifier, List<Statement> statements, IndexService indexService) {
        super(identifier, statements, indexService);
    }

    public List getObjectives() {
        return this.getProperties().get(FML.HAS_OBJECTIVE);
    }

    public List getAlgorithms() { return this.getProperties().get(FML.CONTAINS_ALGORITHM); }

    public List getInformationElements() { return this.getProperties().get(FML.NEEDS_INFORMATION_ELEMENT); }
}
