package nl.maastrichtuniversity.cds.modelcommissioningstation.model;

import nl.maastrichtuniversity.cds.modelcommissioningstation.services.IndexService;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.repository.RepositoryResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RdfRepresentation {
    Map<IRI, List> properties;
    Map<IRI, List> references;
    public final IRI identifier;

    public RdfRepresentation(IRI identifier, List<Statement> statements, IndexService indexService) {
        this.identifier = identifier;
        this.properties = new HashMap<IRI, List>();
        this.references = new HashMap<IRI, List>();
        this.processStatements(statements, indexService);
    }

    private void processStatements(List<Statement> statements, IndexService indexService) {
        for(Statement stmt : statements) {
            if (stmt.getSubject().stringValue().equals(identifier.stringValue())) {
                if (stmt.getObject() instanceof IRI) {
                    RdfRepresentation object = indexService.getObjectForUri((IRI) stmt.getObject());
                    if (object != null) {
                        this.addProperty(stmt.getPredicate(), object);
                    } else {
                        this.addProperty(stmt.getPredicate(), stmt.getObject());
                    }
                } else {
                    this.addProperty(stmt.getPredicate(), stmt.getObject().stringValue());
                }
            }

            if (stmt.getObject().equals(identifier)) {
                this.addReference(stmt.getPredicate(), stmt.getSubject());
            }
        }
    }

    private void addVariable(IRI key, Object value, Map<IRI, List> variable) {
        List objects = null;
        if (variable.containsKey(key)) {
            objects = variable.get(key);
        } else {
            objects = new ArrayList();
            variable.put(key, objects);
        }
        objects.add(value);
    }

    void addProperty(IRI key, Object value) {
        this.addVariable(key, value, this.properties);
    }

    void addReference(IRI key, Object value) {
        this.addVariable(key, value, this.references);
    }

    public String getLabel() {
        String labelFound = "unknown";

        if (this.properties.containsKey(RDFS.LABEL)) {
            labelFound = this.properties.get(RDFS.LABEL).get(0).toString();
        }
        return labelFound;
    }

    @Override
    public boolean equals(Object o) {
        boolean returnVal = false;

        if (o instanceof RdfRepresentation) {
            ((RdfRepresentation) o).identifier.equals(this.identifier);
            returnVal = true;
        }

        return returnVal;
    }
}
