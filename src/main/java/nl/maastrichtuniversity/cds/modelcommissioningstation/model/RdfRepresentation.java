package nl.maastrichtuniversity.cds.modelcommissioningstation.model;

import nl.maastrichtuniversity.cds.modelcommissioningstation.model.ontology.DateTimeHelper;
import nl.maastrichtuniversity.cds.modelcommissioningstation.services.RdfFactory;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.datatypes.XMLDatatypeUtil;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class RdfRepresentation {
    private Logger logger = Logger.getLogger(this.getClass().toString());
    private List<Statement> statements;
    private Map<IRI, List> properties;
    Map<IRI, List> references;
    public final Resource identifier;
    RdfFactory rdfFactory;

    public RdfRepresentation(Resource identifier, List<Statement> statements, RdfFactory rdfFactory, boolean lazyLoading) {
        this.identifier = identifier;
        this.rdfFactory = rdfFactory;
        this.statements = statements;
        this.properties = new HashMap<IRI, List>();
        this.references = new HashMap<IRI, List>();
        if (!lazyLoading) {
            this.processStatements();
        }
    }

    public RdfRepresentation(Resource identifier, List<Statement> statements, RdfFactory rdfFactory) {
        this(identifier, statements, rdfFactory, true);
    }

    /**
     * Retrieve the list of local properties
     * @return Map containing the IRI as key, and a list as value for every property
     */
    protected Map<IRI, List> getProperties() {
        if(this.properties.isEmpty()) {
            this.processStatements();
        }

        return this.properties;
    }

    private void processStatements() {
        for(Statement stmt : this.statements) {
            if (stmt.getSubject().stringValue().equals(identifier.stringValue())) {
                if (stmt.getObject() instanceof IRI) {
                    RdfRepresentation object = this.rdfFactory.getObjectForUri((IRI) stmt.getObject());
                    this.addProperty(stmt.getPredicate(), object);
                } else {
                    if (stmt.getObject() instanceof Literal) {
                        Literal literalObj = (Literal) stmt.getObject();

                        switch(literalObj.getDatatype().toString()) {
                            case "http://www.w3.org/2001/XMLSchema#string":
                            case "http://www.w3.org/1999/02/22-rdf-syntax-ns#langString":
                                this.addProperty(stmt.getPredicate(), literalObj.getLabel());
                                break;
                            case "http://www.w3.org/2001/XMLSchema#decimal":
                                String value = literalObj.getLabel();
                                this.addProperty(stmt.getPredicate(), new BigDecimal(value.toString()));
                                break;
                            case "http://www.w3.org/2001/XMLSchema#dateTime":
                                this.addProperty(stmt.getPredicate(), LocalDateTime.parse(literalObj.getLabel().toString()));
                                break;
                            default:
                                logger.info("Could not parse literal type " + literalObj.getDatatype().toString());
                                this.addProperty(stmt.getPredicate(), literalObj);
                                break;
                        }
                    } else {
                        this.addProperty(stmt.getPredicate(), stmt.getObject());
                    }
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

        properties = this.getProperties();
        if (properties.containsKey(RDFS.LABEL)) {
            labelFound = properties.get(RDFS.LABEL).get(0).toString();
        }
        return labelFound;
    }

    public Resource getIdentifier() {
        return this.identifier;
    }

    public List getTypes() {
        return this.getProperties().get(RDF.TYPE);
    }

    @Override()
    public String toString() {
        properties = this.getProperties();
        if (properties.containsKey(RDFS.LABEL)) {
            return properties.get(RDFS.LABEL).get(0).toString();
        }

        return this.identifier.stringValue();
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
