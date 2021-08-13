package nl.maastrichtuniversity.cds.modelcommissioningstation.services;

import nl.maastrichtuniversity.cds.modelcommissioningstation.helperObjects.AppProperties;
import nl.maastrichtuniversity.cds.modelcommissioningstation.model.*;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Service
public class IndexService extends RdfFactory {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public IndexService(AppProperties properties) {
        super();
        this.initializeRdfStore(properties.getModelRepoType(),
                properties.getModelRepoUrl(),
                properties.getModelRepoId(),
                properties.getModelRepoUser(),
                properties.getModelRepoPass());

        this.reloadIndex(properties);
        this.fetchReferencedFiles();
        logger.info("Done loading all models");
    }

    /**
     * Reload the index by removing the index triples from the RDF store,
     * and loading them again from the standard remote location.
     */
    public void reloadIndex(AppProperties properties) {
        this.logger.info("Clear and reload index");
        this.conn.clear(SimpleValueFactory.getInstance().createIRI(properties.getModelIndexUrl()));
        try {
            this.addRemoteFile(properties.getModelIndexUrl());
        } catch (IOException e) {
            logger.warn("Index URL is incorrect/malformed (" + properties.getModelIndexUrl() + ")");
        }
    }

    /**
     * Add a remote location to the current repository
     * @param remoteLocation: string representation of the URL where the turtle file is located
     */
    public void addRemoteFile(String remoteLocation) throws IOException {
        IRI graphIRI = SimpleValueFactory.getInstance().createIRI(remoteLocation);
        URL documentURL = new URL(remoteLocation);
        RDFFormat format = Rio.getParserFormatForFileName(documentURL.toString()).orElse(RDFFormat.RDFXML);
        org.eclipse.rdf4j.model.Model results = Rio.parse(documentURL.openStream(), remoteLocation, format);

        this.conn.add(results, graphIRI);
    }

    /**
     * Search for the referenced files, as indicated in the index turtle file.
     * Remote files are added to the current in-memory RDF store.
     */
    private void fetchReferencedFiles() {
        IRI predicateToSearch = SimpleValueFactory.getInstance().createIRI("https://fairmodels.org/ontology.owl#referencedInformation");
        RepositoryResult<Statement> statements = this.conn.getStatements(null, predicateToSearch,null);

        while(statements.hasNext()) {
            Statement stmt = statements.next();
            logger.debug(stmt.getSubject().toString() + " | " +
                    stmt.getPredicate().toString() + " | " +
                    stmt.getObject().toString());
            try {
                this.addRemoteFile(stmt.getObject().stringValue());
            } catch (IOException e) {
                logger.warn("Could not load/find referenced file " + stmt.getObject().stringValue(), e);
            }
        }
    }

    /**
     * Retrieve the URI and human-readable label of all available models
     * @return Map containing the URI as key, and the label as value.
     */
    public Map<String,String> getAllModels() {
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
        "PREFIX fml: <https://fairmodels.org/ontology.owl#> " +
        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
        "SELECT ?model ?label " +
        "WHERE { " +
	        "?model rdf:type fml:Model. " +
            "?model rdfs:label ?label. " +
        "}";

        Map<String, String> retResult = new HashMap<String, String>();
        TupleQueryResult result = this.conn.prepareTupleQuery(query).evaluate();
        while(result.hasNext()) {
            BindingSet bs = result.next();
            retResult.put(bs.getValue("model").stringValue(), bs.getValue("label").stringValue());
        }

        return retResult;
    }

    @Override
    public RdfRepresentation determineClassType(List<IRI> classTypes, Resource uri, List<Statement> allStatements) {
        RdfRepresentation returnObject = null;

        if (classTypes.contains(nl.maastrichtuniversity.cds.modelcommissioningstation.model.Model.CLASS_URI)) {
            returnObject = new Model(uri, allStatements, this);
        }

        if (classTypes.contains(Prediction.CLASS_URI)) {
            returnObject = new Prediction(uri, allStatements, this);
        }

        if (classTypes.contains(InformationElement.CLASS_URI)) {
            returnObject = new InformationElement(uri, allStatements, this);
        }

        return returnObject;
    }

}
