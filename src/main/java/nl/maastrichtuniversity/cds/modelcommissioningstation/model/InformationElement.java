package nl.maastrichtuniversity.cds.modelcommissioningstation.model;

import nl.maastrichtuniversity.cds.modelcommissioningstation.services.IndexService;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.util.List;

public class InformationElement extends RdfRepresentation {
    public static final IRI CLASS_URI = FML.INFORMATION_ELEMENT;

    public InformationElement (IRI identifier, List<Statement> statements, IndexService indexService) {
        super(identifier, statements, indexService);
    }

    public String getClassLabel() {
        String returnLabel = "unspecified";

        List<Object> types = this.properties.get(RDF.TYPE);
        for (Object type : types) {
            if(type instanceof SimpleRdfRepresentation) {
                SimpleRdfRepresentation foundObject = (SimpleRdfRepresentation) type;
                if (!foundObject.identifier.equals(FML.INFORMATION_ELEMENT)) {
                    returnLabel = foundObject.getLabel();
                }
            }
        }

        return returnLabel;
    }

    public RdfRepresentation getVariableType() {
        RdfRepresentation varTypeObj = (RdfRepresentation) this.properties.get(FML.IS_VARIABLE_TYPE).get(0);
        return (RdfRepresentation) varTypeObj.properties.get(RDF.TYPE).get(0);
    }
}
