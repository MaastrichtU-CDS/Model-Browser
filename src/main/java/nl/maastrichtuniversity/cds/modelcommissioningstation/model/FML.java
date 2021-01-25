package nl.maastrichtuniversity.cds.modelcommissioningstation.model;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

public class FML {
    public static final String NAMESPACE = "https://fairmodels.org/ontology.owl#";
    public static final IRI MODEL = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "Model");
    public static final IRI PREDICTION = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "Prediction");
    public static final IRI INFORMATION_ELEMENT = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "InformationElement");

    public static final IRI HAS_OBJECTIVE = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "has_objective");
    public static final IRI NEEDS_INFORMATION_ELEMENT = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "needs_information_element");
}
