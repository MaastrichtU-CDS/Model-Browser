package nl.maastrichtuniversity.cds.modelcommissioningstation.model;

import nl.maastrichtuniversity.cds.modelcommissioningstation.services.RdfFactory;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;

import java.util.List;

public class SimpleRdfRepresentation extends RdfRepresentation{

    public SimpleRdfRepresentation (IRI identifier, List<Statement> statements, RdfFactory indexService) {
        super(identifier, statements, indexService);
    }

}
