package nl.maastrichtuniversity.cds.modelcommissioningstation.model.ontology;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

public class FML {
    public static final String NAMESPACE = "https://fairmodels.org/ontology.owl#";
    public static final IRI MODEL = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "Model");
    public static final IRI PREDICTION = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "Prediction");
    public static final IRI INFORMATION_ELEMENT = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "InformationElement");

    public static final IRI HAS_OBJECTIVE = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "has_objective");
    public static final IRI NEEDS_INFORMATION_ELEMENT = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "needs_information_element");
    public static final IRI IS_VARIABLE_TYPE = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "is_variable_type");
    public static final IRI CONTAINS_ALGORITHM = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "contains_algorithm");

    // validation classes and predicates
    public static final IRI VALIDATION_REQUEST = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "ValidationRequest");

    public static final IRI REQUESTED = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "Requested");
    public static final IRI IN_PROGRESS = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "InProgress");
    public static final IRI ERROR = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "Error");
    public static final IRI SUCCESS = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "Success");

    public static final IRI ABOUT_MODEL = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "about_model");
    public static final IRI HAS_STATUS = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "has_status");
    public static final IRI AT_TIME = SimpleValueFactory.getInstance().createIRI(NAMESPACE, "at_time");
}
