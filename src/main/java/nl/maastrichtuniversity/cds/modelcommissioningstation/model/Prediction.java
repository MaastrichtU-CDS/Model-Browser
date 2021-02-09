package nl.maastrichtuniversity.cds.modelcommissioningstation.model;

import nl.maastrichtuniversity.cds.modelcommissioningstation.model.ontology.FML;
import nl.maastrichtuniversity.cds.modelcommissioningstation.services.IndexService;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;

import java.util.List;

public class Prediction extends RdfRepresentation {
    public static final IRI CLASS_URI = FML.PREDICTION;

    public Prediction (IRI identifier, List<Statement> statements, IndexService indexService) {
        super(identifier, statements, indexService);
    }
}
